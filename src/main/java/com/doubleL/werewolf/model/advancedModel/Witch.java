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
public class Witch extends God {

    private boolean usedMedicine;
    private boolean usedPoison;
    private boolean canTakeTwoActionInANight;
    private boolean canRescueSelf;

    public Witch() {
        super();
        this.usedMedicine = false;
        this.usedPoison = false;
        this.canTakeTwoActionInANight = false;
        this.canRescueSelf = false;
    }

    public Witch(boolean canTakeTwoActionInANight, boolean canRescueSelf) {
        super();
        this.usedMedicine = false;
        this.usedPoison = false;
        this.canTakeTwoActionInANight = canTakeTwoActionInANight;
        this.canRescueSelf = canRescueSelf;
    }

    @Override
    public CharacterIdentity getCharacterIdentity() {
        return CharacterIdentity.WITCH;
    }
}
