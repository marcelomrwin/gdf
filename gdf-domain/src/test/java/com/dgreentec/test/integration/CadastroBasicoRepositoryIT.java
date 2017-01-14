//Incluir nos parâmetros do teste (VM Arguments) -DWILDFLY_HOME=${wildfly_home} -DJAVA_HOME=${java_home}
package com.dgreentec.test.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.dgreentec.AbstractTestCase;
import com.dgreentec.IntegrationTest;
import com.dgreentec.domain.boundary.api.NFeService;
import com.dgreentec.domain.model.Certificado;
import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.Empresa;
import com.dgreentec.domain.model.UFEnum;
import com.dgreentec.domain.repository.ContratoRepository;
import com.dgreentec.domain.repository.EmpresaRepository;
import com.dgreentec.domain.xsd.XSDObject;
import com.dgreentec.domain.xsd.consReciNFe_v310.ConsReciNFe;

@RunWith(Arquillian.class)
@Category(IntegrationTest.class)
public class CadastroBasicoRepositoryIT extends AbstractTestCase {

	@Inject
	private ContratoRepository contratoRepository;

	@Inject
	private EmpresaRepository empresaRepository;

	@Deployment
	public static Archive<?> createDeployment() {
		JavaArchive deploy = getEJBDeploy(DATABASE.POSTGRES_DESENV, false);
		// adicionar classes
		deploy.addPackages(true, Contrato.class.getPackage(), ContratoRepository.class.getPackage(), NFeService.class.getPackage(),
				XSDObject.class.getPackage());

		deploy.as(ZipExporter.class).exportTo(new File("/tmp/arquillian_test.jar"), true);
		return deploy;
	}

	@Test
	@InSequence(1)
	@Transactional
	public void salvarContratoTest() {
		Contrato c = new Contrato();
		c.setCnpj("00360305102888");
		contratoRepository.adicionar(c);
		contratoRepository.consultarPorChave(1L);
	}

	@Test
	@InSequence(2)
	@Transactional
	public void salvarEmpresaRIOPOLEM() throws IOException {
		Empresa empresa = new Empresa();
		empresa.setCnpj("07932968000103");
		empresa.setNome("RIOPOLEM");
		empresa.setUf(UFEnum.RJ);
		Contrato contrato = contratoRepository.consultarPorChave(1L);
		empresa.setContrato(contrato);

		File certificados = new File(getTestResourceFolder(), "certificados");

		File pfx = new File(certificados, "07932968000103.pfx");
		byte[] bytes = FileUtils.readFileToByteArray(pfx);

		Certificado cert = new Certificado();
		cert.setArquivo(bytes);
		cert.setSenha("RIOPOLEMLTDA");

		empresa.setCertificado(cert);

		empresaRepository.adicionar(empresa);
	}

	@Test
	@InSequence(3)
	@Transactional
	public void salvarEmpresaDDX() throws IOException {
		Empresa empresa = new Empresa();
		empresa.setCnpj("78570595000108");
		empresa.setNome("DDX");
		empresa.setUf(UFEnum.RJ);
		Contrato contrato = contratoRepository.consultarPorChave(1L);
		empresa.setContrato(contrato);

		File certificados = new File(getTestResourceFolder(), "certificados");

		File pfx = new File(certificados, "78570595000108.pfx");
		byte[] bytes = FileUtils.readFileToByteArray(pfx);

		Certificado cert = new Certificado();
		cert.setArquivo(bytes);
		cert.setSenha("ddx123");

		empresa.setCertificado(cert);

		empresaRepository.adicionar(empresa);
	}

}
