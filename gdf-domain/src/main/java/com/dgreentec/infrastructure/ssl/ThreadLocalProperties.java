package com.dgreentec.infrastructure.ssl;

import java.util.Properties;

public class ThreadLocalProperties extends Properties {

	private static final long serialVersionUID = -653512335164331939L;

	private final ThreadLocal<Properties> localProperties = new ThreadLocal<Properties>() {

		@Override
		protected Properties initialValue() {
			return new Properties();
		}
	};

	public ThreadLocalProperties(Properties properties) {
		super(properties);
	}

	@Override
	public String getProperty(String key) {
		String localValue = localProperties.get().getProperty(key);
		return localValue == null ? super.getProperty(key) : localValue;
	}

	@Override
	public Object setProperty(String key, String value) {
		return localProperties.get().setProperty(key, value);
	}
}
