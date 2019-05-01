package com.globalrelay.servicemonitor.strategy;

/**
 * Strategy interface for Scheduling tasks
 * <p>
 * Concrete implementation of this interface should implement logic to monitor a
 * service
 * 
 * @author Ravikiran Butti
 *
 */
public interface ServiceMonitorStrategy {

	/**
	 * Concrete implementation of this interface should implement logic to monitor a
	 * service for a given host and port number and return true if service is up and
	 * false if service is down
	 * 
	 * @param host - Host name of the service
	 * @param port - Port number of the service
	 * @return- True if the service is up or else false
	 */
	public boolean monitorService(String host, int port);
}
