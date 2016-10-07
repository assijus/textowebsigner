package br.jus.trf2.textoweb.signer;


public class Id {
	int idsecao;
	long idtextoweb;

	public Id(String id) {
		String[] split = id.split("_");
		this.idsecao = Integer.valueOf(split[0]);
		this.idtextoweb = Long.valueOf(split[1]);
	}

	public Id(int idsecao, long idtextoweb) {
		this.idsecao = idsecao;
		this.idtextoweb = idtextoweb;
	}

	public String toString() {
		return idsecao + "_" + idtextoweb;
	}
}
