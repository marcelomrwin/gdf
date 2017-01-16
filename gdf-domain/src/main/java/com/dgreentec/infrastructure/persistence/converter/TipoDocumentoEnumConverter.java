package com.dgreentec.infrastructure.persistence.converter;

import javax.persistence.AttributeConverter;

import com.dgreentec.domain.model.TipoDocumentoEnum;


public class TipoDocumentoEnumConverter implements AttributeConverter<TipoDocumentoEnum, Integer> {

	@Override
	public Integer convertToDatabaseColumn(TipoDocumentoEnum attribute) {
		return attribute.getTipo();
	}

	@Override
	public TipoDocumentoEnum convertToEntityAttribute(Integer dbData) {
		return TipoDocumentoEnum.fromNumberValue(dbData);
	}

}
