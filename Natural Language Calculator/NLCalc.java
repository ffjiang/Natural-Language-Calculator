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

		LinkedList tokens = interpret.analyse(whole.toLowerCase());
		System.out.println("Size of tokens:" + tokens.size());

		for (Object o : tokens) {
			if (o instanceof String) {
				System.out.println("String: " + (String)o);
			} else if (o instanceof Token) {
				System.out.println("Token: " + ((Token)o).token);
			}
		}

		// Remove strings
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i) instanceof String) {
				tokens.remove(i);
				i--;
			}
		}

		// Determine if each token is an operand or an operator
		// 0 represents operand, 1 represents operator
		LinkedList<Integer> oper = new LinkedList<Integer>();

		for (Object o : tokens) {
			if (o instanceof Token) {
				if (interpret.isOperand(((Token)o).token)) {
					oper.addLast(0);
				} else if (interpret.isOperator(((Token)o).token)) {
					oper.addLast(1);
				}
			} else {
				throw new RuntimeException("There are non-Token objects in the tokens LinkedList.");
			}
		}

		// Determine which operators are unary and which are binary
		classify(oper);

		// Print out the values of oper
	/*	for (int i : oper) {
			System.out.println(i);
		} */

		// Evaluate each group of operands as a single operand
		if (tokens.size() != oper.size()) {
			throw new RuntimeException("tokens and oper not of same length.");
		}
		LinkedList<String> groupedOperands = new LinkedList<String>();
		for (int i = 0; i < oper.size(); i++) {
			if (oper.get(i) == 0) {
				groupedOperands.add(((Token)tokens.get(i)).token);
				tokens.remove(i);
				oper.remove(i);
				i--;
			} else {	/* When there is an operator, the current operand ends,
							so the previous group of operands is evaluated (as long as it is nonempty) */
				if (groupedOperands.size() > 0) {
					tokens.add(i, interpret.evaluateOperand(groupedOperands));
					groupedOperands.clear();
					oper.add(i, 0);
					i++;	/* Skip the operator just looked at, to prevent infinite loop,
								because that operator was pushed back by the call of add() */
				}
			}
		}

		// To account for operands at end of expression
		if (groupedOperands.size() > 0) {
			tokens.addLast(interpret.evaluateOperand(groupedOperands));
			oper.add(0);
		}

		for (Object o : tokens) {
			if (o instanceof Double) {
				System.out.println("Operand: " + (Double)o);
			} else {
				System.out.println("Operator: " + ((Token)o).token);
			}
		}

		/* Print out values of oper again to check that there are no
			 two operands next to each other. */
		for (int i : oper) {
			System.out.println(i);
		}

		// Account for the effect of unary operators on operands
		int multiplier = 1; // 1 or -1 at all times
		for (int i = 0; i < tokens.size(); i++) {
			if (oper.get(i) == 1) {
				multiplier *= -1;
				oper.remove(i);
				tokens.remove(i);
				i--;
			} else if (oper.get(i) == 0 && multiplier == -1) {
				tokens.set(i, (Double)tokens.get(i) * multiplier);
				multiplier = 1;
			} else if (oper.get(i) == 2 && multiplier != 1) { // Unary operators cannot come bfore binary operators
				System.out.println("Unary operators cannot come before binary operators");
			}
		}


		// Convert into postfix notation using Djikstra's Shunting Yard Algorithm

		Stack operatorStack = new Stack();
		LinkedList outputQueue = new LinkedList();


		for (Object o : tokens) {
			if (o instanceof Token) {
				String operator;
				if (interpret.operators.containsKey(((Token)o).token)) {
					operator = "" + interpret.operators.get(((Token)o).token);
				} else {
					operator = ((Token)o).token;
				}

				/* Pop operators off stack until operator at top is
					of lower precedence than the current operator */
				if (operator.equals("+") || operator.equals("-")) {
					while (operatorStack.size() > 0) {
						outputQueue.addLast(operatorStack.pop());
					}
					operatorStack.push(operator);
				} else if (operator.equals("*") || operator.equals("/") || operator.equals("%")) {
					while (!operatorStack.peek().equals("+") && !operatorStack.peek().equals("-")) {
						outputQueue.addLast(operatorStack.pop());
					}
					operatorStack.push(operator);
				}

			} else if (o instanceof Double) { // Values are simply moved to outputQueue
				outputQueue.add(o);
			}
		}



/* ------------------------------------- */
		// Calculation time!

	/*	int length = tokens.size()
		String operator1 = "";
		String operator2 = "";
		double operand1;
		double operand2;

		for (int i = 0; i < length; i++) {
			if (tokens.get(i) instanceof Token) {
				if (operator1 == "") {
					operator1 = tokens.get(i).token;
				} else {
					operator2 = tokens.get(i).token;
				}
			} else if (tokens.get(i) instanceof Double) {
				operand = tokens.get(i);
			}
		} */

		// Cheap ScriptEngine Calculation

		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");

		String expression = "";
		for (Object o : tokens) {
			if (o instanceof Token) {
				String operator;
				if (interpret.operators.containsKey(((Token)o).token)) {
					operator = "" + interpret.operators.get(((Token)o).token);
				} else {
					operator = ((Token)o).token;
				}
				expression += operator;
			} else if (o instanceof Double) {
				expression += "(" + o + ")"; // The bracks account for negative numbers
			} else {
				System.out.println("Error converting expression into String for ScriptEngine parsing");
			}
		}

		System.out.println("Expression: " + expression);
		try {
			System.out.println("Result: " + engine.eval(expression));
		} catch (Exception e) {
			System.out.println("Error evaluating expression in ScriptException");
		}

		/* THINGS TO DO:
			- Finish implementing evaluation of operands
			- Implement CALCULATIOM
			- Implement things like mod/modulus, divide/divided by
			- Implement order of operations

			- Implement more functions, like differentiation or integration */

	}

	/* Classify each operator in the oper LinkedList (each 1 value)
		as either a unary operator (1) or a binary operator (2) */
	public static void classify(LinkedList<Integer> oper) {
		// There are two types of arithmetic expression: unary (+,- only) and binary (+,- =,*,/,%).
		// From the order of the oper LinkedList, classify the operators
		int prev = 1;   // Start start is analagous to previous token being unary operator
		for (int i = 0; i < oper.size(); i++) { 		
			if (oper.get(i) == 0) {
				prev = 0;
			} else if (oper.get(i) == 1) {
				if (prev == 0) {		// If the previous token was an operand,
					oper.set(i, 2);		// then this operator is binary.
					prev = 2;
				} else if (prev == 1) {	
					/* If the previous token was a unary operator,
						 then this opeartor is unary, so do nothing */
				} else if (prev == 2) {   // If the previous token was a binary operator,
					prev = 1; 			  // then this operator is unary.
				}						
			} else {
				throw new RuntimeException("oper classifier not set to 0 or 1.");
			}
		}
	}
}