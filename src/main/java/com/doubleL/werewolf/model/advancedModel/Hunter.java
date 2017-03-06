package com.doubleL.werewolf.model.advancedModel;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.enums.CharacterType;
import com.doubleL.werewolf.model.baseModel.Character;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by andreling on 2017/2/27.
 */
@Setter
@Getter
public class Hunter extends Character {

    private boolean poisoned;

    public Hunter() {
        super();
        this.poisoned = false;
    }

    @Override
    public CharacterType getCharacterType() {
        return CharacterType.GOD;
    }

    @Override
    public CharacterIdentity getCharacterIdentity() {
        return CharacterIdentity.HUNTER;
    }
}
