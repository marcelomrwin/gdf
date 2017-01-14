package com.dgreentec.infrastructure.configuration.nfe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dgreentec.domain.model.UFEnum;
import com.dgreentec.infrastructure.configuration.nfe.ambiente.Ambiente;

@XmlRootElement(name = "UF")
@XmlAccessorType(XmlAccessType.FIELD)
public class UF implements Serializable {

	private static final long serialVersionUID = 339929062560895007L;

	@XmlAttribute
	private String sigla;

	@XmlAttribute(required = false)
	private String num;

	@XmlElement(name = "Ambiente")
	private List<Ambiente> ambientes = new ArrayList<>();

	public Ambiente getAmbienteHomologacao() {
		for (Ambiente ambiente : ambientes) {
			if (ambiente.getTpAmb().equals("2"))
				return ambiente;
		}
		throw new RuntimeException("Ambiente de homologação não encontrado");
	}

	public Ambiente getAmbienteProducao() {
		for (Ambiente ambiente : ambientes) {
			if (ambiente.getTpAmb().equals("1"))
				return ambiente;
		}
		throw new RuntimeException("Ambiente de produção não encontrado");
	}

	public UFEnum toEnum() {
		return UFEnum.valueOf(sigla.toUpperCase());
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public List<Ambiente> getAmbientes() {
		return ambientes;
	}

	public void setAmbientes(List<Ambiente> ambientes) {
		this.ambientes = ambientes;
	}
}
