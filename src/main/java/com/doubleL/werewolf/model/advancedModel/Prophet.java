package com.doubleL.werewolf.model.advancedModel;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.model.baseModel.God;

/**
 * Created by andreling on 2017/2/27.
 */
public class Prophet extends God {

    public Prophet() {
        super();
    }

    @Override
    public CharacterIdentity getCharacterIdentity() {
        return CharacterIdentity.PROPHET;
    }
}
