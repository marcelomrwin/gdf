package com.dgreentec;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dgreentec.infrastructure.configuration.ConfigurationManager;
import com.dgreentec.infrastructure.exception.InfraException;
import com.dgreentec.infrastructure.interceptor.TransactionMultitenancyInterceptor;
import com.dgreentec.infrastructure.model.AbstractEntityVersion;
import com.dgreentec.infrastructure.persistence.RepositoryJPA;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.infrastructure.repository.MultiTenantProvider;
import com.dgreentec.infrastructure.repository.SchemaResolver;
import com.dgreentec.infrastructure.service.boundary.AbstractBoundary;
import com.dgreentec.infrastructure.ssl.ThreadLocalCert;
import com.dgreentec.infrastructure.util.JaxbUtils;

import br.inf.portalfiscal.www.nfe.wsdl.nfedistribuicaodfe.NFeDistribuicaoDFeStub;
import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico2.NfeStatusServico2Stub;
import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.RecepcaoEventoStub;

public abstract class AbstractTestCase {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Rule
	public TestCasePrinterRule pr = new TestCasePrinterRule(System.out);

	public enum DATABASE {
		POSTGRES_DESENV("test-persistence.xml"), POSTGRES_DESENV_NO_DROP("test-persistence-no-drop.xml");

		protected final String persistence;

		DATABASE(String pPersistence) {
			this.persistence = pPersistence;
		}

		public String getPersistence() {
			return persistence;
		}
	}

	protected static JavaArchive getEJBDeploy(DATABASE pDataBase, boolean withTransitivity, String... deps) {

		JavaArchive testArchive = ShrinkWrap.create(JavaArchive.class);

		File[] libs = getDependencies(withTransitivity, deps);
		if (libs != null && libs.length > 0)
			for (File file : libs) {
				JavaArchive archive = ShrinkWrap.createFromZipFile(JavaArchive.class, file);
				testArchive.merge(archive);
			}

		testArchive
				.addPackages(true, AbstractTestCase.class.getPackage(), ConfigurationManager.class.getPackage(),
						InfraException.class.getPackage(), TransactionMultitenancyInterceptor.class.getPackage(),
						MultiTenantProvider.class.getPackage(), AbstractEntityVersion.class.getPackage(), RepositoryJPA.class.getPackage(),
						SchemaResolver.class.getPackage(), ThreadLocalCert.class.getPackage(), JaxbUtils.class.getPackage(),
						NFeDistribuicaoDFeStub.class.getPackage(), NfeStatusServico2Stub.class.getPackage(),
						RecepcaoEventoStub.class.getPackage(), AbstractBoundary.class.getPackage())
				.addAsResource("META-INF/" + pDataBase.getPersistence(), "META-INF/persistence.xml").addAsResource("META-INF/beans.xml")
				.addAsResource("config").addAsResource("keystore").addAsResource("META-INF/orm.xml").addAsResource("META-INF/ejb-jar.xml");// .addAsResource("META-INF/TEST-MANIFEST.MF","META-INF/MANIFEST.MF");

		return testArchive;
	}

	protected static WebArchive getWebDeploy(DATABASE pDataBase, boolean withTransitivity, String... additionalDeps) {
		Collection<String> deps = getCommonsLibs(additionalDeps);

		File[] libs = resolveDependencies(deps, withTransitivity);

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "ArquillianWebProject.war")
				.addPackages(true, AbstractTestCase.class.getPackage(), ConfigurationManager.class.getPackage(),
						InfraException.class.getPackage(), TransactionMultitenancyInterceptor.class.getPackage(),
						MultiTenantProvider.class.getPackage(), AbstractEntityVersion.class.getPackage(), RepositoryJPA.class.getPackage(),
						SchemaResolver.class.getPackage(), ThreadLocalCert.class.getPackage(), JaxbUtils.class.getPackage(),
						NFeDistribuicaoDFeStub.class.getPackage(), NfeStatusServico2Stub.class.getPackage(),
						RecepcaoEventoStub.class.getPackage(), AbstractBoundary.class.getPackage())
				.addAsWebInfResource("test-faces-config.xml", "faces-config.xml").addAsWebInfResource("jboss-deployment-structure.xml")
				.addAsResource("META-INF/" + pDataBase.getPersistence(), "META-INF/persistence.xml").addAsResource("config")
				.addAsResource("keystore").addAsResource("META-INF/orm.xml", "META-INF/orm.xml").addAsWebInfResource("META-INF/ejb-jar.xml")
				.addAsWebInfResource("META-INF/beans.xml", "beans.xml").addAsLibraries(libs);

