package com.doubleL.werewolf.util;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.exception.GameException;
import com.doubleL.werewolf.model.Game;
import com.doubleL.werewolf.model.advancedModel.*;
import com.doubleL.werewolf.model.baseModel.Character;
import com.doubleL.werewolf.model.baseModel.Human;
import com.doubleL.werewolf.model.baseModel.Wolf;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by andreling on 3/6/17.
 */
@Slf4j
public class GameInitializer {

    private static final String NUM_OF_CHARACTERS_KEY = "numOfCharacters";
    private static final String WITCH_SELF_RESCUE = "selfRescue";
    private static final String WITCH_DOUBLE_ACTIONS = "doubleActions";

    private static final ObjectMapper mapper = new ObjectMapper();

    private static int roomIdBase = 0;

    public static Game initializeGame(String gameSetupJson) throws GameException {
        String roomId = String.valueOf(1000 + roomIdBase % 9000);
        roomIdBase++;
        Map<String, Integer> gameSetup;
        try {
            gameSetup = mapper.readValue(gameSetupJson, new TypeReference<Map<String, Integer>>() {});
        } catch (IOException e) {
            throw new GameException(String.format("The Game[%s] cannot be started due to json parse error.", roomId),
                                    e);
        }
        return setUpGame(gameSetup, roomId);
    }

    private static List<Integer> generateRandomCharacterAssignment(int numberOfCharacter) {
        List<List<Integer>> randomeListPool  = new ArrayList<>(10);
        for (int i = 0; i < 10; i ++) {
            List<Integer> range = IntStream.range(0, numberOfCharacter).boxed().collect(Collectors.toList());
            Collections.shuffle(range, new Random(System.nanoTime()));
            randomeListPool.add(range);
        }
        return randomeListPool.get(ThreadLocalRandom.current().nextInt(randomeListPool.size()));
    }

    private static void updateCharacterMap(Map<CharacterIdentity, List<Integer>> map, CharacterIdentity
            characterIdentity, int seatNum) {
        if (map.containsKey(characterIdentity)) {
            List<Integer> seatList = map.get(characterIdentity);
            seatList.add(seatNum);
            Collections.sort(seatList);
            map.put(characterIdentity, seatList);
        } else {
            List<Integer> seatList = new ArrayList<>();
            seatList.add(seatNum);
            map.put(characterIdentity, seatList);
        }
    }

    private static void assignSeat(Character character, List<Integer> seatOrder, Character[] characters,
                                   Map<CharacterIdentity, List<Integer>> characterMap) throws GameException {
        try {
            int seatNumber = seatOrder.remove(0);
            character.setSeatNumber(seatNumber + 1);
            characters[seatNumber] = character;
            updateCharacterMap(characterMap, character.getCharacterIdentity(), seatNumber);
        } catch (ArrayIndexOutOfBoundsException e) {
            String errorMessage = "Number of Characters didn't match detailed character number";
            log.error(errorMessage);
            throw new GameException(errorMessage, e);
        }
    }

