package com.doubleL.werewolf.model.advancedModel;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.model.baseModel.Human;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by andreling on 2017/2/28.
 */
@Setter
@Getter
public class Thief extends Human {

    private boolean actionTake;

    public Thief() {
        super();
        this.actionTake = false;
    }

    @Override
    public CharacterIdentity getCharacterIdentity() {
        return CharacterIdentity.THIEF;
    }
}
