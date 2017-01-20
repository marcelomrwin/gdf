package com.dgreentec.domain.boundary.impl;

import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.concurrent.Callable;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.httpclient.protocol.Protocol;

import com.dgreentec.domain.model.Certificado;
import com.dgreentec.domain.model.TipoAmbienteEnum;
import com.dgreentec.domain.model.TipoServicoEnum;
import com.dgreentec.domain.model.UFEnum;
import com.dgreentec.infrastructure.configuration.nfe.WebServices;
import com.dgreentec.infrastructure.ssl.SocketFactoryDinamico;

import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.RecepcaoEventoStub;
import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.RecepcaoEventoStub.NfeCabecMsg;
import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.RecepcaoEventoStub.NfeCabecMsgE;
import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.RecepcaoEventoStub.NfeDadosMsg;
import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.RecepcaoEventoStub.NfeRecepcaoEventoResult;

public class ProcessadorEventoNotificacao implements Callable<String> {

	public ProcessadorEventoNotificacao(String lote, TipoAmbienteEnum ambiente, Certificado certificado) {
		super();
		this.lote = lote;
		this.ambiente = ambiente;
		this.certificado = certificado;
	}

	private String lote;

	private TipoAmbienteEnum ambiente;

	private Certificado certificado;

	@Override
	public String call() throws Exception {

		//debug
		System.out.println(lote);
		//end debug
		
		OMElement ome = AXIOMUtil.stringToOM(lote);
		NfeDadosMsg dados = new NfeDadosMsg();
		dados.setExtraElement(ome);

		NfeCabecMsgE cabecalho = new NfeCabecMsgE();
		NfeCabecMsg nfeCabecMsg = new NfeCabecMsg();
		nfeCabecMsg.setVersaoDados("1.00");
		nfeCabecMsg.setCUF(UFEnum.AN.getCodigo());
		cabecalho.setNfeCabecMsg(nfeCabecMsg);

		NfeRecepcaoEventoResult nfeRecepcaoEventoResult = getResult(dados, cabecalho, ambiente, certificado.getX509Certificate(),
				certificado.getPrivateKey());
		OMElement extraElement = nfeRecepcaoEventoResult.getExtraElement();
		String encoded = extraElement.toString();

		return encoded;
	}

	private NfeRecepcaoEventoResult getResult(NfeDadosMsg dados, NfeCabecMsgE cabecalho, TipoAmbienteEnum ambiente,
			X509Certificate certificate, PrivateKey privateKey) throws Exception {

		KeyStore trustStore = KeyStore.getInstance("JKS");

		InputStream fileCacerts = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("keystore" + File.separator + "NFeCacerts");

		trustStore.load(fileCacerts, "changeit".toCharArray());

		SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(certificate, privateKey, trustStore);

		Protocol protocol = new Protocol("https", socketFactoryDinamico, 443);
		Protocol.registerProtocol("https", protocol);

		RecepcaoEventoStub stub = new RecepcaoEventoStub(
				WebServices.getInstanceConfig().getServico(UFEnum.AN, TipoServicoEnum.RecepcaoEvento, ambiente).getUrl());

		NfeRecepcaoEventoResult result = stub.nfeRecepcaoEvento(dados, cabecalho);
		Protocol.unregisterProtocol("https");

		return result;

	}
}
