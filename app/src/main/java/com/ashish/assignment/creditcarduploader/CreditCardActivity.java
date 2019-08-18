package com.ashish.assignment.creditcarduploader;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CreditCardActivity extends AppCompatActivity {

    private static final int FIELD_ID_CREDIT_CARD_NUMBER = 0;
    private static final int FIELD_ID_EXPIRATION = 1;
    private static final int FIELD_ID_CVV = 2;

    private EditText mCreditCard;
    private TextView mNumberError;
    private EditText mExpiration;
    private TextView mExpirationError;
    private EditText mCVV;
    private TextView mCVVError;
    private ImageView mLogo;
    private Button mSubmit;
    private TextView mSuccess;

    private CreditCardViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);

        mCreditCard = findViewById(R.id.id_credit_card_number);
        mNumberError = findViewById(R.id.id_number_error);
        mLogo = findViewById(R.id.id_credit_card_logo);
        mExpiration = findViewById(R.id.id_expiration);
        mExpirationError = findViewById(R.id.id_expiration_error);
        mCVV = findViewById(R.id.id_cvv);
        mCVVError = findViewById(R.id.id_cvv_error);
        mSubmit = findViewById(R.id.id_submit);
        mSuccess = findViewById(R.id.id_success);

        mViewModel = ViewModelProviders.of(this).get(CreditCardViewModel.class);

        mViewModel.getMaxLength().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer maxCardLength) {
                setLengthFilter(FIELD_ID_CREDIT_CARD_NUMBER, maxCardLength);
            }
        });

        mViewModel.getCVVLength().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer maxCVVLength) {
                setLengthFilter(FIELD_ID_CVV, maxCVVLength);
                Resources res = getResources();
                mCVVError.setText(res.getString(R.string.cvv_error, maxCVVLength));
            }
        });

        mViewModel.getCardLogoResId().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer logoResId) {
                Resources res = getResources();
                Drawable logo = res.getDrawable(logoResId, getTheme());
                mLogo.setImageDrawable(logo);
            }
        });

        mViewModel.isExpirationValid().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isValid) {
                mExpirationError.setVisibility(isValid ? View.GONE : View.VISIBLE);
            }
        });

        mViewModel.isCVVValid().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isValid) {
                mCVVError.setVisibility(isValid ? View.GONE : View.VISIBLE);
            }
        });

        mViewModel.isNumberValid().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isValid) {
                mNumberError.setVisibility(isValid ? View.GONE : View.VISIBLE);
            }
        });

        mViewModel.isSubmissionSuccessful().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isSuccess) {
                mSuccess.setVisibility(isSuccess ? View.VISIBLE : View.GONE);
            }
        });

        onFocusChangedListener focusListener = new onFocusChangedListener();
        // Handling Credit Card Number field
        // Adding text watcher on what user is entering as credit card number
        mCreditCard.addTextChangedListener(new CreditCardTextChangedListener());
        mCreditCard.setOnFocusChangeListener(focusListener);

        mExpiration.addTextChangedListener(new ExpirationChangedListener());
        mExpiration.setOnFocusChangeListener(focusListener);

        mCVV.addTextChangedListener(new CVVChangedListener());
        mCVV.setOnFocusChangeListener(focusListener);

        mSubmit.setOnClickListener(new OnSubmitClickListener());
    }

    private void setLengthFilter(int fieldId, int maxLength) {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(maxLength);
        switch (fieldId) {
            case FIELD_ID_CREDIT_CARD_NUMBER:
                mCreditCard.setFilters(filters);
                break;
            case FIELD_ID_CVV:
                mCVV.setFilters(filters);
                break;
        }
    }

    private class CreditCardTextChangedListener implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mViewModel.onCreditCardChangedListener(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
    }

    private class onFocusChangedListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (view == mExpiration) {
                mViewModel.expirationFocusChanged(mExpiration.getText().toString(), b);
            } else if (view == mCreditCard) {
                mViewModel.onNumberFocusChanged(mCreditCard.getText().toString(), b);
            } else if (view == mCVV) {
                mViewModel.onCVVFocusChanged(mCVV.getText().toString(), b);
            }
        }
    }

    private class ExpirationChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mViewModel.onExpirationChanged(charSequence.toString());
        }
    }

    private class CVVChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mViewModel.onCVVChanged(charSequence.toString());
        }
    }

    private class OnSubmitClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            mViewModel.onSubmitClicked();
        }
    }
}
