package compalier_project;

public class SyntaxErorr extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;

	/*
	 * Constructor
	 */
	public SyntaxErorr(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = "Parsing error due to " + message;
	}
}
