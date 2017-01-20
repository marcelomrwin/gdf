package com.dgreentec.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dgreentec.infrastructure.model.AbstractEntityBuilder;
import com.dgreentec.infrastructure.model.AbstractEntityVersion;

@Entity
@Table(name = "T_LOG_EVENTO_NOTIF")
public class LogEventoNotificacao extends AbstractEntityVersion {

	private static final long serialVersionUID = -1323898133587715383L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_EVENTO_SEQ_GEN")
	@SequenceGenerator(name = "LOG_EVENTO_SEQ_GEN", sequenceName = "SEQ_ID_LOG_EVENTO", allocationSize = 1)
	@Column(name = "ID_SEQ_LOG_EVENTO", updatable = false)
	private Long idLogEvento;

	@Column(name = "IND_TP_AMBIENTE")
	protected Integer tpAmb;

	@Column(name = "TXT_VERSAO_APP", length = 100)
	protected String verAplic;

	@Column(name = "COD_UF_ORGAO")
	protected Integer cOrgao;

	@Column(name = "COD_STATUS")
	protected Integer cStat;

	@Column(name = "TXT_MOTIVO", length = 255)
	protected String xMotivo;

	@Column(name = "TXT_CHAVE_NFE", length = 44)
	protected String chNFe;

	@Column(name = "COD_TP_EVENTO")
	protected Integer tpEvento;

	@Column(name = "TXT_EVENTO")
	protected String xEvento;

	@Column(name = "NUM_SEQ_EVENTO")
	protected Integer nSeqEvento;

	@Column(name = "COD_CNPJ_DEST", length = 14)
	protected String cnpjDest;

	@Column(name = "DT_REGISTRO")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date dhRegEvento;

	@Column(name = "COD_PROTOCOLO", length = 15)
	protected String nProt;

	public Long getIdLogEvento() {
		return idLogEvento;
	}

	public void setIdLogEvento(Long idLogEvento) {
		this.idLogEvento = idLogEvento;
	}

	public Integer getTpAmb() {
		return tpAmb;
	}

	public void setTpAmb(Integer tpAmb) {
		this.tpAmb = tpAmb;
	}

	public String getVerAplic() {
		return verAplic;
	}

	public void setVerAplic(String verAplic) {
		this.verAplic = verAplic;
	}

	public Integer getcOrgao() {
		return cOrgao;
	}

	public void setcOrgao(Integer cOrgao) {
		this.cOrgao = cOrgao;
	}

	public Integer getcStat() {
		return cStat;
	}

	public void setcStat(Integer cStat) {
		this.cStat = cStat;
	}

	public String getxMotivo() {
		return xMotivo;
	}

	public void setxMotivo(String xMotivo) {
		this.xMotivo = xMotivo;
	}

	public String getChNFe() {
		return chNFe;
	}

	public void setChNFe(String chNFe) {
		this.chNFe = chNFe;
	}

	public Integer getTpEvento() {
		return tpEvento;
	}

	public void setTpEvento(Integer tpEvento) {
		this.tpEvento = tpEvento;
	}

	public String getxEvento() {
		return xEvento;
	}

	public void setxEvento(String xEvento) {
		this.xEvento = xEvento;
	}

	public Integer getnSeqEvento() {
		return nSeqEvento;
	}

	public void setnSeqEvento(Integer nSeqEvento) {
		this.nSeqEvento = nSeqEvento;
	}

	public String getCnpjDest() {
		return cnpjDest;
	}

	public void setCnpjDest(String cnpjDest) {
		this.cnpjDest = cnpjDest;
	}

	public Date getDhRegEvento() {
		return dhRegEvento;
	}

	public void setDhRegEvento(Date dhRegEvento) {
		this.dhRegEvento = dhRegEvento;
	}

	public String getnProt() {
		return nProt;
	}

	public void setnProt(String nProt) {
		this.nProt = nProt;
	}

	public static class Builder extends AbstractEntityBuilder<LogEventoNotificacao> {

		public Builder comTpEvento(Integer pTpEvento) {
			this.entity.setTpEvento(pTpEvento);
			return this;
		}

		public Builder comDhRegEvento(Date pDhRegEvento) {
			this.entity.setDhRegEvento(pDhRegEvento);
			return this;
		}

		public Builder comXEvento(String pXEvento) {
			this.entity.setxEvento(pXEvento);
			return this;
		}

		public Builder comCOrgao(Integer pCOrgao) {
			this.entity.setcOrgao(pCOrgao);
			return this;
		}

		public Builder comCnpjDest(String pCnpjDest) {
			this.entity.setCnpjDest(pCnpjDest);
			return this;
		}

		public Builder comTpAmb(Integer pTpAmb) {
			this.entity.setTpAmb(pTpAmb);
			return this;
		}

		public Builder comCStat(Integer pCStat) {
			this.entity.setcStat(pCStat);
			return this;
		}

		public Builder comXMotivo(String pXMotivo) {
			this.entity.setxMotivo(pXMotivo);
			return this;
		}

		public Builder comChNFe(String pChNFe) {
			this.entity.setChNFe(pChNFe);
			return this;
		}

		public Builder comNSeqEvento(Integer pNSeqEvento) {
			this.entity.setnSeqEvento(pNSeqEvento);
			return this;
		}

		public Builder comVerAplic(String pVerAplic) {
			this.entity.setVerAplic(pVerAplic);
			return this;
		}

		public Builder comIdLogEvento(Long pIdLogEvento) {
			this.entity.setIdLogEvento(pIdLogEvento);
			return this;
		}

		public Builder comNProt(String pNProt) {
			this.entity.setnProt(pNProt);
			return this;
		}

	}

}
