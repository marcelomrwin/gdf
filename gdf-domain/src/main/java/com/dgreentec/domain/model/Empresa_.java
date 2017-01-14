package com.dgreentec.domain.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Empresa.class)
public abstract class Empresa_ extends com.dgreentec.infrastructure.model.AbstractEntityVersion_ {

	public static volatile SingularAttribute<Empresa, UFEnum> uf;
	public static volatile SingularAttribute<Empresa, Contrato> contrato;
	public static volatile SingularAttribute<Empresa, String> nome;
	public static volatile SingularAttribute<Empresa, String> cnpj;
	public static volatile SingularAttribute<Empresa, EventoNSU> ultimoNSU;
	public static volatile SingularAttribute<Empresa, Certificado> certificado;

	public static final String UF = "uf";
	public static final String CONTRATO = "contrato";
	public static final String NOME = "nome";
	public static final String CNPJ = "cnpj";
	public static final String ULTIMONSU = "ultimoNSU";
	public static final String CERTIFICADO = "certificado";

}

