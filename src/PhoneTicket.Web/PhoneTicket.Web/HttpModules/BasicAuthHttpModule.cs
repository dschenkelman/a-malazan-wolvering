namespace PhoneTicket.Web.HttpModules
{
    using System;
    using System.Net.Http.Headers;
    using System.Security.Principal;
    using System.Text;
    using System.Threading;
    using System.Web;

    public abstract class BasicAuthHttpModule : IHttpModule
    {
        private const string Realm = "My Realm";

        public void Init(HttpApplication context)
        {
            // Register event handlers
            context.AuthenticateRequest += this.OnApplicationAuthenticateRequest;
            context.EndRequest += this.OnApplicationEndRequest;
        }

        public virtual void Dispose()
        {
        }

        private static void SetPrincipal(IPrincipal principal)
        {
            Thread.CurrentPrincipal = principal;
            if (HttpContext.Current != null)
            {
                HttpContext.Current.User = principal;
            }
        }

        protected abstract bool CheckPassword(string username, string password);

        private bool AuthenticateUser(string credentials)
        {
            bool validated = false;
            try
            {
                var encoding = Encoding.GetEncoding("iso-8859-1");
                credentials = encoding.GetString(Convert.FromBase64String(credentials));

                int separator = credentials.IndexOf(':');
                string name = credentials.Substring(0, separator);
                string password = credentials.Substring(separator + 1);

                validated = this.CheckPassword(name, password);
                if (validated)
                {
                    var identity = new GenericIdentity(name);
                    SetPrincipal(new GenericPrincipal(identity, null));
                }
            }
            catch (FormatException)
            {
                // Credentials were not formatted correctly.
                validated = false;
            }

            return validated;
        }

        private void OnApplicationAuthenticateRequest(object sender, EventArgs e)
        {
            var request = HttpContext.Current.Request;
            var authHeader = request.Headers["Authorization"];
            if (authHeader != null)
            {
                var authHeaderVal = AuthenticationHeaderValue.Parse(authHeader);

                // RFC 2617 sec 1.2, "scheme" name is case-insensitive
                if (authHeaderVal.Scheme.Equals("basic", StringComparison.OrdinalIgnoreCase)
                    && authHeaderVal.Parameter != null)
                {
                    this.AuthenticateUser(authHeaderVal.Parameter);
                }
            }
        }

        // If the request was unauthorized, add the WWW-Authenticate header 
        // to the response.
        private void OnApplicationEndRequest(object sender, EventArgs e)
        {
            var response = HttpContext.Current.Response;
            if (response.StatusCode == 401)
            {
                response.Headers.Add("WWW-Authenticate",
                    string.Format("Basic realm=\"{0}\"", Realm));
            }
        }
    }
}