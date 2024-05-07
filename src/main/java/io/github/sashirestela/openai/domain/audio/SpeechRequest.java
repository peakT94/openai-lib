package io.github.sashirestela.openai.domain.audio;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SpeechRequest {

    @Required
    private String model;

    @Required
    @Size(max = 4096)
    private String input;

    @Required
    private Voice voice;

    private SpeechResponseFormat responseFormat;

    @Range(min = 0.25, max = 4.0)
    private Double speed;

    public enum Voice {

        @JsonProperty("alloy")
        ALLOY,

        @JsonProperty("echo")
        ECHO,

        @JsonProperty("fable")
        FABLE,

        @JsonProperty("onyx")
        ONYX,

        @JsonProperty("nova")
        NOVA,

        @JsonProperty("shimmer")
        SHIMMER;

    }

    public enum SpeechResponseFormat {

        @JsonProperty("mp3")
        MP3,

        @JsonProperty("opus")
        OPUS,

        @JsonProperty("aac")
        AAC,

        @JsonProperty("flac")
        FLAC,

        @JsonProperty("wav")
        WAV,

        @JsonProperty("pcm")
        PCM;

    }

}
