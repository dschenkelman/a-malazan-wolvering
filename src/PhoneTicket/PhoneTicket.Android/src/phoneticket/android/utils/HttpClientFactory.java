package phoneticket.android.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.test.suitebuilder.annotation.Suppress;
import android.util.Log;

public class HttpClientFactory
{
	public static HttpClient createClient()
	{
		DefaultHttpClient client = getHttpClient();
		CredentialsProvider provider = new BasicCredentialsProvider();
		Credentials credentials = new UsernamePasswordCredentials(
				UserManager.getInstance().getEmailAddressCredential(),
				UserManager.getInstance().getPasswordCredential());
		provider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), credentials);
		client.setCredentialsProvider(provider);
		return client;
	}

	private static DefaultHttpClient getHttpClient() { 
		
		HttpParams params = new BasicHttpParams();

	    //Set main protocol parameters
	    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	    HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
	    HttpProtocolParams.setUseExpectContinue(params, true);
	
	    // Turn off stale checking.  Our connections break all the time anyway, and it's not worth it to pay the penalty of checking every time.
	    HttpConnectionParams.setStaleCheckingEnabled(params, false);
	    // FIX v2.2.1+ - Set timeout to 30 seconds, seems like 5 seconds was not enough for good communication
	    HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
	    HttpConnectionParams.setSoTimeout(params, 30 * 1000);
	    HttpConnectionParams.setSocketBufferSize(params, 8192);
	
	    // Don't handle redirects -- return them to the caller.  Our code often wants to re-POST after a redirect, which we must do ourselves.
	    HttpClientParams.setRedirecting(params, false);
	
	    // Register our own "trust-all" SSL scheme
	    SchemeRegistry schReg = new SchemeRegistry();
	    try
	    {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);
	
	        TrustAllSSLSocketFactory sslSocketFactory = new TrustAllSSLSocketFactory();
	
	        Scheme sslTrustAllScheme = new Scheme("https", sslSocketFactory, 443);
	        schReg.register(sslTrustAllScheme);
	    }
	    catch (Exception ex)
	    {
	        Log.w("Http_Client", ex);
	    }
	
	    ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params,schReg);
	    return new DefaultHttpClient(conMgr, params);
	}
	
	
	private static final class TrustAllSSLSocketFactory implements LayeredSocketFactory {

	    private static final TrustAllSSLSocketFactory DEFAULT_FACTORY = new TrustAllSSLSocketFactory();
	
	    @Suppress
	    public static TrustAllSSLSocketFactory getSocketFactory() {
	        return DEFAULT_FACTORY;
	    }
	
	    private SSLContext sslcontext;
	    private javax.net.ssl.SSLSocketFactory socketfactory;
	
	    private TrustAllSSLSocketFactory() {
	        super();
	        TrustManager[] tm = new TrustManager[] { new X509TrustManager() {
	
	            @Override
	            public void checkClientTrusted(X509Certificate[] chain,
	                String authType) throws CertificateException {
	                // do nothing
	            }
	
	            @Override
	            public void checkServerTrusted(X509Certificate[] chain,
	                String authType) throws CertificateException {
	                // do nothing
	            }
	
	            @Override
	            public X509Certificate[] getAcceptedIssuers() {
	                return new X509Certificate[0];
	            }
	
	        } };
	        try {
	            this.sslcontext = SSLContext.getInstance(SSLSocketFactory.TLS);
	            this.sslcontext.init(null, tm, new SecureRandom());
	            this.socketfactory = this.sslcontext.getSocketFactory();
	        } catch ( NoSuchAlgorithmException e ) {
	            Log.e("SSL_Factory", "Failed to instantiate TrustAllSSLSocketFactory!", e);
	        } catch ( KeyManagementException e ) {
	            Log.e("SSL_Factory","Failed to instantiate TrustAllSSLSocketFactory!", e);
	        }
	    }
	
	    @Override
	    public Socket createSocket(Socket socket, String host, int port,
	        boolean autoClose) throws IOException, UnknownHostException {
	        SSLSocket sslSocket = (SSLSocket) this.socketfactory.createSocket(
	            socket, host, port, autoClose);
	        return sslSocket;
	    }
	
	    @Override
	    public Socket connectSocket(Socket sock, String host, int port,
	        InetAddress localAddress, int localPort, HttpParams params)
	        throws IOException, UnknownHostException, ConnectTimeoutException {
	        if ( host == null ) {
	            throw new IllegalArgumentException(
	                "Target host may not be null.");
	        }
	        if ( params == null ) {
	            throw new IllegalArgumentException(
	                "Parameters may not be null.");
	        }
	
	        SSLSocket sslsock = (SSLSocket) ( ( sock != null ) ? sock
	            : createSocket() );
	
	        if ( ( localAddress != null ) || ( localPort > 0 ) ) {
	
	            // we need to bind explicitly
	            if ( localPort < 0 ) {
	                localPort = 0; // indicates "any"
	            }
	
	            InetSocketAddress isa = new InetSocketAddress(localAddress,
	                localPort);
	            sslsock.bind(isa);
	        }
	
	        int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
	        int soTimeout = HttpConnectionParams.getSoTimeout(params);
	
	        InetSocketAddress remoteAddress;
	        remoteAddress = new InetSocketAddress(host, port);
	
	        sslsock.connect(remoteAddress, connTimeout);
	
	        sslsock.setSoTimeout(soTimeout);
	
	        return sslsock;
	    }
	
	    @Override
	    public Socket createSocket() throws IOException {
	        // the cast makes sure that the factory is working as expected
	        return (SSLSocket) this.socketfactory.createSocket();
	    }
	
	    @Override
	    public boolean isSecure(Socket sock) throws IllegalArgumentException {
	        return true;
	    }
	
	}

}
