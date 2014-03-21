public class Token {
	public enum TokenType {OPERAND, INTEGRAL_EXPR, INTEGRAL_FROM, INTEGRAL_TO, OPERATOR, UNARY, BINARY}

	private String token;
	private double value;
	private boolean tokenized;
	private TokenType type;

	public Token(String token, boolean tokenized) {
		this.token = token;
		this.tokenized = tokenized;
		if (tokenized) {
			type = TokenType.OPERATOR;
		}
	}

	public Token(double value) {
		this.value = value;
		tokenized = true;
		type = TokenType.OPERAND;
	}


	// Accessor methods

	public String getToken() {
		return token;
	}

	public void setToken(String token, boolean tokenized) {
		this.token = token;
		this.tokenized = tokenized;
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

	public boolean isOperand() {
		return (type == TokenType.OPERAND);
	}

	public boolean isOperator() {
		return (type == TokenType.OPERATOR || type == TokenType.UNARY || type == TokenType.BINARY);
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