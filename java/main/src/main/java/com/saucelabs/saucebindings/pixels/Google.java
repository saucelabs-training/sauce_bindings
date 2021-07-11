package com.saucelabs.saucebindings.pixels;

import com.saucelabs.saucebindings.SauceEnums;
import lombok.Getter;

public enum Google implements SauceEnums {
    PIXEL("412x732"),
    PIXEL_2("412x732"),
    PIXEL_2_XL("412x823"),
    PIXEL_3("393x786"),
    PIXEL_3_XL("412x846"),
    PIXEL_3A("393x808"),
    PIXEL_3A_XL("412x823"),
    PIXEL_4("393x830"),
    PIXEL_4_XL("412x869"),
    PIXEL_4A("393x851"),
    PIXEL_5("393x851");

    @Getter private final String value;
    @Getter private final String displayName;

    Google(String value) {
        this.value = value;
        this.displayName = "Google " + this.name().replace("_", " ");
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
