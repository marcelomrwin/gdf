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
import java.security.Provider;
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

public class AssinadorXML {  
  
    private static final String C14N_TRANSFORM_METHOD = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";  
  
    private static final String EVENTO = "evento";  
    private static final String NFE = "NFe";  
  
    private PrivateKey privateKey;  
    private KeyInfo keyInfo;  
  
  /** 
     * Assinando o XML de EnvEvento da NF-e. 
     * 
     * @param xml 
     * @param certificadoSenha 
     * @return 
     * @throws java.lang.Exception 
     */  
    public String assinaXmlEnvEvento(String xml, String certificadoSenha) throws Exception {  
        AssinadorXML assinaXml = new AssinadorXML();  
        return assinaXml.assinaEnvEvento(xml, certificadoSenha);  
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
    private String assinaEnviNFe(String xml, String certificadoSenha) throws Exception {  
        Document document = documentFactory(xml);  
        XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");  
        ArrayList<Transform> transformList = transformFactory(signatureFactory);  
        loadCertificates(certificadoSenha, signatureFactory);  
  
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
    private String assinaEnvEvento(String xml, String certificadoSenha) throws Exception {  
        Document document = documentFactory(xml);  
        XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");  
        ArrayList<Transform> transformList = transformFactory(signatureFactory);  
        loadCertificates(certificadoSenha, signatureFactory);  
  
        for (int i = 0; i < document.getDocumentElement().getElementsByTagName(EVENTO).getLength(); i++) {  
            assinar(EVENTO, signatureFactory, transformList, privateKey, keyInfo, document, i);  
        }  
  
        return outputXML(document);  
    }  

    /** 
     * Método responsável por assinar XMLs de TNFe e Eventos 
     * 
     * @param tipo String: variáveis estaticas EVENTO ou NFE 
     * @param fac XMLSignatureFactory 
     * @param transformList ArrayList<Transform> 
     * @param privateKey PrivateKey 
     * @param ki KeyInfo 
     * @param document Document 
     * @param index int 
     */  
    private void assinar(String tipo, XMLSignatureFactory fac, ArrayList<Transform> transformList, PrivateKey privateKey, KeyInfo ki, Document document, int index) throws Exception {  
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
  
        SignedInfo si = fac.newSignedInfo(  
                fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),  
                fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),  
                Collections.singletonList(ref));  
  
        XMLSignature signature = fac.newXMLSignature(si, ki);  
  
        DOMSignContext dsc = new DOMSignContext(privateKey, document.getDocumentElement().getElementsByTagName(tipo).item(index));  
        signature.sign(dsc);  
    }  
  
    /** 
     * Método cria uma lista de Transform, responsável pelo envelopamento da 
     * assinatura. 
     */  
    private ArrayList<Transform> transformFactory(XMLSignatureFactory signatureFactory) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {  
        ArrayList<Transform> transformList = new ArrayList<Transform>();  
  
        // CRIA UM TRANSFORM: <Transform Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"/>  
        Transform envelopedTransform = signatureFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);  
        // CRIA UM TRANSFORM: <Transform Algorithm="http://www.w3.org/TR/2001/REC-xml-c14n-20010315"/>  
        Transform c14NTransform = signatureFactory.newTransform(AssinadorXML.C14N_TRANSFORM_METHOD, (TransformParameterSpec) null);  
  
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
    private void loadCertificates( String certificadoSenha, XMLSignatureFactory signatureFactory) throws Exception {  

        Provider provider = new sun.security.pkcs11.SunPKCS11("SmartCard.cfg");
        
        // KeyStore é utilizado para armazenar a chave publica do certificado digital.  
        // Para este caso é definido que o tipo de arquivo é pkcs12 (.p12 ou .pfx), portanto  
        // se necessário armazenar um certificado digital A3(cartão) ou token, isso deve  
        // ser alterado. (jks, jceks, etc...)  
        KeyStore ks = KeyStore.getInstance("pkcs11");  
  
        try {  
            // Carrega o keystore do certificado digital  
            ks.load(null, certificadoSenha.toCharArray());  
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


