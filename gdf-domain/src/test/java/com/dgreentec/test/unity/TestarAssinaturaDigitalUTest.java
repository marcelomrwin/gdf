package com.dgreentec.test.unity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.dgreentec.domain.model.Certificado;
import com.dgreentec.domain.model.TipoAmbienteEnum;
import com.dgreentec.domain.model.UFEnum;

public class TestarAssinaturaDigitalUTest {

	@Test
	public void testarAssinaturaEnvioEvento() throws Exception {
		TipoAmbienteEnum ambiente = TipoAmbienteEnum.HOMOLOGACAO;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

		Certificado certificado = buscarCertificado("07932968000103");

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
		for (int i = 0; i < 3; i++) {
			StringBuilder evento = new StringBuilder("<evento versao=\"1.00\">").append("<infEvento Id=\"").append(eventoId).append("\">")
					.append("<cOrgao>").append(uf).append("</cOrgao>").append("<tpAmb>").append(ambiente.getTpAmb()).append("</tpAmb>")
					.append("<CNPJ>").append(cnpj).append("</CNPJ>").append("<chNFe>").append(chaveNFe).append("</chNFe>")
					.append("<dhEvento>").append(sdf.format(new Date())).append("</dhEvento>").append("<tpEvento>").append(tpEvento)
					.append("</tpEvento>").append("<nSeqEvento>").append("1").append("</nSeqEvento>").append("<verEvento>").append(versao)
					.append("</verEvento>").append("<detEvento versao=\"").append(versao).append("\">").append("<descEvento>")
					.append(txtEvento).append("</descEvento>").append("</detEvento>").append("</infEvento>").append("</evento>");

			String xmlSemAssinatura = evento.toString();

			String xmlAssinado = assinarEnvioEvento(xmlSemAssinatura, certificado);

			xml.append(xmlAssinado);
		}

		xml.append("</envEvento>");

		String eventoXML = xml.toString();

		System.out.println("***** XML EVENTO *****");
		System.out.println(eventoXML);
	}

	private Certificado buscarCertificado(String pCNPJ) throws IOException {
		Certificado certificado = new Certificado();
		String certsPath = System.getProperty("user.dir") + "/src/test/resources/certificados";
		byte[] bytes = FileUtils.readFileToByteArray(new File(certsPath + File.separator + pCNPJ + ".pfx"));
		certificado.setArquivo(bytes);
		certificado.setSenha("RIOPOLEMLTDA");
		return certificado;
	}

	private String assinarEnvioEvento(String xml, Certificado certificado) throws Exception {
		Document document = documentFactory(xml);

		XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
		ArrayList<Transform> transformList = signatureFactory(signatureFactory);

		KeyInfo keyInfo = certificado.getKeyInfo(signatureFactory);
		PrivateKey privateKey = certificado.getPrivateKey();

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
		DOMSignContext dsc = new DOMSignContext(privateKey, document.getElementsByTagName("evento").item(0));
		signature.sign(dsc);

		String xmlAssinado = outputXML(document);

		return xmlAssinado;
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

	private Document documentFactory(String xml) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		Document document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
		return document;
	}

	private String outputXML(Document doc) throws TransformerException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();

		trans.setOutputProperty("omit-xml-declaration", "yes");

		trans.transform(new DOMSource(doc), new StreamResult(os));
		String xml = os.toString();
		if ((xml != null) && (!"".equals(xml))) {
			xml = xml.replaceAll("\\r\\n", "");
			xml = xml.replaceAll(" standalone=\"no\"", "");
		}

		return xml;
	}
}
