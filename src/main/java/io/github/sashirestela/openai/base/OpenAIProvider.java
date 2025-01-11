package io.github.sashirestela.openai.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.CleverClient;
import io.github.sashirestela.openai.OpenAIRealtime;
import io.github.sashirestela.slimvalidator.Validator;
import io.github.sashirestela.slimvalidator.exception.ConstraintViolationException;
import lombok.NonNull;
import lombok.Setter;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Abstract provider class for initializing and managing OpenAI client services.
 * <p>
 * Subclasses must extend this class to provide specific implementations for interacting with
 * OpenAI APIs. This class handles the configuration of the HTTP client, realtime services,
 * and service caching to optimize performance and resource utilization.
 * </p>
 */
public abstract class OpenAIProvider {

    /**
     * The {@link CleverClient} instance used to create and manage API service interfaces.
     * <p>
     * This client is responsible for executing HTTP requests and handling responses.
     * </p>
     */
    @Setter
    protected CleverClient cleverClient;

    /**
     * The {@link OpenAIRealtime} instance responsible for handling realtime operations.
     * <p>
     * Realtime operations include streaming data and managing persistent connections.
     * </p>
     */
    protected OpenAIRealtime realtime;

    /**
     * A cache for storing service instances to ensure a single instance per service class.
     * <p>
     * This cache improves performance by reusing existing service instances instead of creating new ones.
     * </p>
     */
    private Map<Class<?>, Object> serviceCache = new ConcurrentHashMap<>();

    /**
     * Constructs an {@code OpenAIProvider} with the specified configurator.
     * <p>
     * Initializes the {@link CleverClient} and {@link OpenAIRealtime} instances based on the provided
     * {@link OpenAIConfigurator}. If no custom {@code HttpClient} is configured, a default client is used.
     * </p>
     *
     * @param configurator the configurator containing necessary configuration settings
     * @throws NullPointerException if {@code configurator} is {@code null}
     */
    protected OpenAIProvider(@NonNull OpenAIConfigurator configurator) {
        var clientConfig = configurator.buildConfig();
        var httpClient = Optional.ofNullable(clientConfig.getHttpClient()).orElse(HttpClient.newHttpClient());
        this.cleverClient = buildClient(clientConfig, httpClient);
        this.realtime = buildRealtime(clientConfig, httpClient);
    }

    /**
     * Retrieves an existing service instance or creates a new one if it does not exist.
     * <p>
     * This method ensures that only one instance of each service class is created and reused.
     * </p>
     *
     * @param <T>          the type of the service
     * @param serviceClass the class object of the service to retrieve or create
     * @return an instance of the specified service class
     */
    @SuppressWarnings("unchecked")
    protected <T> T getOrCreateService(Class<T> serviceClass) {
        return (T) serviceCache.computeIfAbsent(serviceClass, key -> cleverClient.create(serviceClass));
    }

    /**
     * Builds and configures the {@link CleverClient} instance based on the provided configuration.
     * <p>
     * This method sets up the HTTP client, base URL, headers, request interceptor, body inspector,
     * end-of-stream indicator, and JSON object mapper for the {@code CleverClient}.
     * </p>
     *
     * @param clientConfig the configuration settings for the client
     * @param httpClient   the {@link HttpClient} to be used for HTTP requests
     * @return a configured {@link CleverClient} instance
     */
    private CleverClient buildClient(ClientConfig clientConfig, HttpClient httpClient) {
        final String END_OF_STREAM = "[DONE]";
        return CleverClient.builder()
                .httpClient(httpClient)
                .baseUrl(clientConfig.getBaseUrl())
                .headers(clientConfig.getHeaders())
                .requestInterceptor(clientConfig.getRequestInterceptor())
                .bodyInspector(bodyInspector())
                .endOfStream(END_OF_STREAM)
                .objectMapper(Optional.ofNullable(clientConfig.getObjectMapper()).orElse(new ObjectMapper()))
                .build();
    }

    /**
     * Builds and configures the {@link OpenAIRealtime} instance based on the provided configuration.
     * <p>
     * If realtime configuration is available, this method initializes the {@code OpenAIRealtime} with
     * the specified settings. Otherwise, it returns {@code null}.
     * </p>
     *
     * @param clientConfig the configuration settings for the client
     * @param httpClient   the {@link HttpClient} to be used for HTTP requests
     * @return a configured {@link OpenAIRealtime} instance or {@code null} if no configuration is provided
     */
    private OpenAIRealtime buildRealtime(ClientConfig clientConfig, HttpClient httpClient) {
        var realtimeConfig = clientConfig.getRealtimeConfig();
        if (realtimeConfig != null) {
            return OpenAIRealtime.builder()
                    .httpClient(httpClient)
                    .realtimeConfig(realtimeConfig)
                    .build();
        }
        return null;
    }

    /**
     * Creates a {@link Consumer} that inspects and validates the request body.
     * <p>
     * This consumer uses a {@link Validator} to check for constraint violations in the request body.
     * If violations are found, a {@link ConstraintViolationException} is thrown.
     * </p>
     *
     * @return a {@code Consumer<Object>} that validates request bodies
     */
    private Consumer<Object> bodyInspector() {
        return body -> {
            var validator = new Validator();
            var violations = validator.validate(body);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        };
    }
}