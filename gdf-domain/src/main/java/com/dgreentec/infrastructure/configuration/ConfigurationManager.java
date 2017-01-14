package com.dgreentec.infrastructure.configuration;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;

import javax.ejb.Singleton;

@Singleton
@LocalBean
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class ConfigurationManager {

	@Inject
	protected UserTransaction userTransaction;

	@Inject
	private transient Logger logger;

	@PostConstruct
	public void init() {
		logger.info("Init configuration");

		System.setProperty("javax.xml.stream.XMLInputFactory", "com.ctc.wstx.stax.WstxInputFactory");
		System.setProperty("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory");
		System.setProperty("javax.xml.stream.XMLEventFactory", "com.ctc.wstx.stax.WstxEventFactory");

		logger.info("Configuration done");
	}
}
