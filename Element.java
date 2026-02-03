import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum SwingElement {
	JFRAME,
	JPANEL,
	JLABEL
}

enum Properties {
	NAME,
}

public class Element {
	private SwingElement type;
	private Element parent;
	private List<Element> children;
	private Map<String, String> properties;

	/*
	 * Initialize as root (main JPanel)
	 */
	public Element() {
		this(SwingElement.JFRAME, "Root");
	}

	public Element(SwingElement type, String name) {
		this.type = type;
		this.parent = null;
		children = new ArrayList<>();
		properties = new HashMap<>();

		properties.put("name", name);
	}

	/* 
	 * Recursively searches for the first element from this element with given name
	 */
	public Element findElementWithName(String name) {
		if (this.getName().equals(name)) {
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

		TermIO.printBoxedWithPrefix(List.of(type.toString(), getName(), properties.toString()), primaryPrefix, secondaryPrefix);

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

	public SwingElement getType() {
		return type;
	}

	public String getName() {
		return properties.get("name"); // TODO: Replace this with enum solution
	}

	public Element getParent() {
		return parent;
	}

	public List<Element> getChildren() {
		return children;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public String toString() {
		return String.format("%s (%s)", getName(), type);
	}

	public boolean equals(Object o) {
		if (o != null && o instanceof Element) {
			return ((Element) o).getName().equals(getName());
		} 

		return false;
	}
}

class Template {
	/*
	 * Templates for each type of class; each class is in its own seperate file
	 * and can be customized by name by replacing "???".
	 */
	public static final Map<SwingElement, String> TEMPLATES = Map.of(
		SwingElement.JFRAME, """
				/*
					?extra-properties?
				 */

				import javax.swing.JFrame;

				public class ?name? {
					private JFrame frame;

					public ?name?() {
						frame = new JFrame();
						frame.setTitle("?name?");
						frame.setSize(500, 500);
						frame.setLocationRelativeTo(null);
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						frame.setVisible(true);

						// TODO: Set panel properties / add elements
					}

					public static void main(String[] args) {
						?name? program = new ?name?();
						program.run();
					}

					public void run() {
						// TODO: Add code here...
					}
				}
				""",
		SwingElement.JPANEL, """
				/*
					?extra-properties?
				 */

				import javax.swing.JPanel;

				public class ?name? extends JPanel {
					public ?name?() {
						// TODO: Set panel properties / add elements
					}

					// TODO: Add code here
				}
				""",
		SwingElement.JLABEL, """
				/*
					?extra-properties?
				 */
				JLabel ?name? = new JLabel("?value?");
				add(?name?);
				"""
	);
}