package com.doubleL.werewolf.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andreling on 2017/2/27.
 */
public enum CharacterIdentity {
    WITCH(7), PROPHET(8), GUARDIAN(9), HUNTER(11), ELDER_OF_SILENCE(10), IDIOT(99), CUPID(2), THIEF(1), TOWNSFOLK(
            99), WOLF(4), WHITE_WOLF(99), BEAUTY_WOLF(5), DAEMON(6), CUPID_EVENT(3);

    private int order;

    CharacterIdentity(int order) {
        this.order = order;
    }

    private static Map<String, CharacterIdentity> map = new HashMap<>();

    static {
        map.put("witch", WITCH);
        map.put("prophet", PROPHET);
        map.put("guardian", GUARDIAN);
        map.put("hunter", HUNTER);
        map.put("elder_of_silence", ELDER_OF_SILENCE);
        map.put("idiot", IDIOT);
        map.put("cupid", CUPID);
        map.put("thief", THIEF);
        map.put("human", TOWNSFOLK);
        map.put("wolf", WOLF);
        map.put("white_wolf", WHITE_WOLF);
        map.put("beauty_wolf", BEAUTY_WOLF);
        map.put("daemon", DAEMON);
        map.put("cupid_event", CUPID_EVENT);
    }

    @JsonCreator
    public static CharacterIdentity forValue(String value) {
        return map.get(StringUtils.lowerCase(value));
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, CharacterIdentity> entry : map.entrySet()) {
            if (entry.getValue() == this) return entry.getKey();
        }

        return null; // or fail
    }

    public int getCharacterOrder() {
        return this.order;
    }
}
