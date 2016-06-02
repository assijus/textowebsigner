package br.jus.trf2.textoweb.signer;


public class Id {
	String cpf;
	int idsecao;
	long idtextoweb;

	public Id(String id) {
		String[] split = id.split("_");
		this.cpf = split[0];
		this.idsecao = Integer.valueOf(split[1]);
		this.idtextoweb = Long.valueOf(split[2]);
	}

	public Id(String cpf, int idsecao, long idtextoweb) {
		this.cpf = cpf;
		this.idsecao = idsecao;
		this.idtextoweb = idtextoweb;
	}

	public String toString() {
		return cpf + "_" + idsecao + "_" + idtextoweb;
	}
}
