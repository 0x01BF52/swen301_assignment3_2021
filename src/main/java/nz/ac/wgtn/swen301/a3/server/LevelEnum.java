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


    private String value;

    LevelEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static LevelEnum fromValue(String text) {
        for (LevelEnum b : LevelEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

    public boolean isGreaterOrEqual(LevelEnum r) {
        return this.compareTo(r) >= 0;
    }
}
