package id.co.asyst.commons.sample.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import id.co.asyst.commons.core.service.BaseRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(value = {"created_time", "updated_time", "hibernateLazyInitializer", "handler"},
        allowGetters = true, ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleRequest extends BaseRequest
{

    @Valid
    @JsonProperty("parameter")
    private SampleParameter parameter;

    public SampleRequest() {
    }

    public SampleRequest(SampleParameter parameter) {
        this.parameter = parameter;
    }

    public SampleParameter getParameter() {
        return parameter;
    }

    public void setParameter(SampleParameter parameter) {
        this.parameter = parameter;
    }


}
