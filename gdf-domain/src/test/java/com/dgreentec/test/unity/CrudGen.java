package com.dgreentec.test.unity;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.dgreentec.domain.model.LogEventoNotificacao;
import com.dgreentec.domain.model.LogEventoNotificacao_;
import com.dgreentec.domain.model.LoteEvento;
import com.dgreentec.domain.model.LoteEvento_;
import com.dgreentec.infrastructure.crud.AbstractCrudGen;
import com.dgreentec.infrastructure.model.AbstractEntityVersion;

public class CrudGen extends AbstractCrudGen {

	@Test
	public void generateService() {

		System.out.println(new Date() + " - begin");
		// Classe do modelo que será usada para gerar artefatos
		Class<? extends AbstractEntityVersion> clazz = LogEventoNotificacao.class;
		try {
			prepare();
			// gera as classes e filtro para os campos abaixo
			generate(clazz, LogEventoNotificacao_.IDLOGEVENTO, LogEventoNotificacao_.CHNFE, LogEventoNotificacao_.CSTAT,
					LogEventoNotificacao_.CNPJDEST, LogEventoNotificacao_.DHREGEVENTO, LogEventoNotificacao_.TPEVENTO,
					LogEventoNotificacao_.NPROT);
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
