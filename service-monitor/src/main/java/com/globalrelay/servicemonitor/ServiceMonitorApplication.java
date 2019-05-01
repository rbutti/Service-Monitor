package com.globalrelay.servicemonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * A Service Monitor Application
 * <p>
 * A service monitor application will be used to monitor the status of multiple
 * services. This application has the following features:
 * <ul>
 * <li>A service is defined as a host/port combination. To check if a service is
 * up, the monitor will establish a TCP connection to the host on the specified
 * port.
 * <li>If a connection is established, the service is up. If the connection is
 * refused, the service is not up.
 * <li>The monitor will allow callers to register interest in a service, and a
 * polling frequency. The callers will be notified when the service goes up and
 * down.
 * <li>The monitor detects multiple callers registering interest in the same
 * service, and will not poll any service more frequently than once a second.
 * <li>The monitor allows callers to register a planned service outage. The
 * caller will specify a start and end time for which no notifications for that
 * service will be delivered.
 * <li>The monitor will allow callers to define a grace time that applies to all
 * services being monitored. If a service is not responding, the monitor will
 * wait for the grace time to expire before notifying any clients. If the
 * service goes back on line during this grace time, no notification will be
 * sent. If the grace time is less than the polling frequency, the monitor
 * should schedule extra checks of the service.
 * 
 * </ul>
 * <p>
 * USAGE: java -jar service-monitor.jar
 * <p>
 * EXAMPLE: java -jar service-monitor.jar
 * 
 * @author Ravikiran Butti
 *
 */
@SpringBootApplication
@EnableJpaAuditing
public class ServiceMonitorApplication {

	/**
	 * Java Entrypoint for ServiceMonitor Application
	 * 
	 * @param args - input arguments for Spring boot application
	 */
	public static void main(String[] args) {
		SpringApplication.run(ServiceMonitorApplication.class, args);
	}

}
