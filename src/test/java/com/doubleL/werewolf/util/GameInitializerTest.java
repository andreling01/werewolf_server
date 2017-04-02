package com.doubleL.werewolf.util;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.exception.GameException;
import com.doubleL.werewolf.model.Game;
import com.doubleL.werewolf.utility.GameCreator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by andreling on 3/9/17.
 */
public class GameInitializerTest {

    private GameCreator gameCreator;

    @Before
    public void init() throws Exception {
        gameCreator = new GameCreator();
    }

    @Test
    public void testNormalGameSetup() throws Exception {
        Game actualGame = gameCreator.createAGame("normalSetup.json");
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
        Game actualGame = gameCreator.createAGame("normalSetup_missingWitchSetup.json");
    }

    @Test(expected = GameException.class)
    public void testNormalGameSetup_MissingNumOfCharacters() throws Exception {
        Game actualGame = gameCreator.createAGame("normalSetup_missingNumOfCharacters.json");
    }

    @Test
    public void testThiefGameSetup() throws Exception {
        Game actualGame = gameCreator.createAGame("thiefSetup.json");
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
        Game actualGame = gameCreator.createAGame("allCharacterSetup.json");
        assertNotNull(actualGame);
        assertEquals("Number of Wolves: ", 7, actualGame.getNumOfWolves());
        assertEquals("Number of Humans: ", 5, actualGame.getNumOfHumans());
        assertEquals("Number of God: ", 7, actualGame.getNumOfGods());
        assertEquals("Number of Players:", 17, actualGame.getNumOfPlayers());
        //check the order of Moving queue
        assertEquals(CharacterIdentity.THIEF, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.CUPID, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.CUPID_EVENT, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.WOLF, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.BEAUTY_WOLF, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.DAEMON, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.WITCH, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.PROPHET, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.GUARDIAN, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.ELDER_OF_SILENCE, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.HUNTER, actualGame.getCharacterOrder().poll());
        assertEquals(CharacterIdentity.TOWNSFOLK, actualGame.getCharacterOrder().poll());
        //check the size of seating array
        assertEquals("Size of Seating array: ", 19, actualGame.getCharacters().length);
        //check the numbers of type of character
        assertEquals("Number of types of character: ", 13, actualGame.getCharacterMap().keySet().size());
    }
}
