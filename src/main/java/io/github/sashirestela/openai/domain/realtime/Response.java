package io.github.sashirestela.openai.domain.realtime;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Response {

    private String id;
    private String object;
    private String status;
    private StatusDetails statusDetails;
    private List<Item> output;
    private UsageResponse usage;

    @Getter
    @ToString
    @NoArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class StatusDetails {

        private String type;
        private String reason;
        private ErrorDetail error;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ErrorDetail {

        private String type;
        private String code;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static class UsageResponse {

        private Integer totalTokens;
        private Integer inputTokens;
        private Integer outputTokens;
        private TokenDetails inputTokenDetails;
        private TokenDetails outputTokenDetails;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static class TokenDetails {

        private Integer textTokens;
        private Integer audioTokens;
        private Integer cachedTokens;

    }
}
