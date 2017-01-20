package com.dgreentec.test.unity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import com.dgreentec.AbstractTestCase;

public class TestCertificado extends AbstractTestCase {

	private File certificados = new File(getTestResourceFolder(), "certificados");

	@Test
	public void lerCertificadoDDX()
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableEntryException {
		File pfx = new File(certificados, "78570595000108.pfx");
		assertThat(pfx.exists(), is(true));

		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(pfx), "ddx123".toCharArray());
		Enumeration<String> aliases = ks.aliases();
		assertThat(aliases, is(not(nullValue())));

		String alias = aliases.nextElement();
		assertThat(alias, is(not(nullValue())));

		Certificate certificate = ks.getCertificate("");
		Entry entry = ks.getEntry("", new KeyStore.PasswordProtection("ddx123".toCharArray()));




	}

}
