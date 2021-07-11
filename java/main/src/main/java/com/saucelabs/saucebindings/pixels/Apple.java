package com.saucelabs.saucebindings.pixels;

import com.saucelabs.saucebindings.SauceEnums;
import lombok.Getter;

public enum Apple implements SauceEnums {
    IPAD_10_2("810x1080"),
    IPAD_9_7("768x1024"),
    IPAD_AIR_2019("834x1112"),
    IPAD_AIR_2020("820x1180"),
    IPAD_AIR_2("768x1024"),
    IPAD_MINI("768x1024"),
    IPAD_PRO_9_7("768x1024"),
    IPAD_PRO_10_5("834x1112"),
    IPAD_PRO_11("834x1194"),
    IPAD_PRO_12_9("1024x1366"),
    IPHONE_11("414x896"),
    IPHONE_11_PRO("375x812"),
    IPHONE_11_PRO_MAX("414x896"),
    IPHONE_12("390x844"),
    IPHONE_12_MINI("360x780"),
    IPHONE_12_PRO("390x844"),
    IPHONE_12_PRO_MAX("428x926"),
    IPHONE_6S("375x667"),
    IPHONE_6S_PLUS("414x736"),
    IPHONE_7("375x667"),
    IPHONE_7_PLUS("414x736"),
    IPHONE_8("375x667"),
    IPHONE_8_PLUS("414x736"),
    IPHONE_SE("320x568"),
    IPHONE_SE_2020("375x667"),
    IPHONE_X("375x812"),
    IPHONE_XR("414x896"),
    IPHONE_XS("375x812"),
    IPHONE_XS_MAX("414x896");

    @Getter private final String value;
    @Getter private final String displayName;

    Apple(String value) {
        this.value = value;
        this.displayName = "Apple " + this.name().replace("_", " ");
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
