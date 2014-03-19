public class Token {
	public enum TokenType {OPERAND, OPERATOR, UNARY, BINARY}

	private String token;
	private double value;
	private boolean tokenized;
	private TokenType type;

	public Token(String token) {
		this.token = token;
	}

	public Token(double value) {
		this.value = value;
		isTokenized = true;
		type = TokenType.OPERAND;
	}


	// Accessor methods

	public String getToken() {
		return token;
	}

	public void setToken(String token, boolean tokenized) {
		this.token = token;
		this.tokenized = tokenized;
		if (tokenized) {
			type = OPERATOR;
		}
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
		tokenized = true;
	}

	public boolean isTokenized() {
		return tokenized;
	}

	public TokenType getType() {
		return type;
	}

	public void setUnary() {
		type = TokenType.UNARY;
	}

	public void setBinary() {
		type = TokenType.BINARY;
	}

	public void setOperand() {
		type = TokenType.OPERAND;
	}

}