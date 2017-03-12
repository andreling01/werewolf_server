package com.doubleL.werewolf.controller;

import com.doubleL.werewolf.exception.GameException;
import com.doubleL.werewolf.util.StorageUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Created by andreling on 3/11/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateGameControllerTest {

    private static final String testInput = "{\n" + "  \"wolf\":4,\n" + "  \"witch\":1,\n" + "  \"prophet\":1,\n" +
            "  \"hunter\":1,\n" + "  \"idiot\":1,\n" + "  \"human\":4,\n" + "  \"selfRescue\":0,\n" +
            "  \"doubleActions\":0,\n" + "  \"numOfCharacters\":12\n" + "}";

    private CreateGameController createGameController;

    private ResponseEntity<String> response;


    @Mock
    HttpServletRequest request;

    @Before
    public void init() throws Exception {
        createGameController = new CreateGameController();
    }

    @Test
    public void testCreateGame()  throws Exception {
        response = createGameController.createGame(testInput, request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), instanceOf(String.class));
    }

    @Test(expected = GameException.class)
    public void testCreateGame_NullData() throws Exception {
        createGameController.createGame(null, request);
    }

    @Test(expected = GameException.class)
    public void testCreateGame_InvaliData() throws Exception {
        createGameController.createGame("", request);
    }

    @After
    public void clean() throws Exception {
        if(response != null) {
            StorageUtil.deleteGameData(response.getBody());
        }
    }
}
