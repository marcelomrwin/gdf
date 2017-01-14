package com.dgreentec.infrastructure.crud;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.dgreentec.domain.model.Certificado;
import com.dgreentec.domain.model.Certificado_;
import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.Contrato_;
import com.dgreentec.infrastructure.model.AbstractEntityVersion;

public class CrudGen extends AbstractCrudGen {

	@Test
	public void generateService() {

		System.out.println(new Date() + " - begin");
		// Classe do modelo que será usada para gerar artefatos
		Class<? extends AbstractEntityVersion> clazz = Certificado.class;
		try {
			prepare();
			// gera as classes e filtro para os campos abaixo
			generate(clazz, Certificado_.SENHA);
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
