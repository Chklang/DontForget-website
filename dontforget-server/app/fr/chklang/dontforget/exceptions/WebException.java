package fr.chklang.dontforget.exceptions;

import play.mvc.Result;

public class WebException extends RuntimeException {
	/** SVUID*/
	private static final long serialVersionUID = 4016115770327673933L;
	
	protected final Result result;
	
	public WebException(Result pResult) {
		super();
		result = pResult;
	}

	public WebException(Result pResult, String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		result = pResult;
	}

	public WebException(Result pResult, String message, Throwable cause) {
		super(message, cause);
		result = pResult;
	}

	public WebException(Result pResult, String message) {
		super(message);
		result = pResult;
	}

	public WebException(Result pResult, Throwable cause) {
		super(cause);
		result = pResult;
	}

	/**
	 * @return the result
	 */
	public Result getResult() {
		return result;
	}
}