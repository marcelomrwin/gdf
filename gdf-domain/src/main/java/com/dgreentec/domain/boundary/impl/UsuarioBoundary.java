package com.dgreentec.domain.boundary.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import com.dgreentec.domain.boundary.api.UsuarioService;
import com.dgreentec.domain.model.Usuario;
import com.dgreentec.domain.repository.UsuarioRepository;
import com.dgreentec.domain.repository.filter.FiltroUsuario;
import com.dgreentec.infrastructure.interceptor.EJBTransactionInterceptor;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.infrastructure.repository.multitenant.TenantInterceptor;
import com.dgreentec.infrastructure.service.boundary.AbstractBoundary;

@Stateless
@Interceptors({ TenantInterceptor.class, EJBTransactionInterceptor.class })
public class UsuarioBoundary extends AbstractBoundary implements UsuarioService {

	@Inject
	private UsuarioRepository usuarioRepository;

	public PagedList<Usuario> consultarUsuarios(FiltroUsuario filtro) {
		return usuarioRepository.consultar(filtro);
	}

	public Usuario adicionarUsuario(Usuario pUsuario) {
		return usuarioRepository.adicionar(pUsuario);
	}

	public Usuario atualizarUsuario(Usuario pUsuario) {
		return usuarioRepository.atualizar(pUsuario);
	}

	public void removerUsuario(Usuario pUsuario) {
		usuarioRepository.excluir(pUsuario);
	}

	public Usuario consultarUsuarioPorCpf(String cpf) {
		return usuarioRepository.consultarPorChave(cpf);
	}

}
