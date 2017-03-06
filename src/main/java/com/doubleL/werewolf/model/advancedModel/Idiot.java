package com.doubleL.werewolf.model.advancedModel;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.enums.CharacterType;
import com.doubleL.werewolf.model.baseModel.Character;

/**
 * Created by andreling on 2017/2/28.
 */
public class Idiot extends Character {

    public Idiot() {
        super();
    }

    @Override
    public CharacterType getCharacterType() {
        return CharacterType.GOD;
    }

    @Override
    public CharacterIdentity getCharacterIdentity() {
        return CharacterIdentity.IDIOT;
    }
}
