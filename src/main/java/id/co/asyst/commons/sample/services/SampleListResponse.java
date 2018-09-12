package id.co.asyst.commons.sample.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import id.co.asyst.commons.core.service.BaseResponse;
import id.co.asyst.commons.sample.model.Sample;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonIgnoreProperties(value = {"created_time", "updated_time", "hibernateLazyInitializer", "handler"},
        allowGetters = true, ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleListResponse extends BaseResponse
{

    @Valid
    @NotNull
    @JsonProperty("result")
    private List<Sample> result;

    public List<Sample> getResult() {
        return result;
    }

    public void setResult(List<Sample> result) {
        this.result = result;
    }
}
