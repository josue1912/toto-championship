package br.com.toto.utils;

public enum BrasilTimesSerieA2018Enum {

	AMERICA_MINEIRO("América MG"),
	ATLETICO_MINEIRO("Atlético MG"),
	ATLETICO_PARANAENSE("Atlético PR"),
	BAHIA("Bahia"),
	BOTAFOGO("Botafogo"),
	CEARA("Ceará"),
	CHAPECOENSE("Chapecoense"),
	CORINTHIANS("Corinthians"),
	CRUZEIRO("Cruzeiro"),
	FLAMENGO("Flamengo"),
	FLUMINENSE("Fluminense"),
	GREMIO("Grêmio"),
	INTERNACIONAL("Internacional"),
	PALMEIRAS("Palmeiras"),
	PARANA("Paraná"),
	SANTOS("Santos"),
	SAO_PAULO("São Paulo"),
	SPORT("Sport"),
	VASCO("Vasco"),
	VITORIA("Vitória");
	
	private final String nome;
	
	BrasilTimesSerieA2018Enum(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
}
