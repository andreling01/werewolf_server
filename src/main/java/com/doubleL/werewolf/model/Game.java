package com.doubleL.werewolf.model;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.model.baseModel.Character;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by andreling on 3/5/17.
 */
@Setter
@Getter
public class Game {
    private String roomId;
    private Character[] characters;
    private Queue<CharacterIdentity> characterOrder;
    private int numOfPlayers;
    private int numOfWolves;
    private int numOfGods;
    private int numOfHumans;
    private Map<CharacterIdentity, List<Integer>> characterMap;
    private boolean isInTheNight;
}
