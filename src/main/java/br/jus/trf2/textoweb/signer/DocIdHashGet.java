package br.jus.trf2.textoweb.signer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.jus.trf2.textoweb.signer.ITextoWebSigner.DocIdHashGetRequest;
import br.jus.trf2.textoweb.signer.ITextoWebSigner.DocIdHashGetResponse;
import br.jus.trf2.textoweb.signer.ITextoWebSigner.IDocIdHashGet;

public class DocIdHashGet implements IDocIdHashGet {
	@Override
	public void run(DocIdHashGetRequest req, DocIdHashGetResponse resp)
			throws Exception {
		Id id = new Id(req.id);

		byte[] pdf = DocIdPdfGet.retrievePdf(id);

		// Produce response
		resp.sha1 = calcSha1(pdf);
		resp.sha256 = calcSha256(pdf);

		resp.policy = "PKCS7";
		resp.doc = pdf;
	}

	public static byte[] calcSha1(byte[] content)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.reset();
		md.update(content);
		byte[] output = md.digest();
		return output;
	}

	public static byte[] calcSha256(byte[] content)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.reset();
		md.update(content);
		byte[] output = md.digest();
		return output;
	}

	@Override
	public String getContext() {
		return "obter o hash";
	}
}
