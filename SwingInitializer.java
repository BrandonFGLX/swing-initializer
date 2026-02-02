import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SwingInitializer {
	private Element root;
	private Element currElement;

	private static final int BOX_PADDING_HORIZONTAL = 2;

	public SwingInitializer() {
		root = new Element();
		currElement = root;
	}

	enum Message {
		WELCOME,
		HELP,
		MAIN_MENU,
		PROMPT,
		NAME_PROMPT,
		ELEMENT_MENU,
		PROPERTY_WARNING,
		INVALID_INPUT,
		CURRENT_ELEMENT,
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

		while (input != 7) {
			print(Message.MAIN_MENU, 1);
			print(Message.PROMPT, -1);
			input = scanner.nextInt();
			scanner.nextLine();
			System.out.println();

			switch (input) {
				case 1 -> {
					print(Message.ELEMENT_MENU, 1);
					print(Message.PROMPT, -1);

					int elType = scanner.nextInt();
					scanner.nextLine();

					String name = null;

					while (name == null) {
						print(Message.NAME_PROMPT, -1);
						name = scanner.nextLine();
						System.out.println();

						if (root.findElementWithName(name) != null) {
							print(Message.INVALID_INPUT, 1);
							name = null;
						}
					}

					Element newElement = null;
					switch (elType) {
						case 1 -> {
							newElement = new Element(SwingElement.JPANEL, name);
						}
						case 2 -> {
							newElement = new Element(SwingElement.JLABEL, name);
						}
						default -> {
							print(Message.INVALID_INPUT, 2);
						}
					}

					currElement.addChild(newElement);
					currElement = newElement;
					print(Message.CURRENT_ELEMENT, 1);
				}
				case 2 -> {
					// TODO: Allow for changing of properties; unknown properties are stored as comments as "TODO"
				}
				case 3 -> {
					print(Message.CURRENT_ELEMENT, 1);
				}
				case 4 -> {
					print(Message.ROOT, 1);
				}
				case 5 -> {
					print(Message.NAME_PROMPT, -1);
					String name = scanner.nextLine();
					System.out.println();

					Element el = root.findElementWithName(name);
					if (el == null) {
						print(Message.INVALID_INPUT, 2);
						break;
					}

					currElement = el;
					print(Message.CURRENT_ELEMENT, 1);
				}
				case 6 -> {
					Element parent = currElement.getParent();

					if (parent == null) {
						print(Message.INVALID_INPUT, 2);
					} else {
						currElement = parent;
					}

					print(Message.CURRENT_ELEMENT, 1);
				}
				case 7 -> {
					print(Message.EXIT, 1);
				}
				default -> {
					print(Message.INVALID_INPUT, 1);
				}
			}
		}

		scanner.close();
	}

	public PrintWriter getOutput(String filePath) {
		File file = new File(filePath);
		PrintWriter output = null;

		try {
			output = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			System.err.printf("ERROR: Cannot open/write to \"%s\"\n!");
			return null;
		}

		return output;
	}

	public void formatExport(String filePath) {
		PrintWriter output = getOutput(filePath);

		// TODO: Write to file

		output.close();
	}

	public void javaExport(String filePath) {
		PrintWriter output = getOutput(filePath);

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
			case HELP -> {
				System.out.println("""
						SwingInitializer - a simple CLI tool to kickstart a Java app using Swing!

						Use the menus to navigate and modify the current Element.

						Elements represent a GUI element, like a JPanel, JLabel, or JButton.
						Each element has a connection to its Parent. A null parent means that the
						element will not be connected to other elements when exported.

						Elements contain a name (to identify itself and should be unique) and 
						properties (modifies the Element, adding formatted code). Some Elements,
						like JLabels, require a property to be present to have any text to be shown.
						Some Elements, like JPanels, do not use any properties. Any properties added
						to such Elements will be shown as "TODO" at the top of the file.

						All Elements have a common Parent of "root". When exported, the program will
						generate a file in which the constructor has a JFrame and adds root as its only
						Component.

						The following shows the properties for some Elements:
						JLabel - value (text to be shown)

						"""
				);
			}
			case MAIN_MENU -> {
				System.out.println(
					"1. Add Swing element\n" +
					"2. Change properties\n" +
					"3. Print current element\n" +
					"4. Print root model\n" +
					"5. Go to element\n" +
					"6. Back to previous element\n" +
					"7. Exit"
				);
			}
			case PROMPT -> {
				System.out.print("Enter value -> ");
			}
			case NAME_PROMPT -> {
				System.out.print("Enter name -> ");
			}
			case ELEMENT_MENU -> {
				System.out.println(
					"1. JPanel\n" +
					"2. JLabel"
				);
			}
			case PROPERTY_WARNING -> {
				System.out.println("WARNING: This Element needs to specify a property to function correctly!");
			}
			case INVALID_INPUT -> {
				System.out.println("Your input was invalid, try again...");
			}
			case CURRENT_ELEMENT -> {
				currElement.print(false);
			}
			case ROOT -> {
				root.print(true);
			}
			case EXIT -> {
				System.out.println("Thank you for using SwingInitializer!");
			}
		}

		if (addSpace == 1 || addSpace == 2) {
			System.out.println();
		}
	}

	public static void printWithBorder(List<String> message, String primaryPrefix, String secondaryPrefix) {
		message = message.stream()
			.filter(x -> x != null)
			.toList();

		int largest = message.stream()
			.mapToInt(String::length)
			.max()
			.orElse(0);

		System.out.print(primaryPrefix + "+");
		for (int i = 0; i < largest + BOX_PADDING_HORIZONTAL * 2; i++) {
			System.out.print("-");
		}
		System.out.println("+");

		for (String s : message) {
			System.out.print(secondaryPrefix + "|");

			for (int i = 0; i < BOX_PADDING_HORIZONTAL; i++) {
				System.out.print(" ");
			}

			System.out.print(s);
			
			for (int i = 0; i < largest - s.length() + BOX_PADDING_HORIZONTAL; i++) {
				System.out.print(" ");
			}
			
			System.out.println("|");
		}

		System.out.print(secondaryPrefix + "+");
		for (int i = 0; i < largest + BOX_PADDING_HORIZONTAL * 2; i++) {
			System.out.print("-");
		}
		System.out.println("+");
	}
}

