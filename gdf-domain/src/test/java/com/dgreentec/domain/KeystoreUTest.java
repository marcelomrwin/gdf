package com.dgreentec.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.junit.Test;

public class KeystoreUTest {

	String certsPath = System.getProperty("user.dir") + "/src/main/resources/certificados";

	@Test
	public void readCerts() throws Throwable {
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(new File(certsPath + File.separator + "07932968000103.pfx")), "RIOPOLEMLTDA".toCharArray());

		String alias = "";
		Enumeration<String> aliasesEnum = ks.aliases();
		while (aliasesEnum.hasMoreElements()) {
			alias = (String) aliasesEnum.nextElement();
			if (ks.isKeyEntry(alias))
				break;
		}

		Certificate cert = ks.getCertificate(alias);
		X509Certificate certificate = (X509Certificate) cert;
		PrivateKey privateKey = (PrivateKey) ks.getKey(alias, "RIOPOLEMLTDA".toCharArray());

		System.out.println(privateKey);

		KeyStore trustStore = KeyStore.getInstance("JKS");

		trustStore.load(new FileInputStream(new File(System.getProperty("user.dir") + "/src/main/resources/certificados" + File.separator
				+ "keystore" + File.separator + "NFeCacerts")), "changeit".toCharArray());

		String nalias = "";
		Enumeration<String> naliasesEnum = trustStore.aliases();
		while (aliasesEnum.hasMoreElements()) {
			nalias = (String) naliasesEnum.nextElement();
			if (trustStore.isKeyEntry(nalias))
				break;
		}

		System.out.println(nalias);
	}
}
