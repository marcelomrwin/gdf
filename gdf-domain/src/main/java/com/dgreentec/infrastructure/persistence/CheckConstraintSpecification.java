package com.dgreentec.infrastructure.persistence;

import java.io.Serializable;
import java.util.List;

import com.dgreentec.infrastructure.model.AbstractEntityVersion;

public interface CheckConstraintSpecification {

	boolean validate(List<Serializable> result, AbstractEntityVersion entity);
}