enum SwingElement {
	JPANEL,
	JLABEL
}

class Element {
	private SwingElement type;
	private String name;
	private Element parent;
	private List<Element> children;

	/*
	 * Initialize as root (main JPanel)
	 */
	public Element() {
		this(SwingElement.JPANEL, "root");
	}

	public Element(SwingElement type, String name) {
		this.type = type;
		this.name = name;
		this.parent = null;
		children = new ArrayList<>();
	}

	/* 
	 * Recursively searches for the first element from this element with given name
	 */
	public Element findElementWithName(String name) {
		if (this.name.equals(name)) {
			return this;
		}

		if (children.isEmpty()) {
			return null;
		}

		for (Element child : children) {
			Element el = child.findElementWithName(name);

			if (el != null) {
				return el;
			}
		}

		return null;
	}

	/*
	 * Prints current element & properties and calls print() for child elements if
	 * printChildren is true
	 */
	public void print(boolean printChildren) {
		print(printChildren, "", "");
	}

	public void print(boolean printChildren, String primaryPrefix, String secondaryPrefix) {
		if (!secondaryPrefix.isEmpty()) {
			System.out.println(secondaryPrefix);
		}

		SwingInitializer.printWithBorder(List.of(type.toString(), name), primaryPrefix, secondaryPrefix);

		if (printChildren) {
			for (Element child : children) {
				child.print(printChildren, secondaryPrefix + "├──", secondaryPrefix + "│  ");
			}
		}
	}

	public void addChild(Element child) {
		children.add(child);
		child.parent = this;
	}

	public void removeChild(Element child) {
		children.remove(child);
		child.parent = null;
	}

	public Element getParent() {
		return parent;
	}

	public String toString() {
		return String.format("%s (%s)", name, type);
	}

	public boolean equals(Object o) {
		if (o != null && o instanceof Element) {
			return ((Element) o).name.equals(name);
		} 

		return false;
	}
}

class Template {
	/*
	 * Templates for each type of class; each class is in its own seperate file
	 * and can be customized by name by replacing "???".
	 */
	public static final Map<String, String> TEMPLATES = Map.of(
		"JFrame", """
				/*
					?extra-properties?
				 */

				import javax.swing.JFrame;

				public class ?class? {
					private JFrame frame;

					public ?class?() {
						frame = new JFrame();
						frame.setTitle("?name?");
						frame.setSize(100, 100);
						frame.setLocationRelativeTo(null);
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						frame.setVisible(true);

						?element?
					}

					public static void main(String[] args) {
						?class? program = new ?class?();
						program.run();
					}

					public void run() {
						// TODO: Add code here...
					}
				}
				""",
		"JPanel", """
				/*
					?extra-properties?
				 */

				import javax.swing.JPanel;

				public class ?class? extends JPanel {
					public ?class?() {
						?element?
					}

					// TODO: Add code here
				}
				""",
		"JLabel", """
				/*
					?extra-properties?
				 */
				JLabel ?name? = new JLabel("?value?");
				add(?name?);
				"""
	);
}