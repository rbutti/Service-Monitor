package com.globalrelay.servicemonitor.facade;

import com.globalrelay.servicemonitor.exception.ServiceMonitorFacadeException;

/**
 * Facade interface for Email Notification
 * <p>
 * Concrete implementation of this interface should implement logic to send an
 * email to desired user with necessary subject line and text
 * 
 * @author Ravikiran Butti
 *
 */
public interface EmailFacade {

	/**
	 * Concrete implementation of this method should implement logic to send an
	 * email to the input user with the given input subject line and text
	 * 
	 * @param to      - email id of the recepient
	 * @param subject - subject line of the email
	 * @param text    - body text of the email
	 * @throws ServiceMonitorFacadeException - Exception thrown if an email cannot
	 *                                       be sent
	 */
	public void sendSimpleMessage(String to, String subject, String text) throws ServiceMonitorFacadeException;
}
