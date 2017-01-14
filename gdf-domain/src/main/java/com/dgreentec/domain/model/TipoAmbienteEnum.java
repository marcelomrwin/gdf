package com.dgreentec.domain.model;

public enum TipoAmbienteEnum {
	HOMOLOGACAO("2"), PRODUCAO("1");

	private final String tpAmb;

	TipoAmbienteEnum(String pTp) {
		this.tpAmb = pTp;
	}

	public String getTpAmb() {
		return tpAmb;
	}
}