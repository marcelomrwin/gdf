package com.dgreentec.infrastructure.model;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AbstractEntityVersion.class)
public abstract class AbstractEntityVersion_ {

	public static volatile SingularAttribute<AbstractEntityVersion, LocalDateTime> dataUltimaAlteracao;
	public static volatile SingularAttribute<AbstractEntityVersion, Integer> version;
	public static volatile SingularAttribute<AbstractEntityVersion, LocalDateTime> dataCriacao;

	public static final String DATAULTIMAALTERACAO = "dataUltimaAlteracao";
	public static final String VERSION = "version";
	public static final String DATACRIACAO = "dataCriacao";

}

