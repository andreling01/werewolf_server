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
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by andreling on 4/2/17.
 */
@AllArgsConstructor
@Slf4j
@RestController
public class JoinRoomController {

    @Autowired
    private ObjectMapper objectMapper;

    @CrossOrigin
    @RequestMapping(value = "/joinRoom", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Integer>> joinRoom(@RequestBody String inputBody, HttpServletRequest request)
            throws GameException {
        try{
            checkNotNull(inputBody);
            Map<String, String> inputMap = objectMapper
                    .readValue(inputBody, new TypeReference<Map<String, String>>() {});
            if (!inputMap.containsKey(Constants.ROOM_ID_KEY)) {
                String errorMessage = String
                        .format("Join Room Request[%s] doesn't have a roomId", request.getRequestedSessionId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            if (!inputMap.containsKey(Constants.DEVICE_UUID)) {
                String errorMessage = String.format("Join Room Request[%s] doesn't have a device uuid", request
                        .getRequestedSessionId());
                log.error(errorMessage);
                throw new GameException(errorMessage);
            }
            Game game = StorageUtil.readGameData(inputMap.get(Constants.ROOM_ID_KEY));
            int alreadySeatedNumber = -1;
            for (int i = 0; i < game.getNumOfPlayers(); i++) {
                if (inputMap.get(Constants.DEVICE_UUID).equals(game.getCharacters()[i].getDeviceUUID())) {
                    alreadySeatedNumber = i + 1;
                }
            }
            Map<String, Integer> result = new HashMap<>();
            result.put("num_of_seats", game.getNumOfPlayers());
            result.put("playerSeatNum", alreadySeatedNumber);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NullPointerException e) {
            String errorMessage = String.format("Join Room Request[%s] has a null input body", request
                    .getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        } catch (IOException e) {
            String errorMessage = String
                    .format("Join Room Request[%s] has a json parsing issue", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        }
    }
}
