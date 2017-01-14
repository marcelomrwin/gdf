package com.dgreentec.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.dgreentec.domain.model.TipoAmbienteEnum;
import com.dgreentec.domain.model.TipoServicoEnum;
import com.dgreentec.domain.model.UFEnum;
import com.dgreentec.domain.xsd.retEnvEvento_v100.RetEnvEvento;
import com.dgreentec.infrastructure.configuration.nfe.WebServices;

import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.RecepcaoEventoStub;
import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.RecepcaoEventoStub.NfeCabecMsg;
import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.RecepcaoEventoStub.NfeCabecMsgE;
import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.RecepcaoEventoStub.NfeDadosMsg;
import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.RecepcaoEventoStub.NfeRecepcaoEventoResult;

public class EfetuarEventoCienciaOperacaoUTest {

	@SuppressWarnings("restriction")
	@Before
	public void configCertificado() {
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

		String certsPath = System.getProperty("user.dir") + "/src/main/resources/certificados";

		// rio polem
		System.setProperty("javax.net.ssl.keyStore", certsPath + File.separator + "07932968000103.pfx");
		System.setProperty("javax.net.ssl.keyStorePassword", "RIOPOLEMLTDA");

		// ddx
//		System.setProperty("javax.net.ssl.keyStore", certsPath + File.separator + "78570595000108.pfx");
//		System.setProperty("javax.net.ssl.keyStorePassword", "ddx123");

		System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		System.setProperty("javax.net.ssl.trustStore", certsPath + File.separator + "keystore" + File.separator + "NFeCacerts");
	}

	@Test
	public void efetuarCienciaOperacao() throws Exception {
		TipoAmbienteEnum ambiente = TipoAmbienteEnum.HOMOLOGACAO;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

		String chaveNFe = "33170178570595000108550010000000041047018006";
		String eventoId = "ID" + "210210" + chaveNFe + "01";
		String lote = "100";
		String uf = UFEnum.AN.getCodigo();
		String cnpj = "07932968000103";
		String tpEvento = "210210";
		String versao = "1.00";
		String txtEvento = "Ciencia da Operacao";

		StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		xml.append("<envEvento versao=\"").append(versao).append("\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">").append("<idLote>")
				.append(lote).append("</idLote>");
//N eventos
		StringBuilder evento = new StringBuilder("<evento versao=\"1.00\">").append("<infEvento Id=\"").append(eventoId).append("\">")
				.append("<cOrgao>").append(uf).append("</cOrgao>").append("<tpAmb>").append(ambiente.getTpAmb()).append("</tpAmb>")
				.append("<CNPJ>").append(cnpj).append("</CNPJ>").append("<chNFe>").append(chaveNFe).append("</chNFe>").append("<dhEvento>")
				.append(sdf.format(new Date())).append("</dhEvento>").append("<tpEvento>").append(tpEvento).append("</tpEvento>")
				.append("<nSeqEvento>").append("1").append("</nSeqEvento>").append("<verEvento>").append(versao).append("</verEvento>")
				.append("<detEvento versao=\"").append(versao).append("\">").append("<descEvento>").append(txtEvento)
				.append("</descEvento>").append("</detEvento>").append("</infEvento>").append("</evento>");

		xml.append(evento.toString());
		xml.append("</envEvento>");

		String eventoXML = xml.toString();

		System.out.println("***** XML EVENTO *****");
		System.out.println(eventoXML);

		String xmlAssinado = assinarEnvioEvento(eventoXML, "07932968000103.pfx", "RIOPOLEMLTDA");
		info(xmlAssinado);

		RecepcaoEventoStub recepcaoEventoStub = new RecepcaoEventoStub(
				WebServices.getInstanceConfig().getServico(UFEnum.AN, TipoServicoEnum.RecepcaoEvento, ambiente).getUrl());
		OMElement ome = AXIOMUtil.stringToOM(xmlAssinado);
		NfeDadosMsg dados = new NfeDadosMsg();
		dados.setExtraElement(ome);

		NfeCabecMsgE cabecalho = new NfeCabecMsgE();
		NfeCabecMsg nfeCabecMsg = new NfeCabecMsg();
		nfeCabecMsg.setVersaoDados("1.00");
		nfeCabecMsg.setCUF(UFEnum.AN.getCodigo());
		cabecalho.setNfeCabecMsg(nfeCabecMsg);

		NfeRecepcaoEventoResult nfeRecepcaoEventoResult = recepcaoEventoStub.nfeRecepcaoEvento(dados, cabecalho);
		OMElement extraElement = nfeRecepcaoEventoResult.getExtraElement();
		String encoded = extraElement.toString();

		System.out.println("***** ENCODED *****");
		System.out.println(encoded);

		com.dgreentec.domain.xsd.retEnvEvento_v100.ObjectFactory of = new com.dgreentec.domain.xsd.retEnvEvento_v100.ObjectFactory();
		Unmarshaller unmarshaller = javax.xml.bind.JAXBContext.newInstance(of.getClass()).createUnmarshaller();

		Object obj = unmarshaller.unmarshal(new java.io.StringReader(encoded));
		RetEnvEvento retEvento = (RetEnvEvento) obj;

		System.out.println(retEvento.getCStat() + "|" + retEvento.getXMotivo());
//

	}

	private String assinarEnvioEvento(String xml, String certificado, String senha) throws Exception {
		Document document = documentFactory(xml);

		XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
		ArrayList<Transform> transformList = signatureFactory(signatureFactory);
		Object[] securities = loadCertificates(certificado, senha, signatureFactory);
		KeyInfo keyInfo = (KeyInfo) securities[0];
		PrivateKey privateKey = (PrivateKey) securities[1];

		NodeList elements = document.getElementsByTagName("infEvento");
		org.w3c.dom.Element el = (org.w3c.dom.Element) elements.item(0);
		el.setIdAttribute("Id", true);
		String id = el.getAttribute("Id");

		Reference ref = signatureFactory.newReference("#" + id, signatureFactory.newDigestMethod(DigestMethod.SHA1, null), transformList,
				null, null);

		SignedInfo si = signatureFactory.newSignedInfo(
				signatureFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
				signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

		XMLSignature signature = signatureFactory.newXMLSignature(si, keyInfo);

//		DOMSignContext dsc = new DOMSignContext(privateKey, document.getFirstChild());
		DOMSignContext dsc = new DOMSignContext(privateKey, document.getElementsByTagName("evento").item(0));

		signature.sign(dsc);

		return outputXML(document);
	}

	private Document documentFactory(String xml) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		Document document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
		return document;
	}

	private ArrayList<Transform> signatureFactory(XMLSignatureFactory signatureFactory)
			throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		ArrayList<Transform> transformList = new ArrayList<Transform>();
		TransformParameterSpec tps = null;
		Transform envelopedTransform = signatureFactory.newTransform(Transform.ENVELOPED, tps);
		Transform c14NTransform = signatureFactory.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", tps);

		transformList.add(envelopedTransform);
		transformList.add(c14NTransform);
		return transformList;
	}

