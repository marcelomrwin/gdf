package com.dgreentec.domain.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LogEventoNotificacao.class)
public abstract class LogEventoNotificacao_ extends com.dgreentec.infrastructure.model.AbstractEntityVersion_ {

	public static volatile SingularAttribute<LogEventoNotificacao, Long> idLogEvento;
	public static volatile SingularAttribute<LogEventoNotificacao, String> nProt;
	public static volatile SingularAttribute<LogEventoNotificacao, Integer> nSeqEvento;
	public static volatile SingularAttribute<LogEventoNotificacao, String> verAplic;
	public static volatile SingularAttribute<LogEventoNotificacao, String> xMotivo;
	public static volatile SingularAttribute<LogEventoNotificacao, String> chNFe;
	public static volatile SingularAttribute<LogEventoNotificacao, Integer> tpAmb;
	public static volatile SingularAttribute<LogEventoNotificacao, Integer> cStat;
	public static volatile SingularAttribute<LogEventoNotificacao, Integer> cOrgao;
	public static volatile SingularAttribute<LogEventoNotificacao, String> cnpjDest;
	public static volatile SingularAttribute<LogEventoNotificacao, String> xEvento;
	public static volatile SingularAttribute<LogEventoNotificacao, Date> dhRegEvento;
	public static volatile SingularAttribute<LogEventoNotificacao, Integer> tpEvento;

	public static final String IDLOGEVENTO = "idLogEvento";
	public static final String NPROT = "nProt";
	public static final String NSEQEVENTO = "nSeqEvento";
	public static final String VERAPLIC = "verAplic";
	public static final String XMOTIVO = "xMotivo";
	public static final String CHNFE = "chNFe";
	public static final String TPAMB = "tpAmb";
	public static final String CSTAT = "cStat";
	public static final String CORGAO = "cOrgao";
	public static final String CNPJDEST = "cnpjDest";
	public static final String XEVENTO = "xEvento";
	public static final String DHREGEVENTO = "dhRegEvento";
	public static final String TPEVENTO = "tpEvento";

}

