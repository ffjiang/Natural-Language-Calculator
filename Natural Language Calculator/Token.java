public class Token {
	private String token;
	private double value;
	private boolean operator;
	private boolean unaryOperator;
	private boolean tokenized;

	public Token(String token) {
		this.token = token;
	}

	public Token(double value) {
		this.value = value;
		isTokenized = true;
	}


	// Accessor methods

	public getToken() {
		return token;
	}

	public setToken(String token, boolean tokenized) {
		this.token = token;
		this.tokenized = tokenized;
	}

	public getValue() {
		return value;
	}

	public setValue(double value) {
		this.value = value;
		tokenized = true;
	}

	public isOperator() {
		return (tokenized && token == NULL);
	}

	public isTokenized() {
		return tokenized;
	}

	public isUnaryOperator() {
		return unaryOperator;
	}

	public setUnaryOperator() {
		unaryOperator = true;
	}
}