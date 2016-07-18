package br.jus.trf2.textoweb.signer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONObject;

import com.crivano.restservlet.IRestAction;
import com.crivano.restservlet.RestUtils;

public class DocIdHashGet implements IRestAction {
	@Override
	public void run(JSONObject req, JSONObject resp) throws Exception {
		Id id = new Id(req.getString("id"));

		byte[] pdf = DocIdPdfGet.retrievePdf(id);

		// Produce response
		String sha1 = RestUtils.base64Encode(calcSha1(pdf));
		String sha256 = RestUtils.base64Encode(calcSha256(pdf));
		resp.put("sha1", sha1);
		resp.put("sha256", sha256);

		resp.put("policy", "PKCS7");
		resp.put("doc", RestUtils.base64Encode(pdf));
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
