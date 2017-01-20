package com.dgreentec.domain.boundary.impl;

import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.xml.bind.Unmarshaller;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;

import com.dgreentec.domain.boundary.api.ContratoService;
import com.dgreentec.domain.boundary.api.EmpresaService;
import com.dgreentec.domain.boundary.api.NFeService;
import com.dgreentec.domain.model.BloqueioSefaz;
import com.dgreentec.domain.model.Empresa;
import com.dgreentec.domain.model.EventoDocumento;
import com.dgreentec.domain.model.EventoDocumentoResponse;
import com.dgreentec.domain.model.EventoNSU;
import com.dgreentec.domain.model.TipoAmbienteEnum;
import com.dgreentec.domain.model.TipoServicoEnum;
import com.dgreentec.domain.model.UFEnum;
import com.dgreentec.domain.model.UltimoEventoNSU;
import com.dgreentec.domain.xsd.distDFeInt_v101.DistDFeInt;
import com.dgreentec.domain.xsd.distDFeInt_v101.DistDFeInt.DistNSU;
import com.dgreentec.domain.xsd.distDFeInt_v101.ObjectFactory;
import com.dgreentec.domain.xsd.procEventoNFe_v100.ProcEventoNFe;
import com.dgreentec.domain.xsd.procNFe_v310.NfeProc;
import com.dgreentec.domain.xsd.resEvento_v101.ResEvento;
import com.dgreentec.domain.xsd.resNFe_v101.ResNFe;
import com.dgreentec.domain.xsd.retDistDFeInt_v101.RetDistDFeInt;
import com.dgreentec.domain.xsd.retDistDFeInt_v101.RetDistDFeInt.LoteDistDFeInt;
import com.dgreentec.domain.xsd.retDistDFeInt_v101.RetDistDFeInt.LoteDistDFeInt.DocZip;
import com.dgreentec.infrastructure.configuration.nfe.WebServices;
import com.dgreentec.infrastructure.model.ConstantesNFe;
import com.dgreentec.infrastructure.model.SchemaEnum;
import com.dgreentec.infrastructure.ssl.SocketFactoryDinamico;
import com.dgreentec.infrastructure.util.CDIUtils;
import com.dgreentec.infrastructure.util.GzipUtils;
import com.dgreentec.infrastructure.util.JaxbUtils;
import com.dgreentec.infrastructure.util.NFeDateUtils;

import br.inf.portalfiscal.www.nfe.wsdl.nfedistribuicaodfe.NFeDistribuicaoDFeStub;
import br.inf.portalfiscal.www.nfe.wsdl.nfedistribuicaodfe.NFeDistribuicaoDFeStub.NfeDadosMsg_type0;
import br.inf.portalfiscal.www.nfe.wsdl.nfedistribuicaodfe.NFeDistribuicaoDFeStub.NfeDistDFeInteresse;
import br.inf.portalfiscal.www.nfe.wsdl.nfedistribuicaodfe.NFeDistribuicaoDFeStub.NfeDistDFeInteresseResponse;
import br.inf.portalfiscal.www.nfe.wsdl.nfedistribuicaodfe.NFeDistribuicaoDFeStub.NfeDistDFeInteresseResult_type0;

@ProcessadorEventoDocumento
public class ProcessadorEventoDocumentoEmpresa implements Callable<EventoDocumentoResponse> {

	@Inject
	protected Logger logger;

	@Inject
	protected EmpresaService empresaService;

	@Inject
	public ProcessadorEventoDocumentoEmpresa(InjectionPoint ip) {
		configureValues(ip);
	}

	private void configureValues(InjectionPoint ip) {
		ProcessadorEventoDocumento evento = null;
		for (Annotation annotation : ip.getQualifiers()) {
			if (annotation.annotationType().equals(ProcessadorEventoDocumento.class)) {
				evento = (ProcessadorEventoDocumento) annotation;
				break;
			}
		}

		if (evento != null) {
			long idContrato = evento.idContrato();
			String cnpj = evento.cnpj();
			ambiente = evento.ambiente();

			ContratoService contratoService = CDIUtils.getInstance().getBeanInstance(ContratoService.class);
			EmpresaService empresaService = CDIUtils.getInstance().getBeanInstance(EmpresaService.class);

			empresa = empresaService.consultarEmpresaPorCnpj(contratoService.consultarContratoPorIdContrato(idContrato), cnpj);
		} else
			throw new IllegalStateException("No @ProcessadorEventoDocumento on InjectionPoint");
	}

	private Empresa empresa;

	private TipoAmbienteEnum ambiente;

