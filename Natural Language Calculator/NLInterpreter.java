/* Provides functionality to analyse a string, determine the substrings that
 * can be interpreted as arithmetic expressions, and compute their values. */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Collections;

public class NLInterpreter {

	HashMap<String, Character> operators;
	HashMap<String, Integer> operands;
	HashMap<String, String> secondaryOperators;

	public NLInterpreter() {
		operators = new HashMap<String, Character>();
		secondaryOperators = new HashMap<String, String>();
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
		operators.put("to the power of",'^');
		operators.put("sqrt",			'√');
		operators.put("square root",	'√');

		secondaryOperators.put("squared", "to the power of two");
		secondaryOperators.put("cubed", "to the power of three");

		operands.put("zero",		0);
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
		operands.put("thousand",	1000);
		operands.put("million", 	1000000);
		operands.put("billion",		1000000000);
		// Points are handled explicitly in evaluateOperand()
		operands.put("point",		-1); 
		operands.put(".",			-1);
	}

	/*--------------------------------------------------------------------------------------------------------*/

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

	/*--------------------------------------------------------------------------------------------------------*/

	/* Takes a single token and determines whether it
		is part of an arithmetic operand */
	public boolean isOperand(String token) {
		if (operands.containsKey(token.toLowerCase())) {
			return true;
		}

		int length = token.length();
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				char c = token.charAt(i);
				if (c <= '/' || c >= ':') {  // Test to see if it is an ascii
					return false;
				}
			}
		}

		return true;
	}

	/*--------------------------------------------------------------------------------------------------------*/

	/* Splits up the String 'whole' into a linked list of tokens (wrapper class for Strings).
		Will throw a runtime error if any leftover strings in the linked list are not empty strings. */ 
	public LinkedList<Token> analyse(String whole) {
		/* Each key in both the operand and operator hashmaps
			will be used as a delimiter to split the remaining strings
			in the LinkedList. Then the delimeter itself is inserted
			as a Token between each pair of strings, preserving the 
			original order. */ 

		LinkedList<Token> tokens = new LinkedList<Token>();
		tokens.add(new Token(whole, false));

		// Check for operands (word form)
		// This is done before operators because words like 'six' contain operators ('x')
		Object[] keys = operands.keySet().toArray();	// Go through keys in reverse order so 'sixty' 
		Arrays.sort(keys, Collections.reverseOrder());	// is seen before 'six' and so forth'.
		for (Object key : keys) {
			lex((String)key, tokens);
		}

		// Check for secondary operators (word form)
/*
		for (String key: secondaryOperators.keySet()) {
			for (Token tok : tokens) {
				tok.setToken(tok.getToken().replace(key, secondaryOperators.get(key)), false);
			}
		} */

		// Check for operators (word form)

		for (String key : operators.keySet()) {
			lex(key, tokens);
		}

		// Check for operators (symbol form)
		for (char key : operators.values()) {
			lex("" + key, tokens);
		}

		// Check for operands (number form)
		for (int i = 0; i < tokens.size(); i++) {
			Token tok = tokens.get(i);
			if (tok.isTokenized()) {
				continue;
			} else {
				String str = tok.getToken();
				String[] numbers = str.split("[^0-9]", 0); // Extract numbers

				int count = 0; // Count of nonempty strings in numbers[]
				for (String s : numbers) {
					if (s.length() > 0) {
						tokens.set(i, new Token(s, true)); // Replace old, unprocessed Token with a tokenized Token
						count++;
					}
				}
				if (count == 0) {		// If there are no digits in 'str', remove the corresponding Token entirely.
					System.out.println("Non-numeric characters and empty strings removed from operand");
					tokens.remove(i);
					i--;
				}
				if (count > 1) {
					throw new RuntimeException("Numerical input must be contiguous.");
				}
			}
		}

		// throw RuntimeException if there are any strings left in the 'tokens' LinkedList
		for (Token t : tokens) {
			if (!t.isTokenized()) {
					throw new RuntimeException("Untokenized objects remaining in 'tokens' LinkedList.");
			}
		}

		return tokens;
	}

	/*--------------------------------------------------------------------------------------------------------*/

	/* The String 'key' is used as a delimiter to split the remaining strings (excluding Tokens)
			in the LinkedList. Then the delimeter itself is inserted as a Token between each 
			pair of strings, preserving the original order. */ 
	private void lex(String key, LinkedList<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {	
			Token tok = tokens.get(i);
			String str = tok.getToken();			
			if (!tok.isTokenized()) {	// If string is not a token, and therefore needs to be processed...
				String[] delimited;

				// If the key happens to be a symbol such as '+', regex requires a double backslash before it

				if (key.equals(".") || operators.containsValue(key.charAt(0)))
				{
					delimited = str.split("\\" + key, -1);

				} else {
					delimited = str.split(key, -1); // keep trailing empty strings
				}

				tokens.remove(i);
				for (int j = delimited.length - 1; j > 0; j--) { // Insert the delimited strings back into the 
					tokens.add(i, new Token(delimited[j], false));				// linked list, but with the delimiter in between each
					tokens.add(i, new Token(key, true));
				}
				tokens.add(i, new Token(delimited[0], false));

				i += (delimited.length * 2) - 2; // Advance i to avoid parsing what was just inserted into the linked list.
			}
		}
	}

	/*--------------------------------------------------------------------------------------------------------*/

	/* Takes a number of tokens representing a single
		arithmetic operand, and returns its value */
	public double evaluateOperand(LinkedList<String> tokens) {
		// Use regex to replace all instances of arguments with their number equivalents
		LinkedList<Double> values = new LinkedList<Double>();
		int length = tokens.size();
		for (int i = 0; i < length; i++) {
			String s = tokens.get(i);
			if (operands.containsKey(s)) {
				values.add((double)operands.get(s));
			} else {
				try {
					for (int j = 0; j < s.length(); j++) {	// Account for zeroes at beginning of strings
						if (s.charAt(j) == '0') {
							values.add(0.0);
						} else {
							break;
						}
					}
					values.add(Double.parseDouble(s));
				} catch (NumberFormatException e) {
					System.out.println("invalid operand token");
					System.exit(1);
				}
			}
		}


		// All operands are now being converted into doubles. They now must be evaluated as one operand.
		// Because of the order in which this is impelmented all numerical inputs which correspond to one 
		// the keys in the operands hashmap will be treated as words.

		// Use simple DFA to determine how operand tokens are concatenated
		DigitDFA dfa = new DigitDFA(values);
		return dfa.evaluate();
	}
}