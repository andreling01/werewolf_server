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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class UseWitchAbilityController {

    @Autowired
    private ObjectMapper objectMapper;

    @CrossOrigin
    @RequestMapping
    public ResponseEntity<String> useWitchAbility(@RequestBody String inputBody, HttpServletRequest request) throws
            GameException {
        try {
            checkNotNull(inputBody);
            Map<String, String> inputMap = objectMapper.readValue(inputBody, new TypeReference<Map<String, String>>() {
            });
            if (!inputMap.containsKey(Constants.ROOM_ID_KEY)) {
                String errorMessage = String.format("Use Witch Ability Request[%s] doesn't have a roomId",
                                                    request.getRequestedSessionId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            if (!inputMap.containsKey(Constants.SEAT_NUMBER_KEY)) {
                String errorMessage = String
                        .format("Use Witch Ability Request[%s] doesn't have a seatNumber", request.getRequestedSessionId());
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
                String errorMessage = String.format("Use Witch Ability Request[%s] didn't match character's " + "identity",
                                                    request.getRequestedSessionId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            Witch witch = (Witch) game.getCharacters()[seatNumber - 1];
            Boolean rescue = false;
            int poisonTarget = -1;
            if (inputMap.containsKey(Constants.RESUCE_OPTION_KEY)) {
                rescue = Boolean.valueOf(inputMap.get(Constants.RESUCE_OPTION_KEY));
            }
            if (inputMap.containsKey(Constants.TARGET_SEAT_NUMBER_KEY)) {
                poisonTarget = Integer.valueOf(inputMap.get(Constants.TARGET_SEAT_NUMBER_KEY));
            }
            if (game.getKilledInTheNight() == seatNumber && rescue && !witch.isCanRescueSelf()) {
                log.error("Witch cannot rescue self in this game");
                throw new GameException("Witch cannot rescue self in this game");
            }
            if (rescue && poisonTarget != -1 && !witch.isCanTakeTwoActionInANight()) {
                log.error("Witch cannot take two actions during the same night");
                throw new GameException("Witch cannot take two actions during the same night");
            }
            if(rescue && !witch.isUsedMedicine()) {
                game.setSavedByWitch(game.getKilledInTheNight());
                witch.setUsedMedicine(true);
            }
            if (poisonTarget != -1 && !witch.isUsedPoison()) {
                game.setPoisonedByWitch(poisonTarget);
                witch.setUsedPoison(true);
            }
            game.getCharacterOrder().add(game.getCharacterOrder().poll());
            StorageUtil.writeGameData(game);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NullPointerException e) {
            String errorMessage = String
                    .format("Use Witch Ability Request[%s] has a null input body", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        } catch (NumberFormatException e) {
            String errorMessage = String
                    .format("Use Witch Ability Request[%s] has a integer parsing issue", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        } catch (IOException e) {
            String errorMessage = String.format("Use Witch Ability Request[%s] has a json parsing issue",
                                                request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        }
    }
}
