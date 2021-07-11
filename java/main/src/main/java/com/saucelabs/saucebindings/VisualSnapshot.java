package com.saucelabs.saucebindings;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter @Getter
public class VisualSnapshot {
    private final String name;
    private final String groupName;
    private final String status;
    private final String url;

    public VisualSnapshot(Map<String, String> results) {
        this.name = results.get("name");
        this.groupName = results.get("groupName");
        this.status = results.get("status");
        this.url = results.get("url");
    }

    @Override
    public String toString() {
        return name + ": " + url;
    }
}
