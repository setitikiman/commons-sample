package id.co.asyst.commons.sample.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import id.co.asyst.commons.core.service.BaseParameter;
import id.co.asyst.commons.sample.model.Sample;

@JsonIgnoreProperties(value = {"created_time", "updated_time", "hibernateLazyInitializer", "handler"},
        allowGetters = true, ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleParameter extends BaseParameter
{

    @JsonProperty("data")
    private Sample sample;

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }
}
