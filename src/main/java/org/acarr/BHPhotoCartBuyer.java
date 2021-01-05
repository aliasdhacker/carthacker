package org.acarr;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import scala.xml.Elem;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class BHPhotoCartBuyer {

    private static final Logger logger = LoggerFactory.getLogger(BHPhotoCartBuyer.class);

    private URL url;

    private static final String bhPhotoCartURL = "https://www.bhphotovideo.com/find/cart.jsp";

    public BHPhotoCartBuyer(URL url) {
        this.url = url;
    }

    public void attemptToPurchase() {
        try (final WebClient webClient = new WebClient()) {
            CookieManager cookieManager = new CookieManager();
            cookieManager = webClient.getCookieManager();
            cookieManager.setCookiesEnabled(true);
            final HtmlPage page;
            try {

                page = webClient.getPage(url);

                for (DomElement element : page.getElementsByTagName("button")) {
                    if (element.toString().toLowerCase().contains("addtocart")
                            || element.toString().toLowerCase().contains("add to cart")) {
                        addToCartBHPhoto(page, element, webClient);
                    }
                }

                System.exit(3);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addToCartBHPhoto(HtmlPage page, DomElement element, WebClient webClient) throws IOException {

        HtmlButton button = ((HtmlButton) element);
        HtmlPage page2 = webClient.getPage(button.click().getUrl());
        HtmlPage page3 = webClient.getPage(bhPhotoCartURL);
        WebResponse resp = page3.getWebResponse();
        String content = resp.getContentAsString();
        FileWriter fw = new FileWriter("test3.html");
        fw.write(content);
        fw.flush();
        fw.close();
        DomElement checkoutAsGuestButton = null;
        for (DomElement element2 : page.getElementsByTagName("button")) {
            if (element2.toString().toLowerCase().contains("check out as a guest")) {
                checkoutAsGuestButton = element2;
                break;
            }
        }

        if (checkoutAsGuestButton != null) {
            HtmlPage cartForm1 = checkoutAsGuestButton.click();
            // REady To Rock And Roll!!
            logger.info("The Cart PAge: {}", cartForm1);
        }

    }
}
