package com.sky.mattca.language2.tokenizer;

import com.sky.mattca.language2.Handler;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    public int currentLine, currentPosition;
    private List<String> source;

    public Tokenizer() {
        source = new ArrayList<>();
        currentLine = 0;
        currentPosition = 0;
    }

    public void addSourceLine(String line) {
        source.add(line);
    }

    private boolean isIdentifierCharacter(char c) {
        return Character.isLetterOrDigit(c) | (c == '_');
    }

    private void consume(int length) {
        source.set(currentLine, source.get(currentLine).substring(length));
        currentPosition += length;
    }

    private Token tokenizeIdentifier() {
        String identifierLine = source.get(currentLine);
        int identifierLength = 0;
        do {
            identifierLength ++;
        } while (identifierLine.length() > identifierLength && isIdentifierCharacter(identifierLine.charAt(identifierLength)));

        Token token = new Token(TokenType.IDENTIFIER, identifierLine.substring(0, identifierLength), currentLine, currentPosition);
        consume(identifierLength);

        if (token.contents.equals("true") || token.contents.equals("false")) {
            token.type = TokenType.BOOL_LITERAL;
        }

        return token;
    }

    private Token tokenizeStringLiteral() {
        String stringLine = source.get(currentLine);
        int stringLength = 0;
        int quoteCount = 0;
        do {
            if (stringLine.charAt(stringLength) == '"') {
                quoteCount ++;
            }
            stringLength ++;
        } while (stringLine.length() > stringLength && quoteCount < 2);

        if (quoteCount != 2) {
            Handler.reportError(new Handler.BuildError(1, currentLine, currentPosition));
            return new Token();
        }

        Token token = new Token(TokenType.STRING_LITERAL, stringLine.substring(0, stringLength), currentLine, currentPosition);
        consume(stringLength);
        return token;
    }

    private Token tokenizeNumericalLiteral() {
        String numberLine = source.get(currentLine);
        int numberLength = 0;
        int pointCount = 0;
        boolean hasFractionalPart = false;

        do {
            if (numberLine.charAt(numberLength) == '.') {
                pointCount ++;
                if (numberLine.length() > numberLength + 1 && Character.isDigit(numberLine.charAt(numberLength + 1))) {
                    hasFractionalPart = true;
                }
            }

            numberLength ++;
        } while (numberLine.length() > numberLength && (Character.isDigit(numberLine.charAt(numberLength)) || numberLine.charAt(numberLength) == '.'));

        if (pointCount > 1) {
            Handler.reportError(new Handler.BuildError(2, currentLine, currentPosition));
            return new Token();
        }

        if (!hasFractionalPart && pointCount > 0) {
            Handler.reportError(new Handler.BuildError(4, currentLine, currentPosition));
            return new Token();
        }

        Token token = new Token((pointCount == 1) ? TokenType.FLOAT_LITERAL : TokenType.INTEGER_LITERAL, numberLine.substring(0, numberLength), currentLine, currentPosition);
        consume(numberLength);
        return token;
    }

    private char[] operators = {'+', '-', '*', '/', '%', '^', ':', '=', '>', '<', '&', '|', '~'};

    private boolean isOperator(char c) {
        for (char o : operators) {
            if (c == o) {
                return true;
            }
        }
        return false;
    }

    private Token tokenizeOperator() {
        String operatorLine = source.get(currentLine);
        boolean isOp = isOperator(operatorLine.charAt(0));

        if (!isOp) {
            Handler.reportError(new Handler.BuildError(3, currentLine, currentPosition, Character.toString(operatorLine.charAt(0))));
            return new Token();
        }

        consume(1);

        TokenType type = TokenType.NONE;
        String contents = "";
        char operator = operatorLine.charAt(0);
        switch (operator) {
            case '+':
                type = TokenType.OP_ADD;
                break;
            case '-':
                type = TokenType.OP_SUB;
                break;
            case '*':
                type = TokenType.OP_MUL;
                break;
            case '/':
                type = TokenType.OP_DIV;
                break;
            case '%':
                type = TokenType.OP_MOD;
                break;
            case '^':
                type = TokenType.OP_POW;
                break;
            case ':':
                type = TokenType.OP_ASSIGNMENT;
                break;
            case '>':
                type = TokenType.OP_GREATER_THAN;
                break;
            case '<':
                type = TokenType.OP_LESS_THAN;
                break;
            case '&':
                type = TokenType.OP_LOGICAL_AND;
                break;
            case '|':
                type = TokenType.OP_LOGICAL_OR;
                break;
            case '~':
                type = TokenType.OP_CONCATENATE;
                break;
            default:
                break;
        }

        contents = Character.toString(operator);

        operatorLine = source.get(currentLine);
        if (operatorLine.length() > 0 && operatorLine.charAt(0) == '=') {
            switch (operator) {
                case '=':
                    type = TokenType.OP_DOUBLE_EQUALS;
                    contents = "==";
                    consume(1);
                    break;
                case '>':
                    type = TokenType.OP_GREATER_EQ_TO;
                    contents = ">=";
                    consume(1);
                    break;
                case '<':
                    type = TokenType.OP_LESS_EQ_TO;
                    contents = "<=";
                    consume(1);
                    break;
                default:
                    Handler.reportError(new Handler.BuildError(3, currentLine, currentPosition, "="));
                    return new Token();
            }
        }

        if (contents.equals("=")) {
            Handler.reportError(new Handler.BuildError(5, currentLine, currentPosition));
        }

        return new Token(type, contents, currentLine, currentPosition);
    }

    private TokenString tokenizeCurrentLine() {
        TokenString line = new TokenString();
        boolean finishedLine = false;

        while (!finishedLine) {
            if (source.get(currentLine).length() == 0) {
                finishedLine = true;
                continue;
            }

            char c = source.get(currentLine).charAt(0);
            if (source.get(currentLine).length() > 2 && (c == '/' && source.get(currentLine).charAt(1) == '/')) {
                finishedLine = true; // Skip line
                continue;
            } else if (Character.isDigit(c)) {
                line.append(tokenizeNumericalLiteral());
            } else if (Character.isLetter(c) || c == '_') {
                line.append(tokenizeIdentifier());
            } else if (c == '"') {
                line.append(tokenizeStringLiteral());
            } else if (isOperator(c)) {
                line.append(tokenizeOperator());
            } else if (c == ',') {
                line.append(new Token(TokenType.SEPARATOR, ",", currentLine, currentPosition));
                consume(1);
            } else if (c == '(') {
                line.append(new Token(TokenType.OPEN_BRACKET, "(", currentLine, currentPosition));
                consume(1);
            } else if (c == ')') {
                line.append(new Token(TokenType.CLOSE_BRACKET, ")", currentLine, currentPosition));
                consume(1);
            } else if (c == '[') {
                line.append(new Token(TokenType.OPEN_SQUARE_BRACKET, "[", currentLine, currentPosition));
                consume(1);
            } else if (c == ']') {
                line.append(new Token(TokenType.CLOSE_SQUARE_BRACKET, "]", currentLine, currentPosition));
                consume(1);
            } else if (c == '{') {
                line.append(new Token(TokenType.OPEN_CURVY_BRACE, "{", currentLine, currentPosition));
                consume(1);
            } else if (c == '}') {
                line.append(new Token(TokenType.CLOSED_CURVY_BRACE, "}", currentLine, currentPosition));
                consume(1);
            } else if (c == ';') {
                line.append(new Token(TokenType.END_STATEMENT, ";", currentLine, currentPosition));
                consume(1);
            } else if (c == '.') {
                line.append(new Token(TokenType.PERIOD, ".", currentLine, currentPosition));
                consume(1);
            } else if (Character.isWhitespace(c)) {
                line.append(new Token(TokenType.WHITESPACE, " ", currentLine, currentPosition));
                consume(1);
            } else {
                Handler.reportError(new Handler.BuildError(3, currentLine, currentPosition, Character.toString(source.get(currentLine).charAt(0))));
                consume(1);
            }

            Handler.abortIfErrors();

        }

        return line;
    }

    public List<TokenString> start() {
        List<TokenString> lines = new ArrayList<>();

        for (int i = 0; i < source.size(); i++) {
            currentLine = i;
            currentPosition = 0;
            lines.add(tokenizeCurrentLine());
        }

        Handler.abortIfErrors();
        Handler.reportStatus("Lexer complete");

        return lines;
    }

}