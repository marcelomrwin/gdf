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
import com.dgreentec.AbstractTestCase.DATABASE;
import com.dgreentec.IntegrationTest;
//** TESTES **//
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.domain.model.Usuario;
import com.dgreentec.domain.model.Usuario_;
import com.dgreentec.domain.repository.UsuarioRepository;
import com.dgreentec.domain.repository.filter.FiltroUsuario;
import com.dgreentec.infrastructure.repository.UsuarioRepositoryJPABean;
import com.dgreentec.domain.boundary.api.UsuarioService;
import com.dgreentec.domain.boundary.impl.UsuarioBoundary;

@RunWith(Arquillian.class)
@Category(IntegrationTest.class)
public class CadastroUsuarioIT extends AbstractTestCase {

	@Inject
	private UsuarioService usuarioService;

	@Deployment
	public static Archive<?> createDeployment() {
		JavaArchive deploy = getEJBDeploy(DATABASE.POSTGRES_DESENV, false);

		deploy.as(ZipExporter.class).exportTo(new File("/tmp/arquillian_test.jar"), true);
		return deploy;
	}

	@Test
	@InSequence(1)
	public void salvarUsuarioComServiceTest() {
		Usuario entidade = new Usuario.Builder().comCpf("03210392463").comNome("Marcelo Sales").build(false);
		usuarioService.adicionarUsuario(entidade);
	}

	@Test
	@InSequence(2)
	public void alterarUsuarioComServiceTest() {

		Usuario entidade = usuarioService.consultarUsuarioPorCpf("03210392463");
		assertNotNull(entidade);
		entidade.setNome("Marcelo Daniel da Silva Sales");
		usuarioService.atualizarUsuario(entidade);
	}

	@Test
	@InSequence(3)
	public void consultarUsuarioPorFiltroTest() {

		FiltroUsuario filtro = new FiltroUsuario();
		// retornar todos os registros respeitando o limite de paginação
		PagedList<Usuario> pagedList = usuarioService.consultarUsuarios(filtro);
		assertNotNull(pagedList);
		assertNotNull(pagedList.getResults());
		assertFalse(pagedList.isEmpty());
		assertThat(pagedList.getQtdResults(), is(1L));

	}

}
