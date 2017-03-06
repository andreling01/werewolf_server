package com.doubleL.werewolf.util;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.model.Game;
import com.doubleL.werewolf.model.advancedModel.Hunter;
import com.doubleL.werewolf.model.advancedModel.Prophet;
import com.doubleL.werewolf.model.advancedModel.WhiteWolf;
import com.doubleL.werewolf.model.advancedModel.Witch;
import com.doubleL.werewolf.model.baseModel.Character;
import com.doubleL.werewolf.model.baseModel.Human;
import com.doubleL.werewolf.model.baseModel.Wolf;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Created by andreling on 3/5/17.
 */
public class StorageUtilTests {
    private static final String TEST_ROOM_ID = "testRoomId";
    private static final String TEST_FILE_NAME = "GAME_testRoomId";

    @Test
    public void testGeneralWriteFunction() throws Exception {
        Game testGame = buildTestGameData();
        StorageUtil.writeGameData(testGame);
        File testFile = new File(TEST_FILE_NAME);
        assertTrue("File should be exist.", testFile.exists());
        testFile.delete();
    }

    @Test
    public void testGeneralReadFunction() throws Exception {
        Game testGame = buildTestGameData();
        StorageUtil.writeGameData(testGame);
        Game actualReturnedGame = StorageUtil.readGameData(TEST_ROOM_ID);
        assertEquals("Game Room Id: ", TEST_ROOM_ID, actualReturnedGame.getRoomId());
        assertEquals("Number of Humans: ", testGame.getNumOfHumans(), actualReturnedGame.getNumOfHumans());
        assertEquals("Number of Gods: ", testGame.getNumOfGods(), actualReturnedGame.getNumOfGods());
        assertEquals("Number of Wolves: ", testGame.getNumOfWolves(), actualReturnedGame.getNumOfWolves());
        assertEquals("Size of Character List: ", testGame.getCharacterList().size(),
                actualReturnedGame.getCharacterList().size());
        assertEquals("Size of Character Order List: ", testGame.getCharacterOrderList(),
                actualReturnedGame.getCharacterOrderList());
        assertThat("First Character should be: ", actualReturnedGame.getCharacterList().get(0),
                instanceOf(Wolf.class));
        assertThat("Second Character should be: ", actualReturnedGame.getCharacterList().get(1),
                instanceOf(WhiteWolf.class));
        assertThat("Third Character should be: ", actualReturnedGame.getCharacterList().get(2),
                instanceOf(Human.class));
        assertThat("Forth Character should be: ", actualReturnedGame.getCharacterList().get(3),
                instanceOf(Witch.class));
        assertThat("Fifth Character should be: ", actualReturnedGame.getCharacterList().get(4),
                instanceOf(Prophet.class));
        assertThat("Sixth Character should be: ", actualReturnedGame.getCharacterList().get(5),
                instanceOf(Hunter.class));
        File testFile = new File(TEST_FILE_NAME);
        testFile.delete();
    }

    private Game buildTestGameData() {
        Game testGame = new Game();
        testGame.setRoomId(TEST_ROOM_ID);
        List<Character>  characterList = new ArrayList<>();
        characterList.add(new Wolf());
        characterList.add(new WhiteWolf());
        characterList.add(new Human());
        characterList.add(new Witch());
        characterList.add(new Prophet());
        characterList.add(new Hunter());
        testGame.setCharacterList(characterList);
        List<CharacterIdentity> characterOrderList = new ArrayList<>();
        characterOrderList.add(CharacterIdentity.WOLF);
        characterOrderList.add(CharacterIdentity.WITCH);
        characterOrderList.add(CharacterIdentity.PROPHET);
        characterOrderList.add(CharacterIdentity.HUNTER);
        testGame.setCharacterOrderList(characterOrderList);
        testGame.setNumOfGods(3);
        testGame.setNumOfHumans(1);
        testGame.setNumOfWolves(2);
        return testGame;
    }
}
