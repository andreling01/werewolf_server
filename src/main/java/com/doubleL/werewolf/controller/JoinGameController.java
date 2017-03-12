package com.doubleL.werewolf.controller;

import com.doubleL.werewolf.exception.GameException;
import com.doubleL.werewolf.model.Game;
import com.doubleL.werewolf.model.baseModel.Character;
import com.doubleL.werewolf.util.StorageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by andreling on 3/10/17.
 */

@RestController
public class JoinGameController {

    @RequestMapping(value = "/joinGame", method = RequestMethod.GET)
    public ResponseEntity<Character> joinGame(@RequestParam(value = "roomId") String roomId,
                                              @RequestParam(value = "seatNumber") int seatNumber,
                                              HttpServletRequest request) throws GameException {
        try {
            checkNotNull(roomId);
            checkArgument(!"".equals(roomId));
            checkArgument(seatNumber > 0);

            Game game = StorageUtil.readGameData(roomId);
            return new ResponseEntity<>(game.getCharacters()[seatNumber - 1], HttpStatus.OK);
        } catch (NullPointerException e) {
            throw new GameException(String.format("Request[%s] doesn't have a roomId",
                    request.getRequestedSessionId()), e);
        } catch (IllegalArgumentException e) {
            throw new GameException(String.format("Request[%s] doesn't have a valid roomId or seatNumber",
                    request.getRequestedSessionId()), e);
        } catch (GameException e) {
            throw new GameException(String.format("Game[%s] has a read issue", roomId), e);
        }
    }
}
