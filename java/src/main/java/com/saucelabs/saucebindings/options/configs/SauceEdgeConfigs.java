package com.saucelabs.saucebindings.options.configs;

import com.saucelabs.saucebindings.CapabilityManager;
import com.saucelabs.saucebindings.JobVisibility;
import com.saucelabs.saucebindings.Prerun;
import com.saucelabs.saucebindings.options.BaseOptions;
import com.saucelabs.saucebindings.options.builders.SauceEdgeBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Accessors(chain = true) @Setter @Getter
public class SauceEdgeConfigs extends BaseOptions {
    private final SauceEdgeBuilder sauceEdgeBuilder;

    private String build;
    private Integer commandTimeout = null;
    private Map<String, Object> customData = null;
    private Integer idleTimeout = null;
    private JobVisibility jobVisibility; // the actual key for this is a Java reserved keyword "public"; uses enum
    private Integer maxDuration = null;
    private String name;
    private String parentTunnel;
    private Map<Prerun, Object> prerun;
    private URL prerunUrl;
    private Integer priority = null;
    private Boolean recordLogs = null;
    private Boolean recordScreenshots = null;
    private Boolean recordVideo = null;
    private String screenResolution;
    private String seleniumVersion;
    private List<String> tags = null;
    private String timeZone;
    private String tunnelIdentifier;
    private Boolean videoUploadOnPass = null;

    public final List<String> validOptions = Arrays.asList(
            "build",
            "commandTimeout",
            "customData",
            "idleTimeout",
            "jobVisibility",
            "maxDuration",
            "name",
            "parentTunnel",
            "prerun",
            "prerunUrl",
            "priority",
            // public, do not use, reserved keyword, using jobVisibility with enum
            "recordLogs",
            "recordScreenshots",
            "recordVideo",
            "screenResolution",
            "seleniumVersion",
            "tags",
            "timeZone",
            "tunnelIdentifier",
            "videoUploadOnPass");

    public SauceEdgeConfigs(SauceEdgeBuilder sauceEdgeBuilder) {
        this.sauceEdgeBuilder = sauceEdgeBuilder;
    }

    public SauceEdgeBuilder build() {
        CapabilityManager builderManager = new CapabilityManager(this);
        CapabilityManager sauceOptionsManager = new CapabilityManager(sauceEdgeBuilder.sauceOptions.sauce());

        getValidOptions().forEach((capability) -> {
            Object value = builderManager.getCapability(capability);
            if (value != null) {
                sauceOptionsManager.setCapability(capability, value);
            }
        });
        return sauceEdgeBuilder;
    }
}