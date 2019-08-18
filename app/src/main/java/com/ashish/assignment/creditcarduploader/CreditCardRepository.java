package com.ashish.assignment.creditcarduploader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreditCardRepository {

    public CreditCard mCreditCard;

    private CardDetailsChangedListener mListener;

    public CreditCardRepository(CardDetailsChangedListener listener) {
        mCreditCard = new CreditCard();
        mListener = listener;
        mCreditCard.type = CreditCard.Type.DEFAULT;
        mListener.onCreditCardChanged(mCreditCard.type);
    }

    public void onCreditCardNumberChanged(String number) {
        if (number == null || number.length() == 0) {
            mCreditCard.number = "";
            mCreditCard.type = CreditCard.Type.DEFAULT;
            return;
        }
        mCreditCard.number = number;
        CreditCard.Type type = identifyCardType(number);
        mCreditCard.type = type;
        if (mCreditCard.cvv == null || mCreditCard.cvv.length() == 0 || mCreditCard.cvv.length() == type.getCVVLength()) {
            mListener.onCVVValidityChanged(true);
        } else {
            mListener.onCVVValidityChanged(false);
        }
        mListener.onCreditCardChanged(mCreditCard.type);
    }

    public boolean checkIfExpiryIsValid() {
        boolean isValid = false;
        String exp = mCreditCard.exp;
        if (exp != null && exp.length() == 5) {
            if (exp.charAt(2) == '/') {
                String month = exp.substring(0, 2);
                String year = exp.substring(3);
                isValid = isValidMonth(month) && isValidYear(year) && isValidMonthYear(month, year);
            }
        }
        return isValid;
    }

    private boolean isNumberValid() {
        if (mCreditCard.number == null || mCreditCard.number.length() != mCreditCard.type.getMaxLength()) {
            return false;
        }
        return doesPassLunhAlgorithm(mCreditCard.number);
    }

    private boolean isValidMonth(String month) {
        if (month == null || month.length() != 2) {
            return false;
        }

        char m1 = month.charAt(0);
        char m2 = month.charAt(1);
        if (m1 != '0' && m1 != '1') {
            return false;
        }
        if (m1 == '0' && (m2 <= '0' || m2 > '9')) {
            return false;
        }
        if (m1 == '1' && (m2 < '0' || m2 > '2')) {
            return false;
        }
        return true;
    }

    private boolean isValidYear(String year) {
        if (year == null || year.length() != 2) {
            return false;
        }
        char y1 = year.charAt(0);
        char y2 = year.charAt(1);

        if (y1 < '0' || y1 > '9' || y2 < '0' || y2 > '9') {
            return false;
        }

        return true;
    }

    private boolean isValidMonthYear(String month, String year) {
        if (!isValidMonth(month) || !isValidYear(year)) {
            return false;
        }

        DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
        String currentYear = df.format(Calendar.getInstance().getTime());
        if (year.equalsIgnoreCase(currentYear)) {
            // Check if month is same month or greater than current month
            DateFormat mdf = new SimpleDateFormat("MM");
            String currentMonth = mdf.format(Calendar.getInstance().getTime());
            char m11 = month.charAt(0);
            char m12 = month.charAt(1);

            char m21 = currentMonth.charAt(0);
            char m22 = currentMonth.charAt(1);

            if (m11 < m21 || (m11 == m21 && m12 < m22)) {
                return false;
            }
        }
        return true;
    }

    private CreditCard.Type identifyCardType(String number) {
        int length = number.length();
        char c = number.charAt(0);
        switch (c) {
            case '4':
                return CreditCard.Type.VISA;
            case '5':
                return CreditCard.Type.MASTERCARD;
            case '6':
                return CreditCard.Type.DISCOVER;
        }

        if (c == '3' && length > 1) {
            c = number.charAt(1);
            switch (c) {
                case '4':
                case '7':
                    return CreditCard.Type.AMEX;
                case '5':
                    return CreditCard.Type.JCB;
            }
        }
        return CreditCard.Type.DEFAULT;
    }

    private boolean isCVVValid() {
        if (mCreditCard.cvv == null || mCreditCard.cvv.length() != mCreditCard.type.getCVVLength()) {
            return false;
        }
        return true;
    }

    private boolean doesPassLunhAlgorithm(String number) {
        int length = number.length();

        int j = 0;
        int sum = 0;
        for (int i = length - 1; i >= 0; i--) {
            int digit = number.charAt(i) - '0';
            if (j%2 == 1) {
                digit *= 2;
                int q = digit/10;
                int m = digit%10;
                digit = m + q;
            }
            sum += digit;
            j++;
        }

        return sum%10 == 0;
    }

    public void validateAllFields() {
        boolean isNumValid = isNumberValid();
        boolean isExpiryValid = checkIfExpiryIsValid();
        boolean isCvvValid = isCVVValid();

        mListener.onNumberValidityChanged(isNumValid);
        mListener.onExpirationValidityChanged(isExpiryValid);
        mListener.onCVVValidityChanged(isCvvValid);

        mListener.onEntriesValidityChanged();
    }
}
