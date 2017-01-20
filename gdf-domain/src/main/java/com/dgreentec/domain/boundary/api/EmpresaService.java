package com.dgreentec.domain.boundary.api;

import javax.ejb.Local;

import com.dgreentec.domain.model.Empresa;
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.repository.filter.FiltroEmpresa;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@Local
public interface EmpresaService {

	PagedList<Empresa> consultarEmpresas(Tenant tenant, FiltroEmpresa filtro);

	Empresa adicionarEmpresa(Tenant tenant, Empresa pEmpresa);

	Empresa atualizarEmpresa(Tenant tenant, Empresa pEmpresa);

	void removerEmpresa(Tenant tenant, Empresa pEmpresa);

	Empresa consultarEmpresaPorCnpj(Tenant tenant, String cnpj);

}
