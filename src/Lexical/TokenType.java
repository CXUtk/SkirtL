package Lexical;

/**
 * TokenType is used for specify type of that token, which can reduce the work when parsing
 */
public enum TokenType {
    DIGIT,
    IDENTIFIER,
    KEYWORD,
    STRING,
    NULL,

    //punctuation
    SEMICOLON,
    LEFT_PARENTHESES,
    RIGHT_PARENTHESES,
    LEFT_BRACKET,
    RIGHT_BRACKET,
    LEFT_BRACE,
    RIGHT_BRACE,
    COMMA,
    DOT,

    //operators
    PLUS,
    MINUS,
    STAR,
    SLASH,
    POWER,
    ASSIGNMENT,
    LESS_THAN,
    LESS_THAN_EQUAL_TO,
    GREATER_THAN,
    GREATER_THAN_EQUAL_TO,
    EQUAL_TO,
    NOT_EQUAL_TO,
    NOT,

    EOF,
    UNKNOWN
}
