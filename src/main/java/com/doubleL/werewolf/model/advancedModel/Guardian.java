package com.doubleL.werewolf.model.advancedModel;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.enums.CharacterType;
import com.doubleL.werewolf.model.baseModel.Character;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by andreling on 2017/2/28.
 */
@Getter
@Setter
public class Guardian extends Character {

    private int lastGuardedCharacter;

    public Guardian() {
        super();
        this.lastGuardedCharacter = -1;
    }

    @Override
    public CharacterType getCharacterType() {
        return CharacterType.GOD;
    }

    @Override
    public CharacterIdentity getCharacterIdentity() {
        return CharacterIdentity.GUARDIAN;
    }
}
