package br.jus.trf2.textoweb.signer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.crivano.swaggerservlet.Swagger;
import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.SwaggerUtils;

public class TextoWebSignerServlet extends SwaggerServlet {
	private static final long serialVersionUID = -1611417120964698257L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		super.setActionPackage("br.jus.trf2.textoweb.signer");

		Swagger sw = new Swagger();
		sw.loadFromInputStream(this.getClass().getResourceAsStream(
				"/swagger.yaml"));

		super.setSwagger(sw);
		super.setAuthorization(SwaggerUtils.getProperty(
				"textowebsigner.password", null));
	}
}
