package com.doubleL.werewolf.model.advancedModel;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.model.baseModel.God;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by andreling on 2017/2/28.
 */
@Setter
@Getter
public class elderOfSlience extends God {

    private int lastBannedCharacter;

    public elderOfSlience() {
        super();
        this.lastBannedCharacter = -1;
    }

    @Override
    public CharacterIdentity getCharacterIdentity() {
        return CharacterIdentity.ELDER_OF_SLIENCE;
    }
}
