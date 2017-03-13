package com.doubleL.werewolf.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andreling on 2017/2/26.
 */
public enum CharacterType {
    WOLF, GOD, HUMAN;

    private static Map<String, CharacterType> map = new HashMap<>();

    static {
        map.put("wolf", WOLF);
        map.put("god", GOD);
        map.put("human", HUMAN);
    }

    @JsonCreator
    public static CharacterType forValue(String value) {
        return map.get(StringUtils.lowerCase(value));
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, CharacterType> entry : map.entrySet()) {
            if (entry.getValue() == this) return entry.getKey();
        }

        return null; // or fail
    }
}
