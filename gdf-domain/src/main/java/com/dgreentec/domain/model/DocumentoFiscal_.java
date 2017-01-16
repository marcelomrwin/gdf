package com.dgreentec.domain.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DocumentoFiscal.class)
public abstract class DocumentoFiscal_ extends com.dgreentec.infrastructure.model.AbstractEntityVersion_ {

	public static volatile SingularAttribute<DocumentoFiscal, TipoDocumentoEnum> tipoDocumento;
	public static volatile SingularAttribute<DocumentoFiscal, String> numDocumento;
	public static volatile SingularAttribute<DocumentoFiscal, Long> idDocumento;
	public static volatile SingularAttribute<DocumentoFiscal, String> documento;
	public static volatile SingularAttribute<DocumentoFiscal, Empresa> empresa;

	public static final String TIPODOCUMENTO = "tipoDocumento";
	public static final String NUMDOCUMENTO = "numDocumento";
	public static final String IDDOCUMENTO = "idDocumento";
	public static final String DOCUMENTO = "documento";
	public static final String EMPRESA = "empresa";

}

