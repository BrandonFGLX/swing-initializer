import java.util.List;

public class TermIO {
	private static final int BOX_PADDING_VERTICAL = 0;
	private static final int BOX_PADDING_HORIZONTAL = 2;

	public static void printBoxed(String message) {
		printBoxed(List.of(message));
	}

	public static void printBoxed(List<String> message) {
		message = message.stream()
			.filter(x -> x != null)
			.toList();

		int largest = message.stream()
			.mapToInt(String::length)
			.max()
			.orElse(0);

		System.out.print("+");
		for (int i = 0; i < largest + BOX_PADDING_HORIZONTAL * 2; i++) {
			System.out.print("-");
		}
		System.out.println("+");

		int index = 0;
		for (int i = 0; i < message.size() + 2 * BOX_PADDING_VERTICAL; i++) {
			String s = message.get(index);

			System.out.print("|");

			if (i >= BOX_PADDING_VERTICAL && i < message.size() + BOX_PADDING_VERTICAL) {
				for (int j = 0; j < BOX_PADDING_HORIZONTAL; j++) {
					System.out.print(" ");
				}

				System.out.print(s);
				
				for (int j = 0; j < largest - s.length() + BOX_PADDING_HORIZONTAL; j++) {
					System.out.print(" ");
				}

				index++;
			} else {
				for (int j = 0; j < largest + 2 * BOX_PADDING_HORIZONTAL; j++) {
					System.out.print(" ");
				}
			}
			
			System.out.println("|");
		}

		System.out.print("+");
		for (int i = 0; i < largest + BOX_PADDING_HORIZONTAL * 2; i++) {
			System.out.print("-");
		}
		System.out.println("+");
	}

	public static void printBoxedWithPrefix(List<String> message, String p1, String p2) {
		message = message.stream()
			.filter(x -> x != null)
			.toList();

		int largest = message.stream()
			.mapToInt(String::length)
			.max()
			.orElse(0);

		System.out.print(p1 + "+");
		for (int i = 0; i < largest + BOX_PADDING_HORIZONTAL * 2; i++) {
			System.out.print("-");
		}
		System.out.println("+");

		int index = 0;
		for (int i = 0; i < message.size() + 2 * BOX_PADDING_VERTICAL; i++) {
			String s = message.get(index);

			System.out.print(p2 + "|");

			if (i >= BOX_PADDING_VERTICAL && i < message.size() + BOX_PADDING_VERTICAL) {
				for (int j = 0; j < BOX_PADDING_HORIZONTAL; j++) {
					System.out.print(" ");
				}

				System.out.print(s);
				
				for (int j = 0; j < largest - s.length() + BOX_PADDING_HORIZONTAL; j++) {
					System.out.print(" ");
				}

				index++;
			} else {
				for (int j = 0; j < largest + 2 * BOX_PADDING_HORIZONTAL; j++) {
					System.out.print(" ");
				}
			}
			
			System.out.println("|");
		}

		System.out.print(p2 + "+");
		for (int i = 0; i < largest + BOX_PADDING_HORIZONTAL * 2; i++) {
			System.out.print("-");
		}
		System.out.println("+");
	}
}

// A formatter to store ANSI formatting when printing.
// Contains certain ANSI-related print methods.
class TermIOFormatter {
	
}