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

		LinkedList tokens = interpret.analyse(whole);
		System.out.println(tokens.size());

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

		// There are two types of arithmetic exprssion: unary and binary.
		// From the order of the oper LinkedList, classify the operators
		for (int i = 0; i < tokens.size; i++) {
			int type; // 0 means invalid token
					  // 1 means operand
					  // 2 means operator
			if (interpret.isOperand(tokens[i])) {
				type = 1;
			} else if (interpret.isOperator(tokens[i])) {
				type = 2;
			} else {
				type = 0;
			}

			// deal with type 0s



		} */
	}
}