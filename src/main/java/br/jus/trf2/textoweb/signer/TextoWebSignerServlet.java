package br.jus.trf2.textoweb.signer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.dependency.TestableDependency;

import br.jus.trf2.assijus.system.api.IAssijusSystem;

public class TextoWebSignerServlet extends SwaggerServlet {
	private static final long serialVersionUID = -1611417120964698257L;

	@Override
	public void initialize(ServletConfig config) throws ServletException {
		setAPI(IAssijusSystem.class);
		setActionPackage("br.jus.trf2.textoweb.signer");

		addRestrictedProperty("datasource.name", "java:/jboss/datasources/ApoloDS");
		addRestrictedProperty("datasource.url", null);
		addRestrictedProperty("datasource.username", null);
		addPrivateProperty("datasource.password", null);
		addRestrictedProperty("datasource.schema", "testeapolotrf");

		addPrivateProperty("password", null);
		super.setAuthorization(getProperty("password"));

		addDependency(new TestableDependency("database", "apolods", false, 0, 10000) {
			@Override
			public String getUrl() {
				return getProperty("datasource.name");
			}

			@Override
			public boolean test() throws Exception {
				Utils.getConnection().close();
				return true;
			}
		});

	}
}
