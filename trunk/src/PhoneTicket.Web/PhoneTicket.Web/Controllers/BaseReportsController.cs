namespace PhoneTicket.Web.Controllers
{
    using System.IO;
    using System.Text;
    using System.Text.RegularExpressions;
    using System.Web;
    using System.Web.Mvc;

    using iTextSharp.text;
    using iTextSharp.text.html.simpleparser;
    using iTextSharp.text.pdf;

    public class BaseReportsController : Controller
    {
        /// <summary>
        /// Renders an action result to a string. This is done by creating a fake http context
        /// and response objects and have that response send the data to a string builder
        /// instead of the browser.
        /// </summary>
        /// <param name="result">The action result to be rendered to string.</param>
        /// <returns>The data rendered by the given action result.</returns>
        protected string RenderActionResultToString(ActionResult result)
        {
            // Create memory writer.
            var sb = new StringBuilder();
            var memWriter = new StringWriter(sb);

            // Create fake http context to render the view.
            var fakeResponse = new HttpResponse(memWriter);
            var fakeContext = new HttpContext(System.Web.HttpContext.Current.Request, fakeResponse);
            var fakeControllerContext = new ControllerContext(
                new HttpContextWrapper(fakeContext),
                this.ControllerContext.RouteData,
                this.ControllerContext.Controller);
            var oldContext = System.Web.HttpContext.Current;
            System.Web.HttpContext.Current = fakeContext;

            // Render the view.
            result.ExecuteResult(fakeControllerContext);

            // Restore data.
            System.Web.HttpContext.Current = oldContext;

            // Flush memory and return output.
            memWriter.Flush();
            return sb.ToString();
        }

        /// <summary>
        /// Returns a PDF action result. This method renders the view to a string then
        /// use that string to generate a PDF file. The generated PDF file is then
        /// returned to the browser as binary content. The view associated with this
        /// action should render an XML compatible with iTextSharp xml format.
        /// </summary>
        /// <param name="model">The model to send to the view.</param>
        /// <returns>The resulted BinaryContentResult.</returns>
        protected ActionResult ViewPdf(object model)
        {
            // Create the iTextSharp document.
            Document doc = new Document();
            // Set the document to write to memory.
            MemoryStream memStream = new MemoryStream();
            PdfWriter writer = PdfWriter.GetInstance(doc, memStream);
            writer.CloseStream = false;
            doc.Open();

            // Render the view xml to a string, then parse that string into an XML dom.
            string htmlText = this.RenderActionResultToString(this.View(model));

            htmlText = Regex.Replace(htmlText, @"(/Images/.*\.png)", m => HttpContext.Server.MapPath(m.Groups[1].Value));

            HTMLWorker worker = new HTMLWorker(doc);
            worker.Parse(new StringReader(htmlText));
            
            // Close and get the resulted binary data.
            doc.Close();
            var buf = new byte[memStream.Position];
            memStream.Position = 0;
            memStream.Read(buf, 0, buf.Length);

            // Send the binary data to the browser.
            return new BinaryContentResult(buf, "application/pdf");
        }
    }

    public class BinaryContentResult : ActionResult
    {
        private readonly string ContentType;
        private readonly byte[] ContentBytes;

        public BinaryContentResult(byte[] contentBytes, string contentType)
        {
            this.ContentBytes = contentBytes;
            this.ContentType = contentType;
        }

        public override void ExecuteResult(ControllerContext context)
        {
            var response = context.HttpContext.Response;
            response.Clear();
            response.Cache.SetCacheability(HttpCacheability.NoCache);
            response.ContentType = this.ContentType;

            var stream = new MemoryStream(this.ContentBytes);
            stream.WriteTo(response.OutputStream);
            stream.Dispose();
        }
    }
}