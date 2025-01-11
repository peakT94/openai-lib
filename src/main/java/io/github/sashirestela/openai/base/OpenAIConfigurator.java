package io.github.sashirestela.openai.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.net.http.HttpClient;

/**
 * Abstract configurator for building {@link ClientConfig} instances used by the OpenAI client.
 * <p>
 * This class provides a foundation for configuring essential parameters such as API key, base URL,
 * HTTP client, and JSON object mapper. Subclasses must implement the {@link #buildConfig()} method to
 * construct a concrete {@link ClientConfig} based on the provided configurations.
 * </p>
 */
@SuperBuilder
@AllArgsConstructor
public abstract class OpenAIConfigurator {

    /**
     * The API key used for authenticating requests to the OpenAI API.
     * <p>
     * This key must be kept secure and should not be exposed in client-side code.
     * </p>
     */
    protected String apiKey;

    /**
     * The base URL for the OpenAI API endpoints.
     * <p>
     * If not specified, a default OpenAI API URL will be used.
     * </p>
     */
    protected String baseUrl;

    /**
     * The {@link HttpClient} instance used for executing HTTP requests.
     * <p>
     * Configuring a custom {@code HttpClient} allows for fine-tuning connection settings, timeouts,
     * and other HTTP client behaviors.
     * </p>
     */
    protected HttpClient httpClient;

    /**
     * The {@link ObjectMapper} instance used for JSON serialization and deserialization.
     * <p>
     * Customizing the object mapper enables handling specific JSON formats or integrating with
     * other serialization libraries.
     * </p>
     */
    protected ObjectMapper objectMapper;

    /**
     * Builds and returns a {@link ClientConfig} instance based on the current configuration.
     * <p>
     * Implementations of this method should utilize the configured fields to assemble a fully
     * initialized {@code ClientConfig} object, ready for use by the OpenAI client.
     * </p>
     *
     * @return a configured {@link ClientConfig} instance
     */
    public abstract ClientConfig buildConfig();

}