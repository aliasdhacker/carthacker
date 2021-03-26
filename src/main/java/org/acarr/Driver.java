package org.acarr;

import geb.Browser;
import groovy.lang.Closure;
import org.openqa.selenium.WebDriver;

public class Driver {

    public void start(String url) {
        Browser browser = new Browser();
        browser.go(url);
//        browser.withNewWindow(null);

//        browser.withNewWindow(new WindowOpeningBlock(new Object()), new WindowExecutingBlock(new Object());
    }

//    class WindowOpeningBlock extends Closure {
//
//        public WindowOpeningBlock(Object owner) {
//            super(owner);
//        }
//    }
//
//    class WindowExecutingBlock extends Closure {
//
//        public WindowExecutingBlock(Object owner) {
//            super(owner);
//        }
//    }
}
