import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum SwingElement {
	JFRAME,
	JPANEL,
	JLABEL;
}

enum Property {
	CLASS,
	NAME,
	JLABEL_VALUE;
}

public class Element {
	private SwingElement type;
	private Element parent;
	private List<Element> children;
	private Map<Property, String> properties;

	/*
	 * Initialize as root (main JPanel)
	 */
	public Element() {
		this(SwingElement.JFRAME);

	}

	public Element(SwingElement type) {
		this.type = type;
		this.parent = null;
		children = new ArrayList<>();
		properties = new HashMap<>();
	}

	/* 
	 * Recursively searches for the first element from this element with given name
	 */
	public Element findElementWithName(String name) {
		if (this.get(Property.NAME).equalsIgnoreCase(name)) {
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

		TermIO.printBoxedWithPrefix(List.of(type.toString(), get(Property.NAME), properties.toString()), primaryPrefix, secondaryPrefix);

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

	public void set(Property p, String val) {
		properties.put(p, val);
	}

	public String get(Property p) {
		return properties.get(p);
	}

	public Element getParent() {
		return parent;
	}

	public List<Element> getChildren() {
		return children;
	}

	public Map<Property, String> getProperties() {
		return properties;
	}

	public String toString() {
		return String.format("%s (%s)", get(Property.NAME), type);
	}

	public boolean equals(Object o) {
		if (o != null && o instanceof Element) {
			return ((Element) o).get(Property.NAME).equals(get(Property.NAME));
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

				import java.awt.*;
				import javax.swing.*;

				public class ?CLASS? {
					private JFrame frame;

					public ?CLASS?() {
						frame = new JFrame();
						frame.setTitle("?NAME?");
						frame.setSize(500, 500);
						frame.setLocationRelativeTo(null);
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						frame.setVisible(true);

						?ELEMENT?
					}

					public static void main(String[] args) {
						?CLASS? program = new ?CLASS?();
						program.run();
					}

					public void add(JComponent component) {
						frame.add(component);
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

				import java.awt.*;
				import javax.swing.*;

				public class ?CLASS? extends JPanel {
					public ?CLASS?() {
						?PROPERTIES?

						?ELEMENT?
					}

					// TODO: Add code here
				}
				""",
		SwingElement.JLABEL, """
				/*
					?extra-properties?
				 */
				JLabel ?NAME? = new JLabel("?JLABEL_VALUE?");
				?PROPERTIES?
				add(?NAME?);
				"""
	);
}