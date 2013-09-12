package phoneticket.android.validator;

import com.throrinstudio.android.common.libs.validator.Validate;

public interface IFormValidator {

	public void addValidates(Validate validate);

	public boolean validate();

}
