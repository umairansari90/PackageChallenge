package com.mobiquityinc.exception;

/**
 * Type of Runtime Exception to be used in case of invalid parameters
 *
 * @author UmairZ
 * @since Apr 20, 2018
 *
 */
public class APIException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public APIException() {
	}

	public APIException(String msg) {
		super(msg);
	}

}
