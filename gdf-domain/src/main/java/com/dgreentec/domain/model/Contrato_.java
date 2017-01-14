package com.dgreentec.domain.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Contrato.class)
public abstract class Contrato_ extends com.dgreentec.infrastructure.model.AbstractEntityVersion_ {

	public static volatile ListAttribute<Contrato, Empresa> empresas;
	public static volatile SingularAttribute<Contrato, String> cnpj;
	public static volatile SingularAttribute<Contrato, Long> idContrato;

	public static final String EMPRESAS = "empresas";
	public static final String CNPJ = "cnpj";
	public static final String IDCONTRATO = "idContrato";

}

