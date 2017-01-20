package com.dgreentec.infrastructure.crud;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.interceptor.Interceptors;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

import com.dgreentec.domain.model.Tenant;
import com.dgreentec.infrastructure.interceptor.EJBTransactionInterceptor;
import com.dgreentec.infrastructure.model.AbstractEntityVersion;
import com.dgreentec.infrastructure.model.EntityClassUtils;
import com.dgreentec.infrastructure.repository.multitenant.TenantInterceptor;

public abstract class AbstractCrudGen {

	protected static File outFolder = new File("/tmp/eclipse/generate");

	public static void generate(Class<? extends AbstractEntityVersion> clazz, String... filterFields) throws Exception {

		gerarFiltro(clazz, filterFields);
		gerarRepository(clazz);
		gerarServiceBoundary(clazz);
		gerarTest(clazz);
		gerarBuilderClass(clazz);
		System.out.println("Arquivos gerados na pasta " + outFolder.getAbsolutePath());
	}

	protected static void gerarBuilderClass(Class<? extends AbstractEntityVersion> clazz) throws Exception {
		File fileService = new File(outFolder, clazz.getSimpleName() + "_BuilderMethod.java");
		try (PrintWriter pw = new PrintWriter(fileService)) {
			pw.println("public static class Builder extends AbstractEntityBuilder<" + clazz.getSimpleName() + "> {");
			Field[] fields = EntityClassUtils.getAllFieldsIncludeDomainObjects(clazz, "version", "dataCriacao", "dataUltimaAlteracao");

			for (Field field : fields) {
				StringBuilder method = new StringBuilder("\tpublic Builder ");
				String capitalized = StringUtils.capitalize(field.getName());
				method.append("com").append(capitalized).append("(").append(field.getType().getSimpleName()).append(" p")
						.append(capitalized).append("){\n\t");
				// corpo
				method.append("this.entity.set").append(capitalized).append("(p").append(capitalized).append(");\n\t")
						.append("return this;\n\t}\n");

				pw.println(method.toString());
			}
			pw.println("}");
		}
	}