		return archive;

	}

	private static File[] getDependencies(boolean withTransitivity, String... additionalDeps) {
		Collection<String> deps = getCommonsLibs();

		for (String dep : additionalDeps) {
			deps.add(dep);
		}
		if (deps != null && !deps.isEmpty()) {
			File[] libs = resolveDependencies(deps, withTransitivity);
			return libs;
		}
		return null;
	}

	protected static File[] resolveDependencies(Collection<String> deps, boolean withTransitivity) {
		if (withTransitivity) {
			File[] files = Maven.resolver().loadPomFromFile("pom.xml").resolve(deps).withTransitivity().asFile();
			return files;
		} else {

			File[] files = Maven.resolver().loadPomFromFile("pom.xml").resolve(deps).withTransitivity().asFile();

			try {
				Logger log = LoggerFactory.getLogger(AbstractTestCase.class);
				log.warn("==================== RESOLVE DEPENDENCIES BEGIN ======================");
				File moduleFolder = new File("/tmp/qg/arquillian/main");
				if (!moduleFolder.exists())
					moduleFolder.mkdirs();
				else {
					File[] fls = moduleFolder.listFiles();
					for (File file : fls) {
						FileUtils.forceDelete(file);
					}
				}

				File module = new File(moduleFolder, "module.xml");
				PrintWriter pw = new PrintWriter(new FileWriter(module));
				pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				pw.println("<module xmlns=\"urn:jboss:module:1.1\" name=\"qg.arquillian\">");
				pw.println("<resources>");
				for (File file : files) {
					log.warn(file.getAbsolutePath());

					File nFile = new File(moduleFolder, file.getName());
					FileUtils.copyFile(file, nFile);
					pw.println("<resource-root path=\"" + nFile.getName() + "\"/>");

				}
				pw.println("</resources>");
				pw.println("<dependencies>");
				pw.println("<module name=\"javax.api\"/>");
				pw.println("<module name=\"javax.servlet.api\" optional=\"true\"/>");
				pw.println("<module name=\"org.apache.commons.logging\"/>");
				pw.println("<module name=\"org.apache.commons.collections\"/>");
				pw.println("<module name=\"org.apache.commons.lang\"/>");
				pw.println("<module name=\"org.apache.commons.beanutils\"/>");
				pw.println("<module name=\"javax.el.api\"/>");
				pw.println("<module name=\"org.antlr\"/>");
				pw.println("<module name=\"org.slf4j\"/>");

				pw.println("<module name=\"org.apache.httpcomponents\"/>");
				pw.println("<module name=\"org.apache.commons.codec\"/>");
				pw.println("<module name=\"org.apache.commons.io\"/>");

				pw.println("</dependencies>");
				pw.println("</module>");
				pw.flush();
				pw.close();

				log.warn("==================== RESOLVE DEPENDENCIES END ======================");

			} catch (Exception e) {
				e.printStackTrace();
			}

			return Maven.resolver().loadPomFromFile("pom.xml").resolve(deps).withoutTransitivity().asFile();

		}
	}

	protected static Collection<String> getCommonsLibs(String... addDeps) {
		Collection<String> deps = new ArrayList<>();
		// deps.add("G:A:V");

		deps.add("org.apache.axis2:axis2-kernel:1.7.4");
		deps.add("org.apache.axis2:axis2-transport-http:1.7.4");
		deps.add("org.apache.axis2:axis2-transport-local:1.7.4");
		deps.add("org.apache.axis2:axis2-adb:1.7.4");

//		deps.add("org.apache.ws.commons.axiom:axiom-api:1.2.20");
//		deps.add("org.apache.ws.commons.axiom:axiom-impl:1.2.20");
//		deps.add("commons-httpclient:commons-httpclient:3.1");
//		deps.add("org.apache.woden:woden-core:1.0M10");
//		deps.add("commons-fileupload:commons-fileupload:1.3.1");
//		deps.add("wsdl4j:wsdl4j:1.6.3");
//		deps.add("org.apache.geronimo.specs:geronimo-stax-api_1.0_spec:1.0.1");
//		deps.add("org.codehaus.woodstox:stax2-api:4.0.0");
//		deps.add("org.codehaus.woodstox:woodstox-core-asl:4.2.0");

		for (String dp : addDeps) {
			deps.add(dp);
		}

		return deps;
	}

	protected static Collection<String> getCommonsLibsEJB(String... addDeps) {
		Collection<String> deps = new ArrayList<>();
		// deps.add("G:A:V");

		deps.add("org.apache.axis2:axis2-kernel:1.7.4");
		deps.add("org.apache.axis2:axis2-transport-http:1.7.4");
		deps.add("org.apache.axis2:axis2-transport-local:1.7.4");
		deps.add("org.apache.axis2:axis2-adb:1.7.4");

//		deps.add("org.apache.ws.commons.axiom:axiom-api:1.2.20");
//		deps.add("org.apache.ws.commons.axiom:axiom-impl:1.2.20");
//		deps.add("commons-httpclient:commons-httpclient:3.1");
//		deps.add("org.apache.woden:woden-core:1.0M10");
//		deps.add("commons-fileupload:commons-fileupload:1.3.1");
//		deps.add("wsdl4j:wsdl4j:1.6.3");
//		deps.add("org.apache.geronimo.specs:geronimo-stax-api_1.0_spec:1.0.1");
//		deps.add("org.codehaus.woodstox:stax2-api:4.0.0");
//		deps.add("org.codehaus.woodstox:woodstox-core-asl:4.2.0");

		for (String dp : addDeps) {
			deps.add(dp);
		}

		return deps;
	}

	protected void validarPageListComTamanho(PagedList<?> pagedList, Long qtd) {
		assertThat(pagedList.getResults(), is(not(nullValue())));
		assertThat(pagedList.isEmpty(), is(false));
		assertThat(pagedList.getQtdResults(), is(qtd));
		assertThat(pagedList.getCurrentSize(), is(qtd.intValue()));
	}

	protected void validarPagedListVazia(PagedList<?> pagedList) {
		assertThat(pagedList.getResults(), is(not(nullValue())));
		assertThat(pagedList.isEmpty(), is(true));
		assertThat(pagedList.getQtdResults(), is(0L));
	}

	protected File getResourceFolder() {
		String basePath = System.getProperty("user.dir");
		String resourcePath = basePath + File.separator + "src" + File.separator + "main" + File.separator + "resources";
		return new File(resourcePath);
	}

	protected File getTestResourceFolder() {
		String basePath = System.getProperty("user.dir");
		String resourcePath = basePath + File.separator + "src" + File.separator + "test" + File.separator + "resources";
		return new File(resourcePath);
	}

	protected File getWebPath() {
		return new File(new File(new File(System.getProperty("user.dir")).getParent(), "gdf-web").getAbsolutePath() + File.separator + "src"
				+ File.separator + "main" + File.separator + "webapp");
	}

	protected File getWebResourcesPath() {
		return new File(getWebPath(), "resources");
	}

	protected File getWebResources() {
		return new File(new File(new File(System.getProperty("user.dir")).getParent(), "gdf-web").getAbsolutePath() + File.separator + "src"
				+ File.separator + "main" + File.separator + "resources");
	}

	protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS");

	protected void log(String id, String pText) {
		System.out.println(sdf.format(new Date()) + " - [" + id + "] " + Thread.currentThread().getName() + " | " + pText);
	}

}
