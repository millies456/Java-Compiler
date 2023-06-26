
import java.util.HashMap;
import java.util.List;
import java.beans.Expression;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {
    private Map<String, FunctionNode> functions;

    public Interpreter() {
        functions = new HashMap<>();
    }

    // lookupFunction method
    private FunctionNode lookupFunction(String functionName) {
        if (!functions.containsKey(functionName)) {
            throw new RuntimeException("Undefined function: " + functionName);
        }
        return functions.get(functionName);
    }


    // Entry point for interpreting a FunctionNode.
    public void interpretFunction(FunctionNode function) {
        // Create a HashMap to store the local variables for the function.
        Map<String, InterpreterDataType> variables = new HashMap<>();

        // Iterate through the function's variables and create new InterpreterDataType instances for each.
        for (VariableNode variable : function.getVariables()) {
            variables.put(variable.getName(), new InterpreterDataType(variable.getType()));
        }

        // Begin interpreting the function's statements, using the local variables map.
        interpretBlock(function.getStatements(), variables);
    }

    // Interpret a block of statements, given the current context of local variables.
    private void interpretBlock(List<StatementNode> statements, Map<String, InterpreterDataType> variables) {
        // Loop through each statement in the block.
        for (StatementNode statement : statements) {
            // Check the type of each statement and call the appropriate interpret method.
            if (statement instanceof IfNode) {
                interpretIf((IfNode) statement, variables);
            } else if (statement instanceof WhileNode) {
                interpretWhile((WhileNode) statement, variables);
            } else if (statement instanceof RepeatNode) {
                interpretRepeat((RepeatNode) statement, variables);
            } else if (statement instanceof ForNode) {
                interpretFor((ForNode) statement, variables);
            } else if (statement instanceof AssignmentNode) {
                interpretAssignment((AssignmentNode) statement, variables);
            } else if (statement instanceof FunctionCallNode) {
                // StatementNode should be FunctionCallNode
                interpretFunctionCall((FunctionCallNode) statement, variables);
            } else {
                // If the statement type is not recognized, throw a Exception.
                throw new RuntimeException("Unknown statement type: " + statement.getClass().getName());
            }
        }
    }

    // IfNode 
    private void interpretIf(IfNode node, Map<String, InterpreterDataType> variables) {
        // Evaluate the condition of the IfNode
        boolean condition = booleanCompare(node.getCondition(), variables);

        // If the condition is true, interpret the block of statements within the IfNode
        if (condition) {
            interpretBlock(node.getStatements(), variables);
        } else if (node.getElsifBranch() != null) {
            // If the condition is false and there is an ElseIfNode, interpret it
            interpretIf(node.getElsifBranch(), variables);
        } else if (node.getElsifBranch() != null) {
            // If the condition is false and there is an ElseNode, interpret its block of statements
            interpretBlock(node.getElsifBranch().getStatements(), variables);
        }
    }

   // WhileNode
    private void interpretWhile(WhileNode node, Map<String, InterpreterDataType> variables) {
        // Continuously evaluate the loop condition (boolean compare) and interpret the loop body
        // until the condition is no longer true.
        while (booleanCompare(node.getCondition(), variables)) {
            interpretBlock(node.getStatements(), variables);
        }
    }

    // RepeatNode
    private void interpretRepeat(RepeatNode node, Map<String, InterpreterDataType> variables) {
        int count = 0; // Initialize the loop count
        // Run the loop for the specified number of iterations 
        for (int i = 0; i < count; i++) {
            interpretBlock(node.getStatements(), variables);
        }
    }

    // ForNode
    private void interpretFor(ForNode node, Map<String, InterpreterDataType> variables) {
        // Evaluate the initial value of the loop counter and the end value
        InterpreterDataType initialValue = evaluateExpression(node.getFrom(), variables);
        InterpreterDataType endValue = evaluateExpression(node.getTo(), variables);

        // Iterate through the loop, incrementing the loop counter until the end is reached
        for (InterpreterDataType currentValue = initialValue;
            currentValue.compareTo(endValue) <= 0;
            currentValue.increment()) {

            // Update the loop counter variable in the variables map
            variables.put(node.getVariable(), currentValue);
            // Interpret the loop body
            interpretBlock(node.getStatements(), variables);
        }
    }

    // AssignmentNode
    private void interpretAssignment(AssignmentNode node, Map<String, InterpreterDataType> variables) {
        // Evaluate the expression on the right-hand side of the assignment
        InterpreterDataType value = evaluateExpression(node.getValue(), variables);
        // Update the variable in the variables map with the new value
        variables.put(node.getVariable(), value);
    }

    private void interpretFunctionCall(FunctionCallNode node, Map<String, InterpreterDataType> variables) {
        // Retrieve the function node using the lookupFunction method
        FunctionNode function = lookupFunction(node.getFunctionName());
    
        if (node.getParameters().size() != function.getParameters().size()) {
            throw new RuntimeException("Incorrect number of parameters for function: " + node.getFunctionName());
        }
    
        // Create a new list for the function's parameter values
        List<InterpreterDataType> parameterValues = new ArrayList<>();
        for (Node argument : node.getParameters()) {
            parameterValues.add(evaluateExpression(argument, variables));
        }
    
        // Execute the function and handle the results
        if (function.isBuiltIn()) {
            function.execute(parameterValues);
        } else {
            interpretFunction(function, parameterValues);
        }
    
        // Copy values back
        for (int i = 0; i < function.getVariables().size(); i++) {
            VariableNode variable = function.getVariables().get(i);
            if (variable.isVar()) {
                variables.put(variable.getName(), parameterValues.get(i));
            }
        }
    }
    

    private InterpreterDataType evaluateExpression(Node node, Map<String, InterpreterDataType> variables) {
        // Check the type of the expression node and handle each case accordingly
        if (node instanceof IntegerNode) {
            // Create a new IntegerDataType instance with the value from the IntegerNode
            return new IntegerDataType(((IntegerNode) node).getValue());
        } else if (node instanceof RealNode) {
            // Create a new RealDataType instance with the value from the RealNode
            return new RealDataType(((RealNode) node).getValue());
        } else if (node instanceof StringNode) {
            // Create a new StringDataType instance with the value from the StringNode
            return new StringDataType(((StringNode) node).getValue());
        } else if (node instanceof VariableReferenceNode) {
            // Get the name of the variable from the VariableReferenceNode
            String name = ((VariableReferenceNode) node).getName();
            // Check if the variable exists in the current scope
            if (!variables.containsKey(name)) {
                throw new RuntimeException("Undefined variable: " + name);
            }
            // Return the InterpreterDataType instance for the variable
            return variables.get(name);
        } else if (node instanceof MathOpNode) {
            // Handle the math operation case
            MathOpNode mathOpNode = (MathOpNode) node;
            // Recursively evaluate the left and right sides of the operation
            InterpreterDataType left = evaluateExpression(mathOpNode.getLeft(), variables);
            InterpreterDataType right = evaluateExpression(mathOpNode.getRight(), variables);
            // Perform the math operation and return the result
            return left.performMathOperation(mathOpNode.getOp(), right);
        } else {
            // If the expression node type is unknown, throw an exception
            throw new RuntimeException("Unknown expression type: " + node.getClass().getName());
        }
    }
    
    private boolean booleanCompare(BooleanCompareNode node, Map<String, InterpreterDataType> variables) {
        // Evaluate the left and right sides of the comparison
        InterpreterDataType left = evaluateExpression(node.getLeft(), variables);
        InterpreterDataType right = evaluateExpression(node.getRight(), variables);
    
        // Compare the left and right sides
        int comparison = left.compareTo(right);
    
        // Check the comparison type and return the result accordingly
        switch (node.getComparisonType()) {
            case EQUAL:
                return comparison == 0;
            case NOT_EQUAL:
                return comparison != 0;
            case LESS_THAN:
                return comparison < 0;
            case LESS_OR_EQUAL:
                return comparison <= 0;
            case GREATER_THAN:
                return comparison > 0;
            case GREATER_OR_EQUAL:
                return comparison >= 0;
            default:
            // If the comparison type is unknown, throw an exception
            throw new RuntimeException("Unknown comparison type: " + node.getComparisonType());
        }
    }
    
}