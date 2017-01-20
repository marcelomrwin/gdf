package com.dgreentec.domain.boundary.api;

import javax.ejb.Local;

import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.repository.filter.FiltroTenant;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@Local
public interface TenantService {

	PagedList<Tenant> consultarTenants(FiltroTenant filtro);

	Tenant adicionarTenant(Tenant pTenant);

	Tenant atualizarTenant(Tenant pTenant);

	void removerTenant(Tenant pTenant);

	Tenant consultarTenantPorIdTenant(Long idTenant);

}
