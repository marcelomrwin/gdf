package com.dgreentec.infrastructure.persistence.converter;

import javax.persistence.AttributeConverter;

import com.dgreentec.infrastructure.model.SchemaEnum;


public class EventoSchemaConverter implements AttributeConverter<SchemaEnum, String> {

	@Override
	public String convertToDatabaseColumn(SchemaEnum attribute) {
		return attribute.getNmSchema();
	}

	@Override
	public SchemaEnum convertToEntityAttribute(String dbData) {
		return SchemaEnum.fromSchemaName(dbData);
	}

}
