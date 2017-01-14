//Incluir nos parâmetros do teste (VM Arguments) -DWILDFLY_HOME=${wildfly_home} -DJAVA_HOME=${java_home}
package com.dgreentec.test.integration;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
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
//** TESTES **//
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.domain.model.Certificado;
import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.Contrato_;
import com.dgreentec.domain.model.Empresa;
import com.dgreentec.domain.model.UFEnum;
import com.dgreentec.domain.repository.ContratoRepository;
import com.dgreentec.domain.repository.EmpresaRepository;
import com.dgreentec.domain.repository.filter.FiltroContrato;
import com.dgreentec.domain.xsd.XSDObject;
import com.dgreentec.infrastructure.repository.ContratoRepositoryJPABean;
import com.dgreentec.domain.boundary.api.ContratoService;
import com.dgreentec.domain.boundary.api.NFeService;
import com.dgreentec.domain.boundary.impl.ContratoBoundary;
import com.dgreentec.domain.boundary.impl.NFeServiceBean;

@RunWith(Arquillian.class)
@Category(IntegrationTest.class)
public class CadastroContratoIT extends AbstractTestCase {

	@Inject
	private ContratoRepository contratoRepository;

	@Inject
	private EmpresaRepository empresaRepository;

	@Inject
	private ContratoService contratoService;

	private File certificados = new File(getTestResourceFolder(), "certificados");

	@Deployment
	public static Archive<?> createDeployment() {
		JavaArchive deploy = getEJBDeploy(DATABASE.POSTGRES_DESENV, true);

		deploy.addPackages(true, Contrato.class.getPackage(), ContratoRepository.class.getPackage(), NFeService.class.getPackage(),
				NFeServiceBean.class.getPackage(), XSDObject.class.getPackage());

		deploy.as(ZipExporter.class).exportTo(new File("/tmp/arquillian_test.jar"), true);
		return deploy;
	}

	@Test
	@InSequence(1)
	@Transactional
	public void salvarContratoTest() {
		Contrato contrato = new Contrato.Builder().comCnpj("00360305102888").build(false);
		contratoRepository.adicionar(contrato);
	}

	@Test
	@InSequence(2)
	@Transactional
	public void salvarEmpresaRIOPOLEM() throws IOException {
		String senha = "RIOPOLEMLTDA";
		String cnpj = "07932968000103";
		String nome = "RIOPOLEM";
		UFEnum uf = UFEnum.RJ;
		Empresa empresa = criarEmpresa(senha, cnpj, nome, uf);

		empresaRepository.adicionar(empresa);
	}

	private Empresa criarEmpresa(String senha, String cnpj, String nome, UFEnum uf) throws IOException {
		Empresa empresa = new Empresa.Builder().comCnpj(cnpj).comNome(nome).comUf(uf)
				.comContrato(contratoRepository.consultarPorChave(1L)).comCertificado(new Certificado.Builder()
						.comArquivo(FileUtils.readFileToByteArray(new File(certificados, cnpj + ".pfx"))).comSenha(senha).build(false))
				.build(false);
		return empresa;
	}

	@Test
	@InSequence(3)
	@Transactional
	public void salvarEmpresaDDX() throws IOException {

		String senha = "ddx123";
		String cnpj = "78570595000108";
		String nome = "DDX";
		UFEnum uf = UFEnum.RJ;
		Empresa empresa = criarEmpresa(senha, cnpj, nome, uf);

		empresaRepository.adicionar(empresa);

	}

	@Test
	@InSequence(4)
	public void alterarContratoComServiceTest() {

		Contrato entidade = contratoService.consultarContratoPorIdContrato(1L);
		assertNotNull(entidade);
		assertThat(entidade.getEmpresas(), is(not(Matchers.empty())));
		contratoService.atualizarContrato(entidade);
	}

	@Test
	@InSequence(6)
	public void consultarContratoPorFiltroTest() {

		FiltroContrato filtro = new FiltroContrato();
// retornar todos os registros respeitando o limite de paginação
		PagedList<Contrato> pagedList = contratoService.consultarContratos(filtro);
		assertNotNull(pagedList);
		assertNotNull(pagedList.getResults());
		assertFalse(pagedList.isEmpty());
		assertThat(pagedList.getQtdResults(), is(1L));

	}

}
