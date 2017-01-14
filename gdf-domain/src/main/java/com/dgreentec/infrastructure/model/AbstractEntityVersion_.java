package com.dgreentec.infrastructure.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AbstractEntityVersion.class)
public abstract class AbstractEntityVersion_ {

	public static volatile SingularAttribute<AbstractEntityVersion, Date> dataUltimaAlteracao;
	public static volatile SingularAttribute<AbstractEntityVersion, Integer> version;
	public static volatile SingularAttribute<AbstractEntityVersion, Date> dataCriacao;

	public static final String DATAULTIMAALTERACAO = "dataUltimaAlteracao";
	public static final String VERSION = "version";
	public static final String DATACRIACAO = "dataCriacao";

}