	@Override
	public EventoDocumentoResponse call() throws Exception {
		return consultarEventosDaEmpresa();
	}

	private EventoDocumentoResponse consultarEventosDaEmpresa() throws Exception {
		EventoDocumentoResponse response = new EventoDocumentoResponse();
		List<EventoDocumento> lista = new LinkedList<>();
		response.setEventos(lista);
		// Antes de realizar qualquer ação verifica se existe bloqueio pra empresa na sefaz
		if (!empresa.existeBloqueioParaEvento()) {

			DistDFeInt dist = new ObjectFactory().createDistDFeInt();
			dist.setVersao("1.00");// FIXME verificar se é possível parametrizar e se vale a pena já que quando uma
									// versão é
									// alterada normalmente se precisa mexer no sistema.
			dist.setTpAmb(ambiente.getTpAmb());
			dist.setCUFAutor(empresa.getUf().getCodigo());
			dist.setCNPJ(empresa.getCnpj());

			DistNSU distNSU = new ObjectFactory().createDistDFeIntDistNSU();
			String nsu = StringUtils.leftPad(String.valueOf(empresa.getUltimoNSU().getUltimoNSU()), 15, "0");
			distNSU.setUltNSU(nsu);
			dist.setDistNSU(distNSU);

			OMElement ome = AXIOMUtil.stringToOM(JaxbUtils.convertEntityToXML(dist, false));

			NfeDadosMsg_type0 dadosMsg = new NfeDadosMsg_type0();
			dadosMsg.setExtraElement(ome);

			NfeDistDFeInteresse distDFeInteresse = new NfeDistDFeInteresse();
			distDFeInteresse.setNfeDadosMsg(dadosMsg);

			com.dgreentec.domain.xsd.retDistDFeInt_v101.RetDistDFeInt retorno = callService(dist, distDFeInteresse, ambiente,
					empresa.getCertificado().getX509Certificate(), empresa.getCertificado().getPrivateKey());

			response.setMaxNSu(retorno.getMaxNSU());
			response.setUltimoNSu(retorno.getUltNSU());

			if (retorno.getCStat().equalsIgnoreCase(ConstantesNFe.RETORNO_EVENTO_MANIFESTO_138)) {
				empresa = empresaService.consultarEmpresaPorCnpj(empresa.getContrato(), empresa.getCnpj());
				LoteDistDFeInt loteDistDFeInt = retorno.getLoteDistDFeInt();
				UltimoEventoNSU ultimo = new UltimoEventoNSU();
				ultimo.setDataUltimoNSU(NFeDateUtils.converterData(retorno.getDhResp()));
				ultimo.setUltimoNSU(NumberUtils.toLong(retorno.getUltNSU()));

				empresa.setUltimoNSU(ultimo);
				empresa.setBloqueioSefaz(null);

				List<DocZip> docZip = loteDistDFeInt.getDocZips();
				// o conteudo do arquivo vem zipado, é preciso descompactar com gzip

				for (DocZip dzip : docZip) {
					SchemaEnum schema = SchemaEnum.fromSchemaName(dzip.getSchema());
					String xml = GzipUtils.decompress(dzip.getValue());
					EventoDocumento evDoc = new EventoDocumento();
					evDoc.setNsu(dzip.getNSU());
					evDoc.setSchema(schema);
					evDoc.setXml(xml);
					switch (schema) {
					case RESNFE: {
						// resumo de NFe, representam notas que foram emitidas para o cnpj mas que não foram
						// manifestadas pelo destinatário.
						ResNFe resNFe = JaxbUtils.convertStringXMLToEntity(xml, ResNFe.class);
						// os resumos de nfe devem ser manifestados manifestar conhecimento da NFe para permitir o
						// download
						// futuramente.
						evDoc.setJaxbObject(resNFe);
						break;
					}
					case NFEPROC: {
						// resultado do processamento da NFe. Contém a nota e assinatura. Tag raiz nfeProc
						NfeProc nfeProc = JaxbUtils.convertStringXMLToEntity(xml, NfeProc.class);
						// guardar a NFe na base/filesystem ...
						evDoc.setJaxbObject(nfeProc);
						break;
					}
					case RESEVENTO: {
						// resumo de evento
						ResEvento resEvento = JaxbUtils.convertStringXMLToEntity(xml, ResEvento.class);
						evDoc.setJaxbObject(resEvento);
						break;
					}
					case PROCEVENTONFE: {
						// resultado do processamento de um evento
						ProcEventoNFe procEventoNFe = JaxbUtils.convertStringXMLToEntity(xml, ProcEventoNFe.class);
						evDoc.setJaxbObject(procEventoNFe);
						break;
					}
					}

					// TODO registar cada evento na empresa
					EventoNSU eventoNSU = new EventoNSU();
					eventoNSU.setIdNsu(evDoc.getNsu());
					eventoNSU.setDtNSU(new Date());
					eventoNSU.setSchema(evDoc.getSchema());
					empresa.adicionarNSU(eventoNSU);

					lista.add(evDoc);
				}
			} else if (retorno.getCStat().equalsIgnoreCase(ConstantesNFe.RETORNO_EVENTO_MANIFESTO_137)) {
				// não houve documentos
				logger.warn("não houve documentos para recuperar [" + retorno.getCStat() + ":" + retorno.getXMotivo() + "] para a empresa "
						+ empresa.getCnpj() + " - " + empresa.getNome());

				empresa = empresaService.consultarEmpresaPorCnpj(empresa.getContrato(), empresa.getCnpj());

				// após um código 137 a SEFAZ recomenda aguardar em torno de 1 hora antes de realizar a nova chamada, do
				// contrário corre-se o risco de cair numa "lista negra".

				// Guadar para a empresa uma flag informando que só após 1 hora poderá consultar novamente
				BloqueioSefaz bloqueio = new BloqueioSefaz();
				Calendar c = GregorianCalendar.getInstance();
				c.add(Calendar.HOUR_OF_DAY, 1);
				bloqueio.setDtCadastroBloqueio(new Date());
				bloqueio.setDtExpiracao(c.getTime());
				empresa.setBloqueioSefaz(bloqueio);

				empresaService.atualizarEmpresa(empresa.getContrato(), empresa);

			} else {
				logger.error("RETORNO DESCONHECIDO [" + retorno.getCStat() + ":" + retorno.getXMotivo() + "] Não processou a empresa "
						+ empresa.getCnpj() + " - " + empresa.getNome());

			}

			// FIXME - processa os ventos aqui? envia por evento cdi? faz chamadas recursivas até receber todos os NSUs?
			empresa = empresaService.atualizarEmpresa(empresa.getContrato(), empresa);
			// nfeService.processarEventoDocumentoParaEmpresa(empresa.getContrato(), empresa, evDoc);
		}
		return response;
	}

	private com.dgreentec.domain.xsd.retDistDFeInt_v101.RetDistDFeInt callService(DistDFeInt dist, NfeDistDFeInteresse distDFeInteresse,
			TipoAmbienteEnum ambiente, X509Certificate certificate, PrivateKey privateKey) throws Exception {

		com.dgreentec.domain.xsd.retDistDFeInt_v101.RetDistDFeInt retorno = null;
		do {
			NfeDistDFeInteresseResponse result = getResult(distDFeInteresse, ambiente, certificate, privateKey);

			NfeDistDFeInteresseResult_type0 nfeDistDFeInteresseResult = result.getNfeDistDFeInteresseResult();
			OMElement extraElement = nfeDistDFeInteresseResult.getExtraElement();
			String encoded = extraElement.toString();
			Unmarshaller unmarshaller = javax.xml.bind.JAXBContext
					.newInstance(new com.dgreentec.domain.xsd.retDistDFeInt_v101.ObjectFactory().getClass()).createUnmarshaller();
			retorno = (RetDistDFeInt) (unmarshaller.unmarshal(new java.io.StringReader(encoded)));
		} while (retorno == null || retorno.getCStat().equalsIgnoreCase(ConstantesNFe.REJEICAO_593));

		return retorno;
	}

	private NfeDistDFeInteresseResponse getResult(NfeDistDFeInteresse distDFeInteresse, TipoAmbienteEnum ambiente,
			X509Certificate certificate, PrivateKey privateKey) throws Exception {

		KeyStore trustStore = KeyStore.getInstance("JKS");

		InputStream fileCacerts = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("keystore" + File.separator + "NFeCacerts");

		trustStore.load(fileCacerts, "changeit".toCharArray());

		SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(certificate, privateKey, trustStore);

		Protocol protocol = new Protocol("https", socketFactoryDinamico, 443);
		Protocol.registerProtocol("https", protocol);

		NFeDistribuicaoDFeStub stub = new NFeDistribuicaoDFeStub(
				WebServices.getInstanceConfig().getServico(UFEnum.AN, TipoServicoEnum.NFeDistribuicaoDFe, ambiente).getUrl());
		NfeDistDFeInteresseResponse result = stub.nfeDistDFeInteresse(distDFeInteresse);
		Protocol.unregisterProtocol("https");

		return result;

	}

}
