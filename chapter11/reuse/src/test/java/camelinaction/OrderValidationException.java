package camelinaction;

public class OrderValidationException extends Exception {
   
	private static final long serialVersionUID = -8925008651642241009L;

	public OrderValidationException(String s) {
        super(s);
    }
}
