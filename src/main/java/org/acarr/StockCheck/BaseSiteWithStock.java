package org.acarr.StockCheck;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.acarr.Receipt;
import org.acarr.swing.TableMaker;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;

public interface BaseSiteWithStock extends Callable<Boolean> {

    @Override
    Boolean call() throws Exception;

    public boolean checkIfInStock();

    public int getArrayIndex();

    public void setArrayIndex(int i);

    public String getRawProduct();

    public Receipt attemptToPurchase(HtmlPage page, DomElement element, WebClient webClient) throws IOException;

    public void setSleepTimer(long sleep);

    public URL getURL();

    public void logCurrentTimeVsSleepTimer();

    public String getSitePrettyName();

    public boolean isInStock();

    public long getLastInStock();

    public void setLastInStock(long lastInStock);

    public void setRawProduct(String string);

    void setGroup(int i);

    public void setTableMaker(TableMaker tableMaker);

    public TableMaker getTableMaker();

    public void updateTableColumnOne(int index);
}
