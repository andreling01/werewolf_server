package com.doubleL.werewolf.controller;

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
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.joining;


/**
 * Created by andreling on 3/12/17.
 */
@AllArgsConstructor
@Slf4j
@RestController
public class StartGameController {

    @Autowired
    private ObjectMapper objectMapper;

    @CrossOrigin
    @RequestMapping(value = "/startGame", method = RequestMethod.POST)
    public ResponseEntity<String> startGame(@RequestBody String inputBody, HttpServletRequest request) throws
            GameException {
        try {
            checkNotNull(inputBody);
            Map<String, String> inputMap = objectMapper
                    .readValue(inputBody, new TypeReference<Map<String, String>>() {});
            if (!inputMap.containsKey(Constants.ROOM_ID_KEY)) {
                throw new GameException(
                        String.format("Start Game Request[%s] doesn't have a roomId", request.getRequestedSessionId()));
            }
            Game game = StorageUtil.readGameData(inputMap.get(Constants.ROOM_ID_KEY));
            List<Integer> unreadyPlayer = new ArrayList<>(game.getNumOfPlayers());
            for (int i = 0; i < game.getNumOfPlayers(); i++) {
                if (!game.getCharacters()[i].isSeatAssigned()) {
                    unreadyPlayer.add(i);
                }
            }
            if (unreadyPlayer.isEmpty()) {
                game.setInTheNight(true);
                StorageUtil.writeGameData(game);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                throw new GameException(
                        "Waiting for Player " + unreadyPlayer.stream().map(seatNumber -> String.valueOf(seatNumber + 1))
                                                           .collect(joining(", ")) + " to join the game");
            }
        } catch (NullPointerException e) {
            String errorMessage = String
                    .format("Start Game Request[%s] has a null input body", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        } catch (IOException e) {
            String errorMessage = String
                    .format("Start Game Request[%s] has a json parsing issue", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        }
    }
}
