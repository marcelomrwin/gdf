package com.dgreentec.infrastructure.configuration.nfe.services;

import java.io.Serializable;

public interface AbstractNFeService extends Serializable {

	String getMethod();

	void setMethod(String method);

	String getVersion();

	void setVersion(String version);

	String getUrl();

	void setUrl(String url);
}
