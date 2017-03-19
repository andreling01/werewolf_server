package com.doubleL.werewolf.controller;

import com.doubleL.werewolf.exception.GameException;
import com.doubleL.werewolf.model.Game;
import com.doubleL.werewolf.util.GameInitializer;
import com.doubleL.werewolf.util.StorageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by andreling on 3/11/17.
 */

@Slf4j
@RestController
public class CreateGameController {

    @CrossOrigin
    @RequestMapping(value = "/createGame", method = RequestMethod.POST)
    public ResponseEntity<String> createGame(@RequestBody String gameSetup, HttpServletRequest request) throws
            GameException {

        try {
            checkNotNull(gameSetup);
            Game game = GameInitializer.initializeGame(gameSetup);
            StorageUtil.writeGameData(game);
            log.info(String.format("Game[%s] has been started.", game.getRoomId()));
            return new ResponseEntity<>(game.getRoomId(), HttpStatus.OK);
        } catch (NullPointerException e) {
            String errorMessage = String.format("Create Game Request[%s] cannot have a null body", request.getRequestedSessionId());
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        }
    }
}
