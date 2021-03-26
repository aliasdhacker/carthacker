package org.acarr.SMS;

import org.acarr.Tracking.Product;

public  interface SMSService {

    public boolean sendSMSMessage(SMSReceipient receipient, SMS smsMessage, Product product);
}
