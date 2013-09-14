namespace PhoneTicket.Web.Handlers
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Net;
    using System.Net.Http;
    using System.Threading;
    using System.Threading.Tasks;

    public class RequireHttpsMessageHandler : DelegatingHandler
    {
        protected override Task<HttpResponseMessage> SendAsync(
            HttpRequestMessage request,
            CancellationToken cancellationToken)
        {
            IEnumerable<string> protos;
            request.Headers.TryGetValues("X-Forwarded-Proto", out protos);

            bool wasHttps = protos != null && protos.Any(p => p.Equals("https", StringComparison.InvariantCultureIgnoreCase));

            bool isHttps = request.RequestUri.Scheme == Uri.UriSchemeHttps;

            if (!wasHttps && !isHttps)
            {
                var forbiddenResponse = request.CreateResponse(HttpStatusCode.Forbidden);

                forbiddenResponse.ReasonPhrase = "SSL Required";
                return Task.FromResult<HttpResponseMessage>(forbiddenResponse);
            }

            return base.SendAsync(request, cancellationToken);
        }
    }
}