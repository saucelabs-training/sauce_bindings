package com.saucelabs.saucebindings;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter @Getter
public class VisualResults {
    private final Boolean passed;
    private final String message;
    private final String status;
    private final List<VisualSnapshot> snapshots = new ArrayList<>();
    private final Long totalNew;
    private final Long totalChanged;
    private final Long totalRejected;
    private final Long totalAccepted;
    private final Long total;

    public VisualResults(Map results) {
        this.passed = (Boolean) results.get("passed");
        this.message = (String) results.get("message");
        this.status = (String) results.get("status"); // success, failure, error, timeout, cancelled

        for (Map state : (List<Map>) results.get("states")) {
            snapshots.add(new VisualSnapshot(state));
        }

        Map totals = (Map) results.get("totals");
        this.totalNew = (Long) totals.get("new");
        this.totalChanged = (Long) totals.get("changed");
        this.totalRejected = (Long) totals.get("rejected");
        this.totalAccepted = (Long) totals.get("accepted");
        this.total = (Long) totals.get("all");
    }

    public String resultSummary() {
        String results = "\nVisual Test Results (" + total + " tests): "
                + "\n\tAccepted: " + totalAccepted + "\n\tNew: " + totalNew
                + "\n\tChanged: " + totalChanged + "\n";
        if (totalChanged > 0) {
            results += printChanged() + "\n";
        }
        results += ("\tRejected: " + totalRejected  + "\n");
        if (totalRejected > 0) {
            results += printRejected();
        }
        return results;
    }

    public List<VisualSnapshot> printTests(String status) {
        return getSnapshots().stream().filter(s -> s.getStatus().equals(status)).collect(Collectors.toList());
    }

    public String printRejected() {
        return printTests("rejected").stream().map(VisualSnapshot::toString).collect(Collectors.joining("\n"));
    }

    public String printChanged() {
        return printTests("changed").stream().map(VisualSnapshot::toString).collect(Collectors.joining("\n"));
    }
}
