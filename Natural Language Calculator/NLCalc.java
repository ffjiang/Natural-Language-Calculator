/* Created by Frank Jiang
 * 
 * Poptip Coding Challenge Problem 5
 *
 * 
 */

import java.util.Scanner;
import java.util.LinkedList;

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

		for (int i : oper) {
			System.out.println(i);
		}

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
							so the previous group of operands is evaluated */
				tokens.add(i, interpret.evaluateOperand(groupedOperands));
				groupedOperands.clear();
				oper.add(i, 0);
				i++;	/* Skip the operator just looked at, to prevent infinite loop,
							because that operator was pushed back by the call of add() */
			}
		}

		// To account for operands at end of expression
		if (groupedOperands.size() > 0) {
			tokens.addLast(interpret.evaluateOperand(groupedOperands));
			oper.add(0);
		}

		for (Object o : tokens) {
			System.out.println("instanceof" + o.getClass());
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