package com.doubleL.werewolf.model.advancedModel;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.model.baseModel.Wolf;

/**
 * Created by andreling on 2017/2/27.
 */

public class BeautyWolf extends Wolf {

    public BeautyWolf() {
        super();
    }

    @Override
    public CharacterIdentity getCharacterIdentity() {
        return CharacterIdentity.BEAUTY_WOLF;
    }
}
