package com.dgreentec.domain.boundary.api;

import javax.ejb.Local;

import com.dgreentec.domain.model.Usuario;
import com.dgreentec.domain.repository.filter.FiltroUsuario;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@Local
public interface UsuarioService {

	PagedList<Usuario> consultarUsuarios(FiltroUsuario filtro);

	Usuario adicionarUsuario(Usuario pUsuario);

	Usuario atualizarUsuario(Usuario pUsuario);

	void removerUsuario(Usuario pUsuario);

	Usuario consultarUsuarioPorCpf(String cpf);

}
