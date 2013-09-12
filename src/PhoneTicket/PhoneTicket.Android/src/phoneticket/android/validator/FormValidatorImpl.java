package phoneticket.android.validator;

import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;

public class FormValidatorImpl implements IFormValidator {
	private Form form;

	public FormValidatorImpl() {
		this.form = new Form();
	}

	@Override
	public void addValidates(Validate validate) {
		this.form.addValidates(validate);
	}

	@Override
	public boolean validate() {
		return this.form.validate();
	}

}
