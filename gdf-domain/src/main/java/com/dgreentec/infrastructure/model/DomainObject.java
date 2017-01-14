package com.dgreentec.infrastructure.model;

import java.io.Serializable;
import java.util.Date;

public interface DomainObject extends Serializable {

	Date getDataUltimaAlteracao();

	Date getDataCriacao();

}
