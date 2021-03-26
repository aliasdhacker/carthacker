package org.acarr.SMS;

import com.twilio.Twilio;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.acarr.BestBuy.GraphicsCard;
import org.acarr.CartHacker;
import org.acarr.Tracking.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class SMSServiceImpl implements SMSService {

    // **** DO NOT SEND A TEXT MESSAGE MORE FREQUENTLY THAN EVERY 30 SECONDS ***** //
// **** DO NOT SEND A TEXT MESSAGE MORE FREQUENTLY THAN EVERY 30 SECONDS ***** //
// **** DO NOT SEND A TEXT MESSAGE MORE FREQUENTLY THAN EVERY 30 SECONDS ***** //
// **** DO NOT SEND A TEXT MESSAGE MORE FREQUENTLY THAN EVERY 30 SECONDS ***** //

    public static long LAST_TEXT_MESSAGE_SENT_DO_NOT_SEND_ANOTHER = 0;

    // **** DO NOT SEND A TEXT MESSAGE MORE FREQUENTLY THAN EVERY 30 SECONDS ***** //
// **** DO NOT SEND A TEXT MESSAGE MORE FREQUENTLY THAN EVERY 30 SECONDS ***** //
// **** DO NOT SEND A TEXT MESSAGE MORE FREQUENTLY THAN EVERY 30 SECONDS ***** //


    private String SID;

    private String token;

    private String mySMSnumber;

    private static Logger logger = LoggerFactory.getLogger(SMSServiceImpl.class);

    private TwilioRestClient client;

    public SMSServiceImpl(String sid, String token, String mySMSnumber) {
        this.SID = sid;
        this.mySMSnumber = mySMSnumber;
        this.token = token;
    }

    @Override
    public boolean sendSMSMessage(SMSReceipient receipient, SMS smsMessage, Product product) {

        if (SMSUtility.enableSMS) {

            if(LAST_TEXT_MESSAGE_SENT_DO_NOT_SEND_ANOTHER==0) {
                /*
                There is something wrong, we just started, or something else, we can't just blindly send a message, even
                if it really is just the first one... can we know programmatically?  We would need absolute confidence
                in our application logic, and for fucks sake, we don't have that.
                 */

                checkWithTwilioToEnsureWeHavenNotSentATextRecently()
            }

            Twilio.init(SID, token);
            Message message = Message.creator(
                    new PhoneNumber(receipient.getDestination()),
                    new PhoneNumber(mySMSnumber),
                    (product.getProductName() + " - " + ((product instanceof GraphicsCard) ? product.getAddToCartURL()
                            : product.getProductLink()))).create();
            return true;

        }

        logger.info("SMS DISABLED!");
        return false;
    }

    private void checkWithTwilioToEnsureWeHavenNotSentATextRecently() {

    }

    public static void main(String[] testargs) throws MalformedURLException {
        //test
        SMSUtility.enableSMS = true;
        SMSService svc = new SMSServiceImpl(CartHacker.getSID(), CartHacker.getTOKEN(), CartHacker.getPhoneId1());
        GraphicsCard prod = new GraphicsCard();
        prod.setProductName("IT WORKS YOU SILLY BASTID");
        prod.setAddToCartURL(new URL("http://test.url"));
        SMS msg = new SMS();
        SMSReceipient to = new SMSReceipient();
        to.setDestination("+14239489206");
        svc.sendSMSMessage(to, msg, prod);

//        to.setDestination("+14234449300");
//        svc.sendSMSMessage(to, msg, prod);

//        to.setDestination("+14236205253");
//        svc.sendSMSMessage(to, msg, prod);
    }
}
