package br.jus.trf2.textoweb.signer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.crivano.restservlet.IRestAction;

public class DocListGet implements IRestAction {

	@Override
	public void run(JSONObject req, JSONObject resp) throws Exception {
		// Parse request
		String cpf = req.getString("cpf");

		// Setup json array
		JSONArray list = new JSONArray();

		// Get documents from Oracle
		//
		// Detalhes Parâmetros:
		// NUM_CPF : Número do CPF sem caracteres especiais.
		//
		// Detalhes dos campos:
		// IdSecao : Identificador único da Seção.
		// IdTextoWeb : Identificador único do Documento.
		// CodDocumento : Número do processo a qual o documento está
		// vinculado.
		// ArquivoCompactado: Vetor de bytes do arquivo PDF compactado.
		// TipoDeTexto : Tipo do texto. (Relatório, Voto, etc.)
		// IncidenteDocumento: Descrição do incidente que se refere o
		// documento.
		//
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = Utils.getConnection();
			pstmt = conn.prepareStatement(Utils.getSQL("list"));
			pstmt.setString(1, cpf);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				JSONObject doc = new JSONObject();

				Id id = new Id(cpf, rset.getInt("IdSecao"),
						rset.getLong("IdTextoWeb"));
				doc.put("id", id.toString());
				doc.put("code", rset.getString("CodDocumento"));
				doc.put("descr", rset.getString("IncidenteDocumento"));
				doc.put("kind", rset.getString("TipoDeTexto"));
				doc.put("origin", "TextoWeb");
				doc.put("urlHash", "textoweb/doc/" + doc.getString("id")
						+ "/hash");
				doc.put("urlSave", "textoweb/doc/" + doc.getString("id")
						+ "/sign");
				doc.put("urlView", "textoweb/doc/" + doc.getString("id")
						+ "/pdf");
				list.put(doc);

				// Acrescenta essa informação na tabela para permitir a
				// posterior visualização.
				Utils.store(cpf + "-" + id.toString(), new byte[] { 1 });
			}
		} finally {
			if (rset != null)
				rset.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		}

		resp.put("list", list);
	}

	@Override
	public String getContext() {
		return "listar documentos do TextoWeb";
	}

}
