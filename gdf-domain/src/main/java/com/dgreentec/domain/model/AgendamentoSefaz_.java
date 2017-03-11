package com.dgreentec.domain.model;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AgendamentoSefaz.class)
public abstract class AgendamentoSefaz_ extends com.dgreentec.infrastructure.model.AbstractEntityVersion_ {

	public static volatile SingularAttribute<AgendamentoSefaz, LocalDateTime> proximaExecucao;
	public static volatile SingularAttribute<AgendamentoSefaz, Boolean> emExecucao;
	public static volatile SingularAttribute<AgendamentoSefaz, LocalDateTime> dtCadastroAgendamento;
	public static volatile SingularAttribute<AgendamentoSefaz, String> textoAgendamento;
	public static volatile SingularAttribute<AgendamentoSefaz, Integer> idAgendamento;
	public static volatile SingularAttribute<AgendamentoSefaz, Boolean> bloqueio;

	public static final String PROXIMAEXECUCAO = "proximaExecucao";
	public static final String EMEXECUCAO = "emExecucao";
	public static final String DTCADASTROAGENDAMENTO = "dtCadastroAgendamento";
	public static final String TEXTOAGENDAMENTO = "textoAgendamento";
	public static final String IDAGENDAMENTO = "idAgendamento";
	public static final String BLOQUEIO = "bloqueio";

}

