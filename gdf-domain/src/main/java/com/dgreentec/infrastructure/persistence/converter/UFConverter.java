package com.dgreentec.infrastructure.persistence.converter;

import javax.persistence.AttributeConverter;

import com.dgreentec.domain.model.UFEnum;

public class UFConverter implements AttributeConverter<UFEnum, String> {

	@Override
	public String convertToDatabaseColumn(UFEnum attribute) {
		return attribute.name();
	}

	@Override
	public UFEnum convertToEntityAttribute(String dbData) {
		return UFEnum.valueOf(dbData);
	}

}
