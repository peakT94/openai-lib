package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.cleverclient.util.CommonUtil;
import io.github.sashirestela.openai.common.content.ContentPart.ChatContentPart;
import io.github.sashirestela.openai.common.tool.ToolCall;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Represents a chat message in the OpenAI chat domain.
 * <p>
 * This abstract class serves as the base for various types of chat messages, each corresponding to different
 * roles such as developer, system, user, assistant, and tool. It encapsulates the common properties shared
 * among all chat messages.
 * </p>
 */
@Getter
@Setter
public abstract class ChatMessage {

    /**
     * The role of the entity sending the message.
     */
    protected ChatRole role;

    /**
     * Enumerates the possible roles of a chat message sender.
     */
    public enum ChatRole {

        /**
         * Represents a developer sending the message.
         */
        @JsonProperty("developer")
        DEVELOPER,

        /**
         * Represents the system sending the message.
         */
        @JsonProperty("system")
        SYSTEM,

        /**
         * Represents a user sending the message.
         */
        @JsonProperty("user")
        USER,

        /**
         * Represents the assistant sending the message.
         */
        @JsonProperty("assistant")
        ASSISTANT,

        /**
         * Represents a tool sending the message.
         */
        @JsonProperty("tool")
        TOOL;
    }

    /**
     * Represents a message sent by a developer.
     */
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DeveloperMessage extends ChatMessage {

        /**
         * The content of the developer's message.
         */
        @Required
        private String content;

        /**
         * The name associated with the developer, if any.
         */
        private String name;

        /**
         * Constructs a {@code DeveloperMessage} with the specified content and name.
         *
         * @param content the content of the message
         * @param name    the name of the developer
         */
        private DeveloperMessage(String content, String name) {
            this.role = ChatRole.DEVELOPER;
            this.content = content;
            this.name = name;
        }

        /**
         * Creates a {@link DeveloperMessage} instance with the specified content and name.
         *
         * @param content the content of the message
         * @param name    the name of the developer
         * @return a new {@link DeveloperMessage} instance
         */
        public static DeveloperMessage of(String content, String name) {
            return new DeveloperMessage(content, name);
        }

        /**
         * Creates a {@link DeveloperMessage} instance with the specified content.
         *
         * @param content the content of the message
         * @return a new {@link DeveloperMessage} instance
         */
        public static DeveloperMessage of(String content) {
            return new DeveloperMessage(content, null);
        }
    }

    /**
     * Represents a message sent by the system.
     */
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SystemMessage extends ChatMessage {

        /**
         * The content of the system's message.
         */
        @Required
        private String content;

        /**
         * The name associated with the system, if any.
         */
        private String name;

        /**
         * Constructs a {@code SystemMessage} with the specified content and name.
         *
         * @param content the content of the message
         * @param name    the name of the system
         */
        private SystemMessage(String content, String name) {
            this.role = ChatRole.SYSTEM;
            this.content = content;
            this.name = name;
        }

        /**
         * Creates a {@link SystemMessage} instance with the specified content and name.
         *
         * @param content the content of the message
         * @param name    the name of the system
         * @return a new {@link SystemMessage} instance
         */
        public static SystemMessage of(String content, String name) {
            return new SystemMessage(content, name);
        }

        /**
         * Creates a {@link SystemMessage} instance with the specified content.
         *
         * @param content the content of the message
         * @return a new {@link SystemMessage} instance
         */
        public static SystemMessage of(String content) {
            return new SystemMessage(content, null);
        }
    }

    /**
     * Represents a message sent by a user.
     */
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UserMessage extends ChatMessage {

        /**
         * The content of the user's message. Can be a {@link String} or {@link ChatContentPart}.
         */
        @Required
        @ObjectType(baseClass = String.class)
        @ObjectType(baseClass = ChatContentPart.class, firstGroup = true)
        private Object content;

        /**
         * The name associated with the user, if any.
         */
        private String name;

        /**
         * Constructs a {@code UserMessage} with the specified content and name.
         *
         * @param content the content of the message
         * @param name    the name of the user
         */
        private UserMessage(Object content, String name) {
            this.role = ChatRole.USER;
            this.content = content;
            this.name = name;
        }

        /**
         * Creates a {@link UserMessage} instance with the specified content and name.
         *
         * @param content the content of the message
         * @param name    the name of the user
         * @return a new {@link UserMessage} instance
         */
        public static UserMessage of(Object content, String name) {
            return new UserMessage(content, name);
        }

        /**
         * Creates a {@link UserMessage} instance with the specified content.
         *
         * @param content the content of the message
         * @return a new {@link UserMessage} instance
         */
        public static UserMessage of(Object content) {
            return new UserMessage(content, null);
        }
    }

