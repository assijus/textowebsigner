package br.jus.trf2.textoweb.signer;

import java.io.ByteArrayInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

import br.jus.trf2.assijus.system.api.IAssijusSystem.DocIdSignPutRequest;
import br.jus.trf2.assijus.system.api.IAssijusSystem.DocIdSignPutResponse;
import br.jus.trf2.assijus.system.api.IAssijusSystem.IDocIdSignPut;

import com.crivano.swaggerservlet.SwaggerUtils;

public class DocIdSignPut implements IDocIdSignPut {

	@Override
	public void run(DocIdSignPutRequest req, DocIdSignPutResponse resp)
			throws Exception {
		Id id = new Id(req.id);
		String envelope = SwaggerUtils.base64Encode(req.envelope);
		String cpf = req.cpf;

		byte[] assinatura = envelope.getBytes("UTF-8");

		byte[] envelopeCompressed = Utils.compress(assinatura);

		// Chama a procedure que faz a gravação da assinatura
		//
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = Utils.getConnection();

			cstmt = conn.prepareCall(Utils.getSQL("save"));

			// p_CodSecao -> Código da Seção Judiciária (50=ES; 51=RJ;
			// 2=TRF)
			cstmt.setInt(1, id.idsecao);

			// p_CodDoc -> Código interno do documento
			cstmt.setLong(2, id.idtextoweb);

			// p_ArqAssin -> Arquivo de assinatura (Compactado)
			cstmt.setBlob(3, new ByteArrayInputStream(envelopeCompressed));

			// CPF
			cstmt.setString(4, cpf);

			// Status
			cstmt.registerOutParameter(5, Types.VARCHAR);

			// Error
			cstmt.registerOutParameter(6, Types.VARCHAR);

			cstmt.execute();

			// Produce response
			String errormsg = cstmt.getString(6);
			if (errormsg != null)
				throw new Exception(errormsg);
			resp.status = "OK";
		} finally {
			if (cstmt != null)
				cstmt.close();
			if (conn != null)
				conn.close();
		}
	}

	@Override
	public String getContext() {
		return "salvar assinatura";
	}
}