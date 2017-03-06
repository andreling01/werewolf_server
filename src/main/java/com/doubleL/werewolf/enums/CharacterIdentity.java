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
    WITCH,
    PROPHET,
    GUARDIAN,
    HUNTER,
    ELDER_OF_SILENCE,
    IDIOT,
    CUPID,
    THIEF,
    TOWNSFOLK,
    WOLF,
    WHITE_WOLF,
    BEAUTY_WOLF,
    DAEMON;

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
        map.put("townsfolk", TOWNSFOLK);
        map.put("wolf", WOLF);
        map.put("white_wolf", WHITE_WOLF);
        map.put("beauty_wolf", BEAUTY_WOLF);
        map.put("daemon", DAEMON);
    }

    @JsonCreator
    public static CharacterIdentity forValue(String value) {
        return map.get(StringUtils.lowerCase(value));
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, CharacterIdentity> entry : map.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }

        return null; // or fail
    }
}
