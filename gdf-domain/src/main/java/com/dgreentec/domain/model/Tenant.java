package com.dgreentec.domain.model;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.NotEmpty;

import com.dgreentec.infrastructure.model.AbstractEntityBuilder;
import com.dgreentec.infrastructure.model.AbstractEntityVersion;

@Entity
@Table(name = "T_TENANT", schema = "comum", indexes = {
		@Index(columnList = "TXT_NM_ESQUEMA", name = "IDX_TENANT_NM", unique = true) }, uniqueConstraints = @UniqueConstraint(name = "UNQ_TENANT_NM_SCHEMA", columnNames = "TXT_NM_ESQUEMA"))
@Cacheable
public class Tenant extends AbstractEntityVersion {

	private static final long serialVersionUID = -7782770469556618757L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_TENANT")
	@SequenceGenerator(name = "SEQ_ID_TENANT", sequenceName = "ID_TENANT_SEQ", allocationSize = 1, schema = "comum")
	@Column(name = "ID_TENANT")
	private Long idTenant;

	@NotEmpty
	@Column(name = "TXT_NM_ESQUEMA", nullable = false, unique = true, updatable = false)
	private String schemaName;

	@Column(name = "TXT_NM_TENANT", nullable = false)
	private String nomeTenant;

	@ManyToMany(mappedBy = "tenants")
	@OrderBy("nome")
	private SortedSet<Usuario> usuarios = new TreeSet<>();

	public Long getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(Long idTenant) {
		this.idTenant = idTenant;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getNomeTenant() {
		return nomeTenant;
	}

	public void setNomeTenant(String nomeTenant) {
		this.nomeTenant = nomeTenant;
	}

	public static class Builder extends AbstractEntityBuilder<Tenant> {

		public Builder comIdTenant(Long pIdTenant) {
			this.entity.setIdTenant(pIdTenant);
			return this;
		}

		public Builder comSchemaName(String pSchemaName) {
			this.entity.setSchemaName(pSchemaName);
			return this;
		}

		public Builder comNomeTenant(String pNomeTenant) {
			this.entity.setNomeTenant(pNomeTenant);
			return this;
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((idTenant == null) ? 0 : idTenant.hashCode());
		result = prime * result + ((nomeTenant == null) ? 0 : nomeTenant.hashCode());
		result = prime * result + ((schemaName == null) ? 0 : schemaName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Tenant)) {
			return false;
		}
		Tenant other = (Tenant) obj;
		if (idTenant == null) {
			if (other.idTenant != null) {
				return false;
			}
		} else if (!idTenant.equals(other.idTenant)) {
			return false;
		}
		if (nomeTenant == null) {
			if (other.nomeTenant != null) {
				return false;
			}
		} else if (!nomeTenant.equals(other.nomeTenant)) {
			return false;
		}
		if (schemaName == null) {
			if (other.schemaName != null) {
				return false;
			}
		} else if (!schemaName.equals(other.schemaName)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Tenant [");
		if (idTenant != null)
			builder.append("idTenant=").append(idTenant).append(", ");
		if (schemaName != null)
			builder.append("schemaName=").append(schemaName).append(", ");
		if (nomeTenant != null)
			builder.append("nomeTenant=").append(nomeTenant).append(", ");
		if (version != null)
			builder.append("version=").append(version).append(", ");
		if (dataCriacao != null)
			builder.append("dataCriacao=").append(dataCriacao).append(", ");
		if (dataUltimaAlteracao != null)
			builder.append("dataUltimaAlteracao=").append(dataUltimaAlteracao);
		builder.append("]");
		return builder.toString();
	}

	public SortedSet<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(SortedSet<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
}
