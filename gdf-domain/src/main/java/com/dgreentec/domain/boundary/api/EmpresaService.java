package com.dgreentec.domain.boundary.api;

import javax.ejb.Local;

import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.Empresa;
import com.dgreentec.domain.repository.filter.FiltroEmpresa;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@Local
public interface EmpresaService {

	PagedList<Empresa> consultarEmpresas(Contrato contrato, FiltroEmpresa filtro);

	Empresa adicionarEmpresa(Contrato contrato, Empresa pEmpresa);

	Empresa atualizarEmpresa(Contrato contrato, Empresa pEmpresa);

	void removerEmpresa(Contrato contrato, Empresa pEmpresa);

	Empresa consultarEmpresaPorCnpj(Contrato contrato, String cnpj);

}
