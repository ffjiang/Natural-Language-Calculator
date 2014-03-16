/* Provides functionality to analyse a string, determine the substrings that
 * can be interpreted as arithmetic expressions, and compute their values. */

import java.util.HashMap;

public class Interpreter {

	HashMap<String, Character> operators;
	HashMap<String, Integer> operands;

	public Interpreter() {
		operators = new HashMap<String, Character>();
		operand = new HashMap<String, Integer>();

		operators.put("plus", 			'+');
		operators.put("minus", 			'-');
		operators.put("times", 			'*');
		operators.put("multiply", 		'*');
		operators.put("multiply by",	'*');
		operators.put("multiplied by", 	'*');
		operators.put("x", 				'*');
		operators.put("X", 				'*');
		operators.put("divide", 		'/');
		operators.put("divided by", 	'/');
		operators.put("divide by", 		'/');
		operators.put("over", 			'/');
		operators.put("modulus", 		'%');
		operators.put("mod",			'%');

		operands.put("one", 		1);
		operands.put("two", 		2);
		operands.put("three", 		3);
		operands.put("four", 		4);
		operands.put("five", 		5);
		operands.put("six", 		6);
		operands.put("seven", 		7);
		operands.put("eight", 		8);
		operands.put("nine", 		9);
		operands.put("ten", 		10);
		operands.put("eleven", 		11);
		operands.put("twelve", 		12);
		operands.put("thirteen", 	13);
		operands.put("fourteen", 	14);
		operands.put("fifteen", 	15);
		operands.put("sixteen", 	16);
		operands.put("seventeen",	17);
		operands.put("eighteen", 	18);
		operands.put("nineteen", 	19);
		operands.put("twenty", 		20);
		operands.put("thirty", 		30);
		operands.put("forty", 		40);
		operands.put("fifty", 		50);
		operands.put("sixty", 		60);
		operands.put("seventy", 	70);
		operands.put("eighty", 		80);
		operands.put("ninety", 		90);
		operands.put("hundred", 	100);
		operands.put("a", 			1);
	}

	/* Takes a single token and determines whether it
		is an arithmetic operator */
	public boolean isOperator(String token) {
		if (operators.containsKey(token.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	/* Takes a single token and determines whether it
		is part of an arithmetic operand */
	public boolean isOperand(String token) {
		if (operands.containsKey(token.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}



	/* Takes a number of tokens representing a single
		arithmetic operand, and returns its value */
	public double evaluateOperand(String[] tokens) {
		// Use regex to replace all instances of arguments with their number equivalents
		

	}

	/* Takes an arithmetic expression consisting of two operands, arg1 and arg2,
		and a String operator, and returns its value. */
	public double compute(double arg1, String operator, double arg2) {
		char op = operators.get(operator);

		switch (op) {
			case '+': return arg1 + arg2;
			case '-': return arg1 - arg2;
			case '*': return arg1 * arg2;
			case '/': return arg1 / arg2;
			case '%': return arg1 % arg2;
			default: System.out.println("This is not a valid operator");
					  return 0;
		}
	}
}