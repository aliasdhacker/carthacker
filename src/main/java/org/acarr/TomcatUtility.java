package org.acarr;

import org.acarr.Web.CustomServlet;
import org.acarr.Web.ProductsServlet;
import org.acarr.Web.ToggleServicesServlet;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import java.io.File;

public class TomcatUtility {
    private static final Logger logger = LoggerFactory.getLogger(TomcatUtility.class);

    private static String keystoreFilename = "/opt/tomcat/tomcat.keystore.jks";

    private static int serverPort = 8080;

    private static int serverSSLPort = 8443;

    private static String keystoreAlias = "key";

    private static String keystorePassword = "changeit";

    private static boolean ssl = true;

    public static Tomcat createTomcatServer() {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));
        tomcat.setPort(serverPort);
        if (ssl) {
            tomcat.setConnector(TomcatUtility.createSSLConnector());
        }
        Connector connector = new Connector();
        connector.setPort(serverPort);
        tomcat.setConnector(connector);
        String contextPath = "";
        String docBase = new File(".").getAbsolutePath();

        Context context = tomcat.addContext(contextPath, docBase);
        HttpServlet servlet = new CustomServlet();
        HttpServlet productServlet = new ProductsServlet();
        HttpServlet toggleSMS = new ToggleServicesServlet();
        String servlet3Name = "ToggleSMS";
        String url3Pattern = "/toggle";
        String servletName = "Servlet1";
        String urlPattern = "/all";
        String servlet2Name = "ProductServlet";
        String url2Pattern = "/";
        tomcat.addServlet(contextPath, servletName, servlet);
        tomcat.addServlet(contextPath, servlet2Name, productServlet);
        tomcat.addServlet(contextPath, servlet3Name, toggleSMS);
        context.addServletMappingDecoded(url3Pattern, servlet3Name);
        context.addServletMappingDecoded(urlPattern, servletName);
        context.addServletMappingDecoded(url2Pattern, servlet2Name);

        return tomcat;
    }

    private static Connector createSSLConnector() {
        Connector connector = new Connector();
        connector.setPort(TomcatUtility.serverSSLPort);
        connector.setSecure(true);
        connector.setScheme("https");
        connector.setAttribute("keyAlias", TomcatUtility.keystoreAlias);
        connector.setAttribute("keystorePass", TomcatUtility.keystorePassword);
        connector.setAttribute("keystoreType", "JKS");
        connector.setAttribute("keystoreFile", TomcatUtility.keystoreFilename);
//        connector.setAttribute("certificateFile", "/etc/ssl/startgpumining.com/e190e4fcc9ca32b0.pem");
//        connector.setAttribute("certificateKeyFile", "/etc/ssl/startgpumining.com/e190e4fcc9ca32b0.pem");
        connector.setAttribute("clientAuth", "false");
        connector.setAttribute("protocol", "HTTP/1.1");
        connector.setAttribute("sslProtocol", "TLS");
        connector.setAttribute("maxThreads", "200");
        connector.setAttribute("protocol", "org.apache.coyote.http11.Http11AprProtocol");
        connector.setAttribute("SSLEnabled", true);
        return connector;
    }

    public static void setServerSSLPort(String arg) {
        try {
            TomcatUtility.serverSSLPort = Integer.parseInt(arg);
        } catch (Exception e) {
            logger.error("Error setting ssl port, using 8443.", e);
            TomcatUtility.serverSSLPort = 8443;
        }
    }

    public static void setServerPort(String arg) {
        try {
            TomcatUtility.serverPort = Integer.valueOf(arg);
        } catch (Exception e) {
            logger.error("Error getting server port from command line, using 8080.", e);
            TomcatUtility.serverPort = 8080;
        }
    }

    public static void setKeystoreAlias(String arg) {
        keystoreAlias = arg;
    }

    public static void setKeystorePassword(String keystorePassword) {
        TomcatUtility.keystorePassword = keystorePassword;
    }

    public static void setKeystoreFilename(String keystoreFilename) {
        TomcatUtility.keystoreFilename = keystoreFilename;
    }

    public static void setSsl(boolean ssl) {
        TomcatUtility.ssl = ssl;
    }

    public static boolean keystoreNotFound() {
        File file = new File(keystoreFilename);
        if (file.exists())
            return false;
        return true;
    }
}
