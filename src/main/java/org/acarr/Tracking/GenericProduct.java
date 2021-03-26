package org.acarr.Tracking;

import java.net.URL;
import java.time.Instant;

public class GenericProduct implements Product {

    private static final long serialVersionUID = -6481014130909205493L;

    public GenericProduct() {
    }

    public GenericProduct(String domain, String productName, URL link) {
        foundAt = Instant.now();
        this.domain = domain;
        this.productName = productName;
        this.link = link;
    }

    private String domain;

    private String productName;

    private Instant foundAt;

    private URL link;

    @Override
    public URL getProductLink() {
        return link;
    }

    @Override
    public URL getAddToCartURL() {
        return null;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public String getProductName() {
        return productName;
    }

    public Instant getFoundAt() {
        return foundAt;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setFoundAt(Instant foundAt) {
        this.foundAt = foundAt;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "Store:=" + domain + ", Link=" + link;
    }
}
