package com.doubleL.werewolf.util;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.exception.GameException;
import com.doubleL.werewolf.model.Game;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by andreling on 3/9/17.
 */
public class GameInitializerTest {

    @Test
    public void testNormalGameSetup() throws Exception {
        String gameSetupData = readTestData("normalSetup.json");
        Game actualGame = GameInitializer.initializeGame(gameSetupData);
        assertNotNull(actualGame);
        assertEquals("Number of Wolves: ", 4, actualGame.getNumOfWolves());
        assertEquals("Number of Humans: ", 4, actualGame.getNumOfHumans());
        assertEquals("Number of God: ", 4, actualGame.getNumOfGods());
        assertEquals("Number of Players:", 12, actualGame.getNumOfPlayers());
        //check the order of Moving queue
        assertEquals(CharacterIdentity.WOLF, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.WITCH, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.PROPHET, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.HUNTER, actualGame.getCharacterOrder().poll());
        //check the size of seating array
        assertEquals("Size of Seating array: ", 12, actualGame.getCharacters().length);
        //check the numbers of type of character
        assertEquals("Number of types of character: ", 6, actualGame.getCharacterMap().keySet().size());
    }

    @Test(expected = GameException.class)
    public void testNormalGameSetup_MissingWitchSetup() throws Exception {
        String gameSetupData = readTestData("normalSetup_missingWitchSetup.json");
        GameInitializer.initializeGame(gameSetupData);
    }

    @Test(expected = GameException.class)
    public void testNormalGameSetup_MissingNumOfCharacters() throws Exception {
        String gameSetupData = readTestData("normalSetup_missingNumOfCharacters.json");
        GameInitializer.initializeGame(gameSetupData);
    }

    @Test
    public void testThiefGameSetup() throws Exception {
        String gameSetupData = readTestData("thiefSetup.json");
        Game actualGame = GameInitializer.initializeGame(gameSetupData);
        assertNotNull(actualGame);
        assertEquals("Number of Wolves: ", 4, actualGame.getNumOfWolves());
        assertEquals("Number of Humans: ", 5, actualGame.getNumOfHumans());
        assertEquals("Number of God: ", 3, actualGame.getNumOfGods());
        assertEquals("Number of Players:", 10, actualGame.getNumOfPlayers());
        //check the order of Moving queue
        assertEquals(CharacterIdentity.THIEF, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.WOLF, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.PROPHET, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.HUNTER, actualGame.getCharacterOrder().poll());
        //check the size of seating array
        assertEquals("Size of Seating array: ", 12, actualGame.getCharacters().length);
        //check the numbers of type of character
        assertEquals("Number of types of character: ", 6, actualGame.getCharacterMap().keySet().size());
    }

    @Test
    public void testAllCharactersGameSetup() throws Exception {
        String gameSetupData = readTestData("allCharacterSetup.json");
        Game actualGame = GameInitializer.initializeGame(gameSetupData);
        assertNotNull(actualGame);
        assertEquals("Number of Wolves: ", 7, actualGame.getNumOfWolves());
        assertEquals("Number of Humans: ", 5, actualGame.getNumOfHumans());
        assertEquals("Number of God: ", 7, actualGame.getNumOfGods());
        assertEquals("Number of Players:", 17, actualGame.getNumOfPlayers());
        //check the order of Moving queue
        assertEquals(CharacterIdentity.THIEF, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.CUPID, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.WOLF, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.BEAUTY_WOLF, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.DAEMON, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.WITCH, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.PROPHET, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.GUARDIAN, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.ELDER_OF_SILENCE, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.HUNTER, actualGame.getCharacterOrder().poll());
        //check the size of seating array
        assertEquals("Size of Seating array: ", 19, actualGame.getCharacters().length);
        //check the numbers of type of character
        assertEquals("Number of types of character: ", 13, actualGame.getCharacterMap().keySet().size());
    }


    private String readTestData(String fileName) throws Exception {
        String fileNamePath = "data/" + fileName;
        File file = new File(getClass().getClassLoader().getResource(fileNamePath).getFile());
        InputStream fileInputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fileInputStream.read(data);
        fileInputStream.close();
        return new String(data, StandardCharsets.UTF_8);
    }
}
