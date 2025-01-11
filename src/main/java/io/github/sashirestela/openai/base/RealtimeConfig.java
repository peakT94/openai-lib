package io.github.sashirestela.openai.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * Configuration settings for realtime operations in the OpenAI client.
 * <p>
 * This class encapsulates the necessary parameters required to configure realtime functionalities,
 * including the model to be used, endpoint URL, default headers, and query parameters.
 * </p>
 */
@Getter
@Builder
@AllArgsConstructor
public class RealtimeConfig {

    /**
     * The name of the model to be used for realtime operations.
     */
    private String model;

    /**
     * The endpoint URL for realtime API requests.
     * <p>
     * If not specified, a default endpoint URL will be used.
     * </p>
     */
    private String endpointUrl;

    /**
     * A map of default HTTP headers to include with each realtime API request.
     * <p>
     * These headers are sent with every request unless overridden by request-specific headers.
     * </p>
     */
    private Map<String, String> headers;

    /**
     * A map of default query parameters to include with each realtime API request.
     * <p>
     * These parameters are appended to the request URL unless overridden by request-specific parameters.
     * </p>
     */
    private Map<String, String> queryParams;

    /**
     * Creates a {@link RealtimeConfig} instance with the specified model.
     * <p>
     * Other configuration parameters are set to {@code null} by default.
     * </p>
     *
     * @param model the name of the model to be used for realtime operations
     * @return a new {@link RealtimeConfig} instance with the specified model
     */
    public static RealtimeConfig of(String model) {
        return new RealtimeConfig(model, null, null, null);
    }

}