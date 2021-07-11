package com.saucelabs.saucebindings.options;

import com.saucelabs.saucebindings.SystemManager;
import lombok.Getter;
import org.openqa.selenium.MutableCapabilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseOptions {
    @Getter protected MutableCapabilities capabilities = new MutableCapabilities();
    protected CapabilityManager capabilityManager;
    @Getter public final List<String> validOptions = null;

    /**
     * Use Case is pulling serialized information from JSON/YAML, converting it to a HashMap and passing it in
     * This is a preferred pattern as it avoids conditionals in code
     * @param capabilitiesToMerge a Map object representing key value pairs to convert to capabilities
     */
    public void mergeCapabilities(Map<String, Object> capabilitiesToMerge) {
        capabilitiesToMerge.forEach(this::setCapability);
    }

    /**
     * This dynamically calls setter
     * Applicable enums must override this method in subclass
     * @param key the name of the capability getting set on the capabilities instance being built
     * @param value the object representing the value of what is set on the capabilities instance being set
     */
    protected void setCapability(String key, Object value) {
        capabilityManager.setCapability(key, value);
    };

    protected String getDefaultBuildName() {
        if (SystemManager.get(knownCITools.get("Jenkins")) != null) {
            return SystemManager.get("BUILD_NAME");
        } else if (SystemManager.get(knownCITools.get("Bamboo")) != null) {
            return SystemManager.get("bamboo_shortJobName");
        } else if (SystemManager.get(knownCITools.get("Travis")) != null) {
            return SystemManager.get("TRAVIS_JOB_NAME");
        } else if (SystemManager.get(knownCITools.get("Circle")) != null) {
            return SystemManager.get("CIRCLE_JOB");
        } else if (SystemManager.get(knownCITools.get("GitLab")) != null) {
            return SystemManager.get("CI_JOB_NAME");
        } else if (SystemManager.get(knownCITools.get("TeamCity")) != null) {
            return SystemManager.get("TEAMCITY_PROJECT_NAME");
        } else if (SystemManager.get("BUILD_NAME") != null) {
            return SystemManager.get("BUILD_NAME");
        } else {
            return "Build Time";
        }
    }

    protected String getDefaultBuildNumber() {
        if (SystemManager.get("BUILD_NUMBER") != null) {
            return SystemManager.get("BUILD_NUMBER");
        } else if (SystemManager.get(knownCITools.get("Bamboo")) != null) {
            return SystemManager.get("bamboo_buildNumber");
        } else if (SystemManager.get(knownCITools.get("Travis")) != null) {
            return SystemManager.get("TRAVIS_JOB_NUMBER");
        } else if (SystemManager.get(knownCITools.get("Circle")) != null) {
            return SystemManager.get("CIRCLE_BUILD_NUM");
        } else if (SystemManager.get(knownCITools.get("GitLab")) != null) {
            return SystemManager.get("CI_JOB_ID");
        } else if (SystemManager.get(knownCITools.get("TeamCity")) != null) {
            return SystemManager.get("BUILD_NUMBER");
        } else if (SystemManager.get("BUILD_NUMBER") != null) {
            return SystemManager.get("BUILD_NUMBER");
        } else {
            String time = String.valueOf(System.currentTimeMillis());
            System.setProperty("BUILD_NUMBER", time);
            return time;
        }
    }

    /**
     * @deprecated This method is no longer supported
     * @return whether CI is accounted for in code
     */
    @Deprecated
    public boolean isKnownCI() {
        return !knownCITools.keySet().stream().allMatch((key) -> SystemManager.get(key) == null);
    }

    /**
     * @deprecated This method will no longer be public
     */
    @Deprecated
    public static final Map<String, String> knownCITools;

    static {
        knownCITools = new HashMap<>();
        knownCITools.put("Jenkins", "BUILD_TAG");
        knownCITools.put("Bamboo", "bamboo_agentId");
        knownCITools.put("Travis", "TRAVIS_JOB_ID");
        knownCITools.put("Circle", "CIRCLE_JOB");
        knownCITools.put("GitLab", "CI");
        knownCITools.put("TeamCity", "TEAMCITY_PROJECT_NAME");
    }
}
