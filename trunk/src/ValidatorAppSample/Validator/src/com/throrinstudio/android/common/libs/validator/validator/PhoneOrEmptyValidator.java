package com.throrinstudio.android.common.libs.validator.validator;

import android.content.Context;
import android.util.Patterns;

import com.throrinstudio.android.common.libs.validator.AbstractValidator;
import com.throrinstudio.android.common.libs.validator.ValidatorException;
import com.throrinstudio.android.example.validator.R;

import java.util.regex.Pattern;

/**
 * Created by MATIAS on 07/09/13.
 */
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
