package com.doubleL.werewolf.controller;

import com.doubleL.werewolf.exception.GameException;
import com.doubleL.werewolf.model.Constants;
import com.doubleL.werewolf.model.Game;
import com.doubleL.werewolf.model.baseModel.Character;
import com.doubleL.werewolf.util.StorageUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
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
@RestController
public class JoinGameController {

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(value = "/joinGame", method = RequestMethod.POST)
    public ResponseEntity<Character> joinGame(@RequestBody String inputBody,
                                              HttpServletRequest request) throws GameException {
        try {
            checkNotNull(inputBody);
            Map<String, String> inputMap = objectMapper.readValue(inputBody,
                    new TypeReference<Map<String, String>>() {});
            if(!inputMap.containsKey(Constants.ROOM_ID_KEY)) {
                throw new GameException(String.format("Request[%s] doesn't have a roomId",
                        request.getRequestedSessionId()));
            }
            if(!inputMap.containsKey(Constants.SEAT_NUMBER_KEY)) {
                throw new GameException(String.format("Request[%s] doesn't have a seatNumber",
                        request.getRequestedSessionId()));
            }
            Game game = StorageUtil.readGameData(inputMap.get(Constants.ROOM_ID_KEY));
            int seatNumber = Integer.valueOf(inputMap.get(Constants.SEAT_NUMBER_KEY));
            return new ResponseEntity<>(game.getCharacters()[seatNumber - 1], HttpStatus.OK);
        } catch (NullPointerException e) {
            throw new GameException(String.format("Request[%s] has a null input body",
                    request.getRequestedSessionId()), e);
        } catch (NumberFormatException e) {
            throw new GameException(String.format("Request[%s] has a integer parsing issue",
                    request.getRequestedSessionId()), e);
        } catch (IOException e) {
            throw new GameException(String.format("Request[%s] has a json parsing issue",
                    request.getRequestedSessionId()), e);
        }
    }
}
