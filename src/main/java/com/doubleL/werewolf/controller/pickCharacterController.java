package com.doubleL.werewolf.controller;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.exception.GameException;
import com.doubleL.werewolf.model.Constants;
import com.doubleL.werewolf.model.Game;
import com.doubleL.werewolf.model.baseModel.Character;
import com.doubleL.werewolf.util.StorageUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by andreling on 3/26/17.
 */

@AllArgsConstructor
@Slf4j
@RestController
public class pickCharacterController {

    @Autowired
    private ObjectMapper objectMapper;

    @CrossOrigin
    @RequestMapping(value = "/pickCharacter", method = RequestMethod.POST)
    public ResponseEntity<Boolean> pickCharacter(@RequestBody String inputBody, HttpServletRequest request)
            throws GameException {
        try {
            checkNotNull(inputBody);
            Map<String, String> inputMap = objectMapper
                    .readValue(inputBody, new TypeReference<Map<String, String>>() {});
            if (!inputMap.containsKey(Constants.ROOM_ID_KEY)) {
                String errorMessage = String
                        .format("Pick Character Request[%s] doesn't have a roomId", request.getRequestedSessionId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            if (!inputMap.containsKey(Constants.SEAT_NUMBER_KEY)) {
                String errorMessage = String
                        .format("Pick Character Request[%s] doesn't have a seatNumber", request.getRequestedSessionId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            if(!inputMap.containsKey(Constants.CHARACTER_IDENTITY_KEY) ||
                    CharacterIdentity.forValue(inputMap.get(Constants.CHARACTER_IDENTITY_KEY)) == null) {
                String errorMessage = String.format("Pick Character Request[%s] doesn't have a correct Character info",
                                                    request.getRequestedSessionId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            Game game = StorageUtil.readGameData(inputMap.get(Constants.ROOM_ID_KEY));
            int seatNumber = Integer.valueOf(inputMap.get(Constants.SEAT_NUMBER_KEY));
            Character[] characters = game.getCharacters();
            if(!game.isInTheNight() || CharacterIdentity.THIEF != game.getCharacterOrder().peek()) {
                String errorMessage = String.format("The game[%s] is not ready for this character[%s].",
                                                    game.getRoomId(), CharacterIdentity.THIEF.toValue());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            if(CharacterIdentity.THIEF != characters[seatNumber - 1].getCharacterIdentity()) {
                String errorMessage = String.format("Pick Character Request[%s] didn't match character's " +
                                                            "identity", request.getRequestedSessionId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            if (game.getNumOfPlayers() == game.getCharacters().length) {
                String errorMessage = String.format("Game[%s] should not have a thief character.", game.getRoomId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            CharacterIdentity pickedCharacterIdentity = CharacterIdentity
                    .forValue(inputMap.get(Constants.CHARACTER_IDENTITY_KEY));
            int pickedCharacterSeatIndex;
            if(characters[characters.length - 1].getCharacterIdentity() == pickedCharacterIdentity) {
                pickedCharacterSeatIndex = characters.length - 1;
            } else if (characters[characters.length - 2].getCharacterIdentity() == pickedCharacterIdentity) {
                pickedCharacterSeatIndex = characters.length - 2;
            } else {
                String errorMessage = String.format("Pick Character Request[%s] doesn't have a correct character " +
                                                            "info", request.getRequestedSessionId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            swapCharacter(characters, seatNumber, pickedCharacterSeatIndex);
            game.getCharacterOrder().poll();
            StorageUtil.writeGameData(game);
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } catch (NullPointerException e) {
            String errorMessage = String.format("Pick Character Request[%s] has a null input body", request
                    .getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        } catch (NumberFormatException e) {
            String errorMessage = String
                    .format("Pick Character Request[%s] has a integer parsing issue", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        } catch (IOException e) {
            String errorMessage = String
                    .format("Pick Character Request[%s] has a json parsing issue", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        }
    }

    private void swapCharacter(Character[] characters, int seatNumber, int pickedCharacterSeatIndex) {
        Character character = characters[seatNumber - 1];
        String deviceUUID = character.getDeviceUUID();
        character.setDeviceUUID(null);
        character.setSeatNumber(pickedCharacterSeatIndex + 1);
        characters[seatNumber - 1] = characters[pickedCharacterSeatIndex];
        characters[seatNumber - 1].setSeatNumber(seatNumber);
        characters[seatNumber - 1].setDeviceUUID(deviceUUID);
        characters[pickedCharacterSeatIndex] = character;
    }
}
