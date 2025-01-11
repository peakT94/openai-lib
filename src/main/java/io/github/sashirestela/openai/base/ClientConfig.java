package io.github.sashirestela.openai.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.http.HttpRequestData;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * Configuration settings for the OpenAI client.
 * <p>
 * This class encapsulates all necessary configuration parameters required to initialize and operate
 * the OpenAI client, including the base URL, default headers, HTTP client settings, request
 * interceptors, JSON object mapper, and realtime configuration.
 * </p>
 */
@Getter
@Builder
public class ClientConfig {

    /**
     * The base URL for the OpenAI API endpoints.
     * <p>
     * This URL serves as the primary endpoint for all API requests made by the client.
     * </p>
     */
    @NonNull
    private final String baseUrl;

    /**
     * A map of default HTTP headers to include with each API request.
     * <p>
     * These headers are sent with every request unless overridden by request-specific headers.
     * </p>
     */
    private final Map<String, String> headers;

    /**
     * The {@link HttpClient} instance used to execute HTTP requests.
     * <p>
     * Configuring a custom {@code HttpClient} allows for fine-tuning of connection settings,
     * timeouts, and other HTTP client behaviors.
     * </p>
     */
    private final HttpClient httpClient;

    /**
     * A unary operator that intercepts and potentially modifies {@link HttpRequestData} before
     * the request is sent.
     * <p>
     * This interceptor can be used to add additional logging, modify request parameters, or enforce
     * custom request policies.
     * </p>
     */
    private final UnaryOperator<HttpRequestData> requestInterceptor;

    /**
     * The {@link ObjectMapper} instance used for JSON serialization and deserialization.
     * <p>
     * Customizing the object mapper allows for handling specific JSON formats or integrating with
     * other serialization libraries.
     * </p>
     */
    private final ObjectMapper objectMapper;

    /**
     * Configuration settings for realtime operations.
     * <p>
     * This includes parameters that control how realtime data is processed and managed by the client.
     * </p>
     */
    private final RealtimeConfig realtimeConfig;

}