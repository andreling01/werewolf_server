package com.doubleL.werewolf.controller;

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
 * Created by andreling on 3/10/17.
 */

@AllArgsConstructor
@Slf4j
@RestController
public class JoinGameController {

    @Autowired
    private ObjectMapper objectMapper;

    @CrossOrigin
    @RequestMapping(value = "/joinGame", method = RequestMethod.POST)
    public ResponseEntity<Character> joinGame(@RequestBody String inputBody, HttpServletRequest request) throws
            GameException {
        try {
            checkNotNull(inputBody);
            Map<String, String> inputMap = objectMapper
                    .readValue(inputBody, new TypeReference<Map<String, String>>() {});
            if (!inputMap.containsKey(Constants.ROOM_ID_KEY)) {
                String errorMessage = String
                        .format("Request[%s] doesn't have a roomId", request.getRequestedSessionId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            if (!inputMap.containsKey(Constants.SEAT_NUMBER_KEY)) {
                String errorMessage = String
                        .format("Request[%s] doesn't have a seatNumber", request.getRequestedSessionId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            Game game = StorageUtil.readGameData(inputMap.get(Constants.ROOM_ID_KEY));
            int seatNumber = Integer.valueOf(inputMap.get(Constants.SEAT_NUMBER_KEY));
            if (!game.getCharacters()[seatNumber - 1].isSeatAssigned()) {
                log.info(String.format("Game[%s] Seat [%s] has been assigned", game.getRoomId(),
                                   String.valueOf(seatNumber)));
                game.getCharacters()[seatNumber - 1].setSeatAssigned(true);
                StorageUtil.writeGameData(game);
                return new ResponseEntity<>(game.getCharacters()[seatNumber - 1], HttpStatus.OK);
            } else {
                throw new GameException(String.format("Game[%s] Seat [%s] has been assigned", game.getRoomId(),
                                                      String.valueOf(seatNumber)));
            }
        } catch (NullPointerException e) {
            String errorMessage = String.format("Request[%s] has a null input body", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        } catch (NumberFormatException e) {
            String errorMessage = String
                    .format("Request[%s] has a integer parsing issue", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        } catch (IOException e) {
            String errorMessage = String
                    .format("Request[%s] has a json parsing issue", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        }
    }
}
