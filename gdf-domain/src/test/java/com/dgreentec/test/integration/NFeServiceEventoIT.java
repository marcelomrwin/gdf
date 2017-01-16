package com.dgreentec.test.integration;

import java.io.File;
import java.util.List;
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
import com.dgreentec.domain.boundary.api.ContratoService;
import com.dgreentec.domain.boundary.api.NFeService;
import com.dgreentec.domain.boundary.impl.NFeServiceBean;
import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.EventoDocumento;
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

	@Deployment
	public static Archive<?> createDeployment() {
		WebArchive deploy = getWebDeploy(DATABASE.POSTGRES_DESENV_NO_DROP, true);

		deploy.addPackages(true, Contrato.class.getPackage(), ContratoRepository.class.getPackage(), NFeService.class.getPackage(),
				NFeServiceBean.class.getPackage(), XSDObject.class.getPackage());

		deploy.as(ZipExporter.class).exportTo(new File("/tmp/arquillian_web.war"), true);
		return deploy;
	}

	@Test
	@InSequence(1)
	public void consultarEventosDoContrato() throws NfeException, InterruptedException, ExecutionException {
		Contrato contrato = contratoService.consultarContratoPorIdContrato(1L);
		assertThat(contrato, is(not(nullValue())));
		assertThat(contrato.getEmpresas(), is(not(Matchers.empty())));

		TipoAmbienteEnum ambiente = TipoAmbienteEnum.HOMOLOGACAO;

		nfeService.processarEventosPorContrato(contrato, ambiente);

		TimeUnit.SECONDS.sleep(30);
	}
}
