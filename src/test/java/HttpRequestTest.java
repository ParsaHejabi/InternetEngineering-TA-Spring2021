import main.HttpException;
import main.HttpRequest;
import main.HttpRequestMethod;
import main.HttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class HttpRequestTest {
    private HttpRequest httpRequest;

    @BeforeEach
    void setUp() {
        String url = "https://jsonplaceholder.typicode.com/todos/1";
        httpRequest = new HttpRequest(url);
    }

    @Test
    void createHttpRequestWithUrlAndMethod() {
        Assertions.assertEquals("https://www.google.com/", this.httpRequest.getUrl());
        Assertions.assertEquals(HttpRequestMethod.GET, this.httpRequest.getHttpRequestMethod());
    }

    @Test
    void successfulGetRequest() {
        try {
            HttpResponse httpResponse = this.httpRequest.request();
            Assertions.assertEquals(200, httpResponse.getStatus());
        } catch (HttpException httpException) {
            httpException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