	protected static void gerarTest(Class<? extends AbstractEntityVersion> clazz) throws Exception {
		String cadastroClassTest = "Cadastro" + clazz.getSimpleName() + "IT";
		File fileService = new File(outFolder, cadastroClassTest + ".java");
		try (PrintWriter pw = new PrintWriter(fileService)) {
			pw.println("//Incluir nos parâmetros do teste (VM Arguments) -DWILDFLY_HOME=${wildfly_home} -DJAVA_HOME=${java_home}");

			pw.println("package com.dgreentec.test.integration;");
			pw.println("import java.io.File;");
			pw.println("import javax.inject.Inject;");
			pw.println("import org.jboss.arquillian.container.test.api.Deployment;");
			pw.println("import org.jboss.arquillian.junit.Arquillian;");
			pw.println("import org.jboss.arquillian.junit.InSequence;");
			pw.println("import org.jboss.arquillian.transaction.api.annotation.Transactional;");
			pw.println("import org.jboss.shrinkwrap.api.Archive;");
			pw.println("import org.jboss.shrinkwrap.api.exporter.ZipExporter;");
			pw.println("import org.jboss.shrinkwrap.api.spec.JavaArchive;");
			pw.println("import org.junit.Test;");
			pw.println("import org.junit.experimental.categories.Category;");
			pw.println("import org.junit.runner.RunWith;");
			pw.println("import com.dgreentec.IntegrationTest;");
			pw.println("import com.dgreentec.AbstractTestCase;");
			pw.println("import com.dgreentec.AbstractTestCase.DATABASE;");
			pw.println("//** TESTES **//");
			pw.println("import static org.junit.Assert.*;");
			pw.println("import static org.hamcrest.CoreMatchers.*;");
			pw.println("import static org.hamcrest.number.OrderingComparison.*;");
			pw.println("import com.dgreentec.infrastructure.persistence.pagination.PagedList;");

			String repositoryName = clazz.getSimpleName() + "Repository";
			String repository = StringUtils.uncapitalize(repositoryName);
			String serviceName = clazz.getSimpleName() + "Service";
			String service = StringUtils.uncapitalize(serviceName);

			// import artifacts
			pw.println("import " + clazz.getName() + ";");
			pw.println("import " + clazz.getName() + "_;");
			pw.println("import com.dgreentec.domain.repository." + clazz.getSimpleName() + "Repository;");
			pw.println("import com.dgreentec.domain.repository.filter.Filtro" + clazz.getSimpleName() + ";");
			pw.println("import com.dgreentec.infrastructure.repository." + clazz.getSimpleName() + "RepositoryJPABean;");
			pw.println("import com.dgreentec.domain.boundary.api." + serviceName + ";");
			pw.println("import com.dgreentec.domain.boundary.impl." + clazz.getSimpleName() + "Boundary;");

			pw.println("\n@RunWith(Arquillian.class)\n@Category(IntegrationTest.class)");
			pw.println("public class " + cadastroClassTest + " extends AbstractTestCase {\n");

			pw.println("@Inject");
			pw.println("private " + repositoryName + " " + repository + ";\n");
			pw.println("@Inject");
			pw.println("private " + serviceName + " " + service + ";\n");

			pw.println("@Deployment\npublic static Archive<?> createDeployment() {");
			pw.println("JavaArchive deploy = getEJBDeploy(DATABASE.POSTGRES_DESENV,false);");
			pw.println("// adicionar classes");
			pw.print("deploy.addClasses(");
			pw.print(clazz.getSimpleName() + ".class,");
			pw.print(clazz.getSimpleName() + "_.class,");
			pw.print(clazz.getSimpleName() + "Repository.class,");
			pw.print("Filtro" + clazz.getSimpleName() + ".class,");

			pw.print(serviceName + ".class,");
			pw.print(clazz.getSimpleName() + "Boundary.class,");

			pw.println(clazz.getSimpleName() + "RepositoryJPABean.class);\n");
			pw.println("deploy.as(ZipExporter.class).exportTo(new File(\"/tmp/arquillian_test.jar\"), true);");
			pw.println("return deploy;\n}\n");

			pw.println("@Test\n@InSequence(1)\n@Transactional\npublic void salvar" + clazz.getSimpleName() + "Test() {");
			pw.println("//FIXME Criar o objeto e invocar o repository para salvar. Exemplo: Builder.comA().comB()");
			pw.println(clazz.getSimpleName() + " " + clazz.getSimpleName().toLowerCase() + " = new " + clazz.getSimpleName()
					+ ".Builder().build(false);");
			pw.println(repository + ".adicionar(" + clazz.getSimpleName().toLowerCase() + ");\n}\n");

			pw.println("@Test\n@InSequence(2)\n@Transactional\npublic void alterar" + clazz.getSimpleName() + "Test() {");
			pw.println("//TODO consulta a entidade");
			pw.println(clazz.getSimpleName() + " entidade = " + repository + ".consultarPorChave(<CHAVE>);");
			pw.println("assertNotNull(entidade);");
			pw.println("//TODO altera algum valor\n");
			pw.println(repository + ".atualizar(entidade);");

			pw.println("//TODO consultar e entidade e associa a uma nova variável");
			pw.println("//TODO compara os valores antigos e os novos \n");
			pw.println("//TODO assertEquals(newValue, newObject.getValue());\n}\n");

			pw.println("@Test\n@InSequence(3)\n@Transactional\npublic void excluir" + clazz.getSimpleName() + "Test() {");
			pw.println("//TODO consultar a entidade");
			pw.println(repository + ".excluir(entidade);\n}\n");

			// incluir com service
			pw.println("@Test\n@InSequence(4)\npublic void salvar" + clazz.getSimpleName() + "ComServiceTest() {");
			pw.println("//TODO salvar a entidade por service");
			pw.println(service + ".adicionar...(entidade);\n}\n");

			// alterar com service
			pw.println("@Test\n@InSequence(5)\npublic void alterar" + clazz.getSimpleName() + "ComServiceTest() {");
			pw.println("//TODO consulta a entidade por service");
			pw.println(clazz.getSimpleName() + " entidade = " + service + ".consultar...(<CHAVE>);\nassertNotNull(entidade);");
			pw.println("//TODO alterar a entidade por service");
			pw.println(service + ".atualizar(entidade);\n}\n");

			// consultar com filtro
			pw.println("@Test\n@InSequence(6)\npublic void consultar" + clazz.getSimpleName() + "PorFiltroTest() {\n");
			pw.println("Filtro" + clazz.getSimpleName() + " filtro = new Filtro" + clazz.getSimpleName() + "();");
			pw.println("// retornar todos os registros respeitando o limite de paginação");
			pw.println(
					"PagedList<" + clazz.getSimpleName() + "> pagedList = " + service + ".consultar" + clazz.getSimpleName() + "(filtro);");
			pw.println("assertNotNull(pagedList);");
			pw.println("assertNotNull(pagedList.getResults());");
			pw.println("assertFalse(pagedList.isEmpty());");
			pw.println("assertThat(pagedList.getQtdResults(), is(<QUANTIDADE_ESPERADA>));");

			pw.println("\n}\n");

			pw.println("}");
		}

	}

