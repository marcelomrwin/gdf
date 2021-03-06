package com.dgreentec.domain.model;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PersistenceException;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import com.dgreentec.infrastructure.exception.NfeException;
import com.dgreentec.infrastructure.model.AbstractEntityBuilder;
import com.dgreentec.infrastructure.model.AbstractEntityVersion;

@Entity
@Table(name = "T_CERTIFICADO")
@Cacheable
public class Certificado extends AbstractEntityVersion {

	private static final long serialVersionUID = 8024294767722480965L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CERTIFICADO_SEQ_GEN")
	@SequenceGenerator(name = "CERTIFICADO_SEQ_GEN", sequenceName = "SEQ_ID_CERTIFICADO", allocationSize = 1)
	@Column(name = "ID_CERTIFICADO", updatable = false)
	private Long idCertificado;

	@Lob
	@Column(nullable = false, name = "BIN_ARQUIVO")
	@NotNull
	@Type(type = "org.hibernate.type.BinaryType")
	private byte[] arquivo;

	@NotEmpty
	@Column(length = 300, nullable = false, name = "TXT_SENHA")
	private String senha;

	@Transient
	private transient X509Certificate cert;

	@Transient
	private transient KeyStore ks;

	@Column(name = "DT_VENCIMENTO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataVencimento;

	@PrePersist
	@PreUpdate
	public void configure() {
		try {
			setDataVencimento(validade());
		} catch (NfeException e) {
			throw new PersistenceException(e);
		}
	}

	private void loadKeystore() throws Exception {
		if (ks == null) {
			ks = KeyStore.getInstance("PKCS12");
			ks.load(new ByteArrayInputStream(arquivo), senha.toCharArray());
		}
	}

	public String alias() throws Exception {
		loadKeystore();
		Enumeration<String> aliasEnum = ks.aliases();
		String aliasKey = (String) aliasEnum.nextElement();
		return aliasKey;
	}

	private void loadCert() throws Exception {
		if (cert == null) {
			String aliasKey = alias();
			if (StringUtils.isNotEmpty(aliasKey)) {
				KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(aliasKey,
						new KeyStore.PasswordProtection(senha.toCharArray()));

				cert = (X509Certificate) pkEntry.getCertificate();
			} else {
				cert = (X509Certificate) ks.getCertificate("");
			}
		}
	}

	public Date validade() throws NfeException {
		Date data = new Date();
		try {
			loadCert();

			if (cert == null) {
				return null;
			}
			data = cert.getNotAfter();
		} catch (Exception e) {
			throw new NfeException("Erro ao Pegar Data Certificado:" + e.getMessage());
		}
		return data;
	}

	// Retorna Os dias Restantes do Certificado Digital
	public Long diasRestantes() throws NfeException {
		Date data = validade();
		if (data == null) {
			return null;
		}
		long differenceMilliSeconds = data.getTime() - new Date().getTime();
		return differenceMilliSeconds / 1000 / 60 / 60 / 24;
	}

	// retorna True se o Certificado for validao
	public boolean valido() throws NfeException {
		if (validade() != null && validade().after(new Date())) {
			return true;
		} else {
			return false;
		}
	}

	public X509Certificate getX509Certificate() throws Exception {
		loadKeystore();
		Certificate ksCert = ks.getCertificate(alias());
		cert = (X509Certificate) ksCert;
		return cert;
	}

	public PrivateKey getPrivateKey() throws Exception {
		loadKeystore();
		PrivateKey privateKey = (PrivateKey) ks.getKey(alias(), senha.toCharArray());
		return privateKey;
	}

	public KeyInfo getKeyInfo(XMLSignatureFactory signatureFactory) throws Exception {
		List<X509Certificate> x509Content = new ArrayList<X509Certificate>();
		KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
		x509Content.add(getX509Certificate());
		X509Data x509Data = keyInfoFactory.newX509Data(x509Content);
		KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));
		return keyInfo;
	}

	public Long getIdCertificado() {
		return idCertificado;
	}

	public void setIdCertificado(Long idCertificado) {
		this.idCertificado = idCertificado;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public static class Builder extends AbstractEntityBuilder<Certificado> {

		public Builder comArquivo(byte[] pArquivo) {
			this.entity.setArquivo(pArquivo);
			return this;
		}

		public Builder comIdCertificado(Long pIdCertificado) {
			this.entity.setIdCertificado(pIdCertificado);
			return this;
		}

		public Builder comSenha(String pSenha) {
			this.entity.setSenha(pSenha);
			return this;
		}

	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(arquivo);
		result = prime * result + ((dataVencimento == null) ? 0 : dataVencimento.hashCode());
		result = prime * result + ((idCertificado == null) ? 0 : idCertificado.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
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
		if (!(obj instanceof Certificado)) {
			return false;
		}
		Certificado other = (Certificado) obj;
		if (!Arrays.equals(arquivo, other.arquivo)) {
			return false;
		}
		if (dataVencimento == null) {
			if (other.dataVencimento != null) {
				return false;
			}
		} else if (!dataVencimento.equals(other.dataVencimento)) {
			return false;
		}
		if (idCertificado == null) {
			if (other.idCertificado != null) {
				return false;
			}
		} else if (!idCertificado.equals(other.idCertificado)) {
			return false;
		}
		if (senha == null) {
			if (other.senha != null) {
				return false;
			}
		} else if (!senha.equals(other.senha)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder2 = new StringBuilder();
		builder2.append("Certificado [");
		if (idCertificado != null)
			builder2.append("idCertificado=").append(idCertificado).append(", ");
		if (arquivo != null)
			builder2.append("arquivo=").append(Arrays.toString(Arrays.copyOf(arquivo, Math.min(arquivo.length, maxLen)))).append(", ");
		if (senha != null)
			builder2.append("senha=").append(senha).append(", ");
		if (cert != null)
			builder2.append("cert=").append(cert.getSubjectDN()).append(", ");
		if (dataVencimento != null)
			builder2.append("dataVencimento=").append(dataVencimento);
		builder2.append("]");
		return builder2.toString();
	}

}
