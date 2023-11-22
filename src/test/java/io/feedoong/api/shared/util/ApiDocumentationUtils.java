package io.feedoong.api.shared.util;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public class ApiDocumentationUtils {
    public static OperationRequestPreprocessor fromRequest() {
        return preprocessRequest(
                modifyUris()
                        .scheme("https")
                        .host("api.feedoong.com")
                        .removePort(),
                prettyPrint()
        );
    }

    public static OperationResponsePreprocessor fromResponse() {
        return preprocessResponse(prettyPrint());
    }
}
