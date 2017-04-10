package com.doubleL.werewolf.controller;

import com.doubleL.werewolf.exception.GameException;
import com.doubleL.werewolf.model.Constants;
import com.doubleL.werewolf.model.Game;
import com.doubleL.werewolf.model.baseModel.Character;
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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Created by andreling on 3/11/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class JoinGameControllerTest {

    private JoinGameController joinGameController;

    private Game game;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ObjectMapper mockObjectMapper;

    @Before
    public void init() throws Exception {
        joinGameController = new JoinGameController(mockObjectMapper);
        when(request.getRequestedSessionId()).thenReturn("Random session id");
        game = new GameCreator().createAGame("normalSetup.json");
        StorageUtil.writeGameData(game);
    }

    @Test
    public void testNormalGame() throws Exception {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(Constants.ROOM_ID_KEY, game.getRoomId());
        inputMap.put(Constants.SEAT_NUMBER_KEY, "1");
        inputMap.put(Constants.DEVICE_UUID, "testDeviceId");
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(inputMap);
        ResponseEntity<Character> response = joinGameController.joinGame("inputData", request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertThat(response.getBody(), instanceOf(Character.class));
    }

    @Test(expected = GameException.class)
    public void testNormalGame_duplicateSeatNumber() throws Exception {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(Constants.ROOM_ID_KEY, game.getRoomId());
        inputMap.put(Constants.SEAT_NUMBER_KEY, "1");
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(inputMap);
        ResponseEntity<Character> response = joinGameController.joinGame("inputData", request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertThat(response.getBody(), instanceOf(Character.class));
        ResponseEntity<Character> secondResponse = joinGameController.joinGame("inputData", request);
        assertEquals(HttpStatus.BAD_REQUEST, secondResponse.getStatusCode());
    }

    @Test(expected = GameException.class)
    public void testNullInput() throws Exception {
        joinGameController.joinGame(null, request);
    }

    @Test(expected = GameException.class)
    public void testNoRoomId() throws Exception {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(Constants.SEAT_NUMBER_KEY, "1");
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(inputMap);
        joinGameController.joinGame("NoRoomId", request);
    }

    @Test(expected = GameException.class)
    public void testNoSeatNumber() throws Exception {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(Constants.ROOM_ID_KEY, game.getRoomId());
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(inputMap);
        joinGameController.joinGame("NoSeatNum", request);
    }

    @Test(expected = GameException.class)
    public void testInvalidSeatNumber() throws Exception {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(Constants.ROOM_ID_KEY, game.getRoomId());
        inputMap.put(Constants.SEAT_NUMBER_KEY, "invalidSeatNumber");
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(inputMap);
        joinGameController.joinGame("invalidSeatNumber", request);
    }

    @After
    public void clean() throws Exception {
        StorageUtil.deleteGameData(game.getRoomId());
    }
}
