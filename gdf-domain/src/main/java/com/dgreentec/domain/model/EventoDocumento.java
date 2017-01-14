package com.dgreentec.domain.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.dgreentec.infrastructure.model.SchemaEnum;

public class EventoDocumento implements Serializable {

	private static final long serialVersionUID = 2339831660676660060L;

	public EventoDocumento() {
		super();
	}

	public EventoDocumento(SchemaEnum schema, Serializable jaxbObject, String xml, Long nsu) {
		super();
		this.schema = schema;
		this.jaxbObject = jaxbObject;
		this.xml = xml;
		this.nsu = nsu;
	}

	private Long nsu;

	private SchemaEnum schema;

	private Serializable jaxbObject;

	private String xml;

	public SchemaEnum getSchema() {
		return schema;
	}

	public void setSchema(SchemaEnum schema) {
		this.schema = schema;
	}

	public Serializable getJaxbObject() {
		return jaxbObject;
	}

	public void setJaxbObject(Serializable jaxbObject) {
		this.jaxbObject = jaxbObject;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public Long getNsu() {
		return nsu;
	}

	public String getNsuFormatado() {
		String pNsu = StringUtils.leftPad(String.valueOf(nsu), 15, "0");
		return pNsu;
	}

	public void setNsu(String nsu) {
		this.nsu = NumberUtils.toLong(nsu);
	}
}
