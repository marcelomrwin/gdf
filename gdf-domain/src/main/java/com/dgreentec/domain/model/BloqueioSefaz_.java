package com.dgreentec.domain.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BloqueioSefaz.class)
public abstract class BloqueioSefaz_ {

	public static volatile SingularAttribute<BloqueioSefaz, Date> dtExpiracao;
	public static volatile SingularAttribute<BloqueioSefaz, String> textoBloqueio;
	public static volatile SingularAttribute<BloqueioSefaz, Date> dtCadastroBloqueio;

	public static final String DTEXPIRACAO = "dtExpiracao";
	public static final String TEXTOBLOQUEIO = "textoBloqueio";
	public static final String DTCADASTROBLOQUEIO = "dtCadastroBloqueio";

}