    /**
     * Represents a message sent by the assistant.
     */
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class AssistantMessage extends ChatMessage {

        /**
         * The content of the assistant's message. Can be a {@link String} or {@link ChatContentPart}.
         */
        @JsonInclude
        @ObjectType(baseClass = String.class)
        @ObjectType(baseClass = ChatContentPart.class, firstGroup = true)
        private Object content;

        /**
         * The reason for refusal, if any.
         */
        private String refusal;

        /**
         * The name associated with the assistant, if any.
         */
        private String name;

        /**
         * The audio response associated with the assistant's message, if any.
         */
        private Audio audio;

        /**
         * A list of tool calls made by the assistant, if any.
         */
        private List<ToolCall> toolCalls;

        /**
         * Constructs an {@code AssistantMessage} with the specified parameters.
         *
         * @param content    the content of the message
         * @param refusal    the refusal reason
         * @param name       the name of the assistant
         * @param audioId    the ID of the audio response
         * @param toolCalls  a list of tool calls made by the assistant
         */
        @Builder
        public AssistantMessage(String content, String refusal, String name, String audioId, List<ToolCall> toolCalls) {
            this.role = ChatRole.ASSISTANT;
            this.content = content;
            this.refusal = refusal;
            this.name = name;
            this.audio = CommonUtil.isNullOrEmpty(audioId) ? null : new Audio(audioId);
            this.toolCalls = toolCalls;
        }

        /**
         * Represents an audio response in the assistant's message.
         */
        @Getter
        @ToString
        static class Audio {

            /**
             * The unique identifier for the audio response.
             */
            @Required
            private String id;

            /**
             * Constructs an {@code Audio} with the specified ID.
             *
             * @param id the unique identifier for the audio response
             */
            public Audio(String id) {
                this.id = id;
            }
        }

        /**
         * Creates an {@link AssistantMessage} instance with the specified content.
         *
         * @param content the content of the message
         * @return a new {@link AssistantMessage} instance
         */
        public static AssistantMessage of(String content) {
            return AssistantMessage.builder().content(content).build();
        }

        /**
         * Creates an {@link AssistantMessage} instance with the specified tool calls.
         *
         * @param toolCalls a list of tool calls made by the assistant
         * @return a new {@link AssistantMessage} instance
         */
        public static AssistantMessage of(List<ToolCall> toolCalls) {
            return AssistantMessage.builder().toolCalls(toolCalls).build();
        }
    }

    /**
     * Represents a message sent by a tool.
     */
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ToolMessage extends ChatMessage {

        /**
         * The content of the tool's message.
         */
        @Required
        private String content;

        /**
         * The identifier for the tool call.
         */
        @Required
        private String toolCallId;

        /**
         * Constructs a {@code ToolMessage} with the specified content and tool call ID.
         *
         * @param content     the content of the message
         * @param toolCallId  the identifier for the tool call
         */
        private ToolMessage(String content, String toolCallId) {
            this.role = ChatRole.TOOL;
            this.content = content;
            this.toolCallId = toolCallId;
        }

        /**
         * Creates a {@link ToolMessage} instance with the specified content and tool call ID.
         *
         * @param content     the content of the message
         * @param toolCallId  the identifier for the tool call
         * @return a new {@link ToolMessage} instance
         */
        public static ToolMessage of(String content, String toolCallId) {
            return new ToolMessage(content, toolCallId);
        }
    }

    /**
     * Represents a response message in the chat.
     */
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseMessage extends ChatMessage {

        /**
         * The content of the response message.
         */
        private String content;

        /**
         * A list of tool calls included in the response.
         */
        private List<ToolCall> toolCalls;

        /**
         * The reason for refusal, if any.
         */
        private String refusal;

        /**
         * The audio response associated with the message, if any.
         */
        private AudioResponse audio;

        /**
         * Represents an audio response in the message.
         */
        @NoArgsConstructor
        @Getter
        @Setter
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class AudioResponse {

            /**
             * The unique identifier for the audio response.
             */
            private String id;

            /**
             * The expiration timestamp for the audio response.
             */
            private Integer expiresAt;

            /**
             * The binary data of the audio response.
             */
            private String data;

            /**
             * The transcript of the audio response.
             */
            private String transcript;
        }
    }
}