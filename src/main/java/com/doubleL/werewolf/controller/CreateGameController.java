package com.doubleL.werewolf.controller;

import com.doubleL.werewolf.exception.GameException;
import com.doubleL.werewolf.model.Game;
import com.doubleL.werewolf.util.GameInitializer;
import com.doubleL.werewolf.util.StorageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by andreling on 3/11/17.
 */

@RestController
public class CreateGameController {

    @RequestMapping(value = "/createGame", method = RequestMethod.POST)
    public ResponseEntity<String> createGame(@RequestBody String gameSetup,
                                             HttpServletRequest request) throws GameException {

        try {
            checkNotNull(gameSetup);

            Game game = GameInitializer.initializeGame(gameSetup);
            StorageUtil.writeGameData(game);
            return new ResponseEntity<>(game.getRoomId(), HttpStatus.OK);
        } catch (NullPointerException e) {
            throw new GameException("Game initial setup cannot be null", e);
        }
    }
}
