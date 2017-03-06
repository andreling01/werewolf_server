package com.doubleL.werewolf.model.advancedModel;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.enums.CharacterType;
import com.doubleL.werewolf.model.baseModel.Character;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by andreling on 2017/2/28.
 */
@Setter
@Getter
public class ElderOfSilence extends Character {

    private int lastBannedCharacter;

    public ElderOfSilence() {
        super();
        this.lastBannedCharacter = -1;
    }

    @Override
    public CharacterType getCharacterType() {
        return CharacterType.GOD;
    }

    @Override
    public CharacterIdentity getCharacterIdentity() {
        return CharacterIdentity.ELDER_OF_SILENCE;
    }
}
