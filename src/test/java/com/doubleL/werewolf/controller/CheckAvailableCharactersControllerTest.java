package com.doubleL.werewolf.controller;

import com.doubleL.werewolf.enums.CharacterIdentity;
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
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

/**
 * Created by andreling on 3/13/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class CheckAvailableCharactersControllerTest {

    private CheckAvailableCharactersController checkAvailableCharactersController;

    private Game game;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ObjectMapper mockObjectMapper;

    @Before
    public void init() throws Exception {
        checkAvailableCharactersController = new CheckAvailableCharactersController(mockObjectMapper);
        when(request.getRequestedSessionId()).thenReturn("Random session id");
        game = new GameCreator().createAGame("thiefSetup.json");
        game.setInTheNight(true);
        StorageUtil.writeGameData(game);
    }

    @Test
    public void testThiefCharacters() throws Exception {
        int thiefSeatNumber = findThiefSeatNumber();
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(Constants.ROOM_ID_KEY, game.getRoomId());
        inputMap.put(Constants.SEAT_NUMBER_KEY, String.valueOf(thiefSeatNumber));
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(inputMap);
        ResponseEntity<List<Character>> response = checkAvailableCharactersController.checkAvailableCharacters
                ("Normal test case", request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test(expected = GameException.class)
    public void testGameNotStart() throws Exception {
        int thiefSeatNumber = findThiefSeatNumber();
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(Constants.ROOM_ID_KEY, game.getRoomId());
        inputMap.put(Constants.SEAT_NUMBER_KEY, String.valueOf(thiefSeatNumber));
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(inputMap);
        game.setInTheNight(false);
        StorageUtil.writeGameData(game);
        checkAvailableCharactersController.checkAvailableCharacters("Game not start test case", request);
    }

    @Test(expected = GameException.class)
    public void testNotAThiefCase() throws Exception {
        int thiefSeatNumber = findThiefSeatNumber();
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(Constants.ROOM_ID_KEY, game.getRoomId());
        inputMap.put(Constants.SEAT_NUMBER_KEY, String.valueOf(thiefSeatNumber + 1));
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(inputMap);
        checkAvailableCharactersController.checkAvailableCharacters("Not a thief test case", request);
    }

    @Test(expected = GameException.class)
    public void testNullInput() throws Exception {
        checkAvailableCharactersController.checkAvailableCharacters(null, request);
    }

    @Test(expected = GameException.class)
    public void testNoRoomId() throws Exception {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(Constants.SEAT_NUMBER_KEY, "1");
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(inputMap);
        checkAvailableCharactersController.checkAvailableCharacters("NoRoomId", request);
    }

    @Test(expected = GameException.class)
    public void testNoSeatNumber() throws Exception {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(Constants.ROOM_ID_KEY, game.getRoomId());
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(inputMap);
        checkAvailableCharactersController.checkAvailableCharacters("NoSeatNum", request);
    }

    @Test(expected = GameException.class)
    public void testInvalidSeatNumber() throws Exception {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(Constants.ROOM_ID_KEY, game.getRoomId());
        inputMap.put(Constants.SEAT_NUMBER_KEY, "invalidSeatNumber");
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(inputMap);
        checkAvailableCharactersController.checkAvailableCharacters("invalidSeatNumber", request);
    }

    @After
    public void clean() throws Exception {
        StorageUtil.deleteGameData(game.getRoomId());
    }


    private int findThiefSeatNumber() throws Exception {
        for (int i = 0; i < game.getCharacters().length; i++) {
            if (game.getCharacters()[i].getCharacterIdentity() == CharacterIdentity.THIEF) {
                return i + 1;
            }
        }
        throw new Exception("Test Error, Thief is not found in test game");
    }
}
