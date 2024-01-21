package compalier_project;

import java.io.File;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Tokenizer {
	private File inputfile;
	List<String> generatedTokens;
	private int currentIndex;

	// list for reserved words
	public static final List<String> reservedWords = Arrays.asList("module", "begin", "end", "const", "var", "integer",
			"real", "char", "procedure", "mod", "div", "readint", "readreal", "readchar", "readln", "writeint",
			"writereal", "writechar", "writeln", "if", "then", "elseif", "else", "while", "do", "loop", "until", "exit",
			"call", "digit");

	// list for operator
	public static final List<String> operator = Arrays.asList(".", ";", ",", "(", "=", ")", ":", ":=", "+", "-", "*",
			"/", " |=", "<", "<=", ">", ">=", "");

	// constructor
	public Tokenizer(File inputfile) {
		super();
		this.inputfile = inputfile;
	}

	// this methods they do scan the file and get the token in the list
	public void tokenizer() {
		this.generatedTokens = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(inputfile);
			int line = 1;
			while (scanner.hasNextLine()) {
				String currentLine = scanner.nextLine();
				int pos = 0;
				for (int i = 0; i < currentLine.length(); i++) {
					char currentChar = currentLine.charAt(i);
					if (currentChar == ' ' || currentChar == '\t') {
						pos++;
						continue;
					}
					// read values
					if (Character.isDigit(currentChar)) {
						String value = "";
						while (Character.isDigit(currentChar) || currentChar == '.') {
							value += currentChar;
							i++;
							if (i < currentLine.length()) {
								currentChar = currentLine.charAt(i);
							} else {
								break;
							}
						}
						i--;
						pos += value.length();
						generatedTokens.add(value);
						continue;
					}

					if (currentChar == '>') {
						if (i + 1 < currentLine.length()) {
							char nextChar = currentLine.charAt(i + 1);
							if (nextChar == '=') {
								addToken(">=");
								i++;
								pos += 2;
								continue;
							}
						}
						addToken(">");
						pos++;
						continue;
					}
					if (currentChar == '<') {
						if (i + 1 < currentLine.length()) {
							char nextChar = currentLine.charAt(i + 1);
							if (nextChar == '=') {
								addToken("<=");
								i++;
								pos += 2;
								continue;
							}
						}
						addToken("<");
						pos++;
						continue;
					}
					if (currentChar == ':') {
						if (i + 1 < currentLine.length()) {
							char nextChar = currentLine.charAt(i + 1);
							if (nextChar == '=') {
								addToken(":=");
								i++;
								pos += 2;
								continue;
							}
						}
						addToken(":");
						pos++;
						continue;
					}
					if (currentChar == '|') {
						if (i + 1 < currentLine.length()) {
							char nextChar = currentLine.charAt(i + 1);
							if (nextChar == '=') {
								addToken("|=");
								i++;
								pos += 2;
								continue;
							}
						}
						
					}
					if (currentChar == '=') {
						addToken("=");
						pos++;
						continue;
					}
					if (currentChar == ';') {
						addToken(";");
						pos++;
						continue;
					}

					// check for ,
					if (currentChar == ',') {
						addToken(",");
						pos++;
						continue;
					}
					if (currentChar == '(') {
						addToken("(");
						pos++;
						continue;
					}
					if (currentChar == ')') {
						addToken(")");
						pos++;
						continue;
					}

					if (currentChar == '.') {
						addToken(".");
						pos++;
						continue;
					}

					if (currentChar == '+') {
						addToken("+");
						pos++;
						continue;
					}

					if (currentChar == '-') {
						addToken("-");
						pos++;
						continue;
					}
					if (currentChar == '*') {
						addToken("*");
						pos++;
						continue;
					}

					if (currentChar == '/') {
						addToken("/");
						pos++;
						continue;
					}
				}
				line++;
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e.getMessage());
		}
	}

	// this funcation checks if the token is a reserved word
	private void addToken(String token) {
		if (reservedWords.contains(token)) {
			// generatedTokens.add(token.toUpperCase());
		} else {
			// generatedTokens.add(token);
		}
		generatedTokens.add(token);
	}

	// this methods for check if the chrachter is operator
	private boolean isTerminalCharacter(char character) {
		return operator.contains(String.valueOf(character));
	}

	/*
	 * get and set
	 */
	public File getInputfile() {
		return inputfile;
	}

	public void setInputfile(File inputfile) {
		this.inputfile = inputfile;
	}

	// return generate Tokens
	public List<String> getGeneratedTokens() {
		return generatedTokens;
	}

	public void setGeneratedTokens(List<String> generatedTokens) {
		this.generatedTokens = generatedTokens;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}


}
