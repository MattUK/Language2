package com.sky.mattca.language2.tokenizer2;

import com.sky.mattca.language2.Handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Matt
 * Date: 12/08/13
 * Time: 21:33
 * To change this template use File | Settings | File Templates.
 */
public class Tokenizer {

    private int currentPosition, currentLine;
    private List<String> source;

    public Tokenizer() {
        currentLine = 0;
        currentPosition = 0;
        source = new ArrayList<>();
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

    private TokenString tokenizeCurrentLine() {
        TokenString tokenString = new TokenString();
        boolean finishedLine = false;

        TokenType[] types = TokenType.values();
        Arrays.sort(types, (o1, o2) -> {
            if (o1.length() > o2.length()) {
                return -1;
            } else if (o1.length() == o2.length()) {
                return 0;
            } else {
                return 1;
            }
        });

        while (!finishedLine) {
            if (source.get(currentLine).length() == 0) {
                finishedLine = true;
                continue;
            }

            boolean matched = false;
            for (TokenType t : types) {
                if (t.match(source.get(currentLine))) {
                    int position = currentPosition;
                    consume(t.length());
                    tokenString.append(new Token(t, t.tokenValue, currentLine, position));
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                if (Character.isDigit(source.get(currentLine).charAt(0))) {
                    tokenString.append(tokenizeNumericalLiteral());
                } else if (isIdentifierCharacter(source.get(currentLine).charAt(0))) {
                    tokenString.append(tokenizeIdentifier());
                } else if (source.get(currentLine).charAt(0) == '"') {
                    tokenString.append(tokenizeStringLiteral());
                } else if (Character.isWhitespace(source.get(currentLine).charAt(0))) {
                    tokenString.append(new Token(TokenType.WHITESPACE, "", currentLine, currentPosition));
                    consume(1);
                } else {
                    Handler.reportError(new Handler.BuildError(3, currentLine, currentPosition, Character.toString(source.get(currentLine).charAt(0))));
                    consume(1);
                }
            }

            Handler.abortIfErrors();
        }

        return tokenString;
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
