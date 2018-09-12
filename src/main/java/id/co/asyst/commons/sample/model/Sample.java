package id.co.asyst.commons.sample.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import id.co.asyst.commons.core.model.BaseModel;

@Entity
@Table(name = "SAMPLE")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"created_time", "updated_time", "hibernateLazyInitializer", "handler"},
        allowGetters = true, ignoreUnknown = true)
public class Sample extends BaseModel
{

	private static final long serialVersionUID = -6026498239099985696L;

	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESC")
	private String description;


	public Sample() {
		this(false);
	}

	public Sample(boolean init) {
		init();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toDataString(boolean isDetail) {
		if (!isDetail)
		{
			return toString();
		}
		return "Sample{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				'}';
	}

}
