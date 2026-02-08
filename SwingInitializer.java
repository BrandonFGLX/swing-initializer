/**
 * swing-initializer
 * 
 * A simple Java program that generates template code for Swing applications.
 * 
 * The program uses a tree-like structure to construct Swing objects. By default,
 * there is a "root" class, which contains the JFrame responsible for running the
 * program. Custom JPanels are stored as seperate files.
 * 
 * Some Swing elements need specific properties for the program to export a "working"
 * Swing application. For example, the JLabel element needs a "JLABEL_VALUE" to show
 * text.
 * 
 * Usage:
 * 		Download the .jar files from the "Releases" tab
 * 		Change to the directory of the jar and run:
 * 			"java -jar SwingInitializer.jar"
 * 		Configure the element tree using the options.
 * 		Use the "exit" option to exit and choose to save.
 * 		The exported Java files should be in the provided directory.
 * 
 * @author BrandonFGLX
 * @version 0.1
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class SwingInitializer {
	private Element root;
	private Element currElement;

	private static final String COMMENT_NOTE = "Generated using SwingInitializer.";
	private static final String ELEMENT_NOTE = "// TODO: Add components here";
	private static final String PROPERTY_NOTE = "// TODO: Add properties here";

	public SwingInitializer() {
		root = new Element();
		root.set(Property.NAME, "root");
		root.set(Property.CLASS, "Root");
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

					Element newElement = null;
					while (newElement == null) {
						int elType = scanner.nextInt();
						scanner.nextLine();

						boolean error = false;
						switch (elType) {
							case 1 -> {
								if (currElement.getType() == SwingElement.JFRAME || 
										currElement.getType() == SwingElement.JPANEL) {
									newElement = new Element(SwingElement.JPANEL);
								} else {
									error = true;
								}
							}
							case 2 -> {
								if (currElement.getType() == SwingElement.JFRAME ||
										currElement.getType() == SwingElement.JPANEL) {
									newElement = new Element(SwingElement.JLABEL);
								} else {
									error = true;
								}
							}
							default -> {
								print(Message.INVALID_INPUT, 2);
							}
						}

						if (error) {
							print(Message.INVALID_INPUT, 2);
						}
					}

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

					newElement.set(Property.NAME, name);

					currElement.addChild(newElement);
					currElement = newElement;
					print(Message.CURRENT_ELEMENT, 1);
				}
				case 2 -> {
					Map<Property, String> properties = currElement.getProperties();
					print(Message.PROPERTIES, 1);

					String name = null;
					Property nameProp = null;

					while (name == null) {
						print(Message.NAME_PROMPT, -1);
						name = scanner.nextLine().toUpperCase();
						try {
							nameProp = Property.valueOf(name);
						} catch (NullPointerException | IllegalArgumentException _) {
							System.out.println("ERROR: Invalid property \"" + name + "\". Try again...");
							name = null;
						}
						System.out.println();
					}

					if (properties.containsKey(nameProp)) {
						String inp = null;

						while (inp == null) {
							System.out.println("Do you want to delete or modify this property?");
							inp = scanner.nextLine();

							if (!inp.equalsIgnoreCase("delete") && !inp.equalsIgnoreCase("modify")) {
								print(Message.INVALID_INPUT, 2);
								inp = null;
							}
						}

						if (inp.equalsIgnoreCase("delete")) {
							properties.remove(nameProp);
							System.out.printf("\nProperty \"%s\" removed from %s\n\n", name, currElement.get(Property.NAME));
							break;
						}
						
					}

					print(Message.PROMPT, -1);
					String value = scanner.nextLine();
					System.out.println();

					properties.put(nameProp, value);

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
						print(Message.INVALID_INPUT, 1);
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
					// Ask to export to a Java project
					System.out.print("Do you want to export to a Java project? -> ");

					String inp = scanner.nextLine();
					if (inp.equalsIgnoreCase("yes")) {
						System.out.print("Enter the directory to create your project -> ");
						String dir = scanner.nextLine();

						System.out.println("\nExporting .java files to: " + dir);
						javaExport(root, dir);
						System.out.println("Finished exporting to: " + dir);
					}

					print(Message.EXIT, 2);
				}
				default -> {
					print(Message.INVALID_INPUT, 1);
				}
			}
		}

		scanner.close();
	}

	public PrintWriter getOutput(String dirName, String fileName) {
		File dir = new File(dirName);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		PrintWriter output = null;

		File file = new File(dir, fileName);
		try {
			file.createNewFile();

			try {
				output = new PrintWriter(file);
			} catch (FileNotFoundException _) {
				System.err.printf("ERROR: Cannot open/write to \"%s\"!\n", file.getAbsolutePath());
				return null;
			}
		} catch (IOException _) {
			System.err.printf("ERROR: File cannot be created at \"%s\"!\n", file.getAbsolutePath());
			return null;
		}

		return output;
	}

	public void javaExport(Element el, String dirPath) {
		// If currEl is a JPanel or JFrame, create a new file
		String fileClass = el.get(Property.CLASS) + ".java";
		PrintWriter output = getOutput(dirPath, fileClass);
		String fileOutput = Template.TEMPLATES.get(el.getType());
		List<Element> children = el.getChildren();

		// Explore all children; call javaExport() on all JPanel or JFrame children to make seperate files
		for (Element child : children) {
			if (child.getType() == SwingElement.JPANEL) {
				javaExport(child, dirPath);
			}			
		}

		fileOutput = formatFileOutput(fileOutput, el, 2);

		if (Pattern.compile("\\?[A-Za-z0-9\\-]+\\?").matcher(fileOutput).find()) {
			System.out.printf("WARNING: \"%s\" may contain invalid syntax due to unmatched required property.\n", el.get(Property.CLASS) + ".java");
		}

		output.println(fileOutput);
		output.close();
	}

	public String formatFileOutput(String fileOutput, Element el, int level) {
		List<String> extraProperties = new ArrayList<>();
		String fileClass = el.get(Property.CLASS) + ".java";
		String tabbing = "\t".repeat(level);

		for (Entry<Property, String> entry : el.getProperties().entrySet()) {
			Property key = entry.getKey();
			String value = entry.getValue();

			if (!fileOutput.contains("?" + key + "?")) {
				System.out.println("WARNING - Adding property as comment (" + fileClass + "): " + entry);
				extraProperties.add(key + " - " + value);
			} else {
				fileOutput = fileOutput.replace("?" + key + "?", value);
			}
		}

		List<Element> children = el.getChildren();

		// Overwrite ?element? to add child and fixes tabbing
		if (fileOutput.contains("?ELEMENT?")) {
			if (children.isEmpty()) {
				fileOutput = fileOutput.replace("?ELEMENT?", ELEMENT_NOTE);
			} else {
				String repl = "";

				for (Element child : children) {
					switch (child.getType()) {
						case JPANEL -> {
							String name = child.get(Property.NAME);
							String cl = child.get(Property.CLASS);

							repl += String.format("%s %s = new %s();\n" + tabbing + "add(%s);\n" + tabbing, cl,
								name, cl, name);
						}
						default -> {
							repl += formatFileOutput(String.join("\n" + tabbing, Template.TEMPLATES.get(child.getType()).split("\n")), child, level + 1);
						}
					}
				}

				fileOutput = fileOutput.replace("?ELEMENT?", repl);
			}
		}

		// Override ?properties? for properties and fixes tabbing
		if (fileOutput.contains("?PROPERTIES?")) {
			List<String> propStrs = getPropertyString(el);
			
			if (propStrs.isEmpty()) {
				fileOutput = fileOutput.replace("?PROPERTIES?", PROPERTY_NOTE);
			} else {
				fileOutput = fileOutput.replace("?PROPERTIES?", String.join("\n" + tabbing, propStrs));
			}
		}

		extraProperties.add(COMMENT_NOTE);
		fileOutput = fileOutput.replace("?extra-properties?", String.join("\n" + tabbing, extraProperties));

		return fileOutput;
	}

	public List<String> getPropertyString(Element e) {
		List<String> list = new ArrayList<>();

		for (Entry<Property, String> entry : e.getProperties().entrySet()) {
			list.add(String.format("// ERROR: Property \"%s\" not found in SwingInitializer.getPropertyString()!", entry.getKey()));
		}

		return list;
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