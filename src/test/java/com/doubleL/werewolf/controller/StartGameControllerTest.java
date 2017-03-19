package com.doubleL.werewolf.controller;

import com.doubleL.werewolf.exception.GameException;
import com.doubleL.werewolf.model.Constants;
import com.doubleL.werewolf.model.Game;
import com.doubleL.werewolf.util.StorageUtil;
import com.doubleL.werewolf.utility.GameCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

/**
 * Created by andreling on 3/12/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class StartGameControllerTest {
    private StartGameController startGameController;

    private Game game;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ObjectMapper mockObjectMapper;

    @Before
    public void init() throws Exception {
        startGameController = new StartGameController(mockObjectMapper);
        when(request.getRequestedSessionId()).thenReturn("Random session id");
    }

    @Test
    public void testNormalCase() throws Exception {
        game = new GameCreator().createAGame("normalSetup.json");
        for(int i = 0; i < game.getNumOfPlayers(); i++) {
            game.getCharacters()[i].setSeatAssigned(true);
        }
        StorageUtil.writeGameData(game);
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(Constants.ROOM_ID_KEY, game.getRoomId());
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(inputMap);
        ResponseEntity<String> response = startGameController.startGame("normalInput", request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Game actualGame = StorageUtil.readGameData(game.getRoomId());
        assertTrue(actualGame.isInTheNight());
    }

    @Test(expected = GameException.class)
    public void testUnreadyCase() throws Exception {
        game = new GameCreator().createAGame("normalSetup.json");
        StorageUtil.writeGameData(game);
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(Constants.ROOM_ID_KEY, game.getRoomId());
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(inputMap);
        ResponseEntity<String> response = startGameController.startGame("normalInput", request);
    }

    @Test(expected = GameException.class)
    public void testNullInput() throws Exception {
        startGameController.startGame(null, request);
    }

    @Test(expected = GameException.class)
    public void testNoRoomIdInput() throws Exception {
        Map<String, String> inputMap = new HashMap<>();
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(inputMap);
        startGameController.startGame("NoRoomId", request);
    }

    @After
    public void clean() throws Exception {
        if(game != null) {
            StorageUtil.deleteGameData(game.getRoomId());
        }
    }
}
