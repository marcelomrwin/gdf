package com.dgreentec.infrastructure.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface DomainObject extends Serializable {

	LocalDateTime getDataUltimaAlteracao();

	LocalDateTime getDataCriacao();

}
