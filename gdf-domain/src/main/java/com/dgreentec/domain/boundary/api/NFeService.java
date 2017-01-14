package com.dgreentec.domain.boundary.api;

import java.util.List;

import javax.ejb.Local;

import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.EventoDocumento;
import com.dgreentec.domain.model.TipoAmbienteEnum;
import com.dgreentec.infrastructure.exception.NfeException;

@Local
public interface NFeService {

	List<EventoDocumento> consultarEventosPorContrato(Contrato contrato, TipoAmbienteEnum ambiente) throws NfeException;
}
