package io.github.sashirestela.openai.domain;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class DomainTestingHelper {

    private static DomainTestingHelper helper = null;

    private DomainTestingHelper() {
    }

    public static DomainTestingHelper get() {
        if (helper == null) {
            helper = new DomainTestingHelper();
        }
        return helper;
    }

    public void mockForStream(HttpClient httpClient, String responseFilePath) throws IOException {
        HttpResponse<Stream<String>> httpResponse = mock(HttpResponse.class);
        var listResponse = Files.readAllLines(Paths.get(responseFilePath));
        when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofLines().getClass())))
                .thenReturn(CompletableFuture.completedFuture(httpResponse));
        when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponse.body()).thenReturn(listResponse.stream());
    }

    public void mockForObject(HttpClient httpClient, String responseFilePath) throws IOException {
        HttpResponse<String> httpResponse = mock(HttpResponse.class);
        var jsonResponse = Files.readAllLines(Paths.get(responseFilePath)).get(0);
        when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
                .thenReturn(CompletableFuture.completedFuture(httpResponse));
        when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponse.body()).thenReturn(jsonResponse);
    }

    public void mockForBinary(HttpClient httpClient, String responseFilePath) throws IOException {
        HttpResponse<InputStream> httpResponse = mock(HttpResponse.class);
        InputStream binaryResponse = new FileInputStream(responseFilePath);
        when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofInputStream().getClass())))
                .thenReturn(CompletableFuture.completedFuture(httpResponse));
        when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponse.body()).thenReturn(binaryResponse);
    }

    public enum MockForType {
        STREAM,
        OBJECT,
        BINARY;
    }

    public void mockFor(HttpClient httpClient, Map<MockForType, List<String>> stepsByMockType) throws IOException {
        HttpResponse<Stream<String>> httpResponseStream = mock(HttpResponse.class);
        HttpResponse<String> httpResponseObject = mock(HttpResponse.class);
        HttpResponse<InputStream> httpResponseBinary = mock(HttpResponse.class);

        for (var stepEntry : stepsByMockType.entrySet()) {
            switch (stepEntry.getKey()) {
                case STREAM:
                    var respStream = responseSequence(stepEntry, Stream.class);
                    when(httpClient.sendAsync(any(HttpRequest.class),
                            any(HttpResponse.BodyHandlers.ofLines().getClass())))
                            .thenReturn(CompletableFuture.completedFuture(httpResponseStream));
                    when(httpResponseStream.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
                    if (respStream.length == 1) {
                        when(httpResponseStream.body()).thenReturn(respStream[0]);
                    } else {
                        when(httpResponseStream.body()).thenReturn(respStream[0],
                                Arrays.copyOfRange(respStream, 1, respStream.length));
                    }
                    break;
                case OBJECT:
                    var respObject = responseSequence(stepEntry, String.class);
                    when(httpClient.sendAsync(any(HttpRequest.class),
                            any(HttpResponse.BodyHandlers.ofString().getClass())))
                            .thenReturn(CompletableFuture.completedFuture(httpResponseObject));
                    when(httpResponseObject.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
                    if (respObject.length == 1) {
                        when(httpResponseObject.body()).thenReturn(respObject[0]);
                    } else {
                        when(httpResponseObject.body()).thenReturn(respObject[0],
                                Arrays.copyOfRange(respObject, 1, respObject.length));
                    }
                    break;
                case BINARY:
                    var respBinary = responseSequence(stepEntry, InputStream.class);
                    when(httpClient.sendAsync(any(HttpRequest.class),
                            any(HttpResponse.BodyHandlers.ofInputStream().getClass())))
                            .thenReturn(CompletableFuture.completedFuture(httpResponseBinary));
                    when(httpResponseBinary.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
                    if (respBinary.length == 1) {
                        when(httpResponseBinary.body()).thenReturn(respBinary[0]);
                    } else {
                        when(httpResponseBinary.body()).thenReturn(respBinary[0],
                                Arrays.copyOfRange(respBinary, 1, respBinary.length));
                    }
                    break;
            }
        }
    }

    private <T> T[] responseSequence(Entry<MockForType, List<String>> stepEntry, Class<T> type) throws IOException {
        List<T> response = new ArrayList<>();
        switch (stepEntry.getKey()) {
            case STREAM:
                for (var fileName : stepEntry.getValue()) {
                    response.add((T) Files.readAllLines(Paths.get(fileName)).stream());
                }
                return (T[]) response.toArray(new Stream[0]);
            case OBJECT:
                for (var fileName : stepEntry.getValue()) {
                    response.add((T) Files.readAllLines(Paths.get(fileName)).get(0));
                }
                return (T[]) response.toArray(new String[0]);
            case BINARY:
                for (var fileName : stepEntry.getValue()) {
                    response.add((T) new FileInputStream(fileName));
                }
                return (T[]) response.toArray(new InputStream[0]);
        }
        return null;
    }

}
