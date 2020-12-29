package com.dpitech.edge.common;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static com.dpitech.edge.common.CommonConst.*;

/**
 * @author rusheng
 */
public class HttpUtil {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(HTTP_TIMEOUT_SECOND))
            .build();

    /**
     * get with special cookie and refer and url
     * @param url url
     * @param cookie cookie
     * @param refer refer
     * @return HttpResponse
     * @throws IOException IOException
     * @throws InterruptedException InterruptedException
     */
    public static HttpResponse<String> get(String url, String cookie, String refer) throws IOException, InterruptedException {
        var httpRequest = defaultHttpRequestBuilder(url, cookie, refer).GET().build();
        return HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    /**
     * get with special cookie and refer and url
     * get InputStream for page process
     * @param url url
     * @param cookie cookie
     * @param refer refer
     * @return HttpResponse
     * @throws IOException IOException
     * @throws InterruptedException InterruptedException
     */
    public static HttpResponse<InputStream> getPageForInputStream(String url, String cookie, String refer) throws IOException, InterruptedException {
        var httpRequest = defaultHttpRequestBuilder(url, cookie, refer).GET().build();
        return HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
    }

    /**
     * post with special cookie and refer and url with form-data
     * @param url url
     * @param bodyString string in body
     * @param cookie cookie
     * @param refer refer
     * @return HttpResponse
     * @throws IOException IOException
     * @throws InterruptedException InterruptedException
     */
    public static HttpResponse<String> postWithFormData(String url, String bodyString, String cookie, String refer)
            throws IOException, InterruptedException {
        var httpRequest = defaultHttpRequestBuilder(url, cookie, refer)
                .setHeader("Content-Type", COMMON_CONTENT_TYPE_FORM_DATA)
                .POST(HttpRequest.BodyPublishers.ofString(bodyString))
                .build();
        return HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    /**
     * post with special cookie and refer and url with json
     * @param url url
     * @param jsonBody json object
     * @param cookie cookie
     * @param refer refer
     * @return HttpResponse
     * @throws IOException IOException
     * @throws InterruptedException InterruptedException
     */
    public static HttpResponse<String> postWithJson(String url, JSONObject jsonBody, String cookie, String refer)
            throws IOException, InterruptedException {
        var httpRequest = defaultHttpRequestBuilder(url, cookie, refer)
                .setHeader("Content-Type", COMMON_CONTENT_TYPE_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toJSONString()))
                .build();
        return HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    /**
     * build default http request for get
     * set <br>cookie = null</br> or make <br>cookie.length == 0</br> can unset the cookie field in request.
     * set <br>refer = null</br> or make <br>refer.length == 0</br> can unset the refer field in request.
     * @param url url
     * @param cookie cookie String after combine
     * @param refer Referer
     * @return HttpRequest
     */
    private static HttpRequest.Builder defaultHttpRequestBuilder(String url, String cookie, String refer) {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
                .setHeader("User-Agent", COMMON_USER_AGENT)
                .setHeader("Accept", COMMON_ACCEPT)
                .setHeader("Origin", COMMON_ORIGIN)
                .setHeader("Sec-Fetch-Site", COMMON_SEC_FETCH_SITE)
                .setHeader("Sec-Fetch-Mode", COMMON_SEC_FETCH_MODE)
                .setHeader("Sec-Fetch-Dest", COMMON_SEC_FETCH_DEST)
                .setHeader("Sec-Fetch-User", COMMON_SEC_FETCH_USER)
                .GET();

        if (!CommonUtil.isEmpty(cookie)) {
            builder.setHeader("Cookie", cookie);
        }
        if (!CommonUtil.isEmpty(refer)) {
            builder.setHeader("Referer", refer);
        }
        return builder;
    }

}
