package com.globalrelay.servicemonitor.strategy.impl;

import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.globalrelay.servicemonitor.strategy.ServiceMonitorStrategy;

/**
 * An implementation of {@linkplain ServiceMonitorStrategy}. This implementation
 * uses Java TCP Socket API to check if the given service is up and running
 * 
 * @author Ravikiran Butti
 *
 */
@Component
public class ServiceMonitorStrategyImpl implements ServiceMonitorStrategy {

	private static Logger LOG = LoggerFactory.getLogger(ServiceMonitorStrategyImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.globalrelay.servicemonitor.strategy.ServiceMonitorStrategy#monitorService
	 * (java.lang.String, int)
	 */
	@Override
	public boolean monitorService(String host, int port) {

		LOG.debug("Started service monitoring | host : {}, port : {}", host, port);
		Socket s = null;
		try {
			// open the socket to check if service is active or not.
			s = new Socket(host, port);

			LOG.info("Service is active | host : {}, port : {}", host, port);
			return true;
		} catch (Exception e) {

			LOG.info("Service is inactive | host : {}, port : {}", host, port);
			return false;
		} finally {
			if (s != null)
				try {
					s.close();
				} catch (Exception e) {
					LOG.warn("Failed to close the socket", e);
				}

			LOG.debug("Completed service monitoring | host : {}, port : {}", host, port);
		}
	}

}
