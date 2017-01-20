package com.dgreentec.domain.repository;

import com.dgreentec.infrastructure.persistence.ModelRepositoryJPA;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.domain.model.Usuario;
import com.dgreentec.domain.repository.filter.FiltroUsuario;

public interface UsuarioRepository extends ModelRepositoryJPA<Usuario> {

	PagedList<Usuario> consultar(FiltroUsuario filtro);

}
