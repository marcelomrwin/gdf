package com.dgreentec.infrastructure.ssl;

public class ThreadLocalCert {

	private static final ThreadLocal<HSKeyManager> threadKeyManager = new ThreadLocal<>();

	public ThreadLocalCert(HSKeyManager hsKeyManager) {
		super();
		threadKeyManager.set(hsKeyManager);
	}

	public ThreadLocalCert() {
		super();
	}

	public HSKeyManager getKeyManager() {
		return threadKeyManager.get();
	}
		
}
