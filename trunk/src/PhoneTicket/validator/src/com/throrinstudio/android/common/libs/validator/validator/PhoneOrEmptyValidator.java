package com.throrinstudio.android.common.libs.validator.validator;

import java.util.regex.Pattern;

import android.content.Context;

import com.throrinstudio.android.common.libs.validator.AbstractValidator;
import com.throrinstudio.android.common.libs.validator.ValidatorException;
import com.throrinstudio.android.example.validator.R;

public class PhoneOrEmptyValidator extends AbstractValidator {

    private static final Pattern mPattern = Pattern.compile(
                    "(\\+[0-9]+[\\- \\.]*)?"
                    + "(\\([0-9]+\\)[\\- \\.]*)?"
                    + "([0-9][0-9\\- \\.]+[0-9])?");

    private int mErrorMessage = R.string.validator_phone;

    public PhoneOrEmptyValidator(Context c) {
        super(c);
    }

    public PhoneOrEmptyValidator(Context c, int errorMessage) {
        super(c);
        mErrorMessage = errorMessage;
    }

    @Override
    public boolean isValid(String value) throws ValidatorException {
        return mPattern.matcher(value).matches();
    }

    @Override
    public String getMessage() {
        return mContext.getString(mErrorMessage);
    }
}
