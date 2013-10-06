namespace PhoneTicket.Web.Controllers
{
    using System.Web.Mvc;

    public class ShowsController : Controller
    {
        public ActionResult Index()
        {
            return this.View();
        }

        public ActionResult Create()
        {
            return this.View();
        }
    }
}