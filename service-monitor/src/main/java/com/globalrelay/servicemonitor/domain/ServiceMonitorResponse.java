package com.globalrelay.servicemonitor.domain;

/**
 * A domain class for ServiceMonitor Application responses.
 * <p>
 * This class will be used to send both the success and failure messages. There
 * are some helper static methods to construct the success and failure response
 * objects
 * 
 * @author Ravikiran Butti
 *
 */
public class ServiceMonitorResponse {

	private static final String SUCCESS = "success";
	private static final String ERROR = "error";

	private String message;
	private String status;
	private Object data;

	private ServiceMonitorResponse() {
	}

	private ServiceMonitorResponse(String status, String message) {
		this.status = status;
		this.message = message;
	}

	private ServiceMonitorResponse(String status, String message, Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public static ServiceMonitorResponse success(String message) {
		return new ServiceMonitorResponse(SUCCESS, message);
	}

	public static ServiceMonitorResponse success(String message, Object data) {
		return new ServiceMonitorResponse(SUCCESS, message, data);
	}

	public static ServiceMonitorResponse error(String message) {
		return new ServiceMonitorResponse(ERROR, message);
	}

	public static ServiceMonitorResponse error(String message, Object data) {
		return new ServiceMonitorResponse(ERROR, message, data);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceMonitorResponse other = (ServiceMonitorResponse) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServiceMonitorResponse [message=" + message + ", status=" + status + ", data=" + data + "]";
	}
}
