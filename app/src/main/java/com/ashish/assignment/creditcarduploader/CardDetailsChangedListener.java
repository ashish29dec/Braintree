package com.ashish.assignment.creditcarduploader;

public interface CardDetailsChangedListener {

    void onCreditCardChanged(CreditCard.Type cardType);

    void onCVVValidityChanged(boolean isValid);

    void onExpirationValidityChanged(boolean isValid);

    void onNumberValidityChanged(boolean isValid);

    void onEntriesValidityChanged();
}
