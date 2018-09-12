package id.co.asyst.commons.sample.repository;

import id.co.asyst.commons.core.repository.RepositoryCustom;
import id.co.asyst.commons.sample.model.Sample;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SampleRepositoryCustom<T> extends RepositoryCustom
{
	
    public List<Sample> findByNameAndDescription(String name, String description);

}
