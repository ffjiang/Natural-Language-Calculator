/* Created by Frank Jiang
 * 
 * Poptip Coding Challenge Problem 5
 *
 * 
 */

import java.util.Scanner;
import java.util.LinkedList;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import java.util.Stack;

public class NLCalc {
	public static void main(String[] args) {
		System.out.println("Enter a simple mathematical expression:");

		Scanner sc = new Scanner(System.in);
		String whole = sc.nextLine();

		Interpreter interpret = new Interpreter();

		LinkedList<Token> tokens = interpret.analyse(whole.toLowerCase());
		System.out.println("Number of tokens:" + tokens.size());

		for (Token t : tokens) {
			System.out.println("Token: " + t.getToken());
		}

		// Determine if each token is an operand or an operator
		// 0 represents operand, 1 represents operator

		for (Token t : tokens) {
			if (interpret.isOperand(t.getToken())) {
				t.setOperand();
			} else if (interpret.isOperator(t.getToken())) {
				/* Do nothing - Interpreter.analyse() should have set all operator tokens to TokenType.OPERATOR */
			}
		}

		// Determine which operators are unary and which are binary
		classify(tokens);

		// Print out operand/unary/binary
	/*	for (Token t : tokens) {
			System.out.println(t.getType());
		} */

		// Evaluate each group of operands as a single operand
		LinkedList<String> groupedOperands = new LinkedList<String>();
		for (int i = 0; i < tokens.size(); i++) {
			Token tok = tokens.get(i);
			if (tok.getType() == Token.TokenType.OPERAND) {
				groupedOperands.add(tok.getToken());
				tokens.remove(i);
				i--;
			} else {	/* When there is an operator, the current operand ends,
							so the previous group of operands is evaluated (as long as it is nonempty) */
				if (groupedOperands.size() > 0) {
					// Add a Token holding a double value to replace the group of operands removed
					Token tok2 = new Token(interpret.evaluateOperand(groupedOperands));
					tok2.setOperand();
					tokens.add(i, tok2);

					groupedOperands.clear();
					i++;	/* Skip the operator just looked at, to prevent infinite loop,
								because that operator was pushed back by the call of add() */
				}
			}
		}

		// To account for operands at end of expression
		if (groupedOperands.size() > 0) {
			Token tok2 = new Token(interpret.evaluateOperand(groupedOperands));
			tok2.setOperand();
			tokens.addLast(tok2);
		}

		// Display the operands and operators to ensure they are correct
		for (Token tok : tokens) {
			if (tok.isOperand()) {
				System.out.println("Operand: " + tok.getValue());
			} else {
				System.out.println("Operator: " + tok.getToken());
			}
		}

		// Account for the effect of unary operators on operands
		int multiplier = 1; // 1 or -1 at all times
		for (int i = 0; i < tokens.size(); i++) {
			Token tok = tokens.get(i);
			if (tok.getType() == Token.TokenType.UNARY) {
				multiplier *= -1;
				tokens.remove(i);
				i--;
			} else if (tok.isOperand() && multiplier == -1) {
				tok.setValue(tok.getValue() * multiplier);
				multiplier = 1;
			} else if (tok.getType() == Token.TokenType.BINARY && multiplier != 1) { // Unary operators cannot come bfore binary operators
				System.out.println("Unary operators cannot come before binary operators");
			}
		}


		// Print out expression.
		String expression = "";
		for (Token tok : tokens) {
			if (tok.isOperator()) {
				String operator;
				if (interpret.operators.containsKey(tok.getToken())) {
					operator = "" + interpret.operators.get(tok.getToken());
				} else {
					operator = tok.getToken();
				}
				expression += operator;
			} else if (tok.isOperand()) {
				expression += "(" + tok.getValue() + ")"; // The bracks account for negative numbers
			} else {
				System.out.println("Error converting expression into String");
			}
		}

		System.out.println("Expression: " + expression);

	/* ------------------------------------- */
		// Calculation time!

		// Convert expression from infix to postfix
		LinkedList<String> postfixExpr = NLCompute.postfix(tokens);

		// Evaluate the postfix expression
		double result = NLCompute.evalPostfix(postfixExpr);


		System.out.println("Result: " + result);



		// Cheap ScriptEngine Calculation
		/*
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");

		try {
			System.out.println("Result: " + engine.eval(expression));
		} catch (Exception e) {
			System.out.println("Script failed");
		}
		*/
		


		/* THINGS TO DO:
			- Finish implementing evaluation of operands

			- Implement more functions, like differentiation or integration */

	}

	/*--------------------------------------------------------------------------------------------------------*/

	/* Classify each operator in the oper LinkedList (each 1 value)
		as either a unary operator (1) or a binary operator (2) */
	public static void classify(LinkedList<Token> tokens) {
		// There are two types of arithmetic expression: unary (+,- only) and binary (+,- =,*,/,%).
		// From the order of the oper LinkedList, classify the operators
		Token.TokenType prev = Token.TokenType.UNARY;   // Start start is analagous to previous token being unary operator
		int length = tokens.size();
		for (int i = 0; i < length; i++) {
			Token tok = tokens.get(i);
			if (tok.isOperand()) {
				prev = Token.TokenType.OPERAND;
			} else if (tok.isOperator()) {
				if (prev == Token.TokenType.OPERAND) {		// If the previous token was an operand,
					tok.setBinary();		// then this operator is binary.
					prev = Token.TokenType.BINARY;
				} else if (prev == Token.TokenType.UNARY) {	// If the previous token was a unary operator,
					tok.setUnary();						// then this operator is unary
				} else if (prev == Token.TokenType.BINARY) {   	 // If the previous token was a binary operator,
					tok.setUnary(); 			  			 // then this operator is unary.
					prev = Token.TokenType.UNARY;
				}						
			} else {
				throw new RuntimeException("Token is neither operator nor operand.");
			}
		}
	}
}