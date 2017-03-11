package com.dgreentec.test.unity;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.dgreentec.domain.model.EventoNSU;
import com.dgreentec.domain.model.EventoNSU_;
import com.dgreentec.domain.model.LogEventoNotificacao;
import com.dgreentec.domain.model.LogEventoNotificacao_;
import com.dgreentec.domain.model.LoteEvento;
import com.dgreentec.domain.model.LoteEvento_;
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.model.Tenant_;
import com.dgreentec.domain.model.Usuario;
import com.dgreentec.domain.model.Usuario_;
import com.dgreentec.infrastructure.crud.AbstractCrudGen;
import com.dgreentec.infrastructure.model.AbstractEntityVersion;

public class CrudGen extends AbstractCrudGen {

	@Test
	public void generateService() {

		System.out.println(new Date() + " - begin");
		// Classe do modelo que será usada para gerar artefatos
		Class<? extends AbstractEntityVersion> clazz = EventoNSU.class;
		try {
			prepare();
			// gera as classes e filtro para os campos abaixo
			generate(clazz, EventoNSU_.IDEVENTONSU, EventoNSU_.DTNSU, EventoNSU_.SCHEMA, EventoNSU_.IDNSU);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println(new Date() + " - end");
		}
	}

	private void prepare() throws IOException {
		// cria as pastas de saída
		outFolder.mkdirs();

		// limpa todos os arquivos da pasta de saída
		File[] files = outFolder.listFiles();
		for (File file : files) {
			FileUtils.forceDelete(file);
		}
	}

}
