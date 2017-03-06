package com.doubleL.werewolf.model.baseModel;

import com.doubleL.werewolf.enums.CharacterIdentity;
import com.doubleL.werewolf.enums.CharacterType;
import com.doubleL.werewolf.model.advancedModel.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by andreling on 2017/2/26.
 */

@Setter
@Getter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Human.class, name = "human"),
        @JsonSubTypes.Type(value = Wolf.class, name = "wolf"),
        @JsonSubTypes.Type(value = BeautyWolf.class, name = "beautyWolf"),
        @JsonSubTypes.Type(value = Cupid.class, name = "cupid"),
        @JsonSubTypes.Type(value = ElderOfSilence.class, name = "elderOfSilence"),
        @JsonSubTypes.Type(value = Guardian.class, name = "guardian"),
        @JsonSubTypes.Type(value = Hunter.class, name = "hunter"),
        @JsonSubTypes.Type(value = Idiot.class, name = "idiot"),
        @JsonSubTypes.Type(value = Prophet.class, name = "prophet"),
        @JsonSubTypes.Type(value = Thief.class, name = "thief"),
        @JsonSubTypes.Type(value = WhiteWolf.class, name = "whiteWolf"),
        @JsonSubTypes.Type(value = Witch.class, name = "witch")
})
@JsonIgnoreProperties({"characterType", "characterIdentity"})
public abstract class Character {
    private int seatNumber;
    private boolean dead;
    private boolean coupled;
    private boolean saved;
    private boolean guarded;
    private boolean charmed;
    private boolean banned;

    public Character() {
        this.seatNumber = -1;
        this.dead = false;
        this.coupled = false;
        this.saved = false;
        this.guarded = false;
        this.charmed = false;
        this.banned = false;
    }

    public abstract CharacterType getCharacterType();

    public abstract CharacterIdentity getCharacterIdentity();
}
