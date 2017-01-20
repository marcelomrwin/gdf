package com.dgreentec.domain.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Tenant.class)
public abstract class Tenant_ extends com.dgreentec.infrastructure.model.AbstractEntityVersion_ {

	public static volatile SingularAttribute<Tenant, Long> idTenant;
	public static volatile SingularAttribute<Tenant, String> nomeTenant;
	public static volatile SingularAttribute<Tenant, String> schemaName;
	public static volatile SetAttribute<Tenant, Usuario> usuarios;

	public static final String IDTENANT = "idTenant";
	public static final String NOMETENANT = "nomeTenant";
	public static final String SCHEMANAME = "schemaName";
	public static final String USUARIOS = "usuarios";

}

