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

	// This is the method if called then the file is parsed
	public void parse() throws SyntaxErorr {
		module_dec();
	}

	// this mehtod to fet next token
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

	// this method to to handle the invalid user defined naming
	private boolean invalidName() throws Error {
		if (Tokenizer.reservedWords.contains(currentToken)) {
			answer = false;
			@SuppressWarnings("unused")
			Error e = new Error(currentToken);
			String message = "Invalid naming at " + currentToken + " used reserved word";
			setError(message);
			currentToken = null;
			return true;
		} else {
			nextToken();
			return false;
		}
	}

	// this method to handle the integer value
	private void invalidINT() throws Error {
		try {
			@SuppressWarnings("unused")
			int value = Integer.parseInt(currentToken);
			nextToken();
		} catch (Exception e1) {
			answer = false;
			@SuppressWarnings("unused")
			Error e = new Error(currentToken);
			String message = "Invalid integer value at " + currentToken;
			setError(message);
			currentToken = null;
		}
	}

	// return the result
	public String returnResult() {
		if (answer == true) {
			return "parsed";
		} else {
			return "failed";
		}
	}

	private boolean match(String expectedToken) throws Error {
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

	private void module_dec() throws Error {
		module_heading();
		declarations();
		procedure_decl();
		block();
		name();
		match(".");
	}

	private void module_heading() throws Error {
		match("module");
		name();
		match(";");
	}

	private void block() throws Error {
		match("begin");
		stmt_list();
		match("end");
	}

	private void declarations() throws Error {
		const_decl();
		var_decl();
	}

	private void const_decl() throws Error {
		if (match("const")) {
			const_list();
		} else {
//			lamda case
		}

	}

//	return to do this in other time
	private void const_list() throws Error {
		while (name()) {
			match(";");
			value();
			match(";");
		}
	}

	private void var_decl() throws Error {
		if (match("var")) {
			var_list();
		} else {
//			lamad case
			nextToken();
		}

	}

	private void var_list() throws Error {
		while (var_item()) {
			match(";");
		}
	}

	private boolean var_item() throws Error {
		name_list();
		match(":");
		data_type();
		return answer;

	}

	private void name_list() throws Error {
		name();
		while (match(",")) {
			name();
		}
	}

	private void data_type() throws Error {
		if (match("integer ")) {
			nextToken();
		} else if (match("real")) {
			nextToken();
		} else if (match("char")) {
			nextToken();
		}
	}

	private void procedure_decl() throws Error {
		procedure_heading();
		declarations();
		block();
		name();
		match(";");
	}

	private void procedure_heading() throws Error {
		match("procedure");
		name();
		match(";");
	}

	private void stmt_list() throws Error {
		statement();
		while (match(";")) {
			statement();
		}
	}

	private void statement() throws Error {
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

	private boolean ass_stmt() throws Error {
		name();
		match(":=");
		exp();
		return true;
	}

	private void exp() throws Error {
		term();
		while (add_oper()) {
			term();
		}
	}

	private void term() throws Error {
		factor();
		while (mul_oper()) {
			factor();
		}
	}

	private void factor() throws Error {
		if (match("(")) {
			exp();
			match(")");
		} else if (name()) {
			nextToken();
		} else if (value()) {
			nextToken();
		}

	}

	private boolean add_oper() throws Error {
		if (currentToken.equals("+")) {
			match("+");
		} else if (currentToken.equals("-")) {
			match("-");
		}
		return true;
	}

	private boolean mul_oper() throws Error {
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

	private boolean read_stmt() throws Error {
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

	private boolean write_stmt() throws Error {
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

	private void write_list() throws Error {
		write_item();
		while (match(",")) {
			write_item();
		}
	}

	private void write_item() throws Error {
		if (name()) {

		} else {
			value();
		}
	}

	private boolean if_stmt() throws Error {
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

	private void elseif_part() throws Error {

	}

	private void else_part() throws Error {
		if (match("else")) {
			stmt_list();
		} else {
			nextToken();
		}
	}

	private boolean while_stmt() throws Error {
		match("while");
		condition();
		match("do");
		stmt_list();
		match("end");
		return true;
	}

	private boolean repeat_stmt() throws Error {
		match("loop");
		stmt_list();
		match("until");
		condition();
		return true;
	}

	private boolean exit_stmt() throws Error {
		match("exit");
		return true;
	}

	private boolean call_stmt() throws Error {
		match("call");
		name();
		return true;
	}

	private void condition() throws Error {
		name_value();
		relational_oper();
		name_value();
	}

	private void name_value() throws Error {
		if (nextToken()) {
			name();
		} else {
			value();
		}
	}

	private void relational_oper() throws Error {
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

	private boolean name() throws Error {
		// letter();

		return true;

	}

	private boolean value() throws Error {
		if (match("integer")) {
			integer_value();
			return true;
		} else {
			real_value();
			return true;
		}

	}

	private void integer_value() throws Error {
		int value = Integer.parseInt(currentToken);
		nextToken();
	}

	private void real_value() throws Error {
		 double value = Double.parseDouble(currentToken);
		 nextToken();
}

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

	public void setError(String error) {
		this.error = error;
	}
}
