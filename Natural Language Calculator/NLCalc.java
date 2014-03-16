/* Created by Frank Jiang
 * 
 * Poptip Coding Challenge Problem 5
 *
 * 
 */

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Scanner;

public class NLCalc {
	public static void main(String[] args) {
		System.out.println("Enter a simple mathematical expression:");

		Scanner sc = new Scanner(System.in);
		String whole = sc.nextLine();

		String[] tokens = whole.split(" ");

		for (String s : tokens) {
			System.out.println(s);
		}
	}
}