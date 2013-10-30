package phoneticket.android.services.post;

public interface IRegisterDiscountsServiceDelegate {

	public void registerDiscountsFinish(IRegisterDiscountsService service);

	public void registerDiscountsFinishWithError(
			IRegisterDiscountsService service, int statusCode);

}
