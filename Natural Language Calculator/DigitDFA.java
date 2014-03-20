import java.util.LinkedList;
import java.util.HashSet;

public class DigitDFA {
	double operandValue;
	int digitCount;
	HashSet<Double> units;
	HashSet<Double> teens;
	HashSet<Double> tens;
	HashSet<Double> greaterThanHundreds;
	LinkedList<Double> values;
	LinkedList<Double> greaterThanHundredsValues;

	private enum DIGIT {START, ZERO, UNITS, TEENS, TENS, HUNDRED, GREATER_THAN_HUNDREDS}

	public DigitDFA(LinkedList<Double> values) {
		this.values = values;

		units = new HashSet<Double>();
		teens = new HashSet<Double>();
		tens = new HashSet<Double>();
		greaterThanHundreds = new HashSet<Double>();

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

		greaterThanHundreds.add(1000.0);
		greaterThanHundreds.add(1000000.0);
		greaterThanHundreds.add(1000000000.0);
	}	

	/*--------------------------------------------------------------------------------------------------------*/

	public double evaluate() {
		double current;
		DIGIT digit = DIGIT.START;
		greaterThanHundredsValues = new LinkedList<Double>(); 
		/* Used to store temporary values such as five million one hundred thousand
			can be evaluated as (five million) + (one hundred thousand)

		four hundred and five
	 		 so an operand such as four hundred and five thousand
	 		 can be evaluated as (four hundred and five) * (thousand) */


		int length = values.size();
		int i;

		// Handle tokens up to a decimal point
		for (i = 0; i < length && (current = values.get(i)) >= 0; i++) {
			switch (digit) {
				case START: digit = handleSTART(current);
						    break;
				case ZERO: digit = handleZERO(current); 
							 break;
				case UNITS: digit = handleUNITS(current);
							  break;
				case TEENS: digit = handleTEENS(current);
							  break;
				case TENS: digit = handleTENS(current);
							 break;
				case HUNDRED: digit = handleHUNDRED(current);
							  break;
				case GREATER_THAN_HUNDREDS: digit = handleGREATER(current);
							 break;
			}
		}

		for (double d : greaterThanHundredsValues) {
			operandValue += d;
		}

		// Handle tokens after a decimal point
		String decimals = ".";
		for (i++; i < length; i++) {
			decimals += (int)((double)values.get(i)); // Unbox to double and then cast to int so 
													  // that no extra decimal points are added to the string
		}

		if (decimals.length() > 1) {
			try {
				operandValue += Double.parseDouble(decimals);
			} catch (Exception e) {
				System.out.println("NLCalc cannot understand the input: Error 1");
				System.out.println("Please try again.");
				System.exit(1);
			}
		}

		return operandValue;
	}

	/*--------------------------------------------------------------------------------------------------------*/

	/* Start state simply takes anything and sets operandValue accordingly. */
	private DIGIT handleSTART(double d) {
		DIGIT digit = DIGIT.START;
		operandValue = d;

		if (d == 0.0) {
			digit = DIGIT.ZERO;
		} else if (units.contains(d)) {
			digit = DIGIT.UNITS;
		} else if (teens.contains(d)) {
			digit = DIGIT.TEENS;
		} else if (tens.contains(d)) {
			digit = DIGIT.TENS;
		} else if (d == 100.0) {
			digit = DIGIT.HUNDRED;
		} else if (greaterThanHundreds.contains(d)) {
			digit = DIGIT.GREATER_THAN_HUNDREDS;
		} else {
			digit = DIGIT.UNITS;		// Other numbers can be treated as a sequence of units
		}

		return digit;

	}

	/*--------------------------------------------------------------------------------------------------------*/

	/* Zeroes can only be followed by units, which are appended onto 
		the current operandValue. */
	private DIGIT handleZERO(double d) {
		DIGIT digit = DIGIT.ZERO;

		if (units.contains(d)) {
			operandValue *= 10;
			operandValue += d;
			digit = DIGIT.UNITS;
		} else {
			System.out.println("NLCalc does not understand the input: Error 2");
			System.out.println("Please try again.");
			System.exit(1);
		}

		return digit;
	}

