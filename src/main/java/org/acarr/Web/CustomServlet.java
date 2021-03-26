package org.acarr.Web;

import org.acarr.CartHacker;
import org.acarr.swing.TableMaker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        writer.println("<html><head>" + getJavaScript() + "<title>Welcome</title></head><body>");
        writer.println("<div>" +
                "<h2>" +
                "GO BACK TO <a href=\"https://www.startgpumining.com\">startgpumining</a> to signup and start getting text alerts about incoming product availability!!" +
                "</h2>" +
                "<p>" +
                "I just got a EVGA 3080 FTW3 at Retail price $922 including shipping and tax -  They are going for 2500$ on ebay.  Other testimonials to come soon!" +
                "</p>" +
                "</div>" +"<h2>CURRENTLY SEARCHING " + TableMaker.getData().length + " URLS!!!</h2>" +
                "<h3>Click <a href=\"/\">here</a> to view summary page.</h3><hr/>");
        writer.println(getOutput());
        writer.println("</body></html>");
    }

    public String getOutput() {
        Object[][] data = TableMaker.getData();
        StringBuilder output = new StringBuilder(

                        "NO URL DATA TO WATCH!!! Check the 'ProductPageList.txt` file is present in program directory and has product urls in it!");
        if (data != null && data.length != 0) {
            output = new StringBuilder("<table><thead><th>Website</th><th>Status</th><th>rawoutput</th></thead><tbody>");
            for (Object[] objects : data) {
                output.append("<tr><td>" + getProductLink(objects[0]) + "</td><td id='status'>" + objects[1] + "</td><td>" + objects[2] + "</td></tr>");
            }
            output.append("</tbody></table>");
        }

        return output.toString();
    }

    public static String getProductLink(Object object) {
        String url = object==null?"":object.toString();
        StringBuilder anchor = new StringBuilder("<a target=\"blank\" href=\"");
        anchor.append(url);
        anchor.append("\">");
        anchor.append("Open Product");
        anchor.append("</a>");

        return anchor.toString();
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
