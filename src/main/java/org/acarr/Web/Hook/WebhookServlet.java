package org.acarr.Web.Hook;

import org.acarr.BestBuy.GraphicsCard;
import org.acarr.CartHacker;
import org.acarr.DateUtility;
import org.acarr.SMS.SMSUtility;
import org.acarr.Tracking.Product;
import org.acarr.Utility;
import org.acarr.Web.CustomServlet;
import org.acarr.swing.TableMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.reverse;

public class WebhookServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(WebhookServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        writer.println("<html><head>" + getJavaScript() + "<title>Welcome</title></head><body>");
        writer.println(buildProductsFound());
        writer.println("</body></h tml>");
    }

    public String buildProductsFound() {
        StringBuilder output = new StringBuilder();

        return output.toString();
    }

    private String getJavaScript() {
        String js = "<script>" +
                    "</script>";

        return js;
    }

}
