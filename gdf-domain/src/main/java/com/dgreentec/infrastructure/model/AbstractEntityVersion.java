package com.dgreentec.infrastructure.model;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

/**
 * Classe base para as entidades
 *
 * @author Marcelo
 */

@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractEntityVersion implements DomainObject, Cloneable, Comparable<AbstractEntityVersion> {

	public AbstractEntityVersion() {
		super();
		setUid(UUID.randomUUID().toString());
	}

	private static final long serialVersionUID = -8661366632524042323L;

	/**
	 * Controla a versão das entidades evitando inconsistência por concorrência
	 */
	@Version
	@Column(name = "NUM_VERSAO", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
	protected Integer version;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "timestamp without time zone default CURRENT_TIMESTAMP", name = "DAT_CRIACAO")
	protected Date dataCriacao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "timestamp without time zone default CURRENT_TIMESTAMP", name = "DAT_ULTIMA_ALTERACAO")
	protected Date dataUltimaAlteracao;

	@Transient
	@XmlTransient
	protected String uid;

	public AbstractEntityVersion clone() {
		try {
			final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			final ObjectOutputStream objOutputStream = new ObjectOutputStream(byteArray);
			objOutputStream.writeObject(this);
			objOutputStream.close();
			final ObjectInputStream objInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArray.toByteArray()));
			final AbstractEntityVersion abstractEntity = (AbstractEntityVersion) objInputStream.readObject();
			objInputStream.close();
			return abstractEntity;
		} catch (final Throwable t) {
			throw new RuntimeException(t);
		}
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}

	protected boolean stringValida(String texto) {
		return StringUtils.isNotEmpty(texto);
	}

	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataCriacao == null) ? 0 : dataCriacao.hashCode());
		result = prime * result + ((dataUltimaAlteracao == null) ? 0 : dataUltimaAlteracao.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		try {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!this.getClass().isAssignableFrom(obj.getClass()))
				return false;
			if (!(obj instanceof AbstractEntityVersion))
				return false;
			AbstractEntityVersion other = (AbstractEntityVersion) obj;
			if (dataCriacao == null) {
				if (other.dataCriacao != null)
					return false;
			} else if (!dataCriacao.equals(other.dataCriacao))
				return false;
			if (dataUltimaAlteracao == null) {
				if (other.dataUltimaAlteracao != null)
					return false;
			} else if (!dataUltimaAlteracao.equals(other.dataUltimaAlteracao))
				return false;

			if (version == null) {
				if (other.version != null)
					return false;
			} else if (!version.equals(other.version))
				return false;
			Field id = getIdField();
			if (id != null) {
				Method getter = PropertyUtils.getReadMethod(new PropertyDescriptor(id.getName(), getClass()));
				Object pKey = getter.invoke(this);
				Object opKey = getter.invoke(other);
				if (pKey == null) {
					if (opKey != null)
						return false;
				} else if (!pKey.equals(opKey))
					return false;
			}

			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Field getIdField() {
		Field id = null;
		for (Field f : getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(Id.class)) {
				id = f;
				break;
			}
		}
		return id;
	}

	public Serializable getIdValue() {
		try {
			Field id = getIdField();
			if (id != null) {
				Method getter = PropertyUtils.getReadMethod(new PropertyDescriptor(id.getName(), getClass()));
				Object pKey = getter.invoke(this);
				if (pKey != null) {
					return (Serializable) pKey;
				}
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public int compareTo(AbstractEntityVersion o) {
		return compare(o);
	}

	protected int compare(AbstractEntityVersion o) {
		if (this.equals(o))
			return 0;
		return 1;
	}

	public Object getFieldValue(String field)
			throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method getter = PropertyUtils.getReadMethod(new PropertyDescriptor(field, getClass()));
		Object value = getter.invoke(this);
		return value;
	}

	public String printXml(boolean format) throws JAXBException, IOException {

		final JAXBContext context = JAXBContext.newInstance(getClass());
		try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			final Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			if (format)
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(this, output);

			final String jaxbEncoding = (String) marshaller.getProperty(Marshaller.JAXB_ENCODING);

			try {
				return new String(output.toByteArray(), jaxbEncoding);
			} catch (UnsupportedEncodingException uee) {
				throw new RuntimeException(uee);
			}
		}
	}

}
