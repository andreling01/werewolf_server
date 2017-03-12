package com.doubleL.werewolf.controller;

import com.doubleL.werewolf.exception.GameException;
import com.doubleL.werewolf.model.Game;
import com.doubleL.werewolf.model.baseModel.Character;
import com.doubleL.werewolf.util.StorageUtil;
import com.doubleL.werewolf.utility.GameCreator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;


/**
 * Created by andreling on 3/11/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class JoinGameControllerTest {

    private GameCreator gameCreator;

    private JoinGameController joinGameController;

    private Game game;

    @Mock
    private HttpServletRequest request;

    @Before
    public void init() throws Exception {
        gameCreator = new GameCreator();
        joinGameController = new JoinGameController();
        when(request.getRequestedSessionId()).thenReturn("Random session id");
        game = gameCreator.createAGame("normalSetup.json");
        StorageUtil.writeGameData(game);
    }

    @Test
    public void testNormalGame() throws Exception {
        ResponseEntity<Character> response = joinGameController.joinGame(game.getRoomId(), 4, request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertThat(response.getBody(), instanceOf(Character.class));
    }

    @Test(expected = GameException.class)
    public void testNullRoomId() throws Exception {
        joinGameController.joinGame(null, 4, request);
    }

    @Test(expected = GameException.class)
    public void testInvalidRoomId() throws Exception {
        joinGameController.joinGame("", 4, request);
    }

    @Test(expected = GameException.class)
    public void testInvalidSeatId() throws Exception {
        joinGameController.joinGame(game.getRoomId(), 0, request);
    }

    @After
    public void clean() throws Exception {
        StorageUtil.deleteGameData(game.getRoomId());
    }
}
