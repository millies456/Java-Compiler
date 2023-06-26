
 public class SyntaxErrorException extends Exception {
    private int tokenIndex;

    public SyntaxErrorException(String message, int tokenIndex) {
        super(message);
        this.tokenIndex = tokenIndex;
    }

    public int getTokenIndex() {
        return tokenIndex;
    }
}


