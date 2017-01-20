package com.dgreentec.domain.repository;
import com.dgreentec.infrastructure.persistence.ModelRepositoryJPA;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.repository.filter.FiltroTenant;

public interface TenantRepository extends ModelRepositoryJPA<Tenant> {

PagedList<Tenant> consultar(FiltroTenant filtro);

}
