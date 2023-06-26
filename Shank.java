

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Shank {
    public static void main(String[] args) {
        // Check if the number of arguments passed is exactly 1 (filename)
        if (args.length != 1) {
            System.out.println("Error: expecting one argument (filename)");
            return;
        }

        // Get the filename from the arguments passed
        String filename = args[0];
        List<String> lines;
        try {
            // Read all the lines in the file
            lines = Files.readAllLines(Paths.get(filename));
        } catch (IOException e) {
            System.out.println("Error: unable to read file");
            return;
        }

        // Create an instance of the Lexer class
        Lexer lexer = new Lexer();
        for (String line : lines) {
            try {
                // Pass the line to the lexer to tokenize
                lexer.lex(line);
            } catch (Exception e) {
                // Print the exception message if an error occurs
                System.out.println("Exception: " + e.getMessage());
                return;
            }
        }

        // Get the tokens from the lexer
        List<Token> tokens = lexer.getTokens();
        // Create an instance of the Parser class with the tokens
        Parser parser = new Parser(tokens);

        try {
            // Call the parse() method of the Parser instance to generate the AST
            ProgramNode ast = (ProgramNode) parser.parse();
            
            // Create an instance of the SemanticAnalysis class
            SemanticAnalysis semanticAnalysis = new SemanticAnalysis();
            // Perform semantic analysis on the AST
            semanticAnalysis.checkAssignments(ast);
        
            // Create an instance of the Interpreter class with the AST
            Interpreter interpreter = new Interpreter(ast);
            // Execute the program using the interpreter
            interpreter.executeProgram();

        } catch (SyntaxErrorException e) {
            // If a syntax error occurs during parsing, print the error message
            System.out.println("Syntax Error: " + e.getMessage());
            return;
        }
    }
}
