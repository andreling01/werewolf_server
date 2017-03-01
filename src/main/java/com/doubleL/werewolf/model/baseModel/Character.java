package com.doubleL.werewolf.model.baseModel;

import com.doubleL.werewolf.enums.CharacterType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by andreling on 2017/2/26.
 */
@Setter
@Getter
public abstract class Character {
    private int seatNumber;
    private boolean dead;
    private boolean coupled;
    private boolean saved;
    private boolean guarded;
    private boolean charmed;
    private boolean banned;

    Character() {
        this.seatNumber = -1;
        this.dead = false;
        this.coupled = false;
        this.saved = false;
        this.guarded = false;
        this.charmed = false;
        this.banned = false;
    }

    public abstract CharacterType getCharacterType();
}
