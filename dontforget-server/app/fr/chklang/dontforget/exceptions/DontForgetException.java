/**
 * 
 */
package fr.chklang.dontforget.exceptions;

/**
 * @author S0075724
 *
 */
public class DontForgetException extends RuntimeException {

	/** SVUID */
	private static final long serialVersionUID = -5203059674585798149L;

	public DontForgetException() {
		super();
	}

	public DontForgetException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DontForgetException(String message, Throwable cause) {
		super(message, cause);
	}

	public DontForgetException(String message) {
		super(message);
	}

	public DontForgetException(Throwable cause) {
		super(cause);
	}

}
