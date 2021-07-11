package com.saucelabs.saucebindings.pixels;

import com.saucelabs.saucebindings.SauceEnums;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum ScreenResolution implements SauceEnums {
    SVGA("800x600"),
    XGA("1024x768"),
    XGA_PLUS("1152x864"),
    WXGA_MINUS("1280x768"),
    WXGA("1280x800"),
    ATARI_TT("1280x960"),
    SXGA("1280x1024"),
    SXGA_PLUS("1400x1050"),
    WXGA_PLUS("1440x900"),
    UXGA("1600x1200"),
    WSXGA_PLUS("1680x1050"),
    FHD("1920x1080"),
    WUXGA("1920x1200"),
    WQXGA("2560x1600");

    @Getter private final String value;
    @Getter private final String displayName;

    private static final class ResolutionLookup {
        private static final Map<String, String> lookup = new HashMap<String, String>();
    }

    public static Set keys() {
        return ResolutionLookup.lookup.keySet();
    }

    ScreenResolution(String value) {
        this.value = value;
        this.displayName = this.name();
        ResolutionLookup.lookup.put(value, this.name());
    }

    public static boolean hasKey(String key) {
        try {
            valueOf(key);
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    public static String fromString(String value) {
        return ResolutionLookup.lookup.get(value);
    }

    public String toString() {
        return this.value;
    }
}
