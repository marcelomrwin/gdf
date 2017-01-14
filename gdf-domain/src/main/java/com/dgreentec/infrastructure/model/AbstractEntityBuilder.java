package com.dgreentec.infrastructure.model;

import java.lang.reflect.ParameterizedType;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Classe de apoio implementando o padrão Builder. Esta classe deve ser
 * extendida internamente pelas entidades, exemplo:<br/>
 * <code>
 * public class <b>A</b> extends AbstractEntityVersion{<br/><br/>
 * 		&nbsp;&nbsp;public static class Builder extends AbstractEntityBuilder&lt;<b>A</b>&gt; {<br/><br/>
 * &nbsp;&nbsp;&nbsp;public Builder comAtributo(@NotNull Atributo <i>pAtributo</i>) {<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;this.entity.setAtributo(pAtributo);<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;return this;<br/>
 * &nbsp;&nbsp;&nbsp;}<br/><br/>
 * &nbsp;&nbsp;}<br/>
 * }
 * </code>
 *
 * @author marcelo.sales
 * @param <T>
 */
public abstract class AbstractEntityBuilder<T extends AbstractEntityVersion> {

	protected Class<T> entityClass;

	protected T entity;

	@SuppressWarnings("unchecked")
	public AbstractEntityBuilder() {
		super();
		entityClass = ((Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		try {
			this.entity = entityClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public final T build(boolean validate) throws ConstraintViolationException {
		if (validate) {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();
			Set<ConstraintViolation<T>> violations = validator.validate(this.entity);
			if (!violations.isEmpty()) {
				throw new ConstraintViolationException(violations);
			}
		}
		return this.entity;
	}

}
