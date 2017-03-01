package com.doubleL.werewolf.model.advancedModel;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.model.baseModel.God;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by andreling on 2017/2/27.
 */
@Setter
@Getter
public class Hunter extends God {

    private boolean poisoned;

    public Hunter() {
        super();
        this.poisoned = false;
    }

    @Override
    public CharacterIdentity getCharacterIdentity() {
        return CharacterIdentity.HUNTER;
    }
}
