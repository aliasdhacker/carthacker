package org.acarr.BestBuy;

import org.acarr.Tracking.Product;

import java.net.URL;
import java.time.Instant;

public class GraphicsCard implements Product {
    private String productName;

    private String domain;

    private URL productLink;

    private Instant foundAt;

    private String SKU;

    private URL addToCartURL;

    private URL thumbnailImage;

    private String UPC;

    private String shortDescription;

    private Double salePrice;

    private Double regularPrice;

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setProductLink(URL productLink) {
        this.productLink = productLink;
    }

    public void setFoundAt(Instant foundAt) {
        this.foundAt = foundAt;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public URL getAddToCartURL() {
        return addToCartURL;
    }

    public void setAddToCartURL(URL addToCartURL) {
        this.addToCartURL = addToCartURL;
    }

    public URL getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(URL thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getUPC() {
        return UPC;
    }

    public void setUPC(String UPC) {
        this.UPC = UPC;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(Double regularPrice) {
        this.regularPrice = regularPrice;
    }

    @Override
    public String getProductName() {
        return this.productName;
    }

    @Override
    public String getDomain() {
        return this.domain;
    }

    @Override
    public URL getProductLink() {
        return this.productLink;
    }

    @Override
    public Instant getFoundAt() {
        return this.foundAt;
    }

    @Override
    public String toString() {
        return "GraphicsCard{" +
                "productName='" + productName + '\'' +
                ", domain='" + domain + '\'' +
                ", productLink=" + productLink +
                ", foundAt=" + foundAt +
                ", SKU='" + SKU + '\'' +
                ", addToCartURL=" + addToCartURL +
                ", thumbnailImage=" + thumbnailImage +
                ", UPC='" + UPC + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", salePrice=" + salePrice +
                ", regularPrice=" + regularPrice +
                '}';
    }
}