	protected static void gerarServiceBoundary(Class<? extends AbstractEntityVersion> clazz) throws Exception {
		String serviceName = clazz.getSimpleName() + "Service";
		File fileService = new File(outFolder, serviceName + ".java");
		Field idField = getIdField(clazz);
		try (PrintWriter pw = new PrintWriter(fileService)) {
			pw.println("package com.dgreentec.domain.boundary.api;");
			pw.println("import javax.ejb.Local;");
			pw.println("import com.dgreentec.infrastructure.persistence.pagination.PagedList;");
			pw.println("import com.dgreentec.domain.model.Tenant;");
			pw.println("import " + clazz.getName() + ";");
			pw.println("import com.dgreentec.domain.repository.filter.Filtro" + clazz.getSimpleName() + ";");

			pw.println("@Local");
			pw.println("public interface " + serviceName + " {\n");

			pw.println("PagedList<" + clazz.getSimpleName() + "> consultar" + clazz.getSimpleName() + "s(Tenant tenant,Filtro"
					+ clazz.getSimpleName() + " filtro);");
			pw.println(clazz.getSimpleName() + " adicionar" + clazz.getSimpleName() + "(Tenant tenant," + clazz.getSimpleName() + " p"
					+ clazz.getSimpleName() + ");");
			pw.println(clazz.getSimpleName() + " atualizar" + clazz.getSimpleName() + "(Tenant tenant," + clazz.getSimpleName() + " p"
					+ clazz.getSimpleName() + ");");
			pw.println("void remover" + clazz.getSimpleName() + "(Tenant tenant," + clazz.getSimpleName() + " p" + clazz.getSimpleName()
					+ ");");

			pw.println(clazz.getSimpleName() + " consultar" + clazz.getSimpleName() + "Por" + StringUtils.capitalize(idField.getName())
					+ "(Tenant tenant," + idField.getType().getSimpleName() + " " + idField.getName() + ");");

			pw.println("\n}");
		}

		File impl = new File(outFolder, clazz.getSimpleName() + "Boundary.java");
		try (PrintWriter pw = new PrintWriter(impl)) {
			pw.println("package com.dgreentec.domain.boundary.impl;\n");

			pw.println("import javax.ejb.Stateless;");
			pw.println("import javax.ejb.TransactionAttribute;");
			pw.println("import javax.ejb.TransactionAttributeType;");
			pw.println("import javax.inject.Inject;\n");
			pw.println("import com.dgreentec.infrastructure.service.boundary.AbstractBoundary;");
			pw.println("import com.dgreentec.infrastructure.exception.BusinessException;");
			pw.println("import com.dgreentec.infrastructure.persistence.pagination.PagedList;\n");
			pw.println("import com.dgreentec.domain.model.Tenant;");
			pw.println("import com.dgreentec.domain.boundary.api." + serviceName + ";");
			pw.println("import " + clazz.getName() + ";");
			pw.println("import com.dgreentec.domain.repository." + clazz.getSimpleName() + "Repository;");
			pw.println("import com.dgreentec.domain.repository.filter.Filtro" + clazz.getSimpleName() + ";");
			pw.println("import javax.interceptor.Interceptors;");
			pw.println("import com.dgreentec.infrastructure.interceptor.EJBTransactionInterceptor;");
			pw.println("import com.dgreentec.infrastructure.repository.multitenant.TenantInterceptor;\n");

			pw.println("@Stateless");
			pw.println("@Interceptors({ TenantInterceptor.class, EJBTransactionInterceptor.class })");
			pw.println("public class " + clazz.getSimpleName() + "Boundary extends AbstractBoundary implements " + serviceName + " {\n");

			pw.println("@Inject");
			String repositoryName = clazz.getSimpleName() + "Repository";
			String repository = StringUtils.uncapitalize(repositoryName);
			pw.println("private " + repositoryName + " " + repository + ";\n");

			pw.println("public PagedList<" + clazz.getSimpleName() + "> consultar" + clazz.getSimpleName() + "s(Tenant tenant,,Filtro"
					+ clazz.getSimpleName() + " filtro){");
			pw.println("return " + repository + ".consultar(filtro);\n}\n");

			pw.println("public " + clazz.getSimpleName() + " adicionar" + clazz.getSimpleName() + "(Tenant tenant,"
					+ clazz.getSimpleName() + " p" + clazz.getSimpleName() + "){");
			pw.println("return " + repository + ".adicionar(p" + clazz.getSimpleName() + ");\n}\n");

			pw.println("public " + clazz.getSimpleName() + " atualizar" + clazz.getSimpleName() + "(Tenant tenant,"
					+ clazz.getSimpleName() + " p" + clazz.getSimpleName() + "){");
			pw.println("return " + repository + ".atualizar(p" + clazz.getSimpleName() + ");\n}\n");

			pw.println("public void remover" + clazz.getSimpleName() + "(Tenant tenant," + clazz.getSimpleName() + " p"
					+ clazz.getSimpleName() + "){");
			pw.println(repository + ".excluir(p" + clazz.getSimpleName() + ");\n}\n");

			pw.println("public " + clazz.getSimpleName() + " consultar" + clazz.getSimpleName() + "Por"
					+ StringUtils.capitalize(idField.getName()) + "(Tenant tenant," + idField.getType().getSimpleName() + " "
					+ idField.getName() + "){");
			pw.println("return " + repository + ".consultarPorChave(" + idField.getName() + ");\n}\n");

			pw.println("}");
		}
	}

