package com.dgreentec.infrastructure;

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
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
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
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// SEQUENCIA PARA ASSINAR UM DOCUMENT
//
// - Instantiating the Document to be Signed
// DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
// dbf.setNamespaceAware(true); 
// DocumentBuilder builder = dbf.newDocumentBuilder();  
// ex:1 Document doc = builder.parse(new FileInputStream(argv[0])); 
// ex:2 Document doc = builder.parse(new ByteArrayInputStream(file.getBytes()));
//
// - Creating a Public Key Pair (aqui entra o certificado digital, então nao usa o KeyPairGenerator, mas sim o KeyStore)
// KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
// kpg.initialize(512);
// KeyPair kp = kpg.generateKeyPair(); 
//
// - Creating a Signing Context
// DOMSignContext dsc = new DOMSignContext (kp.getPrivate(), doc.getDocumentElement()); 
//
// - Assembling the XML Signature
// XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM"); 
// Reference ref = fac.newReference(
//    "Reference URI", 
//    fac.newDigestMethod(DigestMethod.SHA1, null),
//    Collections.singletonList(fac.newTransform(Transform.ENVELOPED,(TransformParameterSpec) null)), 
//    null, 
//    null); 
// SignedInfo si = fac.newSignedInfo(
//    fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS, (C14NMethodParameterSpec) null),
//    fac.newSignatureMethod(SignatureMethod.DSA_SHA1, null),
//    Collections.singletonList(ref));
// KeyInfoFactory kif = fac.getKeyInfoFactory(); 
// KeyValue kv = kif.newKeyValue(kp.getPublic());
// KeyInfo ki = kif.newKeyInfo(Collections.singletonList(kv)); 
// XMLSignature signature = fac.newXMLSignature(si, ki); 
//
// - Generating the XML Signature
// signature.sign(dsc); 
//
// - Printing or Displaying the Resulting Document
// TransformerFactory tf = TransformerFactory.newInstance();
// Transformer trans = tf.newTransformer();
// trans.transform(new DOMSource(doc), new StreamResult(os)); 
/**
 *
 *
 * @author tiago
 */
public class AssinadorXMLA1 {

	private static final String C14N_TRANSFORM_METHOD = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";

	private static final String INFINUT = "infInut";

	private static final String EVENTO = "evento";

	private static final String NFE = "NFe";

	private PrivateKey privateKey;

	private KeyInfo keyInfo;

	/**
	 * Assinando o XML de EnvNFe da NF-e.
	 *
	 * @param xml
	 * @param certificadoArquivo
	 * @param certificadoSenha
	 * @return
	 * @throws java.lang.Exception
	 */
	public String assinaXmlEnviNFe(String xml, byte[] certificadoArquivo, String certificadoSenha) throws Exception {
		AssinadorXMLA1 assinaXml = new AssinadorXMLA1();
		return assinaXml.assinaEnviNFe(xml, certificadoArquivo, certificadoSenha);
	}

	/**
	 * Assinando o XML de InutNFe da NF-e.
	 *
	 * @param xml
	 * @param certificadoArquivo
	 * @param certificadoSenha
	 * @return
	 * @throws java.lang.Exception
	 */
	public String assinaXmlInutNFe(String xml, byte[] certificadoArquivo, String certificadoSenha) throws Exception {
		AssinadorXMLA1 assinaXml = new AssinadorXMLA1();
		return assinaXml.assinaInutNFe(xml, certificadoArquivo, certificadoSenha);
	}

	/**
	 * Assinando o XML de EnvEvento da NF-e.
	 *
	 * @param xml
	 * @param certificadoArquivo
	 * @param certificadoSenha
	 * @return
	 * @throws java.lang.Exception
	 */
	public String assinaXmlEnvEvento(String xml, byte[] certificadoArquivo, String certificadoSenha) throws Exception {
		AssinadorXMLA1 assinaXml = new AssinadorXMLA1();
		return assinaXml.assinaEnvEvento(xml, certificadoArquivo, certificadoSenha);
	}

	/**
	 * Assinatura do XML de Envio de Lote da NF-e utilizando Certificado Digital
	 * A1.
	 *
	 * @param xml
	 * @param certificadoArquivo
	 * @param certificadoSenha
	 * @return
	 * @throws Exception
	 */
	private String assinaEnviNFe(String xml, byte[] certificadoArquivo, String certificadoSenha) throws Exception {
		Document document = documentFactory(xml);
		XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
		ArrayList<Transform> transformList = transformFactory(signatureFactory);
		loadCertificates(certificadoArquivo, certificadoSenha, signatureFactory);

		for (int i = 0; i < document.getDocumentElement().getElementsByTagName(NFE).getLength(); i++) {
			assinar(NFE, signatureFactory, transformList, privateKey, keyInfo, document, i);
		}

		return outputXML(document);
	}

