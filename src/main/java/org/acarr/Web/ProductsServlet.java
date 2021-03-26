package org.acarr.Web;

import org.acarr.BestBuy.GraphicsCard;
import org.acarr.CartHacker;
import org.acarr.DateUtility;
import org.acarr.SMS.SMSUtility;
import org.acarr.ThreadManager;
import org.acarr.Tracking.Product;
import org.acarr.Utility;
import org.acarr.swing.TableMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.*;

public class ProductsServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ProductsServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        writer.println("<html><head>" + getJavaScript() + "<title>Welcome</title></head><body>");
        writer.println(buildProductsFound());
        writer.println("</body></html>");
    }

    public String buildProductsFound() {
        StringBuilder output = new StringBuilder();
        ArrayList<Product> list = null;
        try {
            list = Utility.loadProducts();
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Error in servlet,", e);
            }
        }

        output.append((SMSUtility.enableSMS ? "" +
                "<button style=\"margin:5px\" type=\"button\" class=\"btn btn-success\">SMS ALERTS</button>" :
                "<button style=\"margin:5px\" type=\" button \" class=\"btn btn-danger \">SMS ALERTS</button>") +

                (CartHacker.useBestBuyAPI ? "<button style=\"margin:5px\" type=\"button\" class=\"btn btn-success\">Best Buy API</button>" :
                        "<button type=\"button\" style=\"margin:5px\" class=\"btn btn-danger \">Best Buy API</button>") +
                (CartHacker.trackOtherSites ? "<button style=\"margin:5px\" type=\"button\" class=\"btn btn-success\">Other Site Tracking</button>" :
                        "<button type=\"button\" style=\"margin:5px\" class=\"btn btn-danger \">Other Site Tracking</button>") +
                "<h2>CURRENTLY SEARCHING " + TableMaker.getData().length + " URLS!!! Products will appear here (recent at top) as found.</h2>" +
                "<div>" +

                "</div>" +
                "<div style='clear:both;'>" +
                "<h3>When BestBuy API is on, Polling every: " + (CartHacker.MINIMUM_LOOP_TIME_BEST_BUY / 1000) +
                " secs for graphics cards that cost more than $" + CartHacker.BEST_BUY_FILTER_CARDS_BELOW_THIS_DOLLAR_VALUE + " </h3>" +
                "<h3>Click <a href=\"all\">here</a> to view stock check by URL.</h3><hr/>" +
                "</div>");
        if (list == null || list.isEmpty()) {
            output.append("NO PRODUCTS FOUND!!!");
        } else {
            output.append("<list>");
            List<Product> sorted = list.stream().sorted(
                    Comparator.comparing(Product::getFoundAt)
            ).collect(Collectors.toList());
            reverse(sorted);
            for (Product product : sorted) {
                String link = CustomServlet.getProductLink(product.getProductLink());
                if (product instanceof GraphicsCard) {
                    link = CustomServlet.getProductLink(product.getAddToCartURL());
                }
                output.append("<ul>");
                output.append("Product link: " + link + "</br>");
                output.append("When was it seen? " + DateUtility.formatter.format(product.getFoundAt()) + "</br>");
                output.append(product.toString());
                output.append("</ul>");
            }
            output.append("</list>");
        }

        return output.toString();
    }

    private String getJavaScript() {
        String js = "<script>" +
                "var limit=\"0:05\"\n" +
                "\n" +
                "var doctitle = document.title\n" +
                "var parselimit=limit.split(\":\")\n" +
                "parselimit=parselimit[0]*60+parselimit[1]*1\n" +
                "\n" +
                "function beginrefresh(){\n" +
                "\tif (parselimit==1)\n" +
                "\t\twindow.location.reload()\n" +
                "\telse{ \n" +
                "\t\tparselimit-=1\n" +
                "\t\tcurmin=Math.floor(parselimit/60)\n" +
                "\t\tcursec=parselimit%60\n" +
                "\t\tif (curmin!=0)\n" +
                "\t\t\tcurtime=curmin+\" minutes and \"+cursec+\" seconds left until page refresh!\"\n" +
                "\t\telse\n" +
                "\t\t\tcurtime=cursec+\" seconds left until page refresh!\"\n" +
                "\t\tdocument.title = doctitle + ' (' + curtime +')'\n" +
                "\t\tsetTimeout(\"beginrefresh()\",1000)\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "if (window.addEventListener)\n" +
                "\twindow.addEventListener(\"load\", beginrefresh, false)\n" +
                "else if (window.attachEvent)\n" +
                "\twindow.attachEvent(\"load\", beginrefresh)\n" +
                "\n" +
                "//-->\n" +
                "</script>";

        return js;
    }

}
