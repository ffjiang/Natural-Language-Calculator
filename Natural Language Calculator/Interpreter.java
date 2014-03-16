/* Provides functionality to analyse a string, determine the substrings that
 * can be interpreted as arithmetic expressions, and compute their values. */

import java.util.HashMap;
import java.util.LinkedList;

public class Interpreter {

	HashMap<String, Character> operators;
	HashMap<String, Integer> operands;

	public Interpreter() {
		operators = new HashMap<String, Character>();
		operands = new HashMap<String, Integer>();

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
		if (token.length() == 1) {	// Accounting for symbols such as +,-,*,/,%
			if (operators.containsValue(token.charAt(0))) {
				return true;
			}
		}
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

	/* Splits up the String 'whole' into a linked list of tokens (wrapper class for Strings).
		Will throw a runtime error if any leftover strings in the linked list are not empty strings. */ 
	public LinkedList<Token> analyse(String whole) {
		/* Each key in both the operand and operator hashmaps
			will be used as a delimiter to split the remaining strings
			in the LinkedList. Then the delimeter itself is inserted
			as a Token between each pair of strings, preserving the 
			original order. */ 

		LinkedList tokens = new LinkedList();
		tokens.add(whole);

		for (String key : operators.keySet()) {
			parse(key, tokens);
		}

		for (Character key : operators.values()) {
			parse("" + key, tokens);
		}

		for (String key : operands.keySet()) {
			parse(key, tokens);
		}

	/*	for (Object remaining : tokens) {
			if (remaining instanceof String) {
				if (remaining != "") {
					throw new RuntimeException();
				}
			}
		} */

		return tokens;
	}

	/* The String 'key' is used as a delimiter to split the remaining strings (excluding Tokens)
			in the LinkedList. Then the delimeter itself is inserted as a Token between each 
			pair of strings, preserving the original order. */ 
	private void parse(String key, LinkedList tokens) {
		for (int i = 0; i < tokens.size(); i++) {				
			if (tokens.get(i) instanceof String) {	// If string is not a token, and therefore needs to be processed...
				String nonToken = (String)tokens.get(i);

				String[] delimited;
				// If the key happens to be a symbol such as '+', regex requires a double backslash before it
				if (key.equals("+") || key.equals("-") || key.equals("*") || key.equals("/") || key.equals("%")) {
					delimited = nonToken.split("\\" + key, -1);
				} else {
					delimited = nonToken.split(key, -1); // keep trailing empty strings
				}

				tokens.remove(i);
				for (int j = delimited.length - 1; j > 0; j--) { // Insert the delimited strings back into the 
					tokens.add(i, delimited[j]);				// linked list, but with the delimiter in between each
					tokens.add(i, new Token(key));
				}
				tokens.add(i, delimited[0]);

				i += (delimited.length * 2) - 2; // Advance i to avoid parsing what was just inserted into the linked list.
			}
		}
	}

	/* Takes a number of tokens representing a single
		arithmetic operand, and returns its value */
	public double evaluateOperand(String[] tokens) {
		// Use regex to replace all instances of arguments with their number equivalents
		
		return 1.0;
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