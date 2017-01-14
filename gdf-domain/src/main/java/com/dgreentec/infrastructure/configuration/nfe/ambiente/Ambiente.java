package com.dgreentec.infrastructure.configuration.nfe.ambiente;

import java.io.Serializable;
import com.dgreentec.infrastructure.configuration.nfe.services.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Ambiente")
@XmlAccessorType(XmlAccessType.FIELD)
public class Ambiente implements Serializable {

	private static final long serialVersionUID = -876408072597973269L;

	@XmlAttribute(name = "tpAmb")
	private String tpAmb;

	@XmlElement(required = false, name = "NfeAutorizacao")
	private NfeAutorizacao NfeAutorizacao;

	@XmlElement(required = false, name = "NfeConsultaCadastro")
	private NfeConsultaCadastro NfeConsultaCadastro;

	@XmlElement(required = false, name = "NfeConsultaProtocolo")
	private NfeConsultaProtocolo NfeConsultaProtocolo;

	@XmlElement(required = false, name = "NfeInutilizacao")
	private NfeInutilizacao NfeInutilizacao;

	@XmlElement(required = false, name = "NFeRetAutorizacao")
	private NFeRetAutorizacao NFeRetAutorizacao;

	@XmlElement(required = false, name = "NfeStatusServico")
	private NfeStatusServico NfeStatusServico;

	@XmlElement(required = false, name = "RecepcaoEvento")
	private RecepcaoEvento RecepcaoEvento;

	@XmlElement(required = false, name = "NfeConsultaDest")
	private NfeConsultaDest NfeConsultaDest;

	@XmlElement(required = false, name = "NfeDownloadNF")
	private NfeDownloadNF NfeDownloadNF;

	@XmlElement(required = false, name = "NFeDistribuicaoDFe")
	private NFeDistribuicaoDFe NFeDistribuicaoDFe;

	public String getTpAmb() {
		return tpAmb;
	}

	public void setTpAmb(String tpAmb) {
		this.tpAmb = tpAmb;
	}

	public NfeAutorizacao getNfeAutorizacao() {
		return NfeAutorizacao;
	}

	public void setNfeAutorizacao(NfeAutorizacao nfeAutorizacao) {
		NfeAutorizacao = nfeAutorizacao;
	}

	public NfeConsultaCadastro getNfeConsultaCadastro() {
		return NfeConsultaCadastro;
	}

	public void setNfeConsultaCadastro(NfeConsultaCadastro nfeConsultaCadastro) {
		NfeConsultaCadastro = nfeConsultaCadastro;
	}

	public NfeConsultaProtocolo getNfeConsultaProtocolo() {
		return NfeConsultaProtocolo;
	}

	public void setNfeConsultaProtocolo(NfeConsultaProtocolo nfeConsultaProtocolo) {
		NfeConsultaProtocolo = nfeConsultaProtocolo;
	}

	public NfeInutilizacao getNfeInutilizacao() {
		return NfeInutilizacao;
	}

	public void setNfeInutilizacao(NfeInutilizacao nfeInutilizacao) {
		NfeInutilizacao = nfeInutilizacao;
	}

	public NFeRetAutorizacao getNFeRetAutorizacao() {
		return NFeRetAutorizacao;
	}

	public void setNFeRetAutorizacao(NFeRetAutorizacao nFeRetAutorizacao) {
		NFeRetAutorizacao = nFeRetAutorizacao;
	}

	public NfeStatusServico getNfeStatusServico() {
		return NfeStatusServico;
	}

	public void setNfeStatusServico(NfeStatusServico nfeStatusServico) {
		NfeStatusServico = nfeStatusServico;
	}

	public RecepcaoEvento getRecepcaoEvento() {
		return RecepcaoEvento;
	}

	public void setRecepcaoEvento(RecepcaoEvento recepcaoEvento) {
		RecepcaoEvento = recepcaoEvento;
	}

	public NfeConsultaDest getNfeConsultaDest() {
		return NfeConsultaDest;
	}

	public void setNfeConsultaDest(NfeConsultaDest nfeConsultaDest) {
		NfeConsultaDest = nfeConsultaDest;
	}

	public NfeDownloadNF getNfeDownloadNF() {
		return NfeDownloadNF;
	}

	public void setNfeDownloadNF(NfeDownloadNF nfeDownloadNF) {
		NfeDownloadNF = nfeDownloadNF;
	}

	public NFeDistribuicaoDFe getNFeDistribuicaoDFe() {
		return NFeDistribuicaoDFe;
	}

	public void setNFeDistribuicaoDFe(NFeDistribuicaoDFe nFeDistribuicaoDFe) {
		NFeDistribuicaoDFe = nFeDistribuicaoDFe;
	}
}
