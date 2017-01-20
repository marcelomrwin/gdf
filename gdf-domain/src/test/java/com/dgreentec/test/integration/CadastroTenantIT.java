//Incluir nos parâmetros do teste (VM Arguments) -DWILDFLY_HOME=${wildfly_home} -DJAVA_HOME=${java_home}
package com.dgreentec.test.integration;

import java.io.File;
import javax.inject.Inject;
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
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.model.Tenant_;
import com.dgreentec.domain.repository.TenantRepository;
import com.dgreentec.domain.repository.filter.FiltroTenant;
import com.dgreentec.infrastructure.repository.TenantRepositoryJPABean;
import com.dgreentec.domain.boundary.api.TenantService;
import com.dgreentec.domain.boundary.impl.TenantBoundary;

@RunWith(Arquillian.class)
@Category(IntegrationTest.class)
public class CadastroTenantIT extends AbstractTestCase {

	@Inject
	private TenantService tenantService;

	@Deployment
	public static Archive<?> createDeployment() {
		JavaArchive deploy = getEJBDeploy(DATABASE.POSTGRES_DESENV, false);
		deploy.as(ZipExporter.class).exportTo(new File("/tmp/arquillian_test.jar"), true);
		return deploy;
	}

	@Test
	@InSequence(4)
	public void salvarTenantComServiceTest() {
		Tenant t = new Tenant.Builder().comNomeTenant("TENANT RIOPOLEM").comSchemaName("scm_riopolem").build(false);
		tenantService.adicionarTenant(t);
	}

	@Test
	@InSequence(5)
	public void alterarTenantComServiceTest() {

		Tenant entidade = tenantService.consultarTenantPorIdTenant(1L);
		assertNotNull(entidade);
		entidade.setNomeTenant("TENANT RIO POLEM");

		tenantService.atualizarTenant(entidade);
	}

	@Test
	@InSequence(6)
	public void consultarTenantPorFiltroTest() {

		FiltroTenant filtro = new FiltroTenant();
		// retornar todos os registros respeitando o limite de paginação
		PagedList<Tenant> pagedList = tenantService.consultarTenants(filtro);
		assertNotNull(pagedList);
		assertNotNull(pagedList.getResults());
		assertFalse(pagedList.isEmpty());
		assertThat(pagedList.getQtdResults(), is(1L));

	}

}
