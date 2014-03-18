import java.util.LinkedList;
import java.util.HashSet;

public class DigitDFA {
	double operandValue;
	HashSet units;
	HashSet teens;
	HashSet tens;
	HashSet greater_than_tens;
	LinkedList<Double> values;

	private enum DIGIT {ZERO, UNITS, TEENS, TENS, GREATER_THAN_TENS}

	public DigitDFA(LinkedList<Double> values) {
		this.values = values;

		units = new HashSet<Double>();
		teens = new HashSet<Double>();
		tens = new HashSet<Double>();
		greaterThanTens = new HashSet<Double>();

		units.add(1.0);
		units.add(2.0);
		units.add(3.0);
		units.add(4.0);
		units.add(5.0);
		units.add(6.0);
		units.add(7.0);
		units.add(8.0);
		units.add(9.0);

		teens.add(10.0);
		teens.add(11.0);
		teens.add(12.0);
		teens.add(13.0);
		teens.add(14.0);
		teens.add(15.0);
		teens.add(16.0);
		teens.add(17.0);
		teens.add(18.0);
		teens.add(19.0);

		tens.add(20.0);
		tens.add(30.0);
		tens.add(40.0);
		tens.add(50.0);
		tens.add(60.0);
		tens.add(70.0);
		tens.add(80.0);
		tens.add(90.0);

		greaterThanTens.add(100.0);
		greaterThanTens.add(1000.0);
		greaterThanTens.add(1000000.0);
		greaterThanTens.add(1000000000.0);
	}	

	public double evaluate() {
		operandValue = 0;
		double current;
		DIGIT digit = DIGIT.ZERO;

		int length = values.size();
		int i;

		// Handle tokens up to a decimal point
		for (i = 0; i < length && (current = values.get(i)) >= 0; i++) {
			switch (digit) {
				case (ZERO): digit = handleZERO(current); 
							 break;
				case (UNITS): digit = handleUNITS(current);
							  break;
				case (TEENS): digit = handleTEENS(current);
							  break;
				case (TENS): digit = handleTENS(current);
							 break;
				case (GREATER_THAN_TENS): digit = handleGREATER(current);
							 break;
			}
		}

		// Handle tokens after a decimal point
		String decimals = ".";
		for (; i < length; i++) {
			decimals += d;
		}

		try {
			operandValue += Double.parseDouble(decimals);
		} catch (Exception e) {
			System.out.println("NLCalc cannot understand the input.");
			System.out.println("Please try again.");
			System.exit(1);
		}

		return operandValue;
	}

	/* Zeroes can only be followed by units, which are appended onto 
		the current operandValue. */
	private DIGIT handleZERO(double d) {
		if (units.contains(d)) {
			operandValue *= 10;
			operandValue += d;
			return UNITS;
		} else {
			System.out.println("NLCalc does not understand the input."
			System.out.println("Please try again.");
			System.exit(1);
		}
	}

	/* If DFA is currently in UNITS state, zeroes, units, teens and tens will 
		be appended on the end of the current operandValue. Greater than tens will
		be multipled by the current operandValue. */
	private DIGIT handleUNITS(double d) {
		DIGIT digit = DIGIT.UNITS;
	
		if (d == 0.0) {
			operandValue *= 10;
			digit = DIGIT.ZERO;
		} else if (units.contains(d)) {
			operandValue *= 10;
			operandValue += d;
			digit = DIGIT.UNITS;
			return digit;
		} else if (teens.contains(d)) {
			operandValue *= 100;
			operandValue += d;
			digit = DIGIT.TEENS;
		} else if (tens.contains(d)) {
			operandValue *= 100;
			operandValue += d;
			digit = DIGIT.TENS;
		} else if (greaterThanTens.contains(d)) {
			operandValue *= d;
			digit = DIGIT.GREATER_THAN_TENS;
		} else {
			operandValue = Double.parseDouble("" + operandValue + d);
			digit = DIGIT.UNITS;	// All other numbers can be treated as a sequence of units and appended
		}

		return digit;
	}

	/* IF DFA is currently in TEENS state (between 10 and 19, inclusive), 
		zeroes will be appended, units will produce an error, teens and tens 
		will be appended, and greater than tens will be be multiplied. */
	private DIGIT handleTEENS(double d) {
		DIGIT digit = DIGIT.TEENS;

		if (units.contains(d)) {
			System.out.println("NLCalc does not understand the input.");
			System.out.println("Please try again.");
			System.exit(1);
		} else if (d == 0.0) {
			operandValue *= 10;
			digit = DIGIT.ZERO;
		} else if (teens.contains(d)) {
			operandValue *= 100;
			operandValue += d;
			digit = DIGIT.TEENS;
		} else if (tens.contains(d)) {
			operandValue *= 100;
			operandValue += d;
			digit = DIGIT.TENS;
		} else if (greaterThanTens.contains(d)) {
			operandValue *= d;
			digit = DIGIT.GREATER_THAN_TENS;
		} else {
			// No other numbers are accepted following a teen.
			System.out.println("NLCalc does not understand the input.");
			System.out.println("Please try again.");
			System.exit(1);
		}

		return digit;
	}


	private DIGIT handleTENS(double d) {
		DIGIT digit = DIGIT.ZERO;
		return digit;
	}

	private DIGIT_DFA handleGREATER(double d) {
		DIGIT digit = DIGIT.ZERO;
		return digit;
	}
}