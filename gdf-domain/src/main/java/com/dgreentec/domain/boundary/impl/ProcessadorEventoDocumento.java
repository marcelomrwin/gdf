package com.dgreentec.domain.boundary.impl;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

import com.dgreentec.domain.model.TipoAmbienteEnum;

@Qualifier
@Documented
@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD, PARAMETER })
public @interface ProcessadorEventoDocumento {

	@Nonbinding
	long idContrato() default 0;

	@Nonbinding
	String cnpj() default "";

	@Nonbinding
	TipoAmbienteEnum ambiente() default TipoAmbienteEnum.HOMOLOGACAO;
}
