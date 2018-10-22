package com.haowen.mqtt.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;

 
public class SslUtil {
	public static SSLSocketFactory getSocketFactory(final String caCrtFile, final String crtFile, final String keyFile,
			final String password) throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		// load CA certificate
		PEMReader reader = new PEMReader(
				new InputStreamReader(new ByteArrayInputStream(FileUtils.getResourceFile(caCrtFile))));
		// PEMReader reader = new PEMReader(new InputStreamReader(new
		// ByteArrayInputStream(Variable.ca)));

		X509Certificate caCert = (X509Certificate) reader.readObject();
		reader.close();

		// load client certificate
		// reader = new PEMReader(new InputStreamReader(new
		// ByteArrayInputStream(Variable.client)));

		reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(FileUtils.getResourceFile(crtFile))));
		X509Certificate cert = (X509Certificate) reader.readObject();
		reader.close();

		// load client private key
		reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(FileUtils.getResourceFile(keyFile))),
				new PasswordFinder() {
					@Override
					public char[] getPassword() {
						return password.toCharArray();
					}
				});
		KeyPair key = (KeyPair) reader.readObject();
		reader.close();

		// CA certificate is used to authenticate server
		KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
		caKs.load(null, null);
		caKs.setCertificateEntry("ca-certificate", caCert);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(caKs);

		// client key and certificates are sent to server so it can authenticate
		// us
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		ks.setCertificateEntry("certificate", cert);
		ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(),
				new java.security.cert.Certificate[] { cert });
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(ks, password.toCharArray());
		// finally, create SSL socket factory
		SSLContext context = SSLContext.getInstance("TLSv1");
		context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		return context.getSocketFactory();
	}

	
	private static TrustManager tm2 = new X509TrustManager() {
	    public void checkClientTrusted(X509Certificate[] chain, String authType)
	            throws CertificateException {
	        //do nothing，接受任意客户端证书
	    }

	    public void checkServerTrusted(X509Certificate[] chain, String authType)
	            throws CertificateException {
	        //do nothing，接受任意服务端证书

	    }

	    public X509Certificate[] getAcceptedIssuers() {
	        return new X509Certificate[0];
	    }
	};
	
	public static SSLSocketFactory getSocketFactory2(final String caCrtFile, final String crtFile, final String keyFile,
			final String password) throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		// load CA certificate
		PEMReader reader = new PEMReader(
				new InputStreamReader(new ByteArrayInputStream(FileUtils.getResourceFile(caCrtFile))));
		// PEMReader reader = new PEMReader(new InputStreamReader(new
		// ByteArrayInputStream(Variable.ca)));

		X509Certificate caCert = (X509Certificate) reader.readObject();
		reader.close();

		// load client certificate
		// reader = new PEMReader(new InputStreamReader(new
		// ByteArrayInputStream(Variable.client)));

		reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(FileUtils.getResourceFile(crtFile))));
		X509Certificate cert = (X509Certificate) reader.readObject();
		reader.close();

		// load client private key
		reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(FileUtils.getResourceFile(keyFile))),
				new PasswordFinder() {
					@Override
					public char[] getPassword() {
						return password.toCharArray();
					}
				});
		KeyPair key = (KeyPair) reader.readObject();
		reader.close();

		// CA certificate is used to authenticate server
		KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
		caKs.load(null, null);
		caKs.setCertificateEntry("ca-certificate", caCert);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(caKs);

		// client key and certificates are sent to server so it can authenticate
		// us
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		ks.setCertificateEntry("certificate", cert);
		ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(),
				new java.security.cert.Certificate[] { cert });
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(ks, password.toCharArray());
		// finally, create SSL socket factory
		SSLContext context = SSLContext.getInstance("TLSv1");
		context.init(kmf.getKeyManagers(), new TrustManager[]{tm2}, null);

		return context.getSocketFactory();
	}
}