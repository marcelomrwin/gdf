package com.dgreentec.domain.repository.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.dgreentec.domain.model.LogEventoNotificacao;
import com.dgreentec.domain.model.LogEventoNotificacao_;
import com.dgreentec.infrastructure.persistence.filter.FiltroAbstrato;

@SuppressWarnings({ "rawtypes" })
public class FiltroLogEventoNotificacao extends FiltroAbstrato<LogEventoNotificacao> {

	private static final long serialVersionUID = -6857193404875447079L;

	private Long idLogEvento;

	private boolean idLogEventoAlterado;

	public Long getIdLogEvento() {
		return idLogEvento;
	}

	public void setIdLogEvento(Long idLogEvento) {
		updateProperty(LogEventoNotificacao_.IDLOGEVENTO, idLogEvento);
	}

	public boolean isIdLogEventoAlterado() {
		return idLogEventoAlterado;
	}

	private String chNFe;

	private boolean chNFeAlterado;

	public String getChNFe() {
		return chNFe;
	}

	public void setChNFe(String chNFe) {
		updateProperty(LogEventoNotificacao_.CHNFE, chNFe);
	}

	public boolean isChNFeAlterado() {
		return chNFeAlterado;
	}

	private Integer cStat;

	private boolean cStatAlterado;

	public Integer getCStat() {
		return cStat;
	}

	public void setCStat(Integer cStat) {
		updateProperty(LogEventoNotificacao_.CSTAT, cStat);
	}

	public boolean isCStatAlterado() {
		return cStatAlterado;
	}

	private String cnpjDest;

	private boolean cnpjDestAlterado;

	public String getCnpjDest() {
		return cnpjDest;
	}

	public void setCnpjDest(String cnpjDest) {
		updateProperty(LogEventoNotificacao_.CNPJDEST, cnpjDest);
	}

	public boolean isCnpjDestAlterado() {
		return cnpjDestAlterado;
	}

	private Date dhRegEvento;

	private boolean dhRegEventoAlterado;

	public Date getDhRegEvento() {
		return dhRegEvento;
	}

	public void setDhRegEvento(Date dhRegEvento) {
		updateProperty(LogEventoNotificacao_.DHREGEVENTO, dhRegEvento);
	}

	public boolean isDhRegEventoAlterado() {
		return dhRegEventoAlterado;
	}

	private Integer tpEvento;

	private boolean tpEventoAlterado;

	public Integer getTpEvento() {
		return tpEvento;
	}

	public void setTpEvento(Integer tpEvento) {
		updateProperty(LogEventoNotificacao_.TPEVENTO, tpEvento);
	}

	public boolean isTpEventoAlterado() {
		return tpEventoAlterado;
	}

	private String nProt;

	private boolean nProtAlterado;

	public String getNProt() {
		return nProt;
	}

	public void setNProt(String nProt) {
		updateProperty(LogEventoNotificacao_.NPROT, nProt);
	}

	public boolean isNProtAlterado() {
		return nProtAlterado;
	}

	public boolean isFiltroAlterado() {
		return isIdLogEventoAlterado() || isChNFeAlterado() || isCStatAlterado() || isCnpjDestAlterado() || isDhRegEventoAlterado()
				|| isTpEventoAlterado() || isNProtAlterado();
	}

	protected void limpar() {
		setIdLogEvento(null);
		setChNFe(null);
		setCStat(null);
		setCnpjDest(null);
		setDhRegEvento(null);
		setTpEvento(null);
		setNProt(null);
	}

	public Path<?> findPath(String field, Root<LogEventoNotificacao> root, Join... collection) {
		Path<?> path = root.get(LogEventoNotificacao_.idLogEvento);
		if (field != null) {
			switch (field) {
			case LogEventoNotificacao_.IDLOGEVENTO:
				path = root.get(LogEventoNotificacao_.idLogEvento);
				break;
			case LogEventoNotificacao_.TPAMB:
				path = root.get(LogEventoNotificacao_.tpAmb);
				break;
			case LogEventoNotificacao_.VERAPLIC:
				path = root.get(LogEventoNotificacao_.verAplic);
				break;
			case LogEventoNotificacao_.CORGAO:
				path = root.get(LogEventoNotificacao_.cOrgao);
				break;
			case LogEventoNotificacao_.CSTAT:
				path = root.get(LogEventoNotificacao_.cStat);
				break;
			case LogEventoNotificacao_.XMOTIVO:
				path = root.get(LogEventoNotificacao_.xMotivo);
				break;
			case LogEventoNotificacao_.CHNFE:
				path = root.get(LogEventoNotificacao_.chNFe);
				break;
			case LogEventoNotificacao_.TPEVENTO:
				path = root.get(LogEventoNotificacao_.tpEvento);
				break;
			case LogEventoNotificacao_.XEVENTO:
				path = root.get(LogEventoNotificacao_.xEvento);
				break;
			case LogEventoNotificacao_.NSEQEVENTO:
				path = root.get(LogEventoNotificacao_.nSeqEvento);
				break;
			case LogEventoNotificacao_.CNPJDEST:
				path = root.get(LogEventoNotificacao_.cnpjDest);
				break;
			case LogEventoNotificacao_.DHREGEVENTO:
				path = root.get(LogEventoNotificacao_.dhRegEvento);
				break;
			case LogEventoNotificacao_.NPROT:
				path = root.get(LogEventoNotificacao_.nProt);
				break;
			}
		}
		return path;
	}

	protected Predicate configureWhereClause(CriteriaBuilder criteriaBuilder, Root<LogEventoNotificacao> root, Join... joins) {
		List<Predicate> ands = new ArrayList<>();

		if (isIdLogEventoAlterado()) {
			ands.add(criteriaBuilder.equal(root.get(LogEventoNotificacao_.idLogEvento), getIdLogEvento()));
		}
		if (isCStatAlterado()) {
			ands.add(criteriaBuilder.equal(root.get(LogEventoNotificacao_.cStat), getCStat()));
		}
		if (isChNFeAlterado()) {
			ands.add(criteriaBuilder.equal(root.get(LogEventoNotificacao_.chNFe), getChNFe()));
		}
		if (isTpEventoAlterado()) {
			ands.add(criteriaBuilder.equal(root.get(LogEventoNotificacao_.tpEvento), getTpEvento()));
		}
		if (isCnpjDestAlterado()) {
			ands.add(criteriaBuilder.equal(root.get(LogEventoNotificacao_.cnpjDest), getCnpjDest()));
		}
		if (isDhRegEventoAlterado()) {
			ands.add(criteriaBuilder.equal(root.get(LogEventoNotificacao_.dhRegEvento), getDhRegEvento()));
		}
		if (isNProtAlterado()) {
			ands.add(criteriaBuilder.equal(root.get(LogEventoNotificacao_.nProt), getNProt()));
		}
		return criteriaBuilder.and(ands.toArray(new Predicate[ands.size()]));
	}

}
