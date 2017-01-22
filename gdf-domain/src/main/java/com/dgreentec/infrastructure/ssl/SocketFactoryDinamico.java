package com.dgreentec.infrastructure.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketFactoryDinamico implements ProtocolSocketFactory {

	private static final Logger LOG = LoggerFactory.getLogger(SocketFactoryDinamico.class);

	private SSLContext ssl = null;

	private final ThreadLocalCert thread;

	private final KeyStore trustStore;

	public SocketFactoryDinamico(X509Certificate certificate, PrivateKey privateKey, KeyStore trustStore) {
		this.thread = new ThreadLocalCert(new HSKeyManager(certificate, privateKey));
		this.trustStore = trustStore;

		debug(getClass().getName()+" criado com a thread "+thread.getKeyManager());
	}

	private SSLContext createSSLContext() {
		try {

			debug("criando ssl context " + thread.getKeyManager());

			KeyManager[] keyManagers = createKeyManagers();
			TrustManager[] trustManagers = createTrustManagers();
			SSLContext sslContext = SSLContext.getInstance("TLSv1");
			sslContext.init(keyManagers, trustManagers, null);

			return sslContext;
		} catch (Throwable e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	private SSLContext getSSLContext() {
		if (ssl == null) {
			ssl = createSSLContext();
		}
		return ssl;
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params)
			throws IOException, UnknownHostException, ConnectTimeoutException {

		debug("criando socket " + thread.getKeyManager());

		if (params == null) {
			throw new IllegalArgumentException("Parameters may not be null");
		}
		int timeout = params.getConnectionTimeout();
		SocketFactory socketfactory = getSSLContext().getSocketFactory();

		if (timeout == 0) {
			return socketfactory.createSocket(host, port, localAddress, localPort);
		}

		Socket socket = socketfactory.createSocket();
		((SSLSocket) socket).setEnabledProtocols(new String[] { "SSLv3", "TLSv1" });

		SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
		SocketAddress remoteaddr = new InetSocketAddress(host, port);

		socket.bind(localaddr);
		int retry = 0;
		while (!socket.isConnected()) {
			try {
				socket.connect(remoteaddr, timeout);
			} catch (Exception e) {
				retry++;
				if (retry >= 10)
					throw e;
			}
		}
		return socket;
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(host, port);
	}

	public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
	}

	public KeyManager[] createKeyManagers() {
		HSKeyManager keyManager = thread.getKeyManager();
		return new KeyManager[] { keyManager };
	}

	public TrustManager[] createTrustManagers() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(trustStore);

		return trustManagerFactory.getTrustManagers();
	}

	protected void debug(String text) {
		System.out.println("** DEBUG BEGIN ** |" + getClass().getName() + ":" + Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ " | " + new Date() + " | " + text + "| ** DEBUG END **");
	}

}
