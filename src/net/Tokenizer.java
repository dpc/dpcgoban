/**
 * Helper class to tokenize incoming messages.
 */
class Tokenizer {
	private String str;

	public Tokenizer(String str) {
		this.str = str;
	}

	public String next() {
		if (str.equals("")) {
			return "";
		}
		int i = str.indexOf(' ');
		if (i == -1) {
			String ret = str;
			str = "";
			return ret;
		}

		String ret = str.substring(0, i);
		str = str.substring(i+1, str.length());
		return ret;
	}

	public String rest() {
		String ret = str;
		str = "";
		return ret;
	}

}
