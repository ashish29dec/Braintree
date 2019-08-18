package com.ashish.assignment.creditcarduploader;

public class CreditCard {
    enum Type {
        AMEX (R.drawable.amex, 15, 4),
        DISCOVER (R.drawable.discover, 16, 3),
        JCB (R.drawable.jcb, 16, 3),
        MASTERCARD (R.drawable.mastercard, 16, 3),
        VISA (R.drawable.visa, 16, 3),
        DEFAULT (R.drawable.unknown, 16, 3);

        private int logo;
        private int length;
        private int cvv_length;

        Type(int logoId, int length, int cvvLength) {
            this.logo = logoId;
            this.length = length;
            this.cvv_length = cvvLength;
        }

        int getLogoImageId() {
            return this.logo;
        }

        int getMaxLength() {
            return this.length;
        }

        int getCVVLength() {
            return this.cvv_length;
        }
    }

    public String number;
    public Type type;
    public String exp;
    public String cvv;
}
