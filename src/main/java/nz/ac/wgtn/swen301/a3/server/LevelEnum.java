package nz.ac.wgtn.swen301.a3.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LevelEnum {
    ALL("ALL"),
    DEBUG("DEBUG"),
    INFO("INFO"),
    WARN("WARN"),
    ERROR("ERROR"),
    FATAL("FATAL"),
    TRACE("TRACE"),
    OFF("OFF");


    private final String value;

    LevelEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Made a LevelEnum using the input String. Return null if no corresponding LevelEnum exist.
     *
     * @param text the text
     * @return the level enum
     */
    @JsonCreator
    public static LevelEnum fromValue(String text) {
        for (LevelEnum b : LevelEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Compare this LevelEnum is greater or equal to the one passed as a parameter.
     *
     * @param r the r
     * @return the boolean
     */
    public boolean isGreaterOrEqual(LevelEnum r) {
        return this.compareTo(r) >= 0;
    }

}
