package com.doubleL.werewolf.util;

import com.doubleL.werewolf.exception.GameStorageException;
import com.doubleL.werewolf.model.Game;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by andreling on 3/4/17.
 */
public class StorageUtil {

    private static final String GAME_FILE_NAME_PREFIX = "GAME_";

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void writeGameData(Game game) throws GameStorageException {
        try {
            String fileName = GAME_FILE_NAME_PREFIX + game.getRoomId();
            Writer fileWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName, false), StandardCharsets.UTF_8));
            String jsonString = mapper.writeValueAsString(game);
            fileWriter.write(jsonString);
            fileWriter.close();
        } catch (JsonProcessingException e) {
            throw new GameStorageException(String.format("Game[%s] has a json write exception.", game.getRoomId()), e);
        } catch (IOException e) {
            throw new GameStorageException(String.format("Game[%s] has a write exception.", game.getRoomId()), e);
        }
    }

    public static Game readGameData(String roomId) throws GameStorageException {
        try {
            String fileName = GAME_FILE_NAME_PREFIX + roomId;
            File file = new File(fileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fileInputStream.read(data);
            fileInputStream.close();
            String gameData = new String(data, StandardCharsets.UTF_8);
            return mapper.readValue(gameData, Game.class);
        } catch (FileNotFoundException e) {
            throw new GameStorageException(String.format("The data of Game[%s] cannot be found.", roomId), e);
        } catch (IOException e) {
            throw new GameStorageException(String.format("Game[%s] cannot be read.", roomId), e);
        }
    }

    public static void deleteGameData(String roomId) {
        String fileName = GAME_FILE_NAME_PREFIX + roomId;
        (new File(fileName)).delete();
    }
}
