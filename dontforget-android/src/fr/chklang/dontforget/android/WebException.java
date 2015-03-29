/**
 * 
 */
package fr.chklang.dontforget.android;

/**
 * @author Chklang
 *
 */
public class WebException extends AbstractDontForgetException {

	/** SVUID */
	private static final long serialVersionUID = 228226229887486934L;

	/**
	 * 
	 */
	public WebException() {
	}

	/**
	 * @param detailMessage
	 * @param throwable
	 */
	public WebException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	/**
	 * @param detailMessage
	 */
	public WebException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * @param throwable
	 */
	public WebException(Throwable throwable) {
		super(throwable);
	}

}
