package com.dgreentec.infrastructure.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NFeDateUtils {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

	public static String formatarData(Date data) {
		return sdf.format(data);
	}

	public static String formatarDataAtual() {
		return sdf.format(new Date());
	}

	public static Date converterData(String data) throws ParseException {
		return sdf.parse(data);
	}
}
