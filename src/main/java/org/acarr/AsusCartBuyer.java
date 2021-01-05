package org.acarr;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class AsusCartBuyer {

    private static final Logger logger = LoggerFactory.getLogger(AsusCartBuyer.class);
    public static final String CHECKOUT_PAGE_NAME = "z_asuscheckoutpage.html";

    private final URL theURL;

    public AsusCartBuyer(URL theURL) {
        this.theURL = theURL;
    }

    public void attemptToPurchase() {
        // Buy
        try (final WebClient webClient = new WebClient()) {
            CookieManager cookieManager = new CookieManager();
            cookieManager = webClient.getCookieManager();
            cookieManager.setCookiesEnabled(true);
            final HtmlPage page;
            try {

                page = webClient.getPage(theURL);

                addToCartASUS(page, page.getElementById("item_add_cart"), webClient);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.exit(3);
    }

    private void addToCartASUS(HtmlPage page, DomElement element, WebClient webClient) throws IOException {
        // Handle ASUS buy
        // Asus buy has an "Add to Cart Button" that does nothing, then you must click the "Checkout" button to go cart
        HtmlPage page2 = element.click();
        //add a sleep, because the page takes a min of 5 seconds, we probably need to make this smarter.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
            // ignore
            logger.info("Did not sleep after clicking on 'Add To Cart' on ASUS page.");
        }

        for (DomElement theAnchor : page2.getElementsByTagName("a")) {
            if (theAnchor.toString().toLowerCase().contains("shopping") && theAnchor.toString().toLowerCase().contains("cart")) {
                HtmlPage checkoutPage = ((HtmlAnchor) theAnchor).click();
                File file = new File(CHECKOUT_PAGE_NAME);
                if (file.exists())
                    file.delete();
                FileWriter fw = new FileWriter(CHECKOUT_PAGE_NAME);
                fw.write(checkoutPage.getWebResponse().getContentAsString());
                fw.flush();
                fw.close();

                break;
            }
        }
    }
}