	/**
	 * Assintaruda do XML de Eventos da NF-e utilizando Certificado Digital A1.
	 *
	 * @param InputStream
	 * @param certificadoArquivo
	 * @param certificadoSenha
	 * @return
	 * @throws Exception
	 */
	private String assinaEnvEvento(String xml, byte[] certificadoArquivo, String certificadoSenha) throws Exception {
		Document document = documentFactory(xml);
		XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
		ArrayList<Transform> transformList = transformFactory(signatureFactory);
		loadCertificates(certificadoArquivo, certificadoSenha, signatureFactory);

		for (int i = 0; i < document.getDocumentElement().getElementsByTagName(EVENTO).getLength(); i++) {
			assinar(EVENTO, signatureFactory, transformList, privateKey, keyInfo, document, i);
		}

		return outputXML(document);
	}

	/**
	 * Assinatura do XML de INUTILIZACAO da NF-e utilizando Certificado Digital
	 * A1.
	 *
	 * @param xml
	 * @param certificadoArquivo
	 * @param certificadoSenha
	 * @return
	 * @throws Exception
	 */
	private String assinaInutNFe(String xml, byte[] certificadoArquivo, String certificadoSenha) throws Exception {
		// Parse from XML to Document
		Document document = documentFactory(xml);
		// Obter a implementação de "DOM" de XMLSignatureFactory
		XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
		// Cria lista de Transform
		ArrayList<Transform> transformList = transformFactory(signatureFactory);
		// Carrega informações de certificado digital
		loadCertificates(certificadoArquivo, certificadoSenha, signatureFactory);
		//
		assinarInutilizacao(INFINUT, signatureFactory, transformList, privateKey, document, 0);

		return outputXML(document);
	}

	/**
	 * Método responsável por assinar XMLs de TInutNFe
	 *
	 * @param tipo
	 *            String: variáveis estaticas EVENTO ou NFE
	 * @param fac
	 *            XMLSignatureFactory
	 * @param transformList
	 *            ArrayList<Transform>
	 * @param privateKey
	 *            PrivateKey
	 * @param ki
	 *            KeyInfo
	 * @param document
	 *            Document
	 * @param index
	 *            int
	 */
	private String assinarInutilizacao(String tipo, XMLSignatureFactory fac, ArrayList<Transform> transformList, PrivateKey privateKey,
			Document document, int index) throws Exception {
		// getElementsByTagName retorna NodeList dos elementos continos no document de acordo com parametros buscado
		NodeList elements = document.getElementsByTagName(tipo);
		org.w3c.dom.Element elementFound = (org.w3c.dom.Element) elements.item(index);
		String id = elementFound.getAttribute("Id");
		elementFound.setIdAttribute("Id", true);

		// Reference é utilizado para validação futura do xml, contido em SignedInfo
		Reference reference = fac.newReference(
				// Reference URI = #NFeChaveAcesso (URI do objeto a ser assinado)
				"#" + id,
				// Reference <DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/> (DigestMethod utilizado
				// = SHA1)
				fac.newDigestMethod(DigestMethod.SHA1, null),
				// Reference List <Transforms>
				transformList,
				// Reference type
				null,
				// Reference id
				null);

		// SignedInfo é o objeto que é assinado, contido em XMLSignature
		SignedInfo signedInfo = fac.newSignedInfo(
				// SignedInfo <CanonicalizationMethod Algorithm="http://www.w3.org/TR/2001/REC-xml-c14n-20010315"/>
				fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
				// SignedInfo <SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#rsa-sha1"/>
				fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
				// SignedInfo Reference
				Collections.singletonList(reference));

		// XMLSignature recebe os parametros ja populados de SignedInfo e KeyInfo
		XMLSignature xmlSign = fac.newXMLSignature(signedInfo, keyInfo);

		// DOMSignContext é utilizado para gerar a assinatura, utilizando parâmetros
		// - PrivateKey usada na assinatura, que neste caso é o certificado digital
		// - Document a ser assinado
		DOMSignContext domsc = new DOMSignContext(privateKey, document.getFirstChild());

		// realiza assinatura
		xmlSign.sign(domsc);

		return outputXML(document);
	}

