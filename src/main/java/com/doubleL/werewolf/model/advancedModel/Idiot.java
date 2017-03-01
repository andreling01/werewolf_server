package com.doubleL.werewolf.model.advancedModel;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.model.baseModel.God;

/**
 * Created by andreling on 2017/2/28.
 */
public class Idiot extends God {

    public Idiot() {
        super();
    }

    @Override
    public CharacterIdentity getCharacterIdentity() {
        return CharacterIdentity.IDIOT;
    }
}
