package com.doubleL.werewolf.model;

import com.doubleL.werewolf.model.baseModel.Character;
import com.doubleL.werewolf.enums.CharacterIdentity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by andreling on 3/5/17.
 */
@Setter
@Getter
public class Game {
    private String roomId;
    private List<Character> characterList;
    private List<CharacterIdentity> characterOrderList;
    private int numOfWolves;
    private int numOfGods;
    private int numOfHumans;
}
