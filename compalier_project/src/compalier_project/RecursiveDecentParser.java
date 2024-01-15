package compalier_project;

import java.util.ArrayList;

public class RecursiveDecentParser {

	private int index;
	private String currentToken;
	private boolean answer = true;
	private String error = "";
	ArrayList<String> tokens;

	public RecursiveDecentParser(ArrayList<String> tokens) {
		this.tokens = tokens;
		this.index = 0;
		this.currentToken = tokens.get(0);
	}

	/**
	 * This is the method if called then the file is parsed
	 * 
	 * @throws SyntaxErorr
	 */
	public void parse() throws SyntaxErorr {
		try {
			module_decl();
		} catch (SyntaxErorr e) {
			ERORR("Expected token not found at " + currentToken);
		}
	}

	/**
	 * this methid set the index to the next token index
	 */
	private boolean nextToken() {
		index++;
		if (index < tokens.size()) {
			currentToken = tokens.get(index);
			return true;
		} else {
			currentToken = null;
			return false;
		}
	}

	/*
	 * I created this method to hanldle error modes panicly were i put the next
	 * token as null
	 */
	/**
	 * @param string
	 * @throws SyntaxErorr
	 */
	private void ERORR(String string) throws SyntaxErorr {
		answer = false;
		SyntaxErorr e = new SyntaxErorr(currentToken);
		e.setMessage(string);
		setError(e.getMessage());
		System.out.println(e.getMessage());
		/*
		 * The panic mode
		 */
		currentToken = null;
	}

	/*
	 * I ceated this method to handle the invalid user defined naming
	 */
	/**
	 * @return
	 * @throws SyntaxErorr
	 */
	private boolean invalidName() throws SyntaxErorr {
		if (Tokenizer.reservedWords.contains(currentToken)) {
			answer = false;
			@SuppressWarnings("unused")
			SyntaxErorr e = new SyntaxErorr(currentToken);
			String message = "Invalid naming at " + currentToken + " used reserved word";
			setError(message);
			currentToken = null;
			return true;
		} else {
			nextToken();
			return false;
		}
	}

	/*
	 * I created this method to handle when the value integer is invalid
	 */
	/**
	 * @throws SyntaxErorr
	 */
	private void invalidINT() throws SyntaxErorr {
		try {
			@SuppressWarnings("unused")
			int value = Integer.parseInt(currentToken);
			nextToken();
		} catch (Exception e1) {
			answer = false;
			@SuppressWarnings("unused")
			SyntaxErorr e = new SyntaxErorr(currentToken);
			String message = "Invalid integer value at " + currentToken;
			setError(message);
			currentToken = null;
		}
	}

	/*
	 * This method returns the answer in string to be displayed to usr this is the
	 * final answer of the parsing process
	 */
	/**
	 * @return answer if passed or failure
	 */
	public String returnResult() {
		if (answer == true) {
			return "parsed";
		} else {
			return "failed";
		}
	}

	/*
	 * This method is created for the reason that in each production rule instead of
	 * writing the same logic of matching an expected token I use this method to
	 * match if it doesn't match what is expected like a semi colon for example it
	 * will call the ERROR method which will return the error
	 */
	/**
	 * @param expectedToken
	 * @return
	 * @throws SyntaxErorr
	 */
	private boolean match(String expectedToken) throws SyntaxErorr {
		if (currentToken == null) {
			return false;
		}
		String nextToken = currentToken;
		nextToken();
		if (nextToken == null || !nextToken.equals(expectedToken)) {
			// ERORR("Expected: " + expectedToken + ", but found: " + (nextToken != null ?
			// nextToken : "end of input"));
			return false;
		}
		return true;
	}

	private void module_decl() throws SyntaxErorr {
		module_heading();
		declarations();
		procedure_decl();
		block();
		name();
		match(".");
	}

	private void module_heading() throws SyntaxErorr {
		match("module");
		name();
		match(";");
	}

	private void block() throws SyntaxErorr {
		match("begin");
		stmt_list();
		match("end");
	}

	private void declarations() throws SyntaxErorr {
		const_decl();
		var_decl();
	}

	private void const_decl() throws SyntaxErorr {
		if (match("const")) {
			const_list();
		} else {
//			lamda case
		}

	}

//	return to do this in other time
	private void const_list() throws SyntaxErorr {
		while (name()) {
			match(";");
		}
	}

	private void var_decl() throws SyntaxErorr {
		if (match("var")) {
			var_list();
		} else {
//			lamad case
		}

	}

	private void var_list() throws SyntaxErorr {
		while (var_item()) {
			match(";");
		}
	}

	private boolean var_item() throws SyntaxErorr {
		name_list();
		match(":");
		data_type();
		return answer;

	}

	private void name_list() throws SyntaxErorr {
		name();
		while (match(",")) {
			name();
		}
	}

	private void data_type() throws SyntaxErorr {
		if (match("integer ")) {
			nextToken();
		} else if (match("real")) {
			nextToken();
		} else if (match("char")) {
			nextToken();
		}
	}

