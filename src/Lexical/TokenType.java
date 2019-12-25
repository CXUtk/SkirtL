package Lexical;

/**
 * TokenType is used for specify type of that token, which can reduce the work when parsing
 */
public enum TokenType {
    DIGIT,
    IDENTIFIER,
    PRINT,
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
    AND,
    OR,
    IF,
    ELSE,
    FOR,
    WHILE,
    RETURN,
    FUNCTION,
    THIS,
    SUPER,


    FALSE,
    TRUE,
    VAR,

    CLASS,

    EOF,
    UNKNOWN,
}
