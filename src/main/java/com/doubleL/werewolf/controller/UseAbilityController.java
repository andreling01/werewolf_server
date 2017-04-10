package com.doubleL.werewolf.controller;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.enums.CharacterType;
import com.doubleL.werewolf.exception.GameException;
import com.doubleL.werewolf.model.Constants;
import com.doubleL.werewolf.model.Game;
import com.doubleL.werewolf.model.baseModel.Character;
import com.doubleL.werewolf.util.StorageUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by andreling on 3/19/17.
 */
@AllArgsConstructor
@Slf4j
@RestController
public class UseAbilityController {

    private static final Set<CharacterIdentity> WOLF_SET = ImmutableSet.of(CharacterIdentity.WHITE_WOLF,
                                                                           CharacterIdentity.WOLF,
                                                                           CharacterIdentity.BEAUTY_WOLF,
                                                                           CharacterIdentity.DAEMON);

    @Autowired
    private ObjectMapper objectMapper;

    @CrossOrigin
    @RequestMapping(value = "/useAbility", method = RequestMethod.POST)
    public ResponseEntity<Boolean> useAbility(@RequestBody String inputBody, HttpServletRequest request) throws
            GameException {
        try {
            checkNotNull(inputBody);
            Map<String, Object> inputMap = objectMapper
                    .readValue(inputBody, new TypeReference<Map<String, Object>>() {});
            if (!inputMap.containsKey(Constants.ROOM_ID_KEY)) {
                String errorMessage = String
                        .format("Use Ability Request[%s] doesn't have a roomId", request.getRequestedSessionId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            if (!inputMap.containsKey(Constants.SEAT_NUMBER_KEY)) {
                String errorMessage = String
                        .format("Use Ability Request[%s] doesn't have a seatNumber", request.getRequestedSessionId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            Game game = StorageUtil.readGameData((String) inputMap.get(Constants.ROOM_ID_KEY));
            int seatNumber = (int) inputMap.get(Constants.SEAT_NUMBER_KEY);
            if (game.getCharacters()[seatNumber - 1].isDead()) {
                String errorMessage = String.format("Character[%d] in Game[%s] has been dead.", seatNumber, game
                        .getRoomId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            Character ongoingCharacter = game.getCharacters()[seatNumber - 1];
            List<Integer> targetSeatingNumbers;
            if (!game.isInTheNight()) {
                String errorMessage = String.format("The game[%s] has not started.", game.getRoomId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            //Wolf case
            if (game.getCharacterOrder().peek() == CharacterIdentity.WOLF &&
                    WOLF_SET.contains(ongoingCharacter.getCharacterIdentity())) {
                targetSeatingNumbers = getTargetSeatNumbers(inputMap, request);
                if (targetSeatingNumbers.size() > 0) {
                    game.setKilledInTheNight(targetSeatingNumbers.get(0));
                } else {
                    game.setKilledInTheNight(-1);
                }
                game.getCharacterOrder().add(game.getCharacterOrder().poll());
                StorageUtil.writeGameData(game);
                return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
            }
            if (!game.isInTheNight() || ongoingCharacter.getCharacterIdentity() != game.getCharacterOrder().peek()) {
                String errorMessage = String.format("The game[%s] is not ready for this character[%s].",
                                                    game.getRoomId(), ongoingCharacter.getCharacterIdentity().toValue());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            switch (ongoingCharacter.getCharacterIdentity().toValue()) {
                case "cupid":
                    targetSeatingNumbers = getTargetSeatNumbers(inputMap, request);
                    if (targetSeatingNumbers.size() != 2) {
                        String errorMessage = "Cupid needs to connect two characters as couple";
                        log.error(errorMessage);
                        throw new GameException(errorMessage);
                    }
                    game.setCoupleA(targetSeatingNumbers.get(0));
                    game.setCoupleB(targetSeatingNumbers.get(1));
                    game.getCharacterOrder().poll();
                    StorageUtil.writeGameData(game);
                    return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
                case "beauty_wolf":
                    targetSeatingNumbers = getTargetSeatNumbers(inputMap, request);
                    if (targetSeatingNumbers.size() > 0) {
                        game.setCharmedByBeautyWolf(targetSeatingNumbers.get(0));
                    } else {
                        game.setCharmedByBeautyWolf(-1);
                    }
                    game.getCharacterOrder().add(game.getCharacterOrder().poll());
                    StorageUtil.writeGameData(game);
                    return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
                case "daemon":
                    targetSeatingNumbers = getTargetSeatNumbers(inputMap, request);
                    boolean isGod = targetSeatingNumbers.size() > 0 && CharacterType.GOD ==
                            game.getCharacters()[targetSeatingNumbers.get(0) - 1].getCharacterType();
                    game.getCharacterOrder().add(game.getCharacterOrder().poll());
                    StorageUtil.writeGameData(game);
                    return new ResponseEntity<>(isGod, HttpStatus.OK);
                case "prophet":
                    targetSeatingNumbers = getTargetSeatNumbers(inputMap, request);
                    boolean isWolf = targetSeatingNumbers.size() > 0 && CharacterType.WOLF ==
                            game.getCharacters()[targetSeatingNumbers.get(0) - 1].getCharacterType();
                    game.getCharacterOrder().add(game.getCharacterOrder().poll());
                    StorageUtil.writeGameData(game);
                    return new ResponseEntity<>(isWolf, HttpStatus.OK);
                case "guardian":
                    targetSeatingNumbers = getTargetSeatNumbers(inputMap, request);
                    if (targetSeatingNumbers.size() > 0) {
                        game.setGuarded(targetSeatingNumbers.get(0));
                    } else {
                        game.setGuarded(-1);
                    }
                    game.getCharacterOrder().add(game.getCharacterOrder().poll());
                    StorageUtil.writeGameData(game);
                    return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
                case "elder_of_silence":
                    targetSeatingNumbers = getTargetSeatNumbers(inputMap, request);
                    if (targetSeatingNumbers.size() > 0) {
                        game.setBanned(targetSeatingNumbers.get(0));
                    } else {
                        game.setBanned(-1);
                    }
                    game.getCharacterOrder().add(game.getCharacterOrder().poll());
                    StorageUtil.writeGameData(game);
                    return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
                case "hunter":
                    return new ResponseEntity<>(game.getPoisonedByWitch() != seatNumber - 1, HttpStatus.OK);
                default:
                    throw new GameException("No such Character");
            }
        } catch (NullPointerException e) {
            String errorMessage = String.format("Use Ability Request[%s] has a null input body", request
                    .getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        } catch (NumberFormatException e) {
            String errorMessage = String
                    .format("Use Ability Request[%s] has a integer parsing issue", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        } catch (IOException e) {
            String errorMessage = String
                    .format("Use Ability Request[%s] has a json parsing issue", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Integer> getTargetSeatNumbers(Map<String, Object> inputMap, HttpServletRequest request) throws
            GameException, IOException {
        if (!inputMap.containsKey(Constants.TARGET_SEAT_NUMBER_KEY)) {
            String errorMessage = String.format("Use Ability Request[%s] doesn't have a targeting seatNum" +
                                                        " List", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage);
        }
        return (List<Integer>) inputMap.get(Constants.TARGET_SEAT_NUMBER_KEY);
    }
}
