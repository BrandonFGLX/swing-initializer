import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SwingInitializer {
	private Element root;
	private Element currElement;

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
		PROPERTIES,
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

					// TODO: Add checks so that only applicable types can be added to current element
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
					// TODO: Allow for changing of properties; properties are stored as comments as "TODO"
					Map<String, String> properties = currElement.getProperties();
					print(Message.PROPERTIES, 1);

					print(Message.NAME_PROMPT, -1);
					String name = scanner.nextLine();
					System.out.println();

					if (properties.containsKey(name)) {
						String inp = null;

						while (inp == null) {
							System.out.println("Do you want to delete or modify this key?");
							inp = scanner.nextLine();

							if (!inp.equalsIgnoreCase("delete") && !inp.equalsIgnoreCase("modify")) {
								print(Message.INVALID_INPUT, 2);
								inp = null;
							}
						}

						if (inp.equalsIgnoreCase("delete")) {
							properties.remove(name);
							System.out.printf("\nProperty \"%s\" removed from %s\n\n", name, currElement.getName());
							break;
						}
						
					}

					print(Message.PROMPT, -1);
					String value = scanner.nextLine();
					System.out.println();

					properties.put(name, value);

					print(Message.PROPERTIES, 2);
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
					// TODO: Ask to save SwingInitializer progress as .txt file
					// System.out.println("Do you want to save your progress? -> ");

					// Ask to export to a Java project
					System.out.println("Do you want to export to a Java project? -> ");

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

	// TODO: Import & export for v0.2
	public void formatExport(String filePath) {
		PrintWriter output = getOutput(filePath);

		// TODO: Write to file

		output.close();
	}

	public void javaExport(Element el, String dirPath) {
		List<Element> children = el.getChildren();

		// Export all children
		for (Element child : children) {
			javaExport(child, dirPath);
		}

		PrintWriter output = getOutput(dirPath + el.getName() + ".java");

		String fileOutput = Template.TEMPLATES.get(el.getType());
		List<String> extraProperties = new ArrayList<>();

		for (Entry<String, String> entry : el.getProperties().entrySet()) {
			String key = entry.getKey().toLowerCase();
			String value = entry.getValue();

			if (!fileOutput.contains(key)) {
				System.out.println("WARNING - Adding unnecessary property: " + entry);
				extraProperties.add(key + " - " + value);
			} else {
				fileOutput = fileOutput.replace("?" + key + "?", value);
			}
		}

		if (Pattern.compile("\\?[A-Za-z0-9\\-]+\\?").matcher(fileOutput).find()) {
			System.out.printf("WARNING: \"%s\" may contain invalid syntax due to unmatched required property.\n");
		}

		output.println(fileOutput);
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
					"7. Exit (save)"
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
			case PROPERTIES -> {
				print(Message.CURRENT_ELEMENT, -1);
				System.out.println("Properites: " + currElement.getProperties());
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
}