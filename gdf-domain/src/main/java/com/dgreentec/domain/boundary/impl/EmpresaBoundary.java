package com.dgreentec.domain.boundary.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import com.dgreentec.domain.boundary.api.EmpresaService;
import com.dgreentec.domain.model.Empresa;
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.repository.EmpresaRepository;
import com.dgreentec.domain.repository.filter.FiltroEmpresa;
import com.dgreentec.infrastructure.interceptor.EJBTransactionInterceptor;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.infrastructure.repository.multitenant.TenantInterceptor;
import com.dgreentec.infrastructure.service.boundary.AbstractBoundary;

@Stateless
@Interceptors({ TenantInterceptor.class, EJBTransactionInterceptor.class })
public class EmpresaBoundary extends AbstractBoundary implements EmpresaService {

	@Inject
	private EmpresaRepository empresaRepository;

	public PagedList<Empresa> consultarEmpresas(Tenant tenant, FiltroEmpresa filtro) {
		return empresaRepository.consultar(filtro);
	}

	public Empresa adicionarEmpresa(Tenant tenant, Empresa pEmpresa) {
		return empresaRepository.adicionar(pEmpresa);
	}

	public Empresa atualizarEmpresa(Tenant tenant, Empresa pEmpresa) {
		return empresaRepository.atualizar(pEmpresa);
	}

	public void removerEmpresa(Tenant tenant, Empresa pEmpresa) {
		empresaRepository.excluir(pEmpresa);
	}

	public Empresa consultarEmpresaPorCnpj(Tenant tenant, String cnpj) {
		Empresa empresa = empresaRepository.consultarPorChave(cnpj);
		init(empresa);
		return empresa;
	}

	private void init(Empresa empresa) {
		empresa.getNsus().size();
	}

}
