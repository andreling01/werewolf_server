package com.doubleL.werewolf.model.baseModel;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.enums.CharacterType;

/**
 * Created by andreling on 2017/2/26.
 */
public class Human extends Character {

    public Human() {
        super();
    }

    @Override
    public CharacterType getCharacterType() {
        return CharacterType.HUMAN;
    }

    public CharacterIdentity getCharacterIdentity() {
        return CharacterIdentity.TOWNSFOLK;
    }
}
