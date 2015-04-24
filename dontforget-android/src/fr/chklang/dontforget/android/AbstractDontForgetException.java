/**
 * 
 */
package fr.chklang.dontforget.android;

/**
 * @author Chklang
 *
 */
public class AbstractDontForgetException extends RuntimeException {

	/** SVUID */
	private static final long serialVersionUID = 6792081015174622149L;

	public AbstractDontForgetException() {
		super();
	}

	public AbstractDontForgetException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public AbstractDontForgetException(String detailMessage) {
		super(detailMessage);
	}

	public AbstractDontForgetException(Throwable throwable) {
		super(throwable);
	}

}
