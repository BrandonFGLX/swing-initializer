# swing-initializer
A simple Java program that generates template code for Swing applications.

The program uses a tree-like structure to construct Swing objects. By default,
there is a "root" class, which contains the JFrame responsible for running the
program. Custom JPanels are stored as seperate files.

Some Swing elements need specific properties for the program to export a "working"
Swing application. For example, the JLabel element needs a "JLABEL_VALUE" to show
text.

Usage:
	Download the .jar files from the "Releases" tab
	Change to the directory of the jar and run:
		"java -jar SwingInitializer.jar"
	Configure the element tree using the options.
	Use the "exit" option to exit and choose to save.
	The exported Java files should be in the provided directory.