/* Converts infix expressions into postfix and evaluates postfix expressions. */

import java.util.LinkedList;
import java.util.Stack;

public class NLCompute {

	/* Convert a LinkedList of tokens representing an infix expression
		 into a LinkedList of Strings acting as a queue, representing
		 the expression in postfix notation. Uses Djikstra's Shunting Yard Algorithm */
	public static LinkedList<String> postfix(LinkedList<Token> tokens) {
		
		NLInterpreter interpret = new NLInterpreter();
		Stack<String> operatorStack = new Stack<String>();
		LinkedList<String> outputQueue = new LinkedList<String>(); // Should I use generics and force all my values to be strings


		for (Token tok : tokens) {
			if (tok.isOperator()) {
				String operator;
				if (interpret.operators.containsKey(tok.getToken())) {
					operator = "" + interpret.operators.get(tok.getToken());
				} else {
					operator = tok.getToken();
				}

				/* Pop operators off stack until operator at top is
					of lower precedence than the current operator */
				if (operator.equals("+") || operator.equals("-")) {
					while (operatorStack.size() > 0) {				// Because nothing is lower precedence than +-
						outputQueue.addLast(operatorStack.pop());	// we can simply pop until stack is empty
					}
					operatorStack.push(operator);
				} else if (operator.equals("*") || operator.equals("/") || operator.equals("%")) {
					while (operatorStack.size() > 0 && !operatorStack.peek().equals("+") && !operatorStack.peek().equals("-")) {
						outputQueue.addLast(operatorStack.pop());	// Pop until we see a + or -, which are of lower precedence
					}
					operatorStack.push(operator);
				} else if (operator.equals("^")) {
					while (operatorStack.size() > 0 && operatorStack.peek().equals("^")) {
						outputQueue.addLast(operatorStack.pop());
					}
					operatorStack.push(operator);
				}

			} else if (tok.isOperand()) { // Values are simply moved to outputQueue
				outputQueue.add("" + tok.getValue());
			}
		}
		while (operatorStack.size() > 0) {	// Pop off anything remaining on the operatorStack
			outputQueue.add(operatorStack.pop());
		}

		// Print out the resulting outputQueue
	/*	System.out.println("Postfix Notation - Output Queue:");
		for (String s : outputQueue) {
			System.out.println(s);
		} */

		return outputQueue;
	}

	/*--------------------------------------------------------------------------------------------------------*/


	// Evalues a postfix expression in the form of a LinkedList of Strings. Returns result as double.

	public static double evalPostfix(LinkedList<String> expression) {
		Stack<Double> operandStack = new Stack<Double>();

		try {
			for (String s : expression) {
				if (s.equals("+")) {
					double op2 = operandStack.pop();
					double op1 = operandStack.pop();
					operandStack.push(op1 + op2);
				} else if (s.equals("-")) {
					double op2 = operandStack.pop();
					double op1 = operandStack.pop();
					operandStack.push(op1 - op2);
				} else if (s.equals("*")) {
					double op2 = operandStack.pop();
					double op1 = operandStack.pop();
					operandStack.push(op1 * op2);
				} else if (s.equals("/")) {
					double op2 = operandStack.pop();
					double op1 = operandStack.pop();
					operandStack.push(op1 / op2);
				} else if (s.equals("%")) {
					double op2 = operandStack.pop();
					double op1 = operandStack.pop();
					operandStack.push(op1 % op2);
				} else if (s.equals("^")) {
					double op2 = operandStack.pop();
					double op1 = operandStack.pop();
					operandStack.push(Math.pow(op1, op2));
				} else {
					operandStack.push(Double.parseDouble(s));
				}
			}
		} catch (Exception e) {
			System.out.println("Evaluation of postfix expression failed");
		}

		if (operandStack.size() > 1) {
			System.out.println("Expression contains incorrect operators");
		}

		if (operandStack.size() == 0) {
			return 0.0;
		}

		return operandStack.pop();
	}

}