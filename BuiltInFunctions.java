
import java.util.List;
import java.util.Scanner;



abstract class BuiltInFunction extends FunctionNode {
    //constructor
    public BuiltInFunction() {
        super("", null, null, null, null);
    }

    public abstract void execute(List<InterpreterDataType> parameters);

    @Override
    public boolean isVariadic() {
        return false;
    }
}

// BuiltInRead extends BuiltInFunction
class BuiltInRead extends BuiltInFunction {

    @Override
    public void execute(List<InterpreterDataType> parameters) {
        Scanner scanner = new Scanner(System.in);
        try {
            for (InterpreterDataType parameter : parameters) {
                String input = scanner.nextLine();
                parameter.fromString(input);
            }
        } finally {
            scanner.close();
        }
    }

    @Override
    public boolean isVariadic() {
        return true;
    }
}

// BuiltInWrite extends BuiltInFunction
class BuiltInWrite extends BuiltInFunction {
    @Override
    public void execute(List<InterpreterDataType> parameters) {
        for (InterpreterDataType parameter : parameters) {
            System.out.print(parameter.toString());
        }
    }

    @Override
    public boolean isVariadic() {
        return true;
    }
}

// BuiltInWriteln extends BuiltInFunction
class BuiltInWriteln extends BuiltInFunction {
    @Override
    public void execute(List<InterpreterDataType> parameters) {
        for (InterpreterDataType parameter : parameters) {
            System.out.print(parameter.toString());
        }
        System.out.println();
    }

    @Override
    public boolean isVariadic() {
        return true;
    }
}

// BuiltInAbs extends BuiltInFunction
class BuiltInAbs extends BuiltInFunction {
    @Override
    public void execute(List<InterpreterDataType> parameters) {
        if (parameters.size() != 1 || !(parameters.get(0) instanceof IntegerDataType)) {
            throw new IllegalArgumentException("Abs function requires a single IntegerDataType parameter");
        }
        IntegerDataType intValue = (IntegerDataType) parameters.get(0);
        intValue.setValue(Math.abs(intValue.getValue()));
    }
}

// BuiltInSqr extends BuiltInFunction
class BuiltInSqr extends BuiltInFunction {
    @Override
    public void execute(List<InterpreterDataType> parameters) {
        if (parameters.size() != 1 || !(parameters.get(0) instanceof RealDataType)) {
            throw new IllegalArgumentException("Sqr function requires a single RealDataType parameter");
        }
        RealDataType realValue = (RealDataType) parameters.get(0);
        realValue.setValue((float)Math.sqrt(realValue.getValue()));
    }
}

// BuiltInSin extends BuiltInFunction
class BuiltInSin extends BuiltInFunction {
    @Override
    public void execute(List<InterpreterDataType> parameters) {
        if (parameters.size() != 1 || !(parameters.get(0) instanceof RealDataType)) {
            throw new IllegalArgumentException("Sin function requires a single RealDataType parameter");
        }
        RealDataType realValue = (RealDataType) parameters.get(0);
        realValue.setValue((float)Math.sin(realValue.getValue()));
    }
}

// BuiltInCos extends BuiltInFunction
class BuiltInCos extends BuiltInFunction {
    @Override
    public void execute(List<InterpreterDataType> parameters) {
        if (parameters.size() != 1 || !(parameters.get(0) instanceof RealDataType)) {
            throw new IllegalArgumentException("Cos function requires a single RealDataType parameter");
        }
        RealDataType realValue = (RealDataType) parameters.get(0);
        realValue.setValue((float)Math.cos(realValue.getValue()));
    }
}

// BuiltInExp extends BuiltInFunction
class BuiltInExp extends BuiltInFunction {
    @Override
    public void execute(List<InterpreterDataType> parameters) {
        if (parameters.size() != 1 || !(parameters.get(0) instanceof RealDataType)) {
            throw new IllegalArgumentException("Exp function requires a single RealDataType parameter");
        }
        RealDataType realValue = (RealDataType) parameters.get(0);
        realValue.setValue((float)Math.exp(realValue.getValue()));
    }
}

// BuiltInLn extends BuiltInFunction
class BuiltInLn extends BuiltInFunction {
    @Override
    public void execute(List<InterpreterDataType> parameters) {
        if (parameters.size() != 1 || !(parameters.get(0) instanceof RealDataType)) {
            throw new IllegalArgumentException("Ln function requires a single RealDataType parameter");
        }
        RealDataType realValue = (RealDataType) parameters.get(0);
        realValue.setValue((float)Math.log(realValue.getValue()));
    }
}

// BuiltInSubstring extends BuiltInFunction
class BuiltInSubstring extends BuiltInFunction {
    @Override
    public void execute(List<InterpreterDataType> parameters) {
        if (parameters.size() != 3 || !(parameters.get(0) instanceof StringDataType) ||
            !(parameters.get(1) instanceof IntegerDataType) || !(parameters.get(2) instanceof IntegerDataType)) {
            throw new IllegalArgumentException("Substring function requires a StringDataType and two IntegerDataType parameters");
        }
        StringDataType strValue = (StringDataType) parameters.get(0);
        IntegerDataType startIndex = (IntegerDataType) parameters.get(1);
        IntegerDataType endIndex = (IntegerDataType) parameters.get(2);
        String subStr = strValue.toString().substring(startIndex.getValue(), endIndex.getValue());
        strValue.fromString(subStr);
    }
}

// BuiltInInteger extends BuiltInFunction
class BuiltInInteger extends BuiltInFunction {
    @Override
    public void execute(List<InterpreterDataType> parameters) {
        if (parameters.size() != 1) {
            throw new IllegalArgumentException("Integer function requires a single parameter");
        }
        InterpreterDataType value = parameters.get(0);
        if (value instanceof IntegerDataType) {
            return;
        } else if (value instanceof RealDataType) {
            int intValue = (int) ((RealDataType) value).getValue();
            value = new IntegerDataType(intValue);
        } else if (value instanceof StringDataType) {
            int intValue = Integer.parseInt(value.toString());
            value = new IntegerDataType(intValue);
        } else {
            throw new IllegalArgumentException("Cannot convert value to IntegerDataType");
        }
        parameters.set(0, value);
    }
}

// BuiltInReal extends BuiltInFunction
class BuiltInReal extends BuiltInFunction {
    @Override
    public void execute(List<InterpreterDataType> parameters) {
        if (parameters.size() != 1) {
            throw new IllegalArgumentException("Real function requires a single parameter");
        }
        InterpreterDataType value = parameters.get(0);
        if (value instanceof RealDataType) {
            return;
        } else if (value instanceof IntegerDataType) {
            float realValue = ((IntegerDataType) value).getValue();
            value = new RealDataType(realValue);
        } else if (value instanceof StringDataType) {
            float realValue = Float.parseFloat(value.toString());
            value = new RealDataType(realValue);
        } else {
            throw new IllegalArgumentException("Cannot convert value to RealDataType");
        }
        parameters.set(0, value);
    }
}