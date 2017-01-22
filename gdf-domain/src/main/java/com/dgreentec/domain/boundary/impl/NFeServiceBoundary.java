package com.dgreentec.domain.boundary.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.xml.bind.Unmarshaller;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.dgreentec.domain.boundary.api.DocumentoFiscalService;
import com.dgreentec.domain.boundary.api.EmpresaService;
import com.dgreentec.domain.boundary.api.LogEventoNotificacaoService;
import com.dgreentec.domain.boundary.api.LoteEventoService;
import com.dgreentec.domain.boundary.api.NFeService;
import com.dgreentec.domain.boundary.impl.eventodocumento.ProcessadorEventoDocumentoEmpresa;
import com.dgreentec.domain.boundary.impl.eventodocumento.ProcessadorEventoDocumentoLiteral;
import com.dgreentec.domain.boundary.impl.eventodocumento.ProcessadorEventoNotificacao;
import com.dgreentec.domain.model.Certificado;
import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.DocumentoFiscal;
import com.dgreentec.domain.model.Empresa;
import com.dgreentec.domain.model.EventoDocumento;
import com.dgreentec.domain.model.EventoDocumentoResponse;
import com.dgreentec.domain.model.LogEventoNotificacao;
import com.dgreentec.domain.model.LoteEvento;
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.model.TipoAmbienteEnum;
import com.dgreentec.domain.model.TipoDocumentoEnum;
import com.dgreentec.domain.model.UFEnum;
import com.dgreentec.domain.xsd.procEventoNFe_v100.ProcEventoNFe;
import com.dgreentec.domain.xsd.procNFe_v310.NfeProc;
import com.dgreentec.domain.xsd.resEvento_v101.ResEvento;
import com.dgreentec.domain.xsd.resNFe_v101.ResNFe;
import com.dgreentec.domain.xsd.retEnvEvento_v100.RetEnvEvento;
import com.dgreentec.domain.xsd.retEnvEvento_v100.TRetEvento;
import com.dgreentec.domain.xsd.retEnvEvento_v100.TRetEvento.InfEvento;
import com.dgreentec.infrastructure.exception.NfeException;
import com.dgreentec.infrastructure.interceptor.EJBTransactionInterceptor;
import com.dgreentec.infrastructure.model.ConstantesNFe;
import com.dgreentec.infrastructure.repository.multitenant.TenantInterceptor;
import com.dgreentec.infrastructure.service.boundary.AbstractBoundary;
import com.dgreentec.infrastructure.util.NFeDateUtils;

@Stateless
@Interceptors({ TenantInterceptor.class, EJBTransactionInterceptor.class })
public class NFeServiceBoundary extends AbstractBoundary implements NFeService {

	@Inject
	protected Logger logger;

	@Resource(name = "java:comp/DefaultManagedExecutorService")
	private ManagedExecutorService executor;

	@Inject
	private DocumentoFiscalService documentoFiscalService;

	@Inject
	private LoteEventoService loteEventoService;

	//	@Inject	@Any protected Instance<ProcessadorEventoDocumentoEmpresa> processadorEventoInstance;

	@Inject
	protected LogEventoNotificacaoService logEventoNotificacaoService;

	@Inject
	protected EmpresaService empresaService;

	@Override
	@Asynchronous
	public void processarEventosPorContrato(Tenant tenant, final Contrato contrato, TipoAmbienteEnum ambiente) throws NfeException {

		List<Empresa> empresas = contrato.getEmpresas();

		for (final Empresa empresa : empresas) {
			try {
				processarEventosPorEmpresa(tenant, contrato, empresa, ambiente);
			} catch (Throwable e) {
				throw new NfeException(e);
			}
		}

	}

	private Future<EventoDocumentoResponse> scheduleProcess(Tenant tenant, Empresa empresa, TipoAmbienteEnum ambiente) {
		ProcessadorEventoDocumentoEmpresa processador = new ProcessadorEventoDocumentoEmpresa(empresaService, empresa, tenant, ambiente); //processadorEventoInstance.select(new ProcessadorEventoDocumentoLiteral(tenant.getIdTenant(), pCNPJ, ambiente)).get();

		debug("agendando para a instancia " + processador + " " + tenant + "| Empresa: " + empresa);

		Future<EventoDocumentoResponse> future = executor.submit(processador);
		return future;
	}

	@Asynchronous
	@Override
	public void processarEventosPorEmpresa(Tenant tenant, final Contrato contrato, Empresa empresa, TipoAmbienteEnum ambiente)
			throws NfeException {
		EventoDocumentoResponse eventosDaEmpresa = null;
		do {
			debug("Fez pesquisa! [" + tenant + "]" + empresa);
			Future<EventoDocumentoResponse> future = scheduleProcess(tenant, empresa, ambiente);
			try {
				debug("aguardando get() da pesquisa! [" + tenant + "]" + empresa + " Future: " + future);

				eventosDaEmpresa = future.get(1, TimeUnit.MINUTES);

				debug("get() Fez pesquisa retornou [" + tenant + "]" + empresa);

			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				throw new NfeException(e);
			}

			List<EventoDocumento> resumos = new ArrayList<>();

			// processa os eventos da empresa
			List<EventoDocumento> eventos = eventosDaEmpresa.getEventos();
			for (EventoDocumento evDoc : eventos) {
				switch (evDoc.getSchema()) {
				case RESNFE: {
					// resumo de NFe, representam notas que foram emitidas para o cnpj mas que não foram manifestadas
					// pelo destinatário.
					resumos.add(evDoc);
					break;
				}
				case NFEPROC: {
					// resultado do processamento da NFe. Contém a nota e assinatura. Tag raiz nfeProc
					NfeProc nfeProc = (NfeProc) evDoc.getJaxbObject();
					// guardar a NFe na base/filesystem ...
					DocumentoFiscal doc = new DocumentoFiscal(TipoDocumentoEnum.NFE, evDoc.getXml(),
							nfeProc.getNFe().getInfNFe().getIde().getNNF(), empresa);
					documentoFiscalService.adicionarDocumentoFiscal(tenant, doc);
					break;
				}
				case RESEVENTO: {
					// resumo de evento
					ResEvento resEvento = (ResEvento) evDoc.getJaxbObject();
					break;
				}
				case PROCEVENTONFE: {
					// resultado do processamento de um evento
					ProcEventoNFe procEventoNFe = (ProcEventoNFe) evDoc.getJaxbObject();
					break;
				}
				}

			}
			// inicia o tratamento com os resumos
			// TODO os resumos de nfe devem ser manifestados. Manifestar conhecimento da NFe para permitir o download
			// futuramente.
			manifestarEventosCienciaDocumentoResumo(tenant, contrato, resumos, empresa, ambiente);

		} while (eventosDaEmpresa.possuiEventoRestante());
		debug("Encerrando fez pesquisa! " + tenant + " - " + empresa);
	}

	@Override
	@Asynchronous
	public void manifestarEventosCienciaDocumentoResumo(Tenant tenant, Contrato contrato, List<EventoDocumento> eventos, Empresa empresa,
			TipoAmbienteEnum ambiente) throws NfeException {
		// TODO melhorar o tratamento try/catch
		try {
			debug(" Manifestando resumo " + tenant + " " + empresa);
			// só é possível manifestar no máximo 20 eventos por lote
			List<String> lotes = new LinkedList<>();

			// sequencial de lote evento
			LoteEvento loteEvento = loteEventoService.adicionarLoteEvento(tenant, new LoteEvento(empresa));

			String idLote = String.valueOf(loteEvento.getIdLoteEvento());
			String uf = UFEnum.AN.getCodigo();
			String cnpj = empresa.getCnpj();
			String tpEvento = "210210";
			String versao = "1.00";
			String txtEvento = "Ciencia da Operacao";

			StringBuilder xml = criarCabecalhoEnvEvento(idLote, versao);

			List<String> listaEventos = new LinkedList<>();
			for (EventoDocumento evDoc : eventos) {
				ResNFe resNFe = (ResNFe) evDoc.getJaxbObject();
				String chaveNFe = resNFe.getChNFe();
				String eventoId = "ID" + "210210" + chaveNFe + "01";

				StringBuilder evento = new StringBuilder("<evento versao=\"1.00\">").append("<infEvento Id=\"").append(eventoId)
						.append("\">").append("<cOrgao>").append(uf).append("</cOrgao>").append("<tpAmb>").append(ambiente.getTpAmb())
						.append("</tpAmb>").append("<CNPJ>").append(cnpj).append("</CNPJ>").append("<chNFe>").append(chaveNFe)
						.append("</chNFe>").append("<dhEvento>").append(NFeDateUtils.formatarDataAtual()).append("</dhEvento>")
						.append("<tpEvento>").append(tpEvento).append("</tpEvento>").append("<nSeqEvento>").append("1")
						.append("</nSeqEvento>").append("<verEvento>").append(versao).append("</verEvento>").append("<detEvento versao=\"")
						.append(versao).append("\">").append("<descEvento>").append(txtEvento).append("</descEvento>")
						.append("</detEvento>").append("</infEvento>").append("</evento>");

				listaEventos.add(evento.toString());

				// respeitar o limite máximo de 20 eventos por chamada
				if (listaEventos.size() == 20) {
					for (String ev : listaEventos) {
						xml.append(ev);
					}
					xml.append("</envEvento>");
					lotes.add(xml.toString());

					// novo lote
					loteEvento = loteEventoService.adicionarLoteEvento(tenant, new LoteEvento(empresa));
					idLote = String.valueOf(loteEvento.getIdLoteEvento());

					xml = criarCabecalhoEnvEvento(idLote, versao);
					// reinicia a lista
					listaEventos = new LinkedList<>();
				}
			}

			for (String ev : listaEventos) {
				xml.append(ev);
			}
			xml.append("</envEvento>");
			lotes.add(xml.toString());

			// realiza a manifestação de cada lote
			List<Future<String>> tasks = new ArrayList<>();
			for (String lote : lotes) {

				debug("despachou o lote " + lote + tenant + ", " + contrato + ", " + empresa);

				// assina o lote antes de enviar
				String eventoXMLAssinado = assinarEnvioEvento(lote, empresa.getCertificado());
				Callable<String> task = new ProcessadorEventoNotificacao(eventoXMLAssinado, ambiente, empresa.getCertificado());

				Future<String> futureTask = executor.submit(task);
				tasks.add(futureTask);
			}

			// recupera o retorno de processamento das tasks (notificações da sefaz)
			for (Future<String> future : tasks) {

				debug("solicitando resultado do processamento do lote " + tenant + ", " + contrato + ", " + empresa);

				String encoded = future.get();

				debug("resultado do (get) " + tenant + ", " + contrato + ", " + empresa);

				debug("retorno de lotes " + encoded);

				com.dgreentec.domain.xsd.retEnvEvento_v100.ObjectFactory of = new com.dgreentec.domain.xsd.retEnvEvento_v100.ObjectFactory();
				Unmarshaller unmarshaller = javax.xml.bind.JAXBContext.newInstance(of.getClass()).createUnmarshaller();
				RetEnvEvento retEvento = (com.dgreentec.domain.xsd.retEnvEvento_v100.RetEnvEvento) unmarshaller
						.unmarshal(new java.io.StringReader(encoded));

				if (ConstantesNFe.RETORNO_EVENTO_LOTE_PROCESSADO.equalsIgnoreCase(retEvento.getCStat())) {
					// lote foi processado, tratar o retorno.
					List<TRetEvento> retEventos = retEvento.getRetEventos();
					for (TRetEvento tRetEvento : retEventos) {
						InfEvento infEvento = tRetEvento.getInfEvento();

						LogEventoNotificacao log = new LogEventoNotificacao();
						log.setChNFe(infEvento.getChNFe());
						log.setcOrgao(Integer.valueOf(infEvento.getCOrgao()));
						log.setcStat(Integer.valueOf(infEvento.getCStat()));
						log.setDhRegEvento(NFeDateUtils.converterDataCompleta(infEvento.getDhRegEvento()));
						log.setnProt(infEvento.getNProt());
						log.setnSeqEvento(Integer.valueOf(infEvento.getNSeqEvento()));
						log.setTpAmb(Integer.valueOf(infEvento.getTpAmb()));
						log.setTpEvento(Integer.valueOf(infEvento.getTpEvento()));
						log.setVerAplic(infEvento.getVerAplic());
						log.setxEvento(infEvento.getXEvento());
						log.setxMotivo(infEvento.getXMotivo());

						debug("adicionando log de notificaçnao " + tenant + " " + log);

						logEventoNotificacaoService.adicionarLogEventoNotificacao(tenant, log);

						String cStat = infEvento.getCStat();
						switch (cStat) {
						case ConstantesNFe.REJEICAO_DUPLICIDADE_EVENTO: {
							logger.warn("EVENTO [" + infEvento.getTpEvento() + " - " + infEvento.getXEvento() + "] para a chave ["
									+ infEvento.getChNFe() + "] JÁ REGISTRADO EM " + infEvento.getDhRegEvento() + " AMBIENTE ["
									+ infEvento.getTpAmb() + "]");
							break;
						}
						case ConstantesNFe.RETORNO_EVENTO_REGISTRADO_NAO_VINCULADO_NFE:
						case ConstantesNFe.RETORNO_EVENTO_REGISTRADO_VINCULADO_NFE: {
							break;
						}
						default:
							logger.error("Falha no processamento do evento id: " + infEvento.getId() + ", chave: " + infEvento.getChNFe()
									+ ", protocolo: " + infEvento.getNProt() + ", seqEvento: " + infEvento.getNSeqEvento() + " tipo: "
									+ infEvento.getTpEvento() + " ambiente: " + infEvento.getTpAmb());
						}

					}
				} else {
					throw new NfeException(
							"Falha no processamento do lote. Código de retorno [" + retEvento.getCStat() + "] " + retEvento.getXMotivo());
				}
			}
		} catch (Throwable t) {
			if (t instanceof NfeException) {
				NfeException e = (NfeException) t;
				throw e;
			}
			throw new NfeException(t);
		}
	}

	private String assinarEnvioEvento(String xml, Certificado certificado) throws Exception {
		Document document = documentFactory(xml);

		XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
		ArrayList<Transform> transformList = signatureFactory(signatureFactory);

		KeyInfo keyInfo = certificado.getKeyInfo(signatureFactory);
		PrivateKey privateKey = certificado.getPrivateKey();

		NodeList elements = document.getElementsByTagName("infEvento");
		for (int i = 0; i < elements.getLength(); i++) {
			org.w3c.dom.Element el = (org.w3c.dom.Element) elements.item(i);
			el.setIdAttribute("Id", true);
			String id = el.getAttribute("Id");

			Reference ref = signatureFactory.newReference("#" + id, signatureFactory.newDigestMethod(DigestMethod.SHA1, null),
					transformList, null, null);

			SignedInfo si = signatureFactory.newSignedInfo(
					signatureFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
					signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

			XMLSignature signature = signatureFactory.newXMLSignature(si, keyInfo);
			DOMSignContext dsc = new DOMSignContext(privateKey, document.getElementsByTagName("evento").item(i));
			signature.sign(dsc);
		}
		String xmlAssinado = outputXML(document);

		return xmlAssinado;
	}

	private ArrayList<Transform> signatureFactory(XMLSignatureFactory signatureFactory)
			throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		ArrayList<Transform> transformList = new ArrayList<Transform>();
		TransformParameterSpec tps = null;
		Transform envelopedTransform = signatureFactory.newTransform(Transform.ENVELOPED, tps);
		Transform c14NTransform = signatureFactory.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", tps);

		transformList.add(envelopedTransform);
		transformList.add(c14NTransform);
		return transformList;
	}

	private Document documentFactory(String xml) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		Document document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
		return document;
	}

	private String outputXML(Document doc) throws TransformerException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();

		trans.setOutputProperty("omit-xml-declaration", "yes");

		trans.transform(new DOMSource(doc), new StreamResult(os));
		String xml = os.toString();
		if ((xml != null) && (!"".equals(xml))) {
			xml = xml.replaceAll("\\r\\n", "");
			xml = xml.replaceAll(" standalone=\"no\"", "");
		}

		return xml;
	}

	private StringBuilder criarCabecalhoEnvEvento(String lote, String versao) {
		StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		xml.append("<envEvento versao=\"").append(versao).append("\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">").append("<idLote>")
				.append(lote).append("</idLote>");
		return xml;
	}

}
