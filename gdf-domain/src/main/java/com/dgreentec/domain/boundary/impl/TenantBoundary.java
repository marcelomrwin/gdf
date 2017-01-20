package com.dgreentec.domain.boundary.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import com.dgreentec.domain.boundary.api.TenantService;
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.repository.TenantRepository;
import com.dgreentec.domain.repository.filter.FiltroTenant;
import com.dgreentec.infrastructure.interceptor.EJBTransactionInterceptor;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.infrastructure.repository.multitenant.TenantInterceptor;
import com.dgreentec.infrastructure.repository.multitenant.TenantRegistry;
import com.dgreentec.infrastructure.service.boundary.AbstractBoundary;

@Stateless
@Interceptors({ TenantInterceptor.class, EJBTransactionInterceptor.class })
public class TenantBoundary extends AbstractBoundary implements TenantService {

	@Inject
	private TenantRepository tenantRepository;

	@Inject
	private TenantRegistry tenantRegistry;

	public PagedList<Tenant> consultarTenants(FiltroTenant filtro) {
		return tenantRepository.consultar(filtro);
	}

	public Tenant adicionarTenant(Tenant pTenant) {
		Tenant tenant = tenantRepository.adicionar(pTenant);
		tenantRegistry.registerTenant(tenant);
		return tenant;
	}

	public Tenant atualizarTenant(Tenant pTenant) {
		return tenantRepository.atualizar(pTenant);
	}

	public void removerTenant(Tenant pTenant) {
		tenantRepository.excluir(pTenant);
	}

	public Tenant consultarTenantPorIdTenant(Long idTenant) {
		return tenantRepository.consultarPorChave(idTenant);
	}

}
