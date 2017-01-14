package com.dgreentec.infrastructure.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JaxbUtils {

	public static String convertEntityToXML(Object entity, boolean format, String... replaces) throws Exception {

		JAXBContext ctx = JAXBContext.newInstance(entity.getClass());
		Marshaller marshaller = ctx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		StringWriter sw = new StringWriter();
		marshaller.marshal(entity, sw);

		String xml = sw.toString();
		for (String string : replaces) {
			xml = xml.replaceAll(string, "");
		}
		return xml;
	}

	@SuppressWarnings("unchecked")
	public static <T> T convertStringXMLToEntity(String xml, Class<T> clazz) throws Exception {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller um = context.createUnmarshaller();
		T entity = null;
		try (StringReader reader = new StringReader(xml)) {
			entity = (T) um.unmarshal(reader);
		}
		return entity;
	}

}
