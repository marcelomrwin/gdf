package com.dgreentec.domain.model;

public enum UFEnum {
	AC("Acre", 12), AL("Alagoas", 27), AM("Amazonas", 13), AP("Amapá", 16), BA("Bahia", 29), CE("Ceará", 23), DF("Distrito Federal",
			53), ES("Espirito Santo", 32), GO("Goiás", 52), MA("Maranhão", 21), MG("Minas Gerais",
					31), MS("Mato Grosso do Sul", 50), MT("Mato Grosso", 51), PA("Pará", 15), PB("Paraíba", 25), PE("Pernambuco",
							26), PI("Piauí", 22), PR("Paraná", 41), RJ("Rio de Janeiro", 33), RN("Rio Grande do Norte", 24), RO("Rondônia",
									11), RR("Roraima", 14), RS("Rio Grande do Sul", 43), SC("Santa Catarina", 42), SE("Sergipe",
											28), SP("São Paulo", 35), TO("Tocantins", 17), AN("Ambiente Nacional",
													91), SVAN("Sefaz Virtual do Ambiente Nacional", 0), SVRS("Sefaz Virtual do RS",
															0), SVCAN("Sefaz Virtual de Contingência Ambiente Nacional",
																	0), SVCRS("Sefaz Virtual de Contingência Rio Grande do Sul", 0);

	private final String nome;

	private final Integer codigo;

	UFEnum(String nome, Integer pCodigo) {
		this.nome = nome;
		this.codigo = pCodigo;
	}

	public String getNome() {
		return nome;
	}

	public String getCodigo() {
		return String.valueOf(codigo);
	}
}