	/*--------------------------------------------------------------------------------------------------------*/

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
			return digit;
		} else if (teens.contains(d)) {
			operandValue *= 100;
			operandValue += d;
			digit = DIGIT.TEENS;
		} else if (tens.contains(d)) {
			operandValue *= 100;
			operandValue += d;
			digit = DIGIT.TENS;
		} else if (d == 100.0) {
			operandValue *= d;
			digit = DIGIT.HUNDRED;
		} else if (greaterThanHundreds.contains(d)) {
			operandValue *= d;
			digit = DIGIT.GREATER_THAN_HUNDREDS;
		} else {
			operandValue = Double.parseDouble("" + operandValue + d);
			digit = DIGIT.UNITS;	// All other numbers can be treated as a sequence of units and appended
		}

		return digit;
	}

	/*--------------------------------------------------------------------------------------------------------*/

	/* IF DFA is currently in TEENS state (between 10 and 19, inclusive), 
		zeroes will be appended, units will produce an error, teens and tens 
		will be appended, and greater than tens will be multiplied. */
	private DIGIT handleTEENS(double d) {
		DIGIT digit = DIGIT.TEENS;

		if (units.contains(d)) {
			System.out.println("NLCalc does not understand the input: Error 3");
			System.out.println("Please try again.");
			System.exit(1);
		} else if (d == 0.0) {
			operandValue *= 10;
			digit = DIGIT.ZERO;
		} else if (teens.contains(d)) {
			operandValue *= 100;
			operandValue += d;
		} else if (tens.contains(d)) {
			operandValue *= 100;
			operandValue += d;
			digit = DIGIT.TENS;
		} else if (d == 100.0) {
			operandValue *= d;
			digit = DIGIT.HUNDRED;
		} else if (greaterThanHundreds.contains(d)) {
			operandValue *= d;
			digit = DIGIT.GREATER_THAN_HUNDREDS;
		} else {
			operandValue = Double.parseDouble("" + operandValue + (int)d);
			digit = DIGIT.UNITS;	// All other numbers can be appended and treated as a sequence of units
		}

		return digit;
	}

	/*--------------------------------------------------------------------------------------------------------*/

	/* If DFA is currently in TENS state, zeroes will be appended, units will be added,
		teens and tens will be appended, and greater than tens wil be multiplied. */
	private DIGIT handleTENS(double d) {
		DIGIT digit = DIGIT.TENS;

		if (d == 0.0) {
			operandValue *= 10;
			digit = DIGIT.ZERO;
		} else if (units.contains(d)) {
			operandValue += d;
			digit = DIGIT.UNITS;
		} else if (teens.contains(d)) {
			operandValue *= 100;
			operandValue += d;
			digit = DIGIT.TEENS;
		} else if (tens.contains(d)) {
			operandValue *= 100;
			operandValue += d;
		} else if (d == 100.0) {
			operandValue *= d;
			digit = DIGIT.HUNDRED;
		} else if (greaterThanHundreds.contains(d)) {
			operandValue = operandValue * d;
			digit = DIGIT.GREATER_THAN_HUNDREDS;
		} else {
			operandValue = Double.parseDouble("" + (int)operandValue + (int)d);
			digit = DIGIT.UNITS;	// All other numbers can be appended and treated as a sequence of units
		}
		return digit;
	}

	/*--------------------------------------------------------------------------------------------------------*/

	private DIGIT handleHUNDRED(double d) {
		DIGIT digit = DIGIT.HUNDRED;

		if (d == 0.0) {
			System.out.println("NLCalc does not understand the input: Error 4");
			System.out.println("Please try again.");
			System.exit(1);
		} else if (units.contains(d)) {
			operandValue += d;
			digit = DIGIT.UNITS;
		} else if (teens.contains(d)) {
			operandValue += d;
			digit = DIGIT.TEENS;
		} else if (tens.contains(d)) {
			operandValue += d;
			digit = DIGIT.TENS;
		} else if (d == 100.0) {
			operandValue *= d;
			digit = DIGIT.HUNDRED;
		} else if (greaterThanHundreds.contains(d)) {
			operandValue *= d;
			digit = DIGIT.GREATER_THAN_HUNDREDS;
		} else {
			operandValue += d;	// Treat other numbers as a sequence of units
			digit = DIGIT.UNITS;
		}

		return digit;
	}

	/* If DFA is currently in GREATER_THAN_TENS state, zeroes will be produce an error,
		units will be added... */
	private DIGIT handleGREATER(double d) {
		DIGIT digit = DIGIT.GREATER_THAN_HUNDREDS;

		if (d == 0.0) {
			System.out.println("NLCalc does not understand the input: Error 4");
			System.out.println("Please try again.");
			System.exit(1);
		} else if (units.contains(d)) {
			greaterThanHundredsValues.add(operandValue);
			operandValue = d;
			digit = DIGIT.UNITS;
		} else if (teens.contains(d)) {
			greaterThanHundredsValues.add(operandValue);
			operandValue = d;
			digit = DIGIT.TEENS;
		} else if (tens.contains(d)) {
			greaterThanHundredsValues.add(operandValue);
			operandValue = d;
			digit = DIGIT.TENS;
		} else if (d == 100.0) {
			greaterThanHundredsValues.add(operandValue);
			operandValue = d;
			digit = DIGIT.HUNDRED;
		} else if (greaterThanHundreds.contains(d)) {
			operandValue *= d;
		} else {
			operandValue +=d;	// Treat other numbers as a sequence of units
			digit = DIGIT.UNITS;
		}

		return digit;
	}
}