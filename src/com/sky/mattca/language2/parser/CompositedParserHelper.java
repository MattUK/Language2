package com.sky.mattca.language2.parser;

import com.sky.mattca.language2.tokenizer.TokenString;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that is used to generate, and store, Parse Helper functions.
 *
 * All parsing functions are stored as Operations, which are executed one after the other
 * on a TokenString. The results are returned in the form of a list of statements.
 */
public class CompositedParserHelper {

    private enum ParseState {
        SUCCESS,
        FAILURE
    }

    /**
     * Stores the result of parsing operations
     */
    public class ParseOperationResult {
        private ParseState state;
        private TokenString previousResult;
        private List<Statement> statements;

        public ParseOperationResult(ParseState state, TokenString previousResult, List<Statement> statements) {
            this.state = state;
            this.previousResult = previousResult;
            this.statements = statements;
        }

        public ParseState getState() {
            return state;
        }

        public TokenString getPreviousResult() {
            return previousResult;
        }

        public List<Statement> getStatements() {
            return statements;
        }

        public boolean hasFailed() {
            return state == ParseState.FAILURE;
        }
    }

    @FunctionalInterface
    private interface ParseOperation {
        public ParseOperationResult operate(ParseOperationResult previousResult);

        /**
         * Merges two ParseOperation's, executing this one, followed by the next.
         *
         * @param after The ParseOperation to execute after this one.
         * @return A chained ParseOperation, combining this one and the next.
         */
        public default ParseOperation andThen(ParseOperation after) {
            return (previousResult) -> {
                ParseOperationResult operationResult = operate(previousResult);
                return after.operate(operationResult);
            };
        }
    }

    private ParseOperation matchingOperation;

    /**
     * Creates a new ParserHelper, containing a list of parse-related operations (created by static methods).
     * @param operations The operations to execute on a provided TokenString, resulting in either a success or failure.
     */
    public CompositedParserHelper(ParseOperation... operations) {
        // Chain all operations into one
        matchingOperation = operations[0];

        for (int i = 1; i < operations.length; i++) {
            matchingOperation = matchingOperation.andThen(operations[i]);
        }
    }

    public ParseOperationResult performOn(TokenString string) {
        ParseOperationResult initialState = new ParseOperationResult(ParseState.SUCCESS, string, new ArrayList<>());
        return matchingOperation.operate(initialState);
    }

}
