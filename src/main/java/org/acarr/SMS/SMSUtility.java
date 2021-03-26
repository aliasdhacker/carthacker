package org.acarr.SMS;

import org.acarr.CartHacker;
import org.acarr.Tracking.Product;

public class SMSUtility {

    public static boolean enableSMS = false;

    public static boolean sendMessages(Product card) {
        SMSService svc = new SMSServiceImpl(CartHacker.getSID(), CartHacker.getTOKEN(), CartHacker.getPhoneId1());

        SMS msg = new SMS();
        SMSReceipient to = new SMSReceipient();
        to.setDestination("+14239489206");
        svc.sendSMSMessage(to, msg, card);

//        to.setDestination("+14234449300");
//        svc.sendSMSMessage(to, msg, prod);

        to.setDestination("+14236205253");
        svc.sendSMSMessage(to, msg, card);

        return true;
    }
}
