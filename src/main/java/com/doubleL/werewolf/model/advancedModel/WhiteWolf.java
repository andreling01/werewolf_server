package com.doubleL.werewolf.model.advancedModel;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.enums.CharacterType;
import com.doubleL.werewolf.model.baseModel.Character;
import com.doubleL.werewolf.model.baseModel.Wolf;

/**
 * Created by andreling on 2017/2/27.
 */
public class WhiteWolf extends Character {

    public WhiteWolf() {
        super();
    }

    @Override
    public CharacterType getCharacterType() {
        return CharacterType.WOLF;
    }

    @Override
    public CharacterIdentity getCharacterIdentity() {
        return CharacterIdentity.WHITE_WOLF;
    }

}
