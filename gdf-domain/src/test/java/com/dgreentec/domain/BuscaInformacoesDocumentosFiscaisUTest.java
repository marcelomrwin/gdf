package com.dgreentec.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.Unmarshaller;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dgreentec.domain.model.TipoAmbienteEnum;
import com.dgreentec.domain.model.TipoServicoEnum;
import com.dgreentec.domain.model.UFEnum;
import com.dgreentec.domain.xsd.distDFeInt_v101.DistDFeInt;
import com.dgreentec.domain.xsd.distDFeInt_v101.ObjectFactory;
import com.dgreentec.domain.xsd.distDFeInt_v101.DistDFeInt.DistNSU;
import com.dgreentec.domain.xsd.procEventoNFe_v100.ProcEventoNFe;
import com.dgreentec.domain.xsd.procNFe_v310.NfeProc;
import com.dgreentec.domain.xsd.resEvento_v101.ResEvento;
import com.dgreentec.domain.xsd.resNFe_v101.ResNFe;
import com.dgreentec.domain.xsd.retDistDFeInt_v101.RetDistDFeInt;
import com.dgreentec.domain.xsd.retDistDFeInt_v101.RetDistDFeInt.LoteDistDFeInt;
import com.dgreentec.domain.xsd.retDistDFeInt_v101.RetDistDFeInt.LoteDistDFeInt.DocZip;
import com.dgreentec.infrastructure.configuration.nfe.WebServices;
import com.dgreentec.infrastructure.model.SchemaEnum;
import com.dgreentec.infrastructure.ssl.SocketFactoryDinamico;
import com.dgreentec.infrastructure.ssl.ThreadLocalProperties;
import com.dgreentec.infrastructure.util.GzipUtils;
import com.dgreentec.infrastructure.util.JaxbUtils;

import br.inf.portalfiscal.www.nfe.wsdl.nfedistribuicaodfe.NFeDistribuicaoDFeStub;
import br.inf.portalfiscal.www.nfe.wsdl.nfedistribuicaodfe.NFeDistribuicaoDFeStub.NfeDadosMsg_type0;
import br.inf.portalfiscal.www.nfe.wsdl.nfedistribuicaodfe.NFeDistribuicaoDFeStub.NfeDistDFeInteresse;
import br.inf.portalfiscal.www.nfe.wsdl.nfedistribuicaodfe.NFeDistribuicaoDFeStub.NfeDistDFeInteresseResponse;
import br.inf.portalfiscal.www.nfe.wsdl.nfedistribuicaodfe.NFeDistribuicaoDFeStub.NfeDistDFeInteresseResult_type0;

public class BuscaInformacoesDocumentosFiscaisUTest {

	File outputDir;

	String certsPath = System.getProperty("user.dir") + "/src/test/resources/certificados";

	@Before
	public void configCertificado() throws IOException {

		outputDir = new File(System.getProperty("user.dir") + File.separator + "target" + File.separator + "procnfe");
		FileUtils.deleteDirectory(outputDir);
		outputDir.mkdirs();

	}

	@SuppressWarnings("restriction")
	@BeforeClass
	public static void config() {

		/**
		 * Informações do Certificado Digital.
		 */
		System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");

		System.clearProperty("javax.net.ssl.keyStore");
		System.clearProperty("javax.net.ssl.keyStorePassword");
		System.clearProperty("javax.net.ssl.trustStore");
		System.clearProperty("javax.net.ssl.trustStorePassword");

		System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		System.setProperty("javax.net.ssl.trustStore", System.getProperty("user.dir") + "/src/test/resources/certificados" + File.separator
				+ "keystore" + File.separator + "NFeCacerts");

	}

	public class ThreadExecutor implements Runnable {

		private final TipoAmbienteEnum ambiente;

		private final UFEnum ufAtor;

		private final String cnpj;

		private final String ultNSU;

		private final String keyStorePass;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS");

