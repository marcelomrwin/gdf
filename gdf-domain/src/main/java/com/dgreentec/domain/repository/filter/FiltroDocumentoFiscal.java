package com.dgreentec.domain.repository.filter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.dgreentec.domain.model.DocumentoFiscal;
import com.dgreentec.domain.model.DocumentoFiscal_;
import com.dgreentec.domain.model.TipoDocumentoEnum;
import com.dgreentec.infrastructure.persistence.filter.FiltroAbstrato;

@SuppressWarnings({ "rawtypes" })
public class FiltroDocumentoFiscal extends FiltroAbstrato<DocumentoFiscal> {

	private static final long serialVersionUID = 7030890829392630141L;

	private Long idDocumento;

	private boolean idDocumentoAlterado;

	public Long getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(Long idDocumento) {
		updateProperty(DocumentoFiscal_.IDDOCUMENTO, idDocumento);
	}

	public boolean isIdDocumentoAlterado() {
		return idDocumentoAlterado;
	}

	private String numDocumento;

	private boolean numDocumentoAlterado;

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		updateProperty(DocumentoFiscal_.NUMDOCUMENTO, numDocumento);
	}

	public boolean isNumDocumentoAlterado() {
		return numDocumentoAlterado;
	}

	private TipoDocumentoEnum tipoDocumento;

	private boolean tipoDocumentoAlterado;

	public TipoDocumentoEnum getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoEnum tipoDocumento) {
		updateProperty(DocumentoFiscal_.TIPODOCUMENTO, tipoDocumento);
	}

	public boolean isTipoDocumentoAlterado() {
		return tipoDocumentoAlterado;
	}

	public boolean isFiltroAlterado() {
		return isIdDocumentoAlterado() || isNumDocumentoAlterado() || isTipoDocumentoAlterado();
	}

	protected void limpar() {
		setIdDocumento(null);
		setNumDocumento(null);
		setTipoDocumento(null);
	}

	public Path<?> findPath(String field, Root<DocumentoFiscal> root, Join... collection) {
		Path<?> path = root.get(DocumentoFiscal_.idDocumento);
		if (field != null) {
			switch (field) {
			case DocumentoFiscal_.IDDOCUMENTO:
				path = root.get(DocumentoFiscal_.idDocumento);
				break;
			case DocumentoFiscal_.TIPODOCUMENTO:
				path = root.get(DocumentoFiscal_.tipoDocumento);
				break;
			case DocumentoFiscal_.DOCUMENTO:
				path = root.get(DocumentoFiscal_.documento);
				break;
			case DocumentoFiscal_.NUMDOCUMENTO:
				path = root.get(DocumentoFiscal_.numDocumento);
				break;
			case DocumentoFiscal_.EMPRESA:
				path = root.get(DocumentoFiscal_.empresa);
				break;
			}
		}
		return path;
	}

	protected Predicate configureWhereClause(CriteriaBuilder criteriaBuilder, Root<DocumentoFiscal> root, Join... joins) {
		List<Predicate> ands = new ArrayList<>();

		if (isIdDocumentoAlterado()) {
			ands.add(criteriaBuilder.equal(root.get(DocumentoFiscal_.idDocumento), getIdDocumento()));
		}
		if (isTipoDocumentoAlterado()) {

			ands.add(criteriaBuilder.equal(root.get(DocumentoFiscal_.tipoDocumento), getTipoDocumento()));
		}

		if (isNumDocumentoAlterado()) {
			ands.add(criteriaBuilder.equal(root.get(DocumentoFiscal_.numDocumento), getNumDocumento()));
		}

		return criteriaBuilder.and(ands.toArray(new Predicate[ands.size()]));
	}

}
