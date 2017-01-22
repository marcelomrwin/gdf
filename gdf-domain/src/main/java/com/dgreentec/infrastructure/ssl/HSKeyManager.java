package com.dgreentec.infrastructure.ssl;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509KeyManager;

public class HSKeyManager implements X509KeyManager {

	private final X509Certificate certificate;

	private final PrivateKey privateKey;

	public HSKeyManager(X509Certificate certificate, PrivateKey privateKey) {
		this.certificate = certificate;
		this.privateKey = privateKey;
	}

	@Override
	public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
		return certificate.getIssuerDN().getName();
	}

	@Override
	public String chooseServerAlias(String arg0, Principal[] arg1, Socket arg2) {
		return null;
	}

	@Override
	public X509Certificate[] getCertificateChain(String arg0) {
		return new X509Certificate[] { certificate };
	}

	@Override
	public String[] getClientAliases(String arg0, Principal[] arg1) {
		return new String[] { certificate.getIssuerDN().getName() };
	}

	@Override
	public PrivateKey getPrivateKey(String arg0) {
		return privateKey;
	}

	@Override
	public String[] getServerAliases(String arg0, Principal[] arg1) {
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HSKeyManager [");
		if (certificate != null)
			builder.append("certificate=").append(certificate.getSubjectDN()).append(", ");
		builder.append("]");
		return builder.toString();
	}
}