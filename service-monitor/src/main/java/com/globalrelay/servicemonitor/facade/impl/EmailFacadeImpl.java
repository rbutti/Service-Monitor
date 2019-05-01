package com.globalrelay.servicemonitor.facade.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.globalrelay.servicemonitor.exception.ServiceMonitorFacadeException;
import com.globalrelay.servicemonitor.facade.EmailFacade;

/**
 * An implementation of {@linkplain EmailFacade}. This implementation uses
 * Spring's JavaMail api to send email notifications
 * 
 * @author Ravikiran Butti
 *
 */
@Component
public class EmailFacadeImpl implements EmailFacade {

	private static Logger LOG = LoggerFactory.getLogger(EmailFacadeImpl.class);

	@Autowired
	public JavaMailSender emailSender;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.globalrelay.servicemonitor.facade.EmailFacade#sendSimpleMessage(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	public void sendSimpleMessage(String to, String subject, String text) throws ServiceMonitorFacadeException {

		LOG.debug("Started to send email | to : {}, subject : {}, text : {}", to, subject, text);
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);
			emailSender.send(message);
			LOG.info("Email sent successfully | to : {}, subject : {}, text : {}", to, subject, text);
		} catch (Exception e) {
			LOG.warn("Failed to send email | to : {}, subject : {}, text : {}", to, subject, text);
		}
	}
}
