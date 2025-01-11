package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.common.StreamOptions;
import io.github.sashirestela.openai.common.audio.AudioFormat;
import io.github.sashirestela.openai.common.audio.Voice;
import io.github.sashirestela.openai.common.tool.Tool;
import io.github.sashirestela.openai.common.tool.ToolChoice;
import io.github.sashirestela.openai.common.tool.ToolChoiceOption;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.With;

import java.util.List;
import java.util.Map;

/**
 * Represents a request to initiate a chat interaction with the OpenAI API.
 * <p>
 * This class encapsulates all necessary parameters required to configure and send a chat request,
 * including messages, model selection, penalties, modality options, and tool integrations.
 * It leverages the builder pattern for flexible and readable request construction.
 * </p>
 */
@Getter
@Builder
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatRequest {

    /**
     * The list of messages constituting the chat conversation.
     * <p>
     * Each message represents an interaction from different roles such as user, assistant, system, etc.
     * </p>
     */
    @Required
    @Singular
    private List<ChatMessage> messages;

    /**
     * The name of the model to use for generating responses.
     * <p>
     * Examples include "gpt-3.5-turbo" or "gpt-4".
     * </p>
     */
    @Required
    private String model;

    /**
     * Indicates whether to store the chat interaction.
     */
    private Boolean store;

    /**
     * Specifies the level of reasoning effort to apply in generating responses.
     */
    private ReasoningEffort reasoningEffort;

    /**
     * Additional metadata to include with the chat request.
     */
    private Map<String, String> metadata;

    /**
     * Penalizes repeating the same tokens based on their frequency.
     * <p>
     * Range: -2.0 to 2.0
     * </p>
     */
    @Range(min = -2.0, max = 2.0)
    private Double frequencyPenalty;

    /**
     * Adjusts the likelihood of specified tokens appearing in the response.
     */
    private Map<String, Integer> logitBias;

    /**
     * Determines whether to return log probabilities.
     */
    private Boolean logprobs;

    /**
     * Limits the number of top log probabilities to return for each token.
     * <p>
     * Range: 0 to 20
     * </p>
     */
    @Range(min = 0, max = 20)
    private Integer topLogprobs;

    /**
     * @deprecated OpenAI has deprecated this field in favor of {@link #maxCompletionTokens}.
     */
    @Deprecated(since = "3.9.0", forRemoval = true)
    private Integer maxTokens;

    /**
     * Sets the maximum number of tokens to generate in the completion.
     */
    private Integer maxCompletionTokens;

    /**
     * Specifies the number of completions to generate for each prompt.
     * <p>
     * Range: 1 to 128
     * </p>
     */
    @Range(min = 1, max = 128)
    private Integer n;

    /**
     * Specifies the modalities to include in the chat request.
     * <p>
     * Supported modalities: TEXT, AUDIO.
     * </p>
     */
    @Singular
    private List<Modality> modalities;

    /**
     * Configuration for audio-related functionalities in the chat.
     */
    private Audio audio;

    /**
     * Penalizes repeating the same tokens based on their presence.
     * <p>
     * Range: -2.0 to 2.0
     * </p>
     */
    @Range(min = -2.0, max = 2.0)
    private Double presencePenalty;

    /**
     * Specifies the format of the response.
     */
    private ResponseFormat responseFormat;

    /**
     * Sets a seed for random number generation to ensure reproducibility.
     */
    private Integer seed;

    /**
     * Specifies the service tier for the chat request.
     */
    private ServiceTier serviceTier;

    /**
     * Defines the stopping criteria for the response generation.
     * <p>
     * Can be a single string, a list of strings, or up to four strings.
     * </p>
     */
    @ObjectType(baseClass = String.class)
    @ObjectType(baseClass = String.class, firstGroup = true, maxSize = 4)
    private Object stop;

    /**
     * Enables streaming of responses.
     * <p>
     * When set to {@code true}, the response will be streamed incrementally.
     * </p>
     */
    @With
    private Boolean stream;

    /**
     * Configures additional options for streaming responses.
     */
    @With
    private StreamOptions streamOptions;

    /**
     * Controls the randomness of the response.
     * <p>
     * Range: 0.0 to 2.0
     * </p>
     */
    @Range(min = 0.0, max = 2.0)
    private Double temperature;

    /**
     * Controls the cumulative probability for token selection.
     * <p>
     * Range: 0.0 to 1.0
     * </p>
     */
    @Range(min = 0.0, max = 1.0)
    private Double topP;

    /**
     * A list of tools to integrate with the chat assistant.
     */
    @Singular
    private List<Tool> tools;

    /**
     * Specifies the tool choice options or tool choices.
     */
    @With
    @ObjectType(baseClass = ToolChoiceOption.class)
    @ObjectType(baseClass = ToolChoice.class)
    private Object toolChoice;

    /**
     * Determines whether tool calls should be made in parallel.
     */
    private Boolean parallelToolCalls;

    /**
     * Identifier for the end user.
     */
    private String user;

    /**
     * Enumerates the supported modalities for chat interactions.
     */
    public enum Modality {

        /**
         * Text-based interactions.
         */
        @JsonProperty("text")
        TEXT,

        /**
         * Audio-based interactions.
         */
        @JsonProperty("audio")
        AUDIO;
    }

    /**
     * Enumerates the available service tiers for chat requests.
     */
    public enum ServiceTier {

        /**
         * Automatically selects the service tier based on usage.
         */
        @JsonProperty("auto")
        AUTO,

        /**
         * Uses the default service tier.
         */
        @JsonProperty("default")
        DEFAULT;
    }

    /**
     * Enumerates the levels of reasoning effort for generating responses.
     */
    public enum ReasoningEffort {
        /**
         * Low level of reasoning effort.
         */
        @JsonProperty("low")
        LOW,

        /**
         * Medium level of reasoning effort.
         */
        @JsonProperty("medium")
        MEDIUM,

        /**
         * High level of reasoning effort.
         */
        @JsonProperty("high")
        HIGH;
    }

    /**
     * Represents the audio configuration for the chat request.
     */
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Audio {

        /**
         * The voice configuration for audio responses.
         */
        @Required
        private Voice voice;

        /**
         * The audio format for audio responses.
         */
        @Required
        private AudioFormat format;

        /**
         * Constructs an {@code Audio} instance with the specified voice and format.
         *
         * @param voice  the voice configuration
         * @param format the audio format
         */
        private Audio(Voice voice, AudioFormat format) {
            this.voice = voice;
            this.format = format;
        }

        /**
         * Creates an {@link Audio} instance with the specified voice and format.
         *
         * @param voice  the voice configuration
         * @param format the audio format
         * @return a new {@link Audio} instance
         */
        public static Audio of(Voice voice, AudioFormat format) {
            return new Audio(voice, format);
        }
    }
}