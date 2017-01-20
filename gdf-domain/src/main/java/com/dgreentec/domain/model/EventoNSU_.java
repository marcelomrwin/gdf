package com.dgreentec.domain.model;

import com.dgreentec.infrastructure.model.SchemaEnum;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EventoNSU.class)
public abstract class EventoNSU_ extends com.dgreentec.infrastructure.model.AbstractEntityVersion_ {

	public static volatile SingularAttribute<EventoNSU, SchemaEnum> schema;
	public static volatile SingularAttribute<EventoNSU, String> observacao;
	public static volatile SingularAttribute<EventoNSU, Long> idNsu;
	public static volatile SingularAttribute<EventoNSU, Date> dtNSU;
	public static volatile SingularAttribute<EventoNSU, Long> idEventoNsu;
	public static volatile SingularAttribute<EventoNSU, String> codCNPJ;

	public static final String SCHEMA = "schema";
	public static final String OBSERVACAO = "observacao";
	public static final String IDNSU = "idNsu";
	public static final String DTNSU = "dtNSU";
	public static final String IDEVENTONSU = "idEventoNsu";
	public static final String CODCNPJ = "codCNPJ";

}