		public ThreadExecutor(TipoAmbienteEnum ambiente, UFEnum pUf, String cnpj, String pUltNSU, String pKeyPass) {
			this.ambiente = ambiente;
			this.ufAtor = pUf;
			this.cnpj = cnpj;
			this.ultNSU = pUltNSU;
			this.keyStorePass = pKeyPass;
		}

		@Override
		public void run() {
			try {

				DistDFeInt dist = new ObjectFactory().createDistDFeInt();
				dist.setVersao("1.00");
				dist.setTpAmb(ambiente.getTpAmb());
				dist.setCUFAutor(ufAtor.getCodigo());
				dist.setCNPJ(cnpj);

				DistNSU distNSU = new ObjectFactory().createDistDFeIntDistNSU();
				String nsu = StringUtils.leftPad(ultNSU, 15, "0");
				distNSU.setUltNSU(nsu);
				dist.setDistNSU(distNSU);

				OMElement ome = AXIOMUtil.stringToOM(JaxbUtils.convertEntityToXML(dist, false));

				NfeDadosMsg_type0 dadosMsg = new NfeDadosMsg_type0();
				dadosMsg.setExtraElement(ome);

				NfeDistDFeInteresse distDFeInteresse = new NfeDistDFeInteresse();
				distDFeInteresse.setNfeDadosMsg(dadosMsg);

				com.dgreentec.domain.xsd.retDistDFeInt_v101.RetDistDFeInt retorno = callService(dist, distDFeInteresse);

				if (retorno.getCStat().equalsIgnoreCase("138")) {

					LoteDistDFeInt loteDistDFeInt = retorno.getLoteDistDFeInt();
					List<DocZip> docZip = loteDistDFeInt.getDocZips();
					// o conteudo do arquivo vem zipado, é preciso descompactar com gzip

					for (DocZip dzip : docZip) {
						SchemaEnum schema = SchemaEnum.fromSchemaName(dzip.getSchema());
						String xml = GzipUtils.decompress(dzip.getValue());
						switch (schema) {
						case RESNFE: {
							// resumo de NFe, representam notas que foram emitidas para o cnpj mas que não foram
							// manifestadas pelo destinatário.
							ResNFe resNFe = JaxbUtils.convertStringXMLToEntity(xml, ResNFe.class);
							// os resumos de nfe devem ser manifestados
							// TODO manifestar conhecimento da NFe para permitir o download futuramente.
							//
							break;
						}
						case NFEPROC: {
							// resultado do processamento da NFe. Contém a nota e assinatura. Tag raiz nfeProc
							NfeProc nfeProc = JaxbUtils.convertStringXMLToEntity(xml, NfeProc.class);
							// TODO guardar a NFe na base/filesystem ...
							break;
						}
						case RESEVENTO: {
							// resumo de evento
							ResEvento resEvento = JaxbUtils.convertStringXMLToEntity(xml, ResEvento.class);
							break;
						}
						case PROCEVENTONFE: {
							// resultado do processamento de um evento
							ProcEventoNFe procEventoNFe = JaxbUtils.convertStringXMLToEntity(xml, ProcEventoNFe.class);
							break;
						}
						}

						File nota = new File(outputDir, cnpj + dzip.getNSU() + ".xml");
						FileUtils.writeStringToFile(nota, xml, StandardCharsets.UTF_8);
						log(nota.getAbsolutePath() + " criado!");
					}
				} else if (retorno.getCStat().equalsIgnoreCase("137")) {
					// não houve documentos
					log("não houve documentos para recuperar [" + retorno.getCStat() + ":" + retorno.getXMotivo() + "]");
				} else {
					throw new Exception(retorno.getCStat() + ":" + retorno.getXMotivo());
				}
			} catch (Exception e) {
				error(e);
				throw new RuntimeException(e);
			}

		}

