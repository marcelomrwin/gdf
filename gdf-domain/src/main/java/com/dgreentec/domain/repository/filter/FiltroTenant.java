package com.dgreentec.domain.repository.filter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.model.Tenant_;
import com.dgreentec.infrastructure.persistence.filter.FiltroAbstrato;

@SuppressWarnings({ "rawtypes" })
public class FiltroTenant extends FiltroAbstrato<Tenant> {

	private static final long serialVersionUID = -6910485508887671111L;

	private Long idTenant;

	private boolean idTenantAlterado;

	public Long getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(Long idTenant) {
		updateProperty(Tenant_.IDTENANT, idTenant);
	}

	public boolean isIdTenantAlterado() {
		return idTenantAlterado;
	}

	private String nomeTenant;

	private boolean nomeTenantAlterado;

	public String getNomeTenant() {
		return nomeTenant;
	}

	public void setNomeTenant(String nomeTenant) {
		updateProperty(Tenant_.NOMETENANT, nomeTenant);
	}

	public boolean isNomeTenantAlterado() {
		return nomeTenantAlterado;
	}

	private String schemaName;

	private boolean schemaNameAlterado;

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		updateProperty(Tenant_.SCHEMANAME, schemaName);
	}

	public boolean isSchemaNameAlterado() {
		return schemaNameAlterado;
	}

	public boolean isFiltroAlterado() {
		return isIdTenantAlterado() || isNomeTenantAlterado() || isSchemaNameAlterado();
	}

	protected void limpar() {
		setIdTenant(null);
		setNomeTenant(null);
		setSchemaName(null);
	}

	public Path<?> findPath(String field, Root<Tenant> root, Join... collection) {
		Path<?> path = root.get(Tenant_.idTenant);
		if (field != null) {
			switch (field) {
			case Tenant_.IDTENANT:
				path = root.get(Tenant_.idTenant);
				break;
			case Tenant_.SCHEMANAME:
				path = root.get(Tenant_.schemaName);
				break;
			case Tenant_.NOMETENANT:
				path = root.get(Tenant_.nomeTenant);
				break;
			}
		}
		return path;
	}

	protected Predicate configureWhereClause(CriteriaBuilder criteriaBuilder, Root<Tenant> root, Join... joins) {
		List<Predicate> ands = new ArrayList<>();

		if (isIdTenantAlterado()) {

			ands.add(criteriaBuilder.equal(root.get(Tenant_.idTenant), getIdTenant()));
		}
		if (isSchemaNameAlterado()) {
			ands.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Tenant_.schemaName)), "%" + getSchemaName().toLowerCase() + "%"));
		}
		if (isNomeTenantAlterado()) {
			ands.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Tenant_.nomeTenant)), "%" + getNomeTenant().toLowerCase() + "%"));
		}
		return criteriaBuilder.and(ands.toArray(new Predicate[ands.size()]));
	}

}
