package com.saucelabs.saucebindings.pixels;

import com.saucelabs.saucebindings.SauceEnums;
import lombok.Getter;

public enum Samsung implements SauceEnums {
    GALAXY_A51("412x914"),
    GALAXY_A6("360x720"),
    GALAXY_A70("412x914"),
    GALAXY_FOLD("768x1076"),
    GALAXY_J5("360x640"),
    GALAXY_NOTE_10("412x869"),
    GALAXY_NOTE_10_PLUS("412x869"),
    GALAXY_NOTE_8("414x846"),
    GALAXY_NOTE_9("414x846"),
    GALAXY_NOTE20("412x915"),
    GALAXY_S10("360x760"),
    GALAXY_S10_PLUS("412x869"),
    GALAXY_S10_LITE("412x914"),
    GALAXY_S20("360x800"),
    GALAXY_S20_Plus("384x854"),
    GALAXY_S20_ULTRA("412x915"),
    GALAXY_S21_ULTRA("384x854"),
    GALAXY_S8("360x740"),
    GALAXY_S8_PLUS("360x740"),
    GALAXY_S9("360x740"),
    GALAXY_S9_PLUS("360x740");

    @Getter private final String value;
    @Getter private final String displayName;

    Samsung(String value) {
        this.value = value;
        this.displayName = "Samsung " + this.name().replace("_", " ");
    }

    public static boolean hasKey(String key) {
        try {
            valueOf(key);
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    public String toString() {
        return this.value;
    }
}
