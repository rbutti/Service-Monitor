package com.globalrelay.servicemonitor.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import com.globalrelay.servicemonitor.job.impl.RouterJob;

/**
 * A configuration file currently support making Spring applicationContext to be
 * available for the {@linkplain RouterJob} to implement routing using the
 * ApplicationContextAware aware feature
 * <p>
 * Also see: {@linkplain RouterJob}
 * 
 * @author Ravikiran Butti
 *
 */
@Configuration
public class ServiceMonitorApplicationConfiguration implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