    private static Game setUpGame(Map<String, Integer> gameSetup, String roomId) throws GameException {
        if (!gameSetup.containsKey(NUM_OF_CHARACTERS_KEY)) {
            throw new GameException("Number of Players is not specified in the Json");
        }
        Game game = new Game();
        game.setRoomId(roomId);
        int numOfCharacters = gameSetup.get(NUM_OF_CHARACTERS_KEY);
        int numOfPlayers;
        if (gameSetup.containsKey(CharacterIdentity.THIEF.toValue())) {
            numOfPlayers = numOfCharacters - 2;
        } else {
            numOfPlayers = numOfCharacters;
        }
        game.setNumOfPlayers(numOfPlayers);
        List<Integer> seatOrder = generateRandomCharacterAssignment(numOfCharacters);
        Character[] characters = new Character[numOfCharacters];
        List<CharacterIdentity> characterOrderList = new ArrayList<>();
        Map<CharacterIdentity, List<Integer>> characterMap = new HashMap<>();
        int numOfHumans = 0;
        int numOfWolves = 0;
        int numOfGods = 0;
        for (String key : gameSetup.keySet()) {
            switch (key) {
                case "wolf":
                    for (int i = 0; i < gameSetup.get(CharacterIdentity.WOLF.toValue()); i++) {
                        Character wolf = new Wolf();
                        assignSeat(wolf, seatOrder, characters, characterMap);
                        numOfWolves++;
                    }
                    characterOrderList.add(CharacterIdentity.WOLF);
                    break;
                case "thief":
                    if (gameSetup.get(CharacterIdentity.THIEF.toValue()) > 0) {
                        Character thief = new Thief();
                        assignSeat(thief, seatOrder, characters, characterMap);
                        characterOrderList.add(CharacterIdentity.THIEF);
                        numOfHumans++;
                    }
                    break;
                case "cupid":
                    if (gameSetup.get(CharacterIdentity.CUPID.toValue()) > 0) {
                        Character cupid = new Cupid();
                        assignSeat(cupid, seatOrder, characters, characterMap);
                        characterOrderList.add(CharacterIdentity.CUPID);
                        numOfGods++;
                    }
                    break;
                case "white_wolf":
                    if (gameSetup.get(CharacterIdentity.WHITE_WOLF.toValue()) > 0) {
                        Character white_Wolf = new WhiteWolf();
                        assignSeat(white_Wolf, seatOrder, characters, characterMap);
                        numOfWolves++;
                    }
                    break;
                case "beauty_wolf":
                    if (gameSetup.get(CharacterIdentity.BEAUTY_WOLF.toValue()) > 0) {
                        Character beauty_wolf = new BeautyWolf();
                        assignSeat(beauty_wolf, seatOrder, characters, characterMap);
                        characterOrderList.add(CharacterIdentity.BEAUTY_WOLF);
                        numOfWolves++;
                    }
                    break;
                case "daemon":
                    if (gameSetup.get(CharacterIdentity.DAEMON.toValue()) > 0) {
                        Character daemon = new Daemon();
                        assignSeat(daemon, seatOrder, characters, characterMap);
                        characterOrderList.add(CharacterIdentity.DAEMON);
                        numOfWolves++;
                    }
                    break;
                case "witch":
                    if (gameSetup.get(CharacterIdentity.WITCH.toValue()) > 0) {
                        if (!gameSetup.containsKey(WITCH_SELF_RESCUE) || !gameSetup.containsKey(WITCH_DOUBLE_ACTIONS)) {
                            throw new GameException(String.format("Missing witch setup data for Game[%s].", roomId));
                        }
                        Character witch = new Witch(gameSetup.get(WITCH_DOUBLE_ACTIONS) != 0,
                                                    gameSetup.get(WITCH_SELF_RESCUE) != 0);
                        assignSeat(witch, seatOrder, characters, characterMap);
                        characterOrderList.add(CharacterIdentity.WITCH);
                        numOfGods++;
                    }
                    break;
                case "prophet":
                    if (gameSetup.get(CharacterIdentity.PROPHET.toValue()) > 0) {
                        Character prophet = new Prophet();
                        assignSeat(prophet, seatOrder, characters, characterMap);
                        characterOrderList.add(CharacterIdentity.PROPHET);
                        numOfGods++;
                    }
                    break;
                case "human":
                    for (int i = 0; i < gameSetup.get(CharacterIdentity.TOWNSFOLK.toValue()); i++) {
                        Character human = new Human();
                        assignSeat(human, seatOrder, characters, characterMap);
                        numOfHumans++;
                    }
                    break;
                case "guardian":
                    if (gameSetup.get(CharacterIdentity.GUARDIAN.toValue()) > 0) {
                        Character guardian = new Guardian();
                        assignSeat(guardian, seatOrder, characters, characterMap);
                        characterOrderList.add(CharacterIdentity.GUARDIAN);
                        numOfGods++;
                    }
                    break;
                case "hunter":
                    if (gameSetup.get(CharacterIdentity.HUNTER.toValue()) > 0) {
                        Character hunter = new Hunter();
                        assignSeat(hunter, seatOrder, characters, characterMap);
                        characterOrderList.add(CharacterIdentity.HUNTER);
                        numOfGods++;
                    }
                    break;
                case "elder_of_silence":
                    if (gameSetup.get(CharacterIdentity.ELDER_OF_SILENCE.toValue()) > 0) {
                        Character elder_of_silence = new ElderOfSilence();
                        assignSeat(elder_of_silence, seatOrder, characters, characterMap);
                        characterOrderList.add(CharacterIdentity.ELDER_OF_SILENCE);
                        numOfGods++;
                    }
                    break;
                case "idiot":
                    if (gameSetup.get(CharacterIdentity.IDIOT.toValue()) > 0) {
                        Character idiot = new Idiot();
                        assignSeat(idiot, seatOrder, characters, characterMap);
                        numOfGods++;
                    }
                    break;
                default:
                    break;
            }
        }
        Collections.sort(characterOrderList, new Comparator<CharacterIdentity>() {
            @Override
            public int compare(CharacterIdentity o1, CharacterIdentity o2) {
                return o1.getCharacterOrder() - o2.getCharacterOrder();
            }
        });
        Queue<CharacterIdentity> characterOrder = new LinkedList<>(characterOrderList);
        game.setNumOfWolves(numOfWolves);
        game.setNumOfHumans(numOfHumans);
        game.setNumOfGods(numOfGods);
        game.setCharacterMap(characterMap);
        game.setCharacterOrder(characterOrder);
        game.setCharacters(characters);
        game.setInTheNight(false);
        return game;
    }
}
