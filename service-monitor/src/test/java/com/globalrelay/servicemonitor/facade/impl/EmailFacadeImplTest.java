package com.globalrelay.servicemonitor.facade.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class EmailFacadeImplTest {

	@MockBean
	private JavaMailSender emailSender;

	@InjectMocks
	EmailFacadeImpl emailFacade;
	
	@Test
	public void testSendSimpleMessage() throws Exception {

		Mockito.doNothing().when(emailSender).send(Mockito.any(SimpleMailMessage.class));
		emailFacade.sendSimpleMessage("a", "subject", "text");
	}
}
