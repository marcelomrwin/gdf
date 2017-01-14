package com.dgreentec.domain;

import org.junit.Test;

import com.dgreentec.domain.xsd.distDFeInt_v101.DistDFeInt;
import com.dgreentec.domain.xsd.distDFeInt_v101.ObjectFactory;
import com.dgreentec.domain.xsd.distDFeInt_v101.DistDFeInt.DistNSU;
import com.dgreentec.infrastructure.util.JaxbUtils;

public class JaxbGenerateXMLUTest {

	@Test
	public void generateXML() throws Exception {
		DistDFeInt dist = new ObjectFactory().createDistDFeInt();// new DistDFeInt();
		dist.setVersao("1.00");
		dist.setTpAmb("2");
		dist.setCUFAutor("33");
		dist.setCNPJ("07932968000103");

		DistNSU distNSU = new ObjectFactory().createDistDFeIntDistNSU();// new DistNSU();
		distNSU.setUltNSU("000000000000000");
		dist.setDistNSU(distNSU);

		String dados = "<distDFeInt xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"1.00\">" + "<tpAmb>" + 2 + "</tpAmb>"
				+ "<cUFAutor>" + 33 + "</cUFAutor>" + "<CNPJ>" + "07932968000103" + "</CNPJ>" + "<distNSU>" + "<ultNSU>" + "000000000000000"
				+ "</ultNSU>" + "</distNSU>" + "</distDFeInt>";

		System.out.println(JaxbUtils.convertEntityToXML(dist, false));
		System.out.println("*************************************");
		System.out.println(dados);

	}
}
