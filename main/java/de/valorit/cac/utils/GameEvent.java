package de.valorit.cac.utils;

import de.valorit.cac.checks.CheckResult;

import java.util.ArrayList;
import java.util.List;

public class GameEvent {


    private static final List<CheckResult> results = new ArrayList<>();

    public static void addCheckResult(CheckResult result)  {
        results.add(result);
    }

    public static List<CheckResult> getResults() {
        return results;
    }

    public static void clearResults() {
        results.clear();
    }


}
