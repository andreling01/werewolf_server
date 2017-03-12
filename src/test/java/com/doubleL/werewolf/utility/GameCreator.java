package com.doubleL.werewolf.utility;

import com.doubleL.werewolf.model.Game;
import com.doubleL.werewolf.util.GameInitializer;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by andreling on 3/10/17.
 */
@NoArgsConstructor
public class GameCreator {

    public Game createAGame(String fileName) throws Exception {
        String gameSetupData = readTestData(fileName);
        return GameInitializer.initializeGame(gameSetupData);
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
