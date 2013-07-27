package com.sky.mattca.language2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: matt
 * Date: 23/07/2013
 * Time: 18:46
 * To change this template use File | Settings | File Templates.
 */

public class Handler {

    public static class BuildError {
        public int errorID;
        public int line, position;
        public String[] values;

        public BuildError(int errorID, int line, int position) {
            this.errorID = errorID;
            this.line = line;
            this.position = position;
            this.values = new String[0];
        }

        public BuildError(int errorID, int line, int position, String... values) {
            this(errorID, line, position);
            this.values = values;
        }
    }

    public static final String[] errorMessages = {
            "%d errors were found during build, check output for details.",
            "Unterminated string literal",
            "Unexpected '.'",
            "Unexpected character '%s'",
            "Expected digit to follow decimal point",
            "Incorrect usage of '=', use ':' for assignment"
    };

    private static List<BuildError> errors = new ArrayList<>();

    public static void reportError(BuildError error) {
        errors.add(error);
    }

    public static void reportWarning(String warning, int line, int position) {
        System.out.println("Warning: " + warning + " found at position " + position + ", line " + line + ".");
    }

    public static void reportStatus(String status) {
        System.out.println("Status: " + status);
    }

    private static void printValueError(BuildError error) {
        String copy = new String(errorMessages[error.errorID]);
        int i = 0;
        while (copy.indexOf("%s") != -1) {
            String s1 = copy.substring(0, copy.indexOf("%s"));
            String s2 = copy.substring(copy.indexOf("%s") + 2);
            copy = s1 + error.values[i] + s2;

            i++;
        }
        System.out.printf("Error: " + copy + ". Found near position %d, line %d.\n", error.position, error.line + 1);
    }

    public static void abortIfErrors() {
        if (errors.size() > 0) {
            for (BuildError error: errors) {
                if (error.values.length > 0) {
                    printValueError(error);
                } else {
                    System.out.printf("Error: " + errorMessages[error.errorID] + ". Found near position %d, line %d.\n", error.position, error.line + 1);
                }
            }
            System.out.printf(errorMessages[0], errors.size());

            System.exit(-1);
        }
    }

}
