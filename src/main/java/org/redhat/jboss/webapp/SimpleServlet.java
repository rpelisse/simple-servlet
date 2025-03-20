package org.redhat.jboss.webapp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name="info-webapp", urlPatterns="/*")
public class SimpleServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

    private static final String APP_VERSION = "1.3";
	private static final String TAB = "\t";
	private static final String PLAIN_TEXT_TYPE = "plain/text";
	//private static final String PLAIN_TEXT_TYPE = "text/html";
    private static final String WILDFLY_CLUSTER_CONFIG = "WILDFLY_CLUSTER_CONFIG";
	private static final String UTF8_ENC = "UTF-8";

	private static HttpServletResponse configureResponse(HttpServletResponse response) {
		response.setContentType(PLAIN_TEXT_TYPE);
		response.setCharacterEncoding(UTF8_ENC);
		return response;
	}

	private static String tabs(int nbTabs) {
		final StringBuffer tabs = new StringBuffer(nbTabs);
		for ( int i = 0; i < nbTabs ; i++ )
			tabs.append(TAB);
		return tabs.toString();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {

		response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		response = configureResponse(response);

		addToResponse(response, WILDFLY_CLUSTER_CONFIG + ": " + System.getProperty(WILDFLY_CLUSTER_CONFIG) );
        addToResponse(response, "Info App Version: v" + APP_VERSION);
		response.setStatus(HttpServletResponse.SC_OK);
		return;
	}

	private static String getRemoteHostString(final HttpServletRequest request) {
		return request.getRemoteHost() + " [IP: " + request.getRemoteAddr() + ", port: " + request.getRemotePort()  + " ";
	}

	private static String getLocalHostname()  {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			throw new IllegalStateException("Can't add retrieve local hostname ! Something is quite wrong server side.");
		}
	}

	private static String getLocalHostIP()  {
		try {
			return 	InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			throw new IllegalStateException("Can't add retrieve local hostname ! Something is quite wrong server side.");
		}
	}


	private static String getLocalHostnameString()  {
			return getLocalHostname() + " [IP: " + getLocalHostIP() + " ]" ;
	}

	private static HttpServletResponse addToResponse(final HttpServletResponse response, String content) {
		try {
			response.getWriter().write(content + "\n");
		} catch (IOException ioe ) {
			throw new IllegalStateException("Can't add data to the HTTP response ! Something is quite wrong server side.");
		}
		return response;
	}
}
