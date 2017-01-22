package com.dgreentec.test.integration;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.dgreentec.AbstractTestCase;
import com.dgreentec.IntegrationTest;
import com.dgreentec.TestCasePrinterRule;
import com.dgreentec.domain.boundary.api.ContratoService;
import com.dgreentec.domain.boundary.api.NFeService;
import com.dgreentec.domain.boundary.api.TenantService;
import com.dgreentec.domain.boundary.impl.NFeServiceBoundary;
import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.EventoDocumento;
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.model.TipoAmbienteEnum;
import com.dgreentec.domain.repository.ContratoRepository;
import com.dgreentec.domain.xsd.XSDObject;
import com.dgreentec.infrastructure.exception.NfeException;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;

@RunWith(Arquillian.class)
@Category(IntegrationTest.class)
public class NFeServiceEventoIT extends AbstractTestCase {

	@Inject
	NFeService nfeService;

	@Inject
	ContratoService contratoService;

	@Inject
	TenantService tenantService;

	@Deployment
	public static Archive<?> createDeployment() {
		WebArchive deploy = getWebDeploy(DATABASE.POSTGRES_DESENV_NO_DROP, true);

		deploy.as(ZipExporter.class).exportTo(new File("/tmp/arquillian_web.war"), true);
		return deploy;
	}

	@Test
	@InSequence(1)
	public void consultarEventosDoContrato() throws NfeException, InterruptedException, ExecutionException, IOException {
				TipoAmbienteEnum ambiente = TipoAmbienteEnum.HOMOLOGACAO;
				Tenant rioPolem = tenantService.consultarTenantPorIdTenant(1L);
				assertThat(rioPolem, is(not(nullValue())));
				Contrato contratoRioPolem = contratoService.consultarContratoPorIdContrato(rioPolem, 1L);
				assertThat(contratoRioPolem, is(not(nullValue())));
				assertThat(contratoRioPolem.getEmpresas(), is(not(Matchers.empty())));

				Tenant ddx = tenantService.consultarTenantPorIdTenant(2L);
				Contrato contratoDDX = contratoService.consultarContratoPorIdContrato(ddx, 1L);
				assertThat(contratoDDX, is(not(nullValue())));
				assertThat(contratoDDX.getEmpresas(), is(not(Matchers.empty())));

				nfeService.processarEventosPorContrato(rioPolem, contratoRioPolem, ambiente);
				nfeService.processarEventosPorContrato(ddx, contratoDDX, ambiente);

		//como o serviço é todo assincrono é preciso forçar um sleep
		//		Calendar c = Calendar.getInstance();
		//		c.add(Calendar.MILLISECOND, 5);
		//		Date future = c.getTime();
		//		int wait = 15;
		//		do {
		//			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		//			long startTime = System.currentTimeMillis();
		//			while ((System.currentTimeMillis() - startTime) < wait * 1000 && !in.ready()) {
		//			}
		//			if (in.ready()) {
		//				//se digitar algo no console sai do loop
		//				String line = in.readLine();
		//				System.out.println("Entered " + line);
		//				break;
		//			}
		//		} while (new Date().before(future));
		//		System.out.println("Fim do método");

		TimeUnit.SECONDS.sleep(60);
	}
}
