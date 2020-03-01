package myjavapackage.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.manosbatsis.scrudbeans.api.error.ConstraintViolationEntry;
import com.github.manosbatsis.scrudbeans.api.error.Error;
import lombok.Data;

import java.util.Set;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleErrorResponse implements Error {

    private String message;

    private String remoteAddress;

    private String requestMethod;

    private String requestUrl;

    private Integer httpStatusCode;

    private String httpStatusMessage;

    private String userAgent;

    private Throwable throwable;

    private Set<ConstraintViolationEntry> validationErrors;

}