	protected static void gerarRepository(Class<? extends AbstractEntityVersion> clazz) throws Exception {
		String repositoryName = clazz.getSimpleName() + "Repository";
		File fileRepository = new File(outFolder, repositoryName + ".java");
		try (PrintWriter pw = new PrintWriter(fileRepository)) {
			pw.println("package com.dgreentec.domain.repository;");

			pw.println("import com.dgreentec.infrastructure.persistence.ModelRepositoryJPA;");
			pw.println("import com.dgreentec.infrastructure.persistence.pagination.PagedList;");

			pw.println("import " + clazz.getName() + ";");
			pw.println("import com.dgreentec.domain.repository.filter.Filtro" + clazz.getSimpleName() + ";\n");

			pw.println("public interface " + repositoryName + " extends ModelRepositoryJPA<" + clazz.getSimpleName() + "> {\n");
			pw.println("PagedList<" + clazz.getSimpleName() + "> consultar(Filtro" + clazz.getSimpleName() + " filtro);");
			pw.println("\n}");
		}

		File repImpl = new File(outFolder, repositoryName + "JPABean.java");
		try (PrintWriter pw = new PrintWriter(repImpl)) {
			pw.println("package com.dgreentec.infrastructure.repository;");

			pw.println("import javax.enterprise.context.ApplicationScoped;");
			pw.println("import javax.persistence.TypedQuery;");
			pw.println("import javax.persistence.criteria.CriteriaBuilder;");
			pw.println("import javax.persistence.criteria.CriteriaQuery;");
			pw.println("import javax.persistence.criteria.JoinType;");
			pw.println("import javax.persistence.criteria.Root;");
			pw.println("import javax.persistence.criteria.SetJoin;\n");
			pw.println("import java.util.List;");
			pw.println("import com.dgreentec.infrastructure.persistence.GenericModelRepository;");
			pw.println("import com.dgreentec.infrastructure.persistence.pagination.PageEntityList;");
			pw.println("import com.dgreentec.infrastructure.persistence.pagination.PagedList;\n");

			pw.println("import " + clazz.getName() + ";");
			pw.println("import " + clazz.getName() + "_;\n");

			pw.println("import com.dgreentec.domain.repository." + repositoryName + ";");
			pw.println("import com.dgreentec.domain.repository.filter.Filtro" + clazz.getSimpleName() + ";\n");

			pw.println("@ApplicationScoped");
			pw.println("public class " + repositoryName + "JPABean extends GenericModelRepository<" + clazz.getSimpleName()
					+ "> implements " + repositoryName + " {\n");
			pw.println("private static final long serialVersionUID = " + new Random().nextLong() + "L;\n");

			pw.println("public PagedList<" + clazz.getSimpleName() + "> consultar(Filtro" + clazz.getSimpleName() + " filtro) {");
			pw.println("");
			pw.println("CriteriaBuilder cb = createCriteriaBuilder();");
			pw.println("CriteriaQuery<" + clazz.getSimpleName() + "> cq = cb.createQuery(" + clazz.getSimpleName() + ".class);");
			pw.println("Root<" + clazz.getSimpleName() + "> root = cq.from(" + clazz.getSimpleName() + ".class);");

			pw.println("cq.select(root);");

			pw.println("// order");
			pw.println("configureOrder(cb, cq, root, filtro);");

			pw.println("// filter");
			pw.println("filtro.configurarBusca(cb, cq, root);");

			pw.println("// pagination");
			pw.println("TypedQuery<" + clazz.getSimpleName() + "> tq = createTypedQuery(cq, filtro, true);\n");

			pw.println("// count criteria");
			pw.println("CriteriaBuilder cbc = createCriteriaBuilder();");
			pw.println("CriteriaQuery<Long> qc = cbc.createQuery(Long.class);");
			pw.println("Root<" + clazz.getSimpleName() + "> rootCount = qc.from(" + clazz.getSimpleName() + ".class);");
			pw.println("qc.select(cbc.count(rootCount));");
			pw.println("filtro.configurarBusca(cbc, qc, rootCount);\n");

			pw.println("List<" + clazz.getSimpleName() + "> resultList = tq.getResultList();");

			pw.println("Long count = processCountQuery(qc);");

			pw.println("\nreturn new PageEntityList<>(resultList, count);");
			pw.println("}\n}");

		}
	}