	private Object[] loadCertificates(String certificado, String senha, XMLSignatureFactory signatureFactory) throws Exception {
		Object[] securities = new Object[2];
		String certsPath = System.getProperty("user.dir") + "/src/main/resources/certificados";

		InputStream entrada = new FileInputStream(certsPath + File.separator + certificado);
		KeyStore ks = KeyStore.getInstance("pkcs12");
		try {
			ks.load(entrada, senha.toCharArray());
		} catch (IOException e) {
			throw new Exception("Senha do Certificado Digital incorreta ou Certificado inválido.", e);
		}

		KeyStore.PrivateKeyEntry pkEntry = null;
		Enumeration<String> aliasesEnum = ks.aliases();
		while (aliasesEnum.hasMoreElements()) {
			String alias = (String) aliasesEnum.nextElement();
			if (ks.isKeyEntry(alias)) {
				pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(senha.toCharArray()));
				securities[1] = pkEntry.getPrivateKey();
				break;
			}
		}

		X509Certificate cert = (X509Certificate) pkEntry.getCertificate();
		info("SubjectDN: " + cert.getSubjectDN().toString());

		KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
		List<X509Certificate> x509Content = new ArrayList<X509Certificate>();

		x509Content.add(cert);
		X509Data x509Data = keyInfoFactory.newX509Data(x509Content);
		KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));
		securities[0] = keyInfo;
		return securities;
	}

	private String outputXML(Document doc) throws TransformerException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));
		String xml = os.toString();
		if ((xml != null) && (!"".equals(xml))) {
			xml = xml.replaceAll("\\r\\n", "");
			xml = xml.replaceAll(" standalone=\"no\"", "");
		}
		return xml;
	}

	/**
	 * Log INFO.
	 *
	 * @param info
	 */
	private static void info(String info) {
		System.out.println("| INFO: " + info);
	}

}
