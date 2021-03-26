package org.acarr.SMS;

public class SMS {

    private String destinationNumber;

    private String sourceNumber;

    private String smsMessage;

    private String optionalFrom;

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    public String getSourceNumber() {
        return sourceNumber;
    }

    public void setSourceNumber(String sourceNumber) {
        this.sourceNumber = sourceNumber;
    }

    public String getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }

    public String getOptionalFrom() {
        return optionalFrom;
    }

    public void setOptionalFrom(String optionalFrom) {
        this.optionalFrom = optionalFrom;
    }
}
