package com.ashish.assignment.creditcarduploader;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class CreditCardViewModel extends ViewModel implements CardDetailsChangedListener {

    private CreditCardRepository mRepo;

    private MutableLiveData<Integer> numberMaxLength = new MutableLiveData<>();
    private MutableLiveData<Integer> cvvMaxLength = new MutableLiveData<>();
    private MutableLiveData<Integer> cardLogoResId = new MutableLiveData<>();
    private MutableLiveData<Boolean> isExpirationValid = new MutableLiveData<>();
    private MutableLiveData<Boolean> isCvvValid = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNumberValid = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSubmissionSuccessful = new MutableLiveData<>();

    public CreditCardViewModel() {
        mRepo = new CreditCardRepository(this);
    }

    @Override
    public void onCVVValidityChanged(boolean isValid) {
        isCvvValid.setValue(isValid);
    }

    @Override
    public void onCreditCardChanged(CreditCard.Type cardType) {
        numberMaxLength.setValue(cardType.getMaxLength());
        cvvMaxLength.setValue(cardType.getCVVLength());
        cardLogoResId.setValue(cardType.getLogoImageId());
    }

    @Override
    public void onExpirationValidityChanged(boolean isValid) {
        isExpirationValid.setValue(isValid);
    }

    @Override
    public void onNumberValidityChanged(boolean isValid) {
        isNumberValid.setValue(isValid);
    }

    @Override
    public void onEntriesValidityChanged() {
        isSubmissionSuccessful.setValue(isCvvValid.getValue() && isExpirationValid.getValue() && isNumberValid.getValue());
    }

    public void onCreditCardChangedListener(String number) {
        mRepo.onCreditCardNumberChanged(number);
    }

    public void onExpirationChanged(String exp) {
        mRepo.mCreditCard.exp = exp;
    }

    public void onCVVChanged(String cvv) {
        mRepo.mCreditCard.cvv = cvv;
    }

    public void expirationFocusChanged(String exp, boolean hasFocus) {
        if (hasFocus) {
            isExpirationValid.setValue(true);
            return;
        }
        isExpirationValid.setValue(mRepo.checkIfExpiryIsValid());
    }

    public void onNumberFocusChanged(String number, boolean hasFocus) {
        if (!hasFocus) {
            isNumberValid.setValue(number.length() == mRepo.mCreditCard.type.getMaxLength());
        }
    }

    public void onCVVFocusChanged(String cvv, boolean hasFocus) {
        if (!hasFocus) {
            isCvvValid.setValue(cvv.length() == mRepo.mCreditCard.type.getCVVLength());
        }
    }

    public void onSubmitClicked() {
        // Validate all the fields
        mRepo.validateAllFields();
    }

    public LiveData<Integer> getMaxLength() {
        return numberMaxLength;
    }

    public LiveData<Integer> getCVVLength() {
        return cvvMaxLength;
    }

    public LiveData<Integer> getCardLogoResId() {
        return cardLogoResId;
    }

    public LiveData<Boolean> isExpirationValid() {
        return isExpirationValid;
    }

    public LiveData<Boolean> isCVVValid() {
        return isCvvValid;
    }

    public LiveData<Boolean> isNumberValid() {
        return isNumberValid;
    }

    public LiveData<Boolean> isSubmissionSuccessful() {
        return isSubmissionSuccessful;
    }
}


