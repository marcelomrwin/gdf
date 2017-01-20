package com.dgreentec.domain.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Usuario.class)
public abstract class Usuario_ extends com.dgreentec.infrastructure.model.AbstractEntityVersion_ {

	public static volatile SetAttribute<Usuario, Tenant> tenants;
	public static volatile SingularAttribute<Usuario, String> cpf;
	public static volatile SingularAttribute<Usuario, String> nome;

	public static final String TENANTS = "tenants";
	public static final String CPF = "cpf";
	public static final String NOME = "nome";

}

