package org.acarr.BestBuy;

import jdk.jshell.execution.Util;
import org.acarr.CartHacker;
import org.acarr.DateUtility;
import org.acarr.Tracking.Product;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class API implements Callable<List<Product>> {

    private static final Logger log = LoggerFactory.getLogger(API.class);

    private static final String API_KEY = "qIbqoull72xm69GsEk5m8KnM";

    private long lastAPICall;

    private double filterPriceAmount = 0;

    public API(long lastBestBuyHit) {
        lastAPICall = lastBestBuyHit;
    }

    public void setLastAPICall(long lastAPICall) {
        this.lastAPICall = lastAPICall;
    }

    public long getLastAPICall() {
        return lastAPICall;
    }

    public void setFilterPriceAmount(double filterPriceAmount) {
        this.filterPriceAmount = filterPriceAmount;
    }

    public double getFilterPriceAmount() {
        return filterPriceAmount;
    }

    @Override
    public List<Product> call() throws Exception {
        long current = System.currentTimeMillis();
        long elapsedTimeSinceLastCall = current - lastAPICall;

        while (elapsedTimeSinceLastCall < CartHacker.MINIMUM_LOOP_TIME_BEST_BUY) {
            try {
                Thread.sleep(30);
                elapsedTimeSinceLastCall = System.currentTimeMillis() - lastAPICall;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.info("BBUY CALLED : ELAPSED {} MILLIS", elapsedTimeSinceLastCall);
        return getGraphicsCards();
    }

    public List<Product> getGraphicsCards() throws IOException {
        String prequeryparam = "((search=graphics&search=card)&onlineAvailability=true&(categoryPath.id=abcat0507002))";
        String requestForGfxCard = "https://api.bestbuy.com/v1/products";
        String addtional = "sort=name.asc&show=addToCartUrl,name,sku,thumbnailImage,url,upc,shortDescription,salePrice,regularPrice,shippingCost&pageSize=100&format=json";
        // "?sort=name.asc&show=addToCartUrl,name,sku,thumbnailImage,url,upc,shortDescription,salePrice,regularPrice,shippingCost&pageSize=100&format=json";
        Map<String, String> parameters = new HashMap<>();
        URL url = new URL(requestForGfxCard + prequeryparam + "?apiKey=" + API_KEY + "&" + addtional);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("GET");

//        logServerHit();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        JSONObject parser = new JSONObject(content.toString());
        JSONArray arr = parser.getJSONArray("products");
        List<Product> cards = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            Map map = arr.getJSONObject(i).toMap();
            GraphicsCard card = new GraphicsCard();
            for (Object key : map.keySet()) {
                switch (key.toString()) {
                    case "addToCartUrl":
                        card.setAddToCartURL(new URL((String) map.get(key)));
                        break;
                    case "name":
                        card.setProductName((String) map.get(key));
                        break;
                    case "sku":
                        card.setSKU(String.valueOf(map.get(key)));
                        break;
                    case "thumbnailImage":
                        card.setThumbnailImage(new URL((String) map.get(key)));
                        break;
                    case "url":
                        card.setProductLink(new URL((String) map.get(key)));
                        break;
                    case "upc":
                        card.setUPC((String) map.get(key));
                        break;
                    case "shortDescription":
                        card.setShortDescription((String) map.get(key));
                        break;
                    case "salePrice":
                        card.setSalePrice(((BigDecimal) map.get(key)).doubleValue());
                        break;
                    case "regularPrice":
                        card.setRegularPrice(((BigDecimal) map.get(key)).doubleValue());
                        break;
                    case "shippingCost":

                        break;
                    default:
                        break;
                }

            }

            if (card.getRegularPrice() > filterPriceAmount) {
                card.setFoundAt(Instant.now());
                cards.add(card);
            }
        }

        for (Product card : cards) {
//            System.out.println(
//                    ((GraphicsCard) card).toString());
        }
        in.close();
        con.disconnect();
        setLastAPICall(System.currentTimeMillis());

        return cards;
    }

    public void logServerHit() {
        log.info("BBAPI Server hit at {}", DateUtility.formatter.format(Instant.ofEpochMilli(lastAPICall)));
    }

    public static class ParameterStringBuilder {
        public static String getParamsString(Map<String, String> params)
                throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("&");
            }

            String resultString = result.toString();
            return resultString.length() > 0
                    ? resultString.substring(0, resultString.length() - 1)
                    : resultString;
        }
    }

    public static void main(String[] args) throws IOException {
        //test
        API api = new API(0);


        for (Product prod : api.getGraphicsCards()) {
            System.out.println(((GraphicsCard) prod));
        }

    }

}
