package com.dgreentec.infrastructure.configuration.nfe;

import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.IOUtils;

import com.dgreentec.domain.model.TipoAmbienteEnum;
import com.dgreentec.domain.model.TipoServicoEnum;
import com.dgreentec.domain.model.UFEnum;
import com.dgreentec.infrastructure.configuration.exceptions.ServicoNaoLocalizadoException;
import com.dgreentec.infrastructure.configuration.nfe.ambiente.Ambiente;
import com.dgreentec.infrastructure.configuration.nfe.services.AbstractNFeService;

@XmlRootElement(name = "WebServices")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebServices implements Serializable {

	private static final long serialVersionUID = 8355654482215204113L;

	private static WebServices instance = null;

	@XmlElement(name = "UF")
	private List<UF> ufs = new ArrayList<>();

	public List<UF> getUfs() {
		return ufs;
	}

	public void setUfs(List<UF> ufs) {
		this.ufs = ufs;
	}

	public static WebServices getInstanceConfig() throws Exception {
		if (instance == null) {
			InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/nfe_ws_mod55.xml");
			StringWriter writer = new StringWriter();
			IOUtils.copy(stream, writer, StandardCharsets.UTF_8.name());
			String xml = writer.toString();

			JAXBContext jaxbContext = JAXBContext.newInstance(WebServices.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			instance = (WebServices) unmarshaller.unmarshal(new StringReader(xml));
		}
		return instance;
	}

	public AbstractNFeService getServico(UFEnum pUF, TipoServicoEnum tpServico, TipoAmbienteEnum tpAmb)
			throws ServicoNaoLocalizadoException {
		for (UF uf : ufs) {
			UFEnum euf = uf.toEnum();
			if (euf == pUF) {
				AbstractNFeService service = null;
				Ambiente ambiente = uf.getAmbienteHomologacao();
				if (tpAmb == TipoAmbienteEnum.PRODUCAO)
					ambiente = uf.getAmbienteProducao();

				switch (tpServico) {
				case NfeAutorizacao:
					service = ambiente.getNfeAutorizacao();
					break;
				case NfeConsultaCadastro:
					service = ambiente.getNfeConsultaCadastro();
					break;
				case NfeConsultaDest:
					service = ambiente.getNfeConsultaDest();
					break;
				case NfeConsultaProtocolo:
					service = ambiente.getNfeConsultaProtocolo();
					break;
				case NFeDistribuicaoDFe:
					service = ambiente.getNFeDistribuicaoDFe();
					break;
				case NfeDownloadNF:
					service = ambiente.getNfeDownloadNF();
					break;
				case NfeInutilizacao:
					service = ambiente.getNfeInutilizacao();
					break;
				case NFeRetAutorizacao:
					service = ambiente.getNFeRetAutorizacao();
					break;
				case NfeStatusServico:
					service = ambiente.getNfeStatusServico();
					break;
				case RecepcaoEvento:
					service = ambiente.getRecepcaoEvento();
					break;
				}

				if (service != null)
					return service;
			}
		}
		throw new ServicoNaoLocalizadoException();
	}

}
