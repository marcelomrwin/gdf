package com.dgreentec.domain.boundary.api;

import javax.ejb.Local;

import com.dgreentec.domain.model.Empresa;
import com.dgreentec.domain.repository.filter.FiltroEmpresa;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@Local
public interface EmpresaService {

	PagedList<Empresa> consultarEmpresas(FiltroEmpresa filtro);

	Empresa adicionarEmpresa(Empresa pEmpresa);

	Empresa atualizarEmpresa(Empresa pEmpresa);

	void removerEmpresa(Empresa pEmpresa);

	Empresa consultarEmpresaPorCnpj(String cnpj);

}
