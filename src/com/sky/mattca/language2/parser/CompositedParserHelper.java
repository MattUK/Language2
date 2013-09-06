package com.sky.mattca.language2.parser;

import com.sky.mattca.language2.tokenizer.TokenString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Matt
 * Date: 06/09/13
 * Time: 20:22
 * To change this template use File | Settings | File Templates.
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
        public ParseOperationResult operate(ParseState state, TokenString previousResult, List<Statement> statements);

        /**
         * Merges two ParseOperation's, executing this one, followed by the next.
         *
         * @param after The ParseOperation to execute after this one.
         * @return A chained ParseOperation, combining this one and the next.
         */
        public default ParseOperation andThen(ParseOperation after) {
            return (state, result, statements) -> {
                ParseOperationResult operationResult = operate(state, result, statements);
                return after.operate(operationResult.getState(), operationResult.getPreviousResult(), operationResult.getStatements());
            };
        }
    }

    private ParseOperation matchingOperation;

    public CompositedParserHelper(ParseOperation... operations) {
        // Chain all operations into one
        matchingOperation = operations[0];

        for (int i = 1; i < operations.length; i++) {
            matchingOperation = matchingOperation.andThen(operations[i]);
        }
    }

    public ParseOperationResult performOn(TokenString string) {
        return matchingOperation.operate(ParseState.SUCCESS, string, new ArrayList<>());
    }

}
