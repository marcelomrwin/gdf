package com.dgreentec.domain.boundary.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.dgreentec.domain.boundary.api.EmpresaService;
import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.Empresa;
import com.dgreentec.domain.repository.EmpresaRepository;
import com.dgreentec.domain.repository.filter.FiltroEmpresa;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.infrastructure.service.boundary.AbstractBoundary;

@Stateless
public class EmpresaBoundary extends AbstractBoundary implements EmpresaService {

	@Inject
	private EmpresaRepository empresaRepository;

	public PagedList<Empresa> consultarEmpresas(Contrato contrato, FiltroEmpresa filtro) {
		return empresaRepository.consultar(filtro);
	}

	public Empresa adicionarEmpresa(Contrato contrato, Empresa pEmpresa) {
		return empresaRepository.adicionar(pEmpresa);
	}

	public Empresa atualizarEmpresa(Contrato contrato, Empresa pEmpresa) {
		return empresaRepository.atualizar(pEmpresa);
	}

	public void removerEmpresa(Contrato contrato, Empresa pEmpresa) {
		empresaRepository.excluir(pEmpresa);
	}

	public Empresa consultarEmpresaPorCnpj(Contrato contrato, String cnpj) {
		return empresaRepository.consultarPorChave(cnpj);
	}

}
