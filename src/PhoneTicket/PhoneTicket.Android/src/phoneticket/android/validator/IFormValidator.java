package phoneticket.android.validator;

import com.throrinstudio.android.common.libs.validator.AbstractValidate;

public interface IFormValidator {

	public void addValidates(AbstractValidate confirmFields);

	public boolean validate();

}
