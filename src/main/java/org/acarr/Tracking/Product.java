package org.acarr.Tracking;

import java.io.Serializable;
import java.net.URL;
import java.time.Instant;

public interface Product extends Serializable {

    public String getProductName();

    public String getDomain();

    public URL getProductLink();

    public URL getAddToCartURL();

    public Instant getFoundAt();

}
