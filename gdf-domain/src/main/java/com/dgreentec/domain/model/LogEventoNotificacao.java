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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cOrgao == null) ? 0 : cOrgao.hashCode());
		result = prime * result + ((cStat == null) ? 0 : cStat.hashCode());
		result = prime * result + ((chNFe == null) ? 0 : chNFe.hashCode());
		result = prime * result + ((dhRegEvento == null) ? 0 : dhRegEvento.hashCode());
		result = prime * result + ((idLogEvento == null) ? 0 : idLogEvento.hashCode());
		result = prime * result + ((nProt == null) ? 0 : nProt.hashCode());
		result = prime * result + ((nSeqEvento == null) ? 0 : nSeqEvento.hashCode());
		result = prime * result + ((tpAmb == null) ? 0 : tpAmb.hashCode());
		result = prime * result + ((tpEvento == null) ? 0 : tpEvento.hashCode());
		result = prime * result + ((verAplic == null) ? 0 : verAplic.hashCode());
		result = prime * result + ((xEvento == null) ? 0 : xEvento.hashCode());
		result = prime * result + ((xMotivo == null) ? 0 : xMotivo.hashCode());
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
		if (!(obj instanceof LogEventoNotificacao)) {
			return false;
		}
		LogEventoNotificacao other = (LogEventoNotificacao) obj;
		if (cOrgao == null) {
			if (other.cOrgao != null) {
				return false;
			}
		} else if (!cOrgao.equals(other.cOrgao)) {
			return false;
		}
		if (cStat == null) {
			if (other.cStat != null) {
				return false;
			}
		} else if (!cStat.equals(other.cStat)) {
			return false;
		}
		if (chNFe == null) {
			if (other.chNFe != null) {
				return false;
			}
		} else if (!chNFe.equals(other.chNFe)) {
			return false;
		}
		if (dhRegEvento == null) {
			if (other.dhRegEvento != null) {
				return false;
			}
		} else if (!dhRegEvento.equals(other.dhRegEvento)) {
			return false;
		}
		if (idLogEvento == null) {
			if (other.idLogEvento != null) {
				return false;
			}
		} else if (!idLogEvento.equals(other.idLogEvento)) {
			return false;
		}
		if (nProt == null) {
			if (other.nProt != null) {
				return false;
			}
		} else if (!nProt.equals(other.nProt)) {
			return false;
		}
		if (nSeqEvento == null) {
			if (other.nSeqEvento != null) {
				return false;
			}
		} else if (!nSeqEvento.equals(other.nSeqEvento)) {
			return false;
		}
		if (tpAmb == null) {
			if (other.tpAmb != null) {
				return false;
			}
		} else if (!tpAmb.equals(other.tpAmb)) {
			return false;
		}
		if (tpEvento == null) {
			if (other.tpEvento != null) {
				return false;
			}
		} else if (!tpEvento.equals(other.tpEvento)) {
			return false;
		}
		if (verAplic == null) {
			if (other.verAplic != null) {
				return false;
			}
		} else if (!verAplic.equals(other.verAplic)) {
			return false;
		}
		if (xEvento == null) {
			if (other.xEvento != null) {
				return false;
			}
		} else if (!xEvento.equals(other.xEvento)) {
			return false;
		}
		if (xMotivo == null) {
			if (other.xMotivo != null) {
				return false;
			}
		} else if (!xMotivo.equals(other.xMotivo)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder2 = new StringBuilder();
		builder2.append("LogEventoNotificacao [");
		if (idLogEvento != null)
			builder2.append("idLogEvento=").append(idLogEvento).append(", ");
		if (tpAmb != null)
			builder2.append("tpAmb=").append(tpAmb).append(", ");
		if (verAplic != null)
			builder2.append("verAplic=").append(verAplic).append(", ");
		if (cOrgao != null)
			builder2.append("cOrgao=").append(cOrgao).append(", ");
		if (cStat != null)
			builder2.append("cStat=").append(cStat).append(", ");
		if (xMotivo != null)
			builder2.append("xMotivo=").append(xMotivo).append(", ");
		if (chNFe != null)
			builder2.append("chNFe=").append(chNFe).append(", ");
		if (tpEvento != null)
			builder2.append("tpEvento=").append(tpEvento).append(", ");
		if (xEvento != null)
			builder2.append("xEvento=").append(xEvento).append(", ");
		if (nSeqEvento != null)
			builder2.append("nSeqEvento=").append(nSeqEvento).append(", ");
		if (dhRegEvento != null)
			builder2.append("dhRegEvento=").append(dhRegEvento).append(", ");
		if (nProt != null)
			builder2.append("nProt=").append(nProt);
		builder2.append("]");
		return builder2.toString();
	}

}
