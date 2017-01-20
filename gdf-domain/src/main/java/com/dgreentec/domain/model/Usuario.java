package com.dgreentec.domain.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CPF;

import com.dgreentec.infrastructure.model.AbstractEntityBuilder;
import com.dgreentec.infrastructure.model.AbstractEntityVersion;

@Entity
@Table(name = "T_USUARIO", schema = "comum")
@Cacheable
public class Usuario extends AbstractEntityVersion {

	private static final long serialVersionUID = -7770156576348100980L;

	@Id
	@CPF
	@Column(name = "NUM_CPF", updatable = false)
	private String cpf;

	@NotEmpty
	@Column(name = "TXT_NOME", nullable = false)
	private String nome;

	@ManyToMany(targetEntity = Tenant.class, cascade = CascadeType.REFRESH)
	@JoinTable(name = "T_USUARIO_TENANT", joinColumns = @JoinColumn(name = "NUM_CPF"), inverseJoinColumns = @JoinColumn(name = "ID_TENANT"))
	@OrderBy("nomeTenant")
	@Size(min = 1, message = "Usuário deve estar associado a pelo menos um Tenant")
	private SortedSet<Tenant> tenants = new TreeSet<>();

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public SortedSet<Tenant> getTenants() {
		return tenants;
	}

	public void setTenants(SortedSet<Tenant> tenants) {
		this.tenants = tenants;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		if (!(obj instanceof Usuario)) {
			return false;
		}
		Usuario other = (Usuario) obj;
		if (cpf == null) {
			if (other.cpf != null) {
				return false;
			}
		} else if (!cpf.equals(other.cpf)) {
			return false;
		}
		if (nome == null) {
			if (other.nome != null) {
				return false;
			}
		} else if (!nome.equals(other.nome)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("Usuario [");
		if (cpf != null)
			builder.append("cpf=").append(cpf).append(", ");
		if (nome != null)
			builder.append("nome=").append(nome).append(", ");
		if (tenants != null)
			builder.append("tenants=").append(toString(tenants, maxLen));
		builder.append("]");
		return builder.toString();
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

	public static class Builder extends AbstractEntityBuilder<Usuario> {

		public Builder comCpf(String pCpf) {
			this.entity.setCpf(pCpf);
			return this;
		}

		public Builder comNome(String pNome) {
			this.entity.setNome(pNome);
			return this;
		}

	}

}
