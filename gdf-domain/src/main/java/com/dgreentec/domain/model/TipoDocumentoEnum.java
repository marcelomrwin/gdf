package com.dgreentec.domain.model;

public enum TipoDocumentoEnum {
	NFE(0);

	private final Integer tipo;

	TipoDocumentoEnum(Integer pTipo) {
		this.tipo = pTipo;
	}

	public Integer getTipo() {
		return tipo;
	}

	public static TipoDocumentoEnum fromNumberValue(Integer pValue) {
		switch (pValue) {
		case 0:
			return TipoDocumentoEnum.NFE;
		default:
			throw new IllegalArgumentException("Index for enum not found!");
		}
	}
}
