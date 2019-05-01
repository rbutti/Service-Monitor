package com.globalrelay.servicemonitor.exception;

/**
 * The ServiceMonitorJobException wraps all checked standard Java exception
 * and enriches them with a custom error code.
 *<p>
 * This exception will be throw from scheduled job layer
 */
public class ServiceMonitorJobException extends Exception {

	private static final long serialVersionUID = 7718828512143293558L;

	private final ErrorCode code;

	public ServiceMonitorJobException(ErrorCode code) {
		super();
		this.code = code;
	}

	public ServiceMonitorJobException(String message, Throwable cause, ErrorCode code) {
		super(message, cause);
		this.code = code;
	}

	public ServiceMonitorJobException(String message, ErrorCode code) {
		super(message);
		this.code = code;
	}

	public ServiceMonitorJobException(Throwable cause, ErrorCode code) {
		super(cause);
		this.code = code;
	}

	public ErrorCode getErrorCode() {
		return this.code;
	}

	@Override
	public String toString() {
		return "[code=" + code + "] | " + super.toString();
	}	
}
