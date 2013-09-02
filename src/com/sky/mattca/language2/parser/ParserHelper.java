package com.sky.mattca.language2.parser;

import com.sky.mattca.language2.tokenizer.Token;
import com.sky.mattca.language2.tokenizer.TokenString;
import com.sky.mattca.language2.tokenizer.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Matt
 * Date: 02/09/13
 * Time: 21:02
 * To change this template use File | Settings | File Templates.
 */
public class ParserHelper {

    public static class ParseOperationResult {
        public static final ParseOperationResult UNSUCCESSFUL = new ParseOperationResult();

        public boolean successful;
        public TokenString remainder;

        public ParseOperationResult() {
            successful = false;
        }

        public ParseOperationResult(boolean successful, TokenString remainder) {
            this.successful = successful;
            this.remainder = remainder;
        }

    }

    @FunctionalInterface
    private interface ParserOperation<T> {
        public ParseOperationResult invoke(final TokenString token, final T comparisonValue);
    }

    private enum ParserOperationType {
        MATCH_STRING,
        MATCH_TYPE,
        MATCH_EXPRESSION,
        MATCH_CONDITION,
        MATCH_EITHER
    }

    private class ParserOperationHandler<T> {

        public TokenString currentString;
        public ParseOperationResult result;
        public ParserOperationType type;

        public ParserOperation<T> operation;

        public ParserOperationHandler(final ParserOperation<T> operation, final ParserOperationType type) {
            this.operation = operation;
        }

        public ParserOperationHandler(final ParserOperationType type) {
            this.type = type;
        }

        public ParserOperationHandler(final TokenString currentString, final ParserOperation<T> operation) {
            this.currentString = currentString;
            this.operation = operation;
        }

        public TokenString getCurrentString() {
            return currentString;
        }

        public ParseOperationResult getResult() {
            return result;
        }

        public ParserOperationType getType() {
            return type;
        }

    }

    private List<ParserOperationHandler> operations;
    private List<Statement> matchingResult;

    private ParserHelper() {
        operations = new ArrayList<>();
        matchingResult = new ArrayList<>();
    }

    public static ParserHelper start() {
        return new ParserHelper();
    }

    public ParserHelper matchConsume(final String contentToMatch) {
        ParserOperationHandler<String> parserOperationHandler = new ParserOperationHandler<>((t, s) -> {
            ParseOperationResult result;

            if (t.peek().contents.equals(s)) {
                Token temp = t.consume();

                result = new ParseOperationResult(true, t);
                matchingResult.add(new Statement(temp));
                return result;
            } else {
                return ParseOperationResult.UNSUCCESSFUL;
            }
        }, ParserOperationType.MATCH_STRING);

        operations.add(parserOperationHandler);

        return this;
    }

    public ParserHelper matchConsume(final TokenType typeToMatch) {
        ParserOperationHandler<TokenType> parserOperationHandler = new ParserOperationHandler<>((t, type) -> {
            ParseOperationResult result;

            if (t.match(type)) {
                Token temp = t.consume();

                result = new ParseOperationResult(true, t);
                matchingResult.add(new Statement(temp));
                return result;
            } else {
                return ParseOperationResult.UNSUCCESSFUL;
            }
        }, ParserOperationType.MATCH_TYPE);

        operations.add(parserOperationHandler);

        return this;
    }

    public ParserHelper whitespace() {
        return matchConsume(TokenType.WHITESPACE);
    }

    public ParserHelper matchExpression() {
        return this;
    }

    public ParserHelper matchConditional() {
        return this;
    }

    public ParserHelper either() {
        operations.add(new ParserOperationHandler(ParserOperationType.MATCH_EITHER));

        return this;
    }

    public ParseOperationResult performMatching(TokenString string) {
        return ParseOperationResult.UNSUCCESSFUL;
    }

}
