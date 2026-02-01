import java.util.Map;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SwingInitializer {
	private Element root;

	public SwingInitializer() {
		root = new Element();
	}

	enum Message {
		WELCOME,
		MAIN_MENU,
		PROMPT,
		INVALID_INPUT,
		ROOT,
		EXIT
	}

	public static void main(String[] args) {
		new SwingInitializer().run();
	}

	public void run() {
		Scanner scanner = new Scanner(System.in);
		int input = 0;
		print(Message.WELCOME, 2);

		while (input != 6) {
			print(Message.MAIN_MENU, 1);
			print(Message.PROMPT, -1);
			input = scanner.nextInt();
			scanner.nextLine();

			switch (input) {
				case 1 -> {

				}
				default -> {

				}
			}
		}

		print(Message.EXIT, 2);

		scanner.close();
	}

	public void export(String filePath) {
		File file = new File(filePath);
		PrintWriter output = null;

		try {
			output = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			System.err.printf("ERROR: Cannot open/write to \"%s\"\n!");
			return;
		}

		// TODO: Write to file

		output.close();
	}

	public void print(Message message, int addSpace) {
		if (addSpace == 0 || addSpace == 2) {
			System.out.println();
		}

		switch (message) {
			case WELCOME -> {
				System.out.println("Welcome to SwingInitializer!");
			}
			case MAIN_MENU -> {
				System.out.println(
					"1. Add Swing element\n" +
					"2. Change properties\n" +
					"3. Print current element\n" +
					"4. Print root model\n" +
					"5. Back to previous element\n" +
					"6. Exit"
				);
			}
			case PROMPT -> {
				System.out.print("Enter selection -> ");
			}
			case INVALID_INPUT -> {
				System.out.println("Your input was invalid, try again...");
			}
			case ROOT -> {
				// TODO: Print object model map
			}
			case EXIT -> {
				System.out.println("Thank you for using SwingInitializer!");
			}
		}

		if (addSpace == 1 || addSpace == 2) {
			System.out.println();
		}
	}
}

class Element {
	/*
	 * Initialize as root (main JPanel)
	 */
	public Element() {

	}

	/*
	 * Prints current element & properties and calls print() for child elements if
	 * printChildren is true
	 */
	public void print(boolean printChildren) {

	}
}

class Template {
	public static final Map<String, String> TEMPLATES = Map.of(
		"JFrame", """
				
				""",
		"JPanel", """
				
				""",
		"JLabel", """
				
				"""
	);
}