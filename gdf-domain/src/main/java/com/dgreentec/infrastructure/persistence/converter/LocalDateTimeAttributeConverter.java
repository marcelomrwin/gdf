package com.dgreentec.infrastructure.persistence.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDateTime attribute) {
		Timestamp ts = Timestamp.valueOf(attribute);
		return new Date(ts.getTime());
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Date value) {
		return new Timestamp(value.getTime()).toLocalDateTime();
	}

}
