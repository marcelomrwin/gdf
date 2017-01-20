package com.dgreentec.test.integration;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.dgreentec.AbstractTestCase;
import com.dgreentec.TestCasePrinterRule;

@RunWith(Arquillian.class)
public class GenerateTableSchemaTest extends AbstractTestCase {

	@Deployment
	public static Archive<?> createDeployment() {
		JavaArchive deploy = getEJBDeployCreate();
		deploy.addClasses(AbstractTestCase.class, TestCasePrinterRule.class);
		deploy.as(ZipExporter.class).exportTo(new File("/tmp/arquillian_test.jar"), true);
		return deploy;
	}

	@Test
	public void smokeTest() {
		logger.debug("test end!");
	}
}
