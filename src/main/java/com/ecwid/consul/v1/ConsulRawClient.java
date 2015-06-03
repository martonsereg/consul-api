package com.ecwid.consul.v1;

import com.ecwid.consul.UrlParameters;
import com.ecwid.consul.Utils;
import com.ecwid.consul.transport.DefaultHttpTransport;
import com.ecwid.consul.transport.HttpTransport;
import com.ecwid.consul.transport.RawResponse;
import org.apache.http.client.HttpClient;

/**
 * @author Vasily Vasilkov (vgv@ecwid.com)
 */
public class ConsulRawClient {

	private static final String DEFAULT_HOST = "localhost";
	private static final int DEFAULT_PORT = 8500;
    private static final int DEFAULT_TIMEOUT = 10000;

	private final HttpTransport httpTransport;
	private final String agentAddress;

	public ConsulRawClient() {
		this(DEFAULT_HOST);
	}

    public ConsulRawClient(int timeout) {
        this(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_TIMEOUT);
    }

    public ConsulRawClient(String agentHost) {
        this(agentHost, DEFAULT_PORT, DEFAULT_TIMEOUT);
    }

    public ConsulRawClient(String agentHost, int timeout) {
        this(agentHost, DEFAULT_PORT, timeout);
    }

	public ConsulRawClient(String agentHost, int agentPort, int timeout) {
		this(new DefaultHttpTransport(timeout), agentHost, agentPort);
	}

	public ConsulRawClient(HttpClient httpClient) {
		this(DEFAULT_HOST, httpClient);
	}

	public ConsulRawClient(String agentHost, HttpClient httpClient) {
		this(new DefaultHttpTransport(httpClient), agentHost, DEFAULT_PORT);
	}

	public ConsulRawClient(String agentHost, int agentPort, HttpClient httpClient) {
		this(new DefaultHttpTransport(httpClient), agentHost, agentPort);
	}

	// hidden constructor, for tests
	ConsulRawClient(HttpTransport httpTransport, String agentHost, int agentPort) {
		this.httpTransport = httpTransport;

		// check that agentHost has scheme or not
		String agentHostLowercase = agentHost.toLowerCase();
		if (!agentHostLowercase.startsWith("https://") && !agentHostLowercase.startsWith("http://")) {
			// no scheme in host, use default 'http'
			agentHost = "http://" + agentHost;
		}

		this.agentAddress = agentHost + ":" + agentPort;
	}

	public RawResponse makeGetRequest(String endpoint, UrlParameters... urlParams) {
		String url = agentAddress + endpoint;
		url = Utils.generateUrl(url, urlParams);

		return httpTransport.makeGetRequest(url);
	}

	public RawResponse makePutRequest(String endpoint, String content, UrlParameters... urlParams) {
		String url = agentAddress + endpoint;
		url = Utils.generateUrl(url, urlParams);

		return httpTransport.makePutRequest(url, content);
	}

	public RawResponse makePutRequest(String endpoint, byte[] content, UrlParameters... urlParams) {
		String url = agentAddress + endpoint;
		url = Utils.generateUrl(url, urlParams);

		return httpTransport.makePutRequest(url, content);
	}

	public RawResponse makeDeleteRequest(String endpoint, UrlParameters... urlParams) {
		String url = agentAddress + endpoint;
		url = Utils.generateUrl(url, urlParams);

		return httpTransport.makeDeleteRequest(url);
	}

}
