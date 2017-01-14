package com.dgreentec.infrastructure.configuration.nfe;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dgreentec.domain.model.TipoAmbienteEnum;
import com.dgreentec.domain.model.TipoServicoEnum;
import com.dgreentec.domain.model.UFEnum;
import com.dgreentec.infrastructure.configuration.exceptions.ServicoNaoLocalizadoException;

public class ParseXMLConfigUTest {

	protected static WebServices webservices = null;

	@BeforeClass
	public static void config() throws Exception {
		webservices = WebServices.getInstanceConfig();
	}

	// garante que o servico de download corresponde ao igual
	@Test
	public void consultarServicoExistente() throws Exception {
		assertThat(webservices.getServico(UFEnum.AN, TipoServicoEnum.NfeDownloadNF, TipoAmbienteEnum.HOMOLOGACAO).getUrl(),
				is(equalTo("https://hom.nfe.fazenda.gov.br/NfeDownloadNF/NfeDownloadNF.asmx")));
	}

	// garante que o servico de status está igual
	@Test
	public void consultaServicoExistenteValido() throws Exception {
		assertThat(webservices.getServico(UFEnum.RS, TipoServicoEnum.NfeStatusServico, TipoAmbienteEnum.PRODUCAO).getUrl(),
				is(equalTo("https://nfe.sefaz.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx")));
	}

	// busca um servico que não existe
	@Test(expected = ServicoNaoLocalizadoException.class)
	public void consultaServicoQueNaoExiste() throws Exception {
		assertThat(webservices.getServico(UFEnum.AN, TipoServicoEnum.NfeConsultaProtocolo, TipoAmbienteEnum.PRODUCAO), is(nullValue()));
	}

}
