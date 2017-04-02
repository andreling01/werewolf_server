package com.doubleL.werewolf.controller;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.exception.GameException;
import com.doubleL.werewolf.model.Constants;
import com.doubleL.werewolf.model.Game;
import com.doubleL.werewolf.model.advancedModel.Witch;
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
 * Created by andreling on 4/1/17.
 */
@AllArgsConstructor
@Slf4j
@RestController
public class CheckKilledPlayerController {

    @Autowired
    private ObjectMapper objectMapper;

    @CrossOrigin
    @RequestMapping(value = "/checkKilledPlayer", method = RequestMethod.POST)
    public ResponseEntity<Integer> checkKilledPlayer(@RequestBody String inputBody, HttpServletRequest request) throws
            GameException {
        try {
            checkNotNull(inputBody);
            Map<String, String> inputMap = objectMapper.readValue(inputBody, new TypeReference<Map<String, String>>() {
            });
            if (!inputMap.containsKey(Constants.ROOM_ID_KEY)) {
                String errorMessage = String.format("Check Killed Player Request[%s] doesn't have a roomId",
                                                    request.getRequestedSessionId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            if (!inputMap.containsKey(Constants.SEAT_NUMBER_KEY)) {
                String errorMessage = String
                        .format("Check Killed Player Request[%s] doesn't have a seatNumber", request
                                .getRequestedSessionId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            int seatNumber = Integer.valueOf(inputMap.get(Constants.SEAT_NUMBER_KEY));
            Game game = StorageUtil.readGameData(inputMap.get(Constants.ROOM_ID_KEY));
            if (!game.isInTheNight() || CharacterIdentity.WITCH != game.getCharacterOrder().peek()) {
                String errorMessage = String
                        .format("The game[%s] is not ready for this character[%s].", game.getRoomId(),
                                CharacterIdentity.WITCH.toValue());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            if (CharacterIdentity.WITCH != game.getCharacters()[seatNumber - 1].getCharacterIdentity()) {
                String errorMessage = String.format("Check Killed Player Request[%s] didn't match character's " + "identity",
                                                    request.getRequestedSessionId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            Witch witch = (Witch) game.getCharacters()[seatNumber - 1];
            if (witch.isUsedMedicine()) {
                return new ResponseEntity<Integer>(-1, HttpStatus.OK);
            } else {
                return new ResponseEntity<Integer>(game.getKilledInTheNight(), HttpStatus.OK);
            }
        } catch (NullPointerException e) {
            String errorMessage = String
                    .format("Check Killed Player Request[%s] has a null input body", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        } catch (NumberFormatException e) {
            String errorMessage = String
                    .format("Check Killed Player Request[%s] has a integer parsing issue", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        } catch (IOException e) {
            String errorMessage = String.format("Check Killed Player Request[%s] has a json parsing issue",
                                                request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        }
    }
}
