

import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class SemanticAnalysis {

    // Analyzes the AST to ensure that all variables are declared and used with the correct types.
    public void checkAssignments(ProgramNode rootNode) {
        Map<String, String> functionContext = new HashMap<>();
        for (FunctionNode function : rootNode.getFunctions()) {
            checkFunction(function, functionContext);
        }
    }

    // Checks a function for type violations and variable usage.
    private void checkFunction(FunctionNode function, Map<String, String> context) {
        Map<String, String> localContext = new HashMap<>(context);
        
        // Add parameters to the local context
        for (VariableNode parameter : function.getParameters()) {
            localContext.put(parameter.getName(), parameter.getType());
        }

        // Add local variables to the local context
        for (VariableNode variable : function.getVariables()) {
            localContext.put(variable.getName(), variable.getType());
        }

        // Check the function's statements
        for (StatementNode statement : function.getStatements()) {
            checkStatement(statement, localContext);
        }
    }

    // Checks a statement for type violations and variable usage.
    private void checkStatement(StatementNode statement, Map<String, String> context) {
        if (statement instanceof AssignmentNode) {
            checkAssignment((AssignmentNode) statement, context);
        } else if (statement instanceof IfNode) {
            checkIf((IfNode) statement, context);
        } else if (statement instanceof WhileNode) {
            checkWhile((WhileNode) statement, context);
        } else if (statement instanceof RepeatNode) {
            checkRepeat((RepeatNode) statement, context);
        } else if (statement instanceof ForNode) {
            checkFor((ForNode) statement, context);
        } else if (statement instanceof FunctionCallNode) {
            checkFunctionCall((FunctionCallNode) statement, context);
        } else {
            throw new RuntimeException("Unknown statement type: " + statement.getClass().getName());
        }
    }

    // Checks an assignment statement for type violations and variable usage.
    private void checkAssignment(AssignmentNode node, Map<String, String> context) {
        // Check if the variable is in the context
        String variableName = node.getVariable();
        if (!context.containsKey(variableName)) {
            throw new RuntimeException("Undefined variable: " + variableName);
        }

        // Check if the expression has the correct type
        String expectedType = context.get(variableName);
        checkExpression(node.getValue(), expectedType, context);
    }

    // Checks an if statement for type violations and variable usage.
    private void checkIf(IfNode node, Map<String, String> context) {
        checkExpression(node.getCondition(), "boolean", context);
        checkBlock(node.getStatements(), context);

        if (node.getElsifBranch() != null) {
            checkIf(node.getElsifBranch(), context);
        } else if (node.getElseBranch() != null) {
            checkBlock(node.getElseBranch().getStatements(), context);
        }
    }

    // Checks a while statement for type violations and variable usage.
    private void checkWhile(WhileNode node, Map<String, String> context) {
        checkExpression(node.getCondition(), "boolean", context);
        checkBlock(node.getStatements(), context);
    }

    // Checks a repeat statement for type violations and variable usage.
    private void checkRepeat(RepeatNode node, Map<String, String> context) {
        checkExpression(node.getCounter(), "integer", context);
        checkBlock(node.getStatements(), context);
    }
    // Checks a for statement for type violations and variable usage.
    private void checkFor(ForNode node, Map<String, String> context) {
        checkExpression(node.getFrom(), "integer", context);
        checkExpression(node.getTo(), "integer", context);
        checkBlock(node.getStatements(), context);
    }

    // Checks a function call statement for type violations and variable usage.
    private void checkFunctionCall(FunctionCallNode node, Map<String, String> context) {
        // Check the function call's arguments
        for (ExpressionNode argument : node.getArguments()) {
            checkExpression(argument, null, context);
        }
    }

    // Checks a block of statements for type violations and variable usage.
    private void checkBlock(List<StatementNode> block, Map<String, String> context) {
        for (StatementNode statement : block) {
            checkStatement(statement, context);
        }
    }

    // Checks an expression for type violations and variable usage.
    private void checkExpression(ExpressionNode node, String expectedType, Map<String, String> context) {
        if (node instanceof VariableExpressionNode) {
            String variableName = ((VariableExpressionNode) node).getName();

            if (!context.containsKey(variableName)) {
                throw new RuntimeException("Undefined variable: " + variableName);
            }

            String actualType = context.get(variableName);
            if (expectedType != null && !expectedType.equals(actualType)) {
                throw new RuntimeException("Type mismatch: Expected " + expectedType + " but found " + actualType);
            }
        }
    }
}

