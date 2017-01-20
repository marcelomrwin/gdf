//Incluir nos parâmetros do teste (VM Arguments) -DWILDFLY_HOME=${wildfly_home} -DJAVA_HOME=${java_home}
package com.dgreentec.test.integration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Arrays;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Ignore;
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
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.model.UFEnum;
import com.dgreentec.domain.repository.ContratoRepository;
import com.dgreentec.domain.repository.EmpresaRepository;
import com.dgreentec.domain.repository.filter.FiltroContrato;
import com.dgreentec.domain.xsd.XSDObject;
import com.dgreentec.infrastructure.repository.ContratoRepositoryJPABean;
import com.dgreentec.domain.boundary.api.ContratoService;
import com.dgreentec.domain.boundary.api.EmpresaService;
import com.dgreentec.domain.boundary.api.NFeService;
import com.dgreentec.domain.boundary.api.TenantService;
import com.dgreentec.domain.boundary.impl.ContratoBoundary;
import com.dgreentec.domain.boundary.impl.NFeServiceBean;

@RunWith(Arquillian.class)
@Category(IntegrationTest.class)
public class CadastroContratoIT extends AbstractTestCase {

	@Inject
	private EmpresaService empresaService;

	@Inject
	private ContratoService contratoService;

	@Inject
	private TenantService tenantService;

	@Resource(lookup = "java:jboss/datasources/testGDFPostgreDS")
	private DataSource dataSource;

	private File certificados = new File(getTestResourceFolder(), "certificados");

	@Deployment
	public static Archive<?> createDeployment() {
		JavaArchive deploy = getEJBDeploy(DATABASE.POSTGRES_DESENV, true);

		deploy.addPackages(true, Contrato.class.getPackage(), ContratoRepository.class.getPackage(), NFeService.class.getPackage(),
				NFeServiceBean.class.getPackage(), XSDObject.class.getPackage());

		deploy.as(ZipExporter.class).exportTo(new File("/tmp/arquillian_test.jar"), true);
		return deploy;
	}

	private Empresa criarEmpresa(String senha, String cnpj, Contrato contrato, Long idTenant, String nome, UFEnum uf) throws IOException {
		Tenant tenant = tenantService.consultarTenantPorIdTenant(idTenant);
		Empresa empresa = new Empresa.Builder().comCnpj(cnpj).comNome(nome).comUf(uf)
				.comContrato(contrato).comCertificado(new Certificado.Builder()
						.comArquivo(FileUtils.readFileToByteArray(new File(certificados, cnpj + ".pfx"))).comSenha(senha).build(false))
				.build(false);
		return empresa;
	}

	@Test
	@InSequence(1)
	public void salvarContratoTest() {
		Tenant tenant = tenantService.consultarTenantPorIdTenant(1L);
		Contrato contratoRioPolem = new Contrato.Builder().comCnpj("07932968000103").comTenant(tenant)
				.comValidade(LocalDateTime.now().plusYears(1)).build(false);
		contratoService.adicionarContrato(tenant, contratoRioPolem);

		Tenant tn2 = tenantService.consultarTenantPorIdTenant(2L);
		Contrato contratoDDX = new Contrato.Builder().comCnpj("78570595000108").comTenant(tn2).comValidade(LocalDateTime.now().plusYears(1))
				.build(false);
		contratoService.adicionarContrato(tn2, contratoDDX);
	}

	@Test
	@InSequence(2)
	public void salvarEmpresaRIOPOLEM() throws IOException {
		Tenant tenant = tenantService.consultarTenantPorIdTenant(1L);
		String senha = "RIOPOLEMLTDA";
		String cnpj = "07932968000103";
		String nome = "RIOPOLEM";
		UFEnum uf = UFEnum.RJ;

		Contrato contrato = contratoService.consultarContratoPorIdContrato(tenant, 1L);

		Empresa empresa = criarEmpresa(senha, cnpj, contrato, 1L, nome, uf);
		System.out.println("SALVANDO EMPRESA RIOPOLEM");
		empresaService.adicionarEmpresa(tenant, empresa);
	}

	@Test
	@InSequence(3)
	public void salvarEmpresaDDX() throws IOException {
		Tenant tenant = tenantService.consultarTenantPorIdTenant(2L);
		String senha = "ddx123";
		String cnpj = "78570595000108";
		String nome = "DDX";
		UFEnum uf = UFEnum.RJ;
		Contrato contrato = contratoService.consultarContratoPorIdContrato(tenant, 1L);
		Empresa empresa = criarEmpresa(senha, cnpj, contrato, 2L, nome, uf);
		System.out.println("SALVANDO EMPRESA DDX");
		empresaService.adicionarEmpresa(tenant, empresa);

	}

	@Test
	@InSequence(4)
	public void alterarContratoComServiceTest() {
		Tenant tenant = tenantService.consultarTenantPorIdTenant(1L);
		Contrato entidade = contratoService.consultarContratoPorIdContrato(tenant, 1L);
		assertNotNull(entidade);
		assertThat(entidade.getEmpresas(), is(not(Matchers.empty())));
		contratoService.atualizarContrato(tenant, entidade);
	}

	@Test
	@InSequence(6)
	public void consultarContratoPorFiltroTest() {
		Tenant tenant = tenantService.consultarTenantPorIdTenant(1L);
		FiltroContrato filtro = new FiltroContrato();
		// retornar todos os registros respeitando o limite de paginação
		PagedList<Contrato> pagedList = contratoService.consultarContratos(tenant, filtro);
		assertNotNull(pagedList);
		assertNotNull(pagedList.getResults());
		assertFalse(pagedList.isEmpty());
		assertThat(pagedList.getQtdResults(), is(1L));

	}

}
