package com.dgreentec.test.integration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
		logger.info("test end!");
		adjustSqlFile();
	}

	private void adjustSqlFile() {
		try {
			File sqlFolder = new File(getResourceFolder(), "sql");
			File create = new File(sqlFolder, "projectCreate.ddl");
			System.out.println("Parse file " + create.getAbsolutePath());
			List<String> nLines = new ArrayList<>();
			BufferedReader reader = new BufferedReader(new FileReader(create));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.toLowerCase().startsWith("alter table ") && line.toLowerCase().contains("unique")
						&& line.toLowerCase().contains("uk_")) {
					String fk = line.substring(line.indexOf("UK_"), line.toLowerCase().lastIndexOf(" unique "));
					String nfk = "UNQ_" + line.substring(line.lastIndexOf("(") + 1, line.lastIndexOf(")"));
					line = line.replaceAll(fk, nfk);
				}
				line = line + ";";
				nLines.add(line);
			}
			reader.close();

			PrintWriter pw = new PrintWriter(new FileWriter(create));
			for (String linha : nLines) {
				pw.println(linha);
			}
			pw.close();
			System.out.println(create.getAbsolutePath() + " parsed");

			pw = new PrintWriter(new FileWriter(new File(sqlFolder, "create_no_comum.ddl")));
			for (String linha : nLines) {
				if (!linha.toLowerCase().contains("comum."))
					pw.println(linha);
			}
			pw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
