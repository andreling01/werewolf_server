package com.doubleL.werewolf.controller;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.exception.GameException;
import com.doubleL.werewolf.model.Constants;
import com.doubleL.werewolf.model.Game;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by andreling on 4/9/17.
 */
@AllArgsConstructor
@Slf4j
@RestController
public class CheckResultController {

    @Autowired
    private ObjectMapper objectMapper;

    @CrossOrigin
    @RequestMapping(value = "/checkResult", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> checkResult(@RequestBody String inputBody, HttpServletRequest request)
            throws GameException {
        try {
            checkNotNull(inputBody);
            Map<String, String> inputMap = objectMapper
                    .readValue(inputBody, new TypeReference<Map<String, String>>() {});
            if (!inputMap.containsKey(Constants.ROOM_ID_KEY)) {
                throw new GameException(
                        String.format("Check Result Request[%s] doesn't have a roomId",
                                      request.getRequestedSessionId()));
            }
            Game game = StorageUtil.readGameData(inputMap.get(Constants.ROOM_ID_KEY));
            if (game.isInTheNight()) {
                throw new GameException(
                        String.format("Game[%s] is still in the night", game.getRoomId()));
            }
            Map<String, Object> result = new HashMap<>();
            List<Integer> killedPlayers = new ArrayList<>();
            if (game.getSavedByWitch() != -1 && game.getSavedByWitch() == game.getGuarded()) {
                killedPlayers.add(game.getSavedByWitch());
                game.getCharacters()[game.getSavedByWitch() - 1].setDead(true);
            }
            if (game.getKilledInTheNight() != -1 && game.getKilledInTheNight() != game.getSavedByWitch()
                    && game.getKilledInTheNight() != game.getGuarded() &&
                    !killedPlayers.contains(game.getKilledInTheNight())) {
                killedPlayers.add(game.getKilledInTheNight());
                game.getCharacters()[game.getKilledInTheNight() - 1].setDead(true);
            }
            if (game.getPoisonedByWitch() != -1 && game.getCharacters()[game.getPoisonedByWitch()]
                    .getCharacterIdentity() != CharacterIdentity.DAEMON) {
                killedPlayers.add(game.getPoisonedByWitch());
                game.getCharacters()[game.getPoisonedByWitch() - 1].setDead(true);
            }
            if (killedPlayers.contains(game.getCoupleA()) || killedPlayers.contains(game.getCoupleB())) {
                if (killedPlayers.contains(game.getCoupleA())) {
                    killedPlayers.add(game.getCoupleB());
                    game.getCharacters()[game.getCoupleB() - 1].setDead(true);
                    result.put(Constants.SUICIDE_FOR_LOVE, game.getCoupleB());
                } else {
                    killedPlayers.add(game.getCoupleA());
                    game.getCharacters()[game.getCoupleA() - 1].setDead(true);
                    result.put(Constants.SUICIDE_FOR_LOVE, game.getCoupleA());
                }
            }
            result.put(Constants.KILLED_PLAYER, killedPlayers);
            result.put(Constants.BANNED, game.getBanned());
            StorageUtil.writeGameData(game);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NullPointerException e) {
            String errorMessage = String
                    .format("Check Result Request[%s] has a null input body", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        } catch (IOException e) {
            String errorMessage = String
                    .format("Check Result Request[%s] has a json parsing issue", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        }
    }
}
