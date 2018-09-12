package id.co.asyst.commons.sample.repository;

import id.co.asyst.commons.sample.model.Sample;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import id.co.asyst.commons.core.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SampleRepository extends BaseRepository<Sample, String>, SampleRepositoryCustom
{
	
	@Query("SELECT p FROM Sample p WHERE UPPER(p.name) = UPPER(:name)")
    public List<Sample> findByName(@Param("name") String name);

}
