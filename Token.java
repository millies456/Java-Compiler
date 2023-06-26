

public class Token {

    // enumeration of TokenType
    public enum Type {
        INTEGER, FLOAT, WORD, STRING, STRINGLITERAL, CHARACTERLITERAL, 
        INDENT, DEDENT, 
        PLUS, MINUS, MULTIPLY, DIVIDE, 
        EQ, NEQ, GT, GTE, LT, LTE, 
        ASSIGN, 
        LEFT_PAREN, RIGHT_PAREN, 
        COMMA, SEMICOLON, 
        IF, ELSE, FOR, WHILE, DO, BREAK, CONTINUE, 
        INT, DOUBLE, RETURN, CHAR, CASE, SIZEOF, 
        LONG, SHORT, TYPEDEF, SWITCH, UNSIGNED, VOID, STATIC, 
        STRUCT, GOTO, DEFAULT, 
        LEFT_BRACE, RIGHT_BRACE, LEFT_BRACKET, RIGHT_BRACKET, 
        PUNCTUATION, NUMBER, MODULO, COLON, 
        END_OF_LINE, IDENTIFIER, DEFINE, VARIABLE, CONSTANT, TRUE, FALSE, 
        BOOLEAN, EQUAL, GREATER_EQUAL, NOT_EQUAL, GREATER_THAN, LESS_EQUAL, LESS_THAN, FROM, REAL, TO, THEN, ELSIF, REPEAT, UNTIL, INTEGER_LITERAL, REAL_LITERAL, VARIABLE_DECLARATION,  
    }
    
    private Type type;
    private String value;
    private int lineNumber;

    // constructor to initialize tokenType and value
    public Token(Type type, String value, int lineNumber) {
        this.type = type;
        this.value = value;
        this.lineNumber = lineNumber;
    }

     // getter for tokenType
    public Type getType() {
        return type;
    }
    
    // getter for value
    public String getValue() {
        return value;
    }
    
    //get line number
    public int getLineNumber() {
        return lineNumber;
    }
    
    // toString method to return the string representation of the Token object
    @Override
    public String toString() {
        return "Token{" + "type=" + type + ", value='" + value + '\'' + ", lineNumber=" + lineNumber + '}';
    }
}
