
import java.util.List;

public abstract class InterpreterDataType {
    public abstract String toString();
    public abstract void fromString(String input);
}

class IntegerDataType extends InterpreterDataType {
    private int value;

    public IntegerDataType(int value) {
        this.value = value;
        
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public void fromString(String input) {
        value = Integer.parseInt(input);
    }
}

// RealDataType
class RealDataType extends InterpreterDataType {
    private float value;

    public RealDataType(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Float.toString(value);
    }

    @Override
    public void fromString(String input) {
        value = Float.parseFloat(input);
    }
}

// ArrayDataType
class ArrayDataType<T extends InterpreterDataType> extends InterpreterDataType {
    private List<T> value;

    public ArrayDataType(List<T> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public void fromString(String input) {
        throw new IllegalArgumentException("FromString is not supported for ArrayDataType");
    }
}

// StringDataType
class StringDataType extends InterpreterDataType {
    private String value;

    public StringDataType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public void fromString(String input) {
        value = input;
    }
}

// CharacterDataType
class CharacterDataType extends InterpreterDataType {
    private char value;

    public CharacterDataType(char value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Character.toString(value);
    }

    @Override
    public void fromString(String input) {
        if (input.length() != 1) {
            throw new IllegalArgumentException("Input must be a single character");
        }
        value = input.charAt(0);
    }
}

// BooleanDataType
class BooleanDataType extends InterpreterDataType {
    private boolean value;

    public BooleanDataType(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    @Override
    public void fromString(String input) {
        value = Boolean.parseBoolean(input);
    }
}