	private void procedure_decl() throws SyntaxErorr {
		procedure_heading();
		declarations();
		block();
		name();
		match(";");
	}

	private void procedure_heading() throws SyntaxErorr {
		match("procedure");
		name();
		match(";");
	}

	private void stmt_list() throws SyntaxErorr {
		statement();
		while (match(";")) {
			statement();
		}
	}

	private void statement() throws SyntaxErorr {
		if (ass_stmt()) {
			nextToken();
		} else if (read_stmt()) {
			nextToken();
		} else if (write_stmt()) {
			nextToken();
		} else if (if_stmt()) {
			nextToken();
		} else if (while_stmt()) {
			nextToken();
		} else if (repeat_stmt()) {
			nextToken();
		} else if (exit_stmt()) {
			nextToken();
		} else if (call_stmt()) {
			nextToken();
		} else {
//			case lamda
			nextToken();
		}
	}

	private boolean ass_stmt() throws SyntaxErorr {
		name();
		match(":=");
		exp();
		return true;
	}

	private void exp() throws SyntaxErorr {
		term();
		while (add_oper()) {
			term();
		}
	}

	private void term() throws SyntaxErorr {
		factor();
		while (mul_oper()) {
			factor();
		}
	}

	private void factor() throws SyntaxErorr {
		if (match("(")) {
			exp();
			match(")");
		} else if (name()) {
			nextToken();
		} else if (value()) {
			nextToken();
		}

	}

	private boolean add_oper() throws SyntaxErorr {
		if (currentToken.equals("+")) {
			match("+");
		} else if (currentToken.equals("-")) {
			match("-");
		}
		return true;
	}

	private boolean mul_oper() throws SyntaxErorr {
		if (currentToken.equals("*")) {
			match("*");
		} else if (currentToken.equals("/")) {
			match("/");
		} else if (currentToken.equals(match("mod"))) {
			match("mod");
		} else if (currentToken.equals(match("div"))) {
			match("div");
		}
		return true;
	}

	private boolean read_stmt() throws SyntaxErorr {
		match("readint");
		if (match("(")) {
			name_list();
			match(")");
		} else if (currentToken.equals(match("readreal"))) {
			if (match("(")) {
				name_list();
				match(")");
			}
		} else if (currentToken.equals(match("readchar"))) {
			if (match("(")) {
				name_list();
				match(")");
			}
		} else {
			match("readln");
		}
		return true;
	}

	private boolean write_stmt() throws SyntaxErorr {
		match("writeint");
		if (match("(")) {
			write_list();
			match(")");
		} else if (currentToken.equals(match("writereal"))) {
			if (match("(")) {
				write_list();
				match(")");
			}
		} else if (currentToken.equals(match("writechar"))) {
			if (match("(")) {
				write_list();
				match(")");
			}
		} else {
			match("writeln");
		}
		return true;
	}

	private void write_list() throws SyntaxErorr {
		write_item();
		while (match(",")) {
			write_item();
		}
	}

	private void write_item() throws SyntaxErorr {
		if (name()) {

		} else if (value()) {

		}
	}

	private boolean if_stmt() throws SyntaxErorr {
		if (match("if")) {
			condition();
		} else if (match("then")) {
			stmt_list();
			elseif_part();
			else_part();
		} else {
			match("end");
		}
		return true;
	}

	private void elseif_part() throws SyntaxErorr {

	}

	private void else_part() throws SyntaxErorr {
		if (match("else")) {
			stmt_list();
		} else {
			nextToken();
		}
	}

	private boolean while_stmt() throws SyntaxErorr {
		match("while");
		condition();
		match("do");
		stmt_list();
		match("end");
		return true;
	}

	private boolean repeat_stmt() throws SyntaxErorr {
		match("loop");
		stmt_list();
		match("until");
		condition();
		return true;
	}

	private boolean exit_stmt() throws SyntaxErorr {
		match("exit");
		return true;
	}

	private boolean call_stmt() throws SyntaxErorr {
		match("call");
		name();
		return true;
	}

	private void condition() throws SyntaxErorr {
		name_value();
		relational_oper();
		name_value();
	}

	private void name_value() throws SyntaxErorr {
		if (nextToken()) {
			name();
		} else {
			value();
		}
	}

	private void relational_oper() throws SyntaxErorr {
		if (currentToken.equals("=")) {
			match("=");
		} else if (currentToken.equals("|=")) {
			match("<|=>");
		} else if (currentToken.equals("<")) {
			match("<");
		} else if (currentToken.equals("<=")) {
			match("<=");
		} else if (currentToken.equals(">")) {
			match(">");
		} else if (currentToken.equals(">=")) {
			match(">=");
		}
	}

	private boolean name() throws SyntaxErorr {
//		letter();
		return true;

	}

	private boolean value() throws SyntaxErorr {
		if (match("integer")) {
			integer_value();
			return true;
		} else {
			real_value();
			return true;
		}

	}

	private void integer_value() throws SyntaxErorr {

	}

	private void real_value() throws SyntaxErorr {

	}

	public void setError(String error) {
		this.error = error;
	}
}
