package com.dgreentec.infrastructure.configuration.nfe;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.dgreentec.domain.xsd.retDistDFeInt_v101.RetDistDFeInt;
import com.dgreentec.domain.xsd.retDistDFeInt_v101.RetDistDFeInt.LoteDistDFeInt;
import com.dgreentec.domain.xsd.retDistDFeInt_v101.RetDistDFeInt.LoteDistDFeInt.DocZip;
import com.dgreentec.infrastructure.util.GzipUtils;

public class ProcessarRetornoDistNFeUTest {

	@Test
	public void processarArquivoRetorno() throws Exception {
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("retDistDFeInt_070120172305.xml");
		StringWriter writer = new StringWriter();
		IOUtils.copy(stream, writer, StandardCharsets.UTF_8.name());
		String xml = writer.toString();

		JAXBContext jaxbContext = JAXBContext.newInstance(RetDistDFeInt.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		RetDistDFeInt retorno = (RetDistDFeInt) unmarshaller.unmarshal(new StringReader(xml));

		LoteDistDFeInt loteDistDFeInt = retorno.getLoteDistDFeInt();
		List<DocZip> docZip = loteDistDFeInt.getDocZips();
		// o conteudo do arquivo vem zipado, é preciso descompactar com gzip

		File outputDir = new File(System.getProperty("user.dir") + File.separator + "target" + File.separator + "procnfe");
		outputDir.mkdirs();

		for (DocZip dzip : docZip) {
			String texto = GzipUtils.decompress(dzip.getValue());
			File nota = new File(outputDir, dzip.getNSU() + ".xml");
			FileUtils.writeStringToFile(nota, texto, StandardCharsets.UTF_8);
			System.out.println(nota.getAbsolutePath()+" criado!");
		}

	}

}
