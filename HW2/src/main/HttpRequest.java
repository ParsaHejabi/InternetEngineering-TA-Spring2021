package main;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String url;
    private HttpRequestMethod httpRequestMethod;
    private HashMap<String, String> headers;
    private HashMap<String, String> params;
    private HashMap<String, String> body;

    public HttpRequest(String url) {
        this(url, HttpRequestMethod.GET, new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    public HttpRequest(String url, HttpRequestMethod httpRequestMethod) {
        this(url, httpRequestMethod, new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    public HttpRequest(String url, HttpRequestMethod httpRequestMethod, HashMap<String, String> headers, HashMap<String, String> params, HashMap<String, String> body) {
        this.url = url;
        this.httpRequestMethod = httpRequestMethod;
        this.headers = headers;
        this.params = params;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpRequestMethod getHttpRequestMethod() {
        return httpRequestMethod;
    }

    public void setHttpRequestMethod(HttpRequestMethod httpRequestMethod) {
        this.httpRequestMethod = httpRequestMethod;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public HashMap<String, String> getBody() {
        return body;
    }

    public void setBody(HashMap<String, String> body) {
        this.body = body;
    }

    public HttpResponse request() throws HttpException, IOException {
        HttpResponse httpResponse = null;
        URL url = new URL(this.url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(this.httpRequestMethod.toString());

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(ParameterStringBuilder.getParamsString(this.params));
        out.flush();
        out.close();

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            con.addRequestProperty(entry.getKey(), entry.getValue());
        }

        con.connect();
        int status = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        httpResponse = new HttpResponse(status, con.getHeaderFields(), content.toString());

        if (status >= 400) {
            throw new HttpException("Status code 4xx or 5xx", httpResponse);
        } else {
            return httpResponse;
        }
    }
}
