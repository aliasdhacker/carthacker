package org.acarr.Web;

import org.acarr.CartHacker;
import org.acarr.SMS.SMSUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ToggleServicesServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ToggleServicesServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        String key = req.getParameter("key");
        String service = req.getParameter("service");

        if (key != null && key.equals("asdfasdf") && service != null) {
            if (service.equals("sms")) {
                if (SMSUtility.enableSMS)
                    SMSUtility.enableSMS = false;
                else
                    SMSUtility.enableSMS = true;

                writer.println("toggled sms to " + SMSUtility.enableSMS);
            } else if (service.equals("bbapi")) {
                if (CartHacker.useBestBuyAPI)
                    CartHacker.useBestBuyAPI = false;
                else {
                    CartHacker.useBestBuyAPI = true;
                }

                writer.println("toggled bbapi to " + CartHacker.useBestBuyAPI);
            } else if (service.equals("othersites")) {
                if (CartHacker.trackOtherSites)
                    CartHacker.trackOtherSites = false;
                else {
                    CartHacker.trackOtherSites = true;
                }

                writer.println("toggled trackOtherSites to " + CartHacker.trackOtherSites);
            } else if (service.equals("bestbuypricefilter")) {
                String price = req.getParameter("price");
                if (price != null) {
                    CartHacker.BEST_BUY_FILTER_CARDS_BELOW_THIS_DOLLAR_VALUE = Double.parseDouble(price);
                }
                writer.println("changed price filter to " + CartHacker.BEST_BUY_FILTER_CARDS_BELOW_THIS_DOLLAR_VALUE);
            } else {
                writer.println(" no svcid - foff ");
            }
        } else {
            writer.println(" no key - foff ");
        }
    }

}
