namespace PhoneTicket.Web.Controllers
{
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.ViewModels;

    public class ShowsController : Controller
    {
        public ActionResult Index()
        {
            return this.View();
        }

        [HttpGet]
        public ActionResult Create()
        {
            return this.View();
        }

        [HttpPost]
        public JsonResult Create(CreateShowsViewModel viewModel)
        {
            return this.Json(1);
        }

        [HttpGet]
        public ActionResult CreationConfirmation()
        {
            this.ViewBag.Message = "Las funciones se han creado con éxito.";
            this.ViewBag.LinkText = "Aceptar";
            this.ViewBag.Action = "Index";
            this.ViewBag.Controller = "Shows";
            return this.View("~/Views/Shared/Confirmation.cshtml");
        }
    }
}