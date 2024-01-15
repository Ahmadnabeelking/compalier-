package compalier_project;

import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tokenizer {

	private StringBuilder inputfile;
	private ArrayList<String> generatedTokens;
	private int currentIndex;

	public static final List<String> reservedWords = Arrays.asList("module", "begin", "end", "const", "var", "integer",
			"real", "char", "procedure", "mod", "div", "readint", "readreal", "readchar", "readln", "writeint",
			"writereal", "writechar", "writeln", "if", "then", "elseif", "else", "while", "do", "loop", "until", "exit",
			"call");

	public static final List<String> terminals = Arrays.asList(".", ";", ",", "(", "=", ")", ":", ":=", "+", "-", "*",
			"/", " |=", "<", "<=", ">", ">=", "");

	public Tokenizer(StringBuilder inputfile) {
		this.inputfile = inputfile;
		this.generatedTokens = new ArrayList<>();
		this.currentIndex = 0;
	}

	public ArrayList<String> tokenize() {
		StringBuilder currentToken = new StringBuilder();

		while (currentIndex < inputfile.length()) {
			char currentChar = inputfile.charAt(currentIndex);

			if (Character.isWhitespace(currentChar)) {
				currentIndex++;
				continue;
			}

			if (currentChar == ':' && currentIndex + 1 < inputfile.length()
					&& inputfile.charAt(currentIndex + 1) == '=') {
				// Tokenize ":=" as a single token
				if (currentToken.length() > 0) {
					addToken(currentToken.toString());
					currentToken.setLength(0);
				}
				addToken(":=");
				currentIndex += 2;
				continue;
			}

			if (currentChar == '<' && currentIndex + 1 < inputfile.length()
					&& inputfile.charAt(currentIndex + 1) == '>') {
				// Tokenize "<>" as a single token
				if (currentToken.length() > 0) {
					addToken(currentToken.toString());
					currentToken.setLength(0);
				}
				addToken("<>");
				currentIndex += 2;
				continue;
			}

			if (currentChar == '<' && currentIndex + 1 < inputfile.length()
					&& inputfile.charAt(currentIndex + 1) == '=') {
				// Tokenize "<=" as a single token
				if (currentToken.length() > 0) {
					addToken(currentToken.toString());
					currentToken.setLength(0);
				}
				addToken("<=");
				currentIndex += 2;
				continue;
			}

			if (currentChar == '>' && currentIndex + 1 < inputfile.length()
					&& inputfile.charAt(currentIndex + 1) == '=') {
				// Tokenize ">=" as a single token
				if (currentToken.length() > 0) {
					addToken(currentToken.toString());
					currentToken.setLength(0);
				}
				addToken(">=");
				currentIndex += 2;
				continue;
			}

			if (isTerminalCharacter(currentChar)) {
				/*
				 * If token is terminal here
				 */
				if (currentToken.length() > 0) {
					addToken(currentToken.toString());
					currentToken.setLength(0);
				}
				addToken(String.valueOf(currentChar));
			} else {

				currentToken.append(currentChar);
				String tokenString = currentToken.toString();

				if (Character.isDigit(currentChar)) {
					if (currentToken.length() > 1 && Character.isDigit(tokenString.charAt(0))) {
						// Tokenize the previous part of the token as a reserved word
						String reservedPart = tokenString.substring(0, tokenString.length() - 1);
						if (reservedWords.contains(reservedPart)) {
							addToken(reservedPart);
							currentToken.setLength(0);
							currentToken.append(currentChar);
						}
					}
				}

				else {
					/*
					 * 
					 * If resever words here
					 */

					if (reservedWords.contains(tokenString)) {
						addToken(tokenString);
						currentToken.setLength(0);
					}

				}
			}
			currentIndex++;
		}
		if (currentToken.length() > 0) {
			addToken(currentToken.toString());
		}
		return generatedTokens;
	}

	/*
	 * Check if the chrachter is terminal
	 */
	/**
	 * @param character
	 * @return
	 */
	private boolean isTerminalCharacter(char character) {
		return terminals.contains(String.valueOf(character));
	}

	/*
	 * This method checks if the token is a reserved word
	 */
	/**
	 * @param token
	 */
	private void addToken(String token) {
		if (reservedWords.contains(token)) {
			// generatedTokens.add(token.toUpperCase());
		} else {
			// generatedTokens.add(token);
		}
		generatedTokens.add(token);
	}

	/*
	 * Getters and setters
	 */
	/**
	 * @return the input file
	 */
	public StringBuilder getInputfile() {
		return inputfile;
	}

	/**
	 * @param input file the input file to set
	 */
	public void setInputfile(StringBuilder inputfile) {
		this.inputfile = inputfile;
	}

	/**
	 * @return the generatedTokens
	 */
	public ArrayList<String> getGeneratedTokens() {
		return generatedTokens;
	}

	/**
	 * @param generatedTokens the generatedTokens to set
	 */
	public void setGeneratedTokens(ArrayList<String> generatedTokens) {
		this.generatedTokens = generatedTokens;
	}

	/**
	 * @return the currentIndex
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}

	/**
	 * @param currentIndex the currentIndex to set
	 */
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

}
