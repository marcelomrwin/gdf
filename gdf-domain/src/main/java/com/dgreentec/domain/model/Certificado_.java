package com.dgreentec.domain.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Certificado.class)
public abstract class Certificado_ extends com.dgreentec.infrastructure.model.AbstractEntityVersion_ {

	public static volatile SingularAttribute<Certificado, String> senha;
	public static volatile SingularAttribute<Certificado, Date> dataVencimento;
	public static volatile SingularAttribute<Certificado, byte[]> arquivo;
	public static volatile SingularAttribute<Certificado, Long> idCertificado;

	public static final String SENHA = "senha";
	public static final String DATAVENCIMENTO = "dataVencimento";
	public static final String ARQUIVO = "arquivo";
	public static final String IDCERTIFICADO = "idCertificado";

}

