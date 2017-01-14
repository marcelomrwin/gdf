package com.dgreentec.domain.repository;

import com.dgreentec.domain.model.Empresa;
import com.dgreentec.domain.model.EventoNSU;
import com.dgreentec.domain.repository.filter.FiltroEmpresa;
import com.dgreentec.infrastructure.persistence.ModelRepositoryJPA;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

public interface EmpresaRepository extends ModelRepositoryJPA<Empresa> {

	PagedList<Empresa> consultar(FiltroEmpresa filtro);

	EventoNSU consultarUltimoNSUParaEmpresa(String cnpj);

}