	/**
	 * Método responsável por assinar XMLs de TNFe e Eventos
	 *
	 * @param tipo
	 *            String: variáveis estaticas EVENTO ou NFE
	 * @param fac
	 *            XMLSignatureFactory
	 * @param transformList
	 *            ArrayList<Transform>
	 * @param privateKey
	 *            PrivateKey
	 * @param ki
	 *            KeyInfo
	 * @param document
	 *            Document
	 * @param index
	 *            int
	 */
	private void assinar(String tipo, XMLSignatureFactory fac, ArrayList<Transform> transformList, PrivateKey privateKey, KeyInfo ki,
			Document document, int index) throws Exception {
		NodeList elements = null;

		if (tipo.equals(EVENTO)) {
			elements = document.getElementsByTagName("infEvento");
		} else if (tipo.equals(NFE)) {
			elements = document.getElementsByTagName("infNFe");
		}

		org.w3c.dom.Element el = (org.w3c.dom.Element) elements.item(index);
		String id = el.getAttribute("Id");
		el.setIdAttribute("Id", true);

		Reference ref = fac.newReference("#" + id, fac.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);

		SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
				fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

		XMLSignature signature = fac.newXMLSignature(si, ki);

		DOMSignContext dsc = new DOMSignContext(privateKey, document.getDocumentElement().getElementsByTagName(tipo).item(index));
		signature.sign(dsc);
	}

	/**
	 * Método cria uma lista de Transform, responsável pelo envelopamento da
	 * assinatura.
	 */
	private ArrayList<Transform> transformFactory(XMLSignatureFactory signatureFactory)
			throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		ArrayList<Transform> transformList = new ArrayList<Transform>();

		// CRIA UM TRANSFORM: <Transform Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"/>
		Transform envelopedTransform = signatureFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
		// CRIA UM TRANSFORM: <Transform Algorithm="http://www.w3.org/TR/2001/REC-xml-c14n-20010315"/>
		Transform c14NTransform = signatureFactory.newTransform(AssinadorXMLA1.C14N_TRANSFORM_METHOD, (TransformParameterSpec) null);

		transformList.add(envelopedTransform);
		transformList.add(c14NTransform);
		return transformList;
	}

	/**
	 * Método responsável por realizar o parse from:xml to:document utilizando
	 * DocumentBuilderFactory com valor default.
	 */
	private Document documentFactory(String xml) throws SAXException, IOException, ParserConfigurationException {
		// Cria instancia com valor default do Document
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// "Mantém o Document ciente da operação"
		factory.setNamespaceAware(true);
		// Realiza o parse do xml para Document
		Document document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));

		return document;
	}

	/**
	 * Método responsável por carregar os dados do certificado digital
	 */
	private void loadCertificates(byte[] certificadoArquivo, String certificadoSenha, XMLSignatureFactory signatureFactory)
			throws Exception {
		// Carrega o certificado em um inputStream
		InputStream certificado = new ByteArrayInputStream(certificadoArquivo);

		// KeyStore é utilizado para armazenar a chave publica do certificado digital.
		// Para este caso é definido que o tipo de arquivo é pkcs12 (.p12 ou .pfx), portanto
		// se necessário armazenar um certificado digital A3(cartão) ou token, isso deve
		// ser alterado. (jks, jceks, etc...)
		KeyStore ks = KeyStore.getInstance("pkcs12");

		try {
			// Carrega o keystore do certificado digital
			ks.load(certificado, certificadoSenha.toCharArray());
		} catch (IOException e) {
			throw new Exception("Senha do Certificado Digital incorreta ou Certificado inválido.");
		}

		KeyStore.PrivateKeyEntry pkEntry = null;
		Enumeration<String> aliasesEnum = ks.aliases();
		while (aliasesEnum.hasMoreElements()) {
			String alias = (String) aliasesEnum.nextElement();
			if (ks.isKeyEntry(alias)) {
				pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(certificadoSenha.toCharArray()));
				privateKey = pkEntry.getPrivateKey();
				break;
			}
		}

		X509Certificate cert = (X509Certificate) pkEntry.getCertificate();

		List<X509Certificate> x509Content = new ArrayList<X509Certificate>();
		x509Content.add(cert);

		// Através do XMLSignatureFactory torna-se possível criar um KeyInfoFactory...
		KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
		// ...e foi utilizado para criar um x509Data que dispoem de conteúdo X.509
		// que é uma estrutura de chave pública utilizada em certificados digitais
		X509Data x509Data = keyInfoFactory.newX509Data(x509Content);

		// KeyInfo é um elemento opcional que habilita o recipiente obter a chave necessária para
		// validar um XMLSignature, e neste caso é utilizada para gerenciar informações de X509Data
		keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));
	}

	/**
	 * Método utilizado para printar ou exibir o Document assinado
	 */
	private String outputXML(Document doc) throws TransformerException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		// TransformerFactory é utilizado para criar instancias de objetos Transformer e Templates,
		// e neste caso transforma o conteudo de Document em um resultado String com transformer.
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.transform(new DOMSource(doc), new StreamResult(os));

		// valida documento
		String xml = os.toString();
		if ((xml != null) && (!"".equals(xml))) {
			xml = xml.replaceAll("\\r\\n", "");
			xml = xml.replaceAll(" standalone=\"no\"", "");
		}
		return xml;
	}
}
