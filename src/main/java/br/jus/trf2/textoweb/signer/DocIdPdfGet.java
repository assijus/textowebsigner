package br.jus.trf2.textoweb.signer;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.crivano.restservlet.IRestAction;
import com.crivano.restservlet.RestUtils;

public class DocIdPdfGet implements IRestAction {
	@Override
	public void run(HttpServletRequest request, HttpServletResponse response,
			JSONObject req, JSONObject resp) throws Exception {
		Id id = new Id(req.getString("id"));

		byte[] pdf = retrievePdf(id);

		// Produce response
		resp.put("doc", Base64.encodeBase64String(pdf));
	}

	protected static byte[] retrievePdf(Id id) throws Exception, SQLException {
		byte[] pdfCompressed = null;
		String status;
		String error;
		// Chama a procedure que recupera os dados do PDF para viabilizar a
		// assinatura
		//
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = Utils.getConnection();

			cstmt = conn.prepareCall(Utils.getSQL("pdfinfo"));

			// CodSecao -> Código da Seção Judiciária (50=ES; 51=RJ;
			// 2=TRF)
			cstmt.setInt(1, id.idsecao);

			// CodTxtWeb -> Código interno do documento
			cstmt.setLong(2, id.idtextoweb);

			// CPF
			cstmt.setString(3, id.cpf);

			// PDF uncompressed
			cstmt.registerOutParameter(4, Types.BLOB);

			// Status
			cstmt.registerOutParameter(5, Types.VARCHAR);

			// Error
			cstmt.registerOutParameter(6, Types.VARCHAR);

			cstmt.execute();

			// Retrieve parameters

			// recupera o pdf para fazer assinatura sem política, apenas se ele
			// for diferente de null
			Blob blob = cstmt.getBlob(4);
			if (blob != null)
				pdfCompressed = blob.getBytes(1, (int) blob.length());
			// Temporariamente estamos recuperando o pdf e guardando no cache.
			// byte[] pdfCompressed = Utils.compress(pdf);
			// if (pdfCompressed == null)
			// throw new Exception("Não foi possível comprimir o PDF.");
			// Utils.store(sha1, pdfCompressed);

			status = cstmt.getString(5);
			error = cstmt.getString(6);
		} finally {
			if (cstmt != null)
				cstmt.close();
			if (conn != null)
				conn.close();
		}

		if (pdfCompressed == null)
			throw new Exception("Não foi possível localizar o PDF.");

		// Utils.fileWrite("pdf-compressed.doc", docCompressed);

		// Decompress
		byte[] pdf = Utils.decompress(pdfCompressed);

		// Utils.fileWrite("pdf-uncompressed.doc", doc);

		if (pdf == null)
			throw new Exception("Não foi possível descomprimir o PDF.");

		return pdf;
	}

	@Override
	public String getContext() {
		return "visualizar documento do TextoWeb";
	}
}
