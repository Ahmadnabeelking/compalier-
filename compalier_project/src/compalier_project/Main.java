package compalier_project;

import java.io.File;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("");
		Tokenizer token = new Tokenizer(file);
		token.tokenizer();
		RecursiveDecentParser par = new RecursiveDecentParser((ArrayList<String>) token.generatedTokens);
		try {
			par.parse();
		} catch (SyntaxErorr e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
