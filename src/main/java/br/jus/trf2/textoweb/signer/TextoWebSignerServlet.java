package br.jus.trf2.textoweb.signer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.SwaggerUtils;
import com.crivano.swaggerservlet.dependency.TestableDependency;

import br.jus.trf2.assijus.system.api.IAssijusSystem;

public class TextoWebSignerServlet extends SwaggerServlet {
	private static final long serialVersionUID = -1611417120964698257L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		super.setAPI(IAssijusSystem.class);

		super.setActionPackage("br.jus.trf2.textoweb.signer");

		super.setAuthorization(SwaggerUtils.getProperty("textowebsigner.password", null));

		addDependency(new TestableDependency("database", "apolods", false, 0, 10000) {
			@Override
			public String getUrl() {
				return "java:/jboss/datasources/ApoloDS";
			}

			@Override
			public boolean test() throws Exception {
				Utils.getConnection().close();
				return true;
			}
		});

	}
}
