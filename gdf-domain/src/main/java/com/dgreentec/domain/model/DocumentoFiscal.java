package com.dgreentec.domain.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dgreentec.infrastructure.model.AbstractEntityVersion;
import com.dgreentec.infrastructure.persistence.converter.TipoDocumentoEnumConverter;

@Entity
@Table(name = "T_DOCUMENTO_FISCAL", indexes = { @Index(name = "IDX_DOC_DOCUMENTO", columnList = "XML_DOCUMENTO"),
		@Index(name = "IDX_DOC_NUM_DOC", columnList = "NUM_DOCUMENTO") })
public class DocumentoFiscal extends AbstractEntityVersion {

	public DocumentoFiscal() {
		super();
	}

	public DocumentoFiscal(TipoDocumentoEnum tipoDocumento, String documento, String numDocumento) {
		super();
		this.tipoDocumento = tipoDocumento;
		this.documento = documento;
		this.numDocumento = numDocumento;
	}

	public DocumentoFiscal(TipoDocumentoEnum tipoDocumento, String documento, String numDocumento, Empresa empresa) {
		super();
		this.tipoDocumento = tipoDocumento;
		this.documento = documento;
		this.numDocumento = numDocumento;
		this.empresa = empresa;
	}

	private static final long serialVersionUID = -9147472477429486584L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_DOCUMENTO")
	@SequenceGenerator(name = "SEQ_ID_DOCUMENTO", sequenceName = "SEQ_ID_DOCUMENTO", allocationSize = 1)
	@Column(name = "ID_DOCUMENTO",updatable=false)
	private Long idDocumento;

	@Convert(converter = TipoDocumentoEnumConverter.class)
	@Column(name = "ID_TP_DOCUMENTO", nullable = false)
	@NotNull
	private TipoDocumentoEnum tipoDocumento;

	@NotNull
	@Column(name = "XML_DOCUMENTO", columnDefinition = "XML", length = 4000)
	private String documento;

	@Column(name = "NUM_DOCUMENTO", nullable = false, length = 50)
	private String numDocumento;

	@ManyToOne
	@JoinColumn(name = "COD_CNPJ", foreignKey = @ForeignKey(name = "FK_DOC_EMPRESA"))
	private Empresa empresa;

	public Long getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}

	public TipoDocumentoEnum getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoEnum tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
}