	protected static void gerarFiltro(Class<? extends AbstractEntityVersion> clazz, String... filterFields) throws FileNotFoundException {
		List<Field> fields = new ArrayList<>();
		for (String f : filterFields) {
			Field[] df = clazz.getDeclaredFields();
			for (Field field : df) {
				if (field.getName().equalsIgnoreCase(f)) {
					fields.add(field);
					break;
				}
			}
		}

		// criar o filtro
		String filterName = "Filtro" + clazz.getSimpleName();
		File fileFilter = new File(outFolder, filterName + ".java");
		try (PrintWriter pw = new PrintWriter(fileFilter)) {

			// package
			pw.println("package com.dgreentec.domain.repository.filter;\n");

			// imports
			pw.println("import java.util.List;");
			pw.println("import java.util.ArrayList;");
			pw.println("import javax.persistence.criteria.CriteriaBuilder;");
			pw.println("import javax.persistence.criteria.Join;");
			pw.println("import javax.persistence.criteria.Path;");
			pw.println("import javax.persistence.criteria.Predicate;");
			pw.println("import javax.persistence.criteria.Root;");
			pw.println("import javax.persistence.criteria.SetJoin;");
			pw.println("import com.dgreentec.infrastructure.persistence.filter.FiltroAbstrato;\n");

			pw.println("import " + clazz.getName() + ";");
			pw.println("import " + clazz.getName() + "_;");

			// class
			pw.println("@SuppressWarnings({ \"rawtypes\"})");
			pw.println("public class " + filterName + " extends FiltroAbstrato<" + clazz.getSimpleName() + "> {\n");

			// attributes
			pw.println("private static final long serialVersionUID = " + new Random().nextLong() + "L;");

			for (Field fd : fields) {
				pw.println("private " + fd.getType().getSimpleName() + " " + fd.getName() + ";");
				pw.println("private boolean " + fd.getName() + "Alterado;");
				// get
				pw.println("public " + fd.getType().getSimpleName() + " get" + StringUtils.capitalize(fd.getName()) + "(){");
				pw.println("return " + fd.getName() + ";\n}");
				// set
				pw.println("public void set" + StringUtils.capitalize(fd.getName()) + "(" + fd.getType().getSimpleName() + " "
						+ fd.getName() + "){");
				pw.println("updateProperty(" + clazz.getSimpleName() + "_." + fd.getName().toUpperCase() + "," + fd.getName() + ");\n}");

				pw.println("\npublic boolean is" + StringUtils.capitalize(fd.getName()) + "Alterado(){");
				pw.println("return " + fd.getName() + "Alterado;\n}");
			}

			pw.println("\npublic boolean isFiltroAlterado() {");
			pw.print("return ");
			String sep = "";
			for (Field fd : fields) {
				pw.print(sep + "is" + StringUtils.capitalize(fd.getName()) + "Alterado()");
				sep = " || ";
			}
			pw.println(";\n}\n");

			pw.println("protected void limpar() {");
			for (Field fd : fields) {
				pw.println("set" + StringUtils.capitalize(fd.getName()) + "(null);");
			}
			pw.println("}\n");

			// jpa methods
			// path order
			pw.println("\npublic Path<?> findPath(String field, Root<" + clazz.getSimpleName() + "> root, Join... collection) {");
			Field id = getIdField(clazz);
			pw.println("Path<?> path = root.get(" + clazz.getSimpleName() + "_." + id.getName() + ");");
			pw.println("if (field != null) {");
			pw.println("switch (field) {");
			Field[] df = clazz.getDeclaredFields();
			for (Field field : df) {
				if (field.getName().equalsIgnoreCase("serialVersionUID"))
					continue;
				pw.println("case " + clazz.getSimpleName() + "_." + field.getName().toUpperCase() + ":");
				pw.println("path = root.get(" + clazz.getSimpleName() + "_." + field.getName() + ");");
				pw.println("break;");
			}
			pw.println("}\n}\nreturn path;\n}");

			// predicate
			pw.println("protected Predicate configureWhereClause(CriteriaBuilder criteriaBuilder, Root<" + clazz.getSimpleName()
					+ "> root, Join... joins) {");
			//			pw.println("Predicate where = criteriaBuilder.conjunction();");
			pw.println("List<Predicate> ands = new ArrayList<>();");
			pw.println("// List<Predicate> ors = new ArrayList<>();");
			Set<String> setFields = new TreeSet<>(Arrays.asList(filterFields));
			for (Field fd : df) {
				if (fd.getName().equalsIgnoreCase("serialVersionUID") || !setFields.contains(fd.getName()))
					continue;
				pw.println("if (is" + StringUtils.capitalize(fd.getName()) + "Alterado()){");
				pw.println("/*exemplo:\nEquals\n" + "ands.add(criteriaBuilder.equal(root.get(" + clazz.getSimpleName() + "_." + fd.getName()
						+ "), get" + StringUtils.capitalize(fd.getName()) + "()));");
				pw.println("Like\nands.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(" + clazz.getSimpleName() + "_."
						+ fd.getName() + ")), \"%\" + get" + StringUtils.capitalize(fd.getName()) + "().toLowerCase() + \"%\"));\n*/");
				pw.println("<< AJUSTAR CONDIÇÃO >>\n}");
			}
			pw.println("return criteriaBuilder.and(ands.toArray(new Predicate[ands.size()]));\n}");

			// final do arquivo
			pw.println("\n}");
		}
	}

	protected static Field getIdField(Class<? extends AbstractEntityVersion> clazz) {
		Field[] fds = clazz.getDeclaredFields();
		for (Field field : fds) {
			if (field.isAnnotationPresent(Id.class)) {
				return field;
			}
		}
		return null;
	}
}