		protected com.dgreentec.domain.xsd.retDistDFeInt_v101.RetDistDFeInt callService(DistDFeInt dist,
				NfeDistDFeInteresse distDFeInteresse) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException,
				FileNotFoundException, UnrecoverableKeyException, RemoteException, Exception {

			com.dgreentec.domain.xsd.retDistDFeInt_v101.RetDistDFeInt retorno = null;
			int tentativa = 0;
			do {

				log("TENTATIVA " + ++tentativa);
				log(JaxbUtils.convertEntityToXML(dist, false));

				NfeDistDFeInteresseResponse result = getResult(distDFeInteresse);

				NfeDistDFeInteresseResult_type0 nfeDistDFeInteresseResult = result.getNfeDistDFeInteresseResult();
				OMElement extraElement = nfeDistDFeInteresseResult.getExtraElement();
				String encoded = extraElement.toString();
				Unmarshaller unmarshaller = javax.xml.bind.JAXBContext
						.newInstance(new com.dgreentec.domain.xsd.retDistDFeInt_v101.ObjectFactory().getClass()).createUnmarshaller();
				retorno = (RetDistDFeInt) (unmarshaller.unmarshal(new java.io.StringReader(encoded)));
			} while (retorno == null || retorno.getCStat().equalsIgnoreCase("593"));

			return retorno;
		}

		private NfeDistDFeInteresseResponse getResult(NfeDistDFeInteresse distDFeInteresse) throws Exception {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			ks.load(new FileInputStream(new File(certsPath + File.separator + cnpj + ".pfx")), keyStorePass.toCharArray());
			String alias = "";
			Enumeration<String> aliasesEnum = ks.aliases();
			while (aliasesEnum.hasMoreElements()) {
				alias = (String) aliasesEnum.nextElement();
				if (ks.isKeyEntry(alias))
					break;
			}
			Certificate cert = ks.getCertificate(alias);
			X509Certificate certificate = (X509Certificate) cert;
			PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keyStorePass.toCharArray());

			KeyStore trustStore = KeyStore.getInstance("JKS");
			FileInputStream fileCacerts = new FileInputStream(new File(System.getProperty("user.dir") + "/src/test/resources/certificados"
					+ File.separator + "keystore" + File.separator + "NFeCacerts"));
			trustStore.load(fileCacerts, "changeit".toCharArray());

			SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(certificate, privateKey, trustStore);

			Protocol protocol = new Protocol("https", socketFactoryDinamico, 443);
			Protocol.registerProtocol("https", protocol);

			NFeDistribuicaoDFeStub stub = new NFeDistribuicaoDFeStub(
					WebServices.getInstanceConfig().getServico(UFEnum.AN, TipoServicoEnum.NFeDistribuicaoDFe, ambiente).getUrl());
			NfeDistDFeInteresseResponse result = stub.nfeDistDFeInteresse(distDFeInteresse);
			Protocol.unregisterProtocol("https");

			return result;

		}

		private void log(String pText) {
			System.out.println(sdf.format(new Date()) + " - [" + cnpj + "] " + Thread.currentThread().getName() + " | " + pText);
		}

		private void error(Throwable t) {
			String prefix = sdf.format(new Date()) + " - [" + cnpj + "]";
			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			System.err.println(prefix + "\n" + sw.toString());
		}

	}

	@Test
	public void consultarSefazNacionalRecuperandoNotasParaCNPJ() throws Exception {

		final TipoAmbienteEnum ambiente = TipoAmbienteEnum.HOMOLOGACAO;

		ThreadExecutor riopolem = new ThreadExecutor(ambiente, UFEnum.RJ, "07932968000103", "0", "RIOPOLEMLTDA");
		ThreadExecutor ddx = new ThreadExecutor(ambiente, UFEnum.RJ, "78570595000108", "0", "ddx123");

		Thread tddx = new Thread(ddx, "THREAD-DDX");
		Thread triopolem = new Thread(riopolem, "THREAD-RIOPOLEM");

		triopolem.start();
		tddx.start();

		tddx.join();
		triopolem.join();

		System.out.println("END");

	}
}
