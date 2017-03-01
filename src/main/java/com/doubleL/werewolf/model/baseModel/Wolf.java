package com.doubleL.werewolf.model.baseModel;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.enums.CharacterType;

/**
 * Created by andreling on 2017/2/26.
 */

public class Wolf extends Character {

    public Wolf() {
        super();
    }

    @Override
    public CharacterType getCharacterType() {
        return CharacterType.WOLF;
    }

    public CharacterIdentity getCharacterIdentity() {
        return CharacterIdentity.WOLF;
    }
}
