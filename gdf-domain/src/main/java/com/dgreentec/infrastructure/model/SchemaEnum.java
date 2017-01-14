package com.dgreentec.infrastructure.model;

public enum SchemaEnum {
	RESNFE("resNFe"), NFEPROC("procNFe"), RESEVENTO("resEvento"), PROCEVENTONFE("procEventoNFe");

	private final String nmSchema;

	SchemaEnum(String pNmSchema) {
		this.nmSchema = pNmSchema;
	}

	public String getNmSchema() {
		return nmSchema;
	}

	public static SchemaEnum fromSchemaName(String pSchema) {
		if (pSchema.contains(RESNFE.nmSchema))
			return RESNFE;
		if (pSchema.contains(NFEPROC.nmSchema))
			return NFEPROC;
		if (pSchema.contains(RESEVENTO.nmSchema))
			return RESEVENTO;
		if (pSchema.contains(PROCEVENTONFE.nmSchema))
			return PROCEVENTONFE;

		throw new IllegalArgumentException(pSchema + " - Schema not found!");
	}

}
