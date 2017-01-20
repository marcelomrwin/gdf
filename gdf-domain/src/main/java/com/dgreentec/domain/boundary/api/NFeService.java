package com.dgreentec.domain.boundary.api;

import java.util.List;

import javax.ejb.Local;

import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.Empresa;
import com.dgreentec.domain.model.EventoDocumento;
import com.dgreentec.domain.model.TipoAmbienteEnum;
import com.dgreentec.infrastructure.exception.NfeException;

@Local
public interface NFeService {

	void processarEventosPorContrato(Contrato contrato, TipoAmbienteEnum ambiente) throws NfeException;

	void processarEventosPorEmpresa(Contrato contrato, Empresa empresa, TipoAmbienteEnum ambiente) throws NfeException;

	void manifestarEventosCienciaDocumentoResumo(Contrato contrato, List<EventoDocumento> eventos, Empresa empresa,
			TipoAmbienteEnum ambiente) throws NfeException;
}
