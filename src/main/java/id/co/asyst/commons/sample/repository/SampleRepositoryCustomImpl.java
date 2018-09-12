package id.co.asyst.commons.sample.repository;

import id.co.asyst.commons.core.repository.BaseRepositoryCustom;
import id.co.asyst.commons.sample.model.Sample;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class SampleRepositoryCustomImpl extends BaseRepositoryCustom implements SampleRepositoryCustom
{

	private static final long serialVersionUID = -5313753338761352517L;

	public List<Sample> findByNameAndDescription(String name, String description) {

		Query query = entityManager.createNamedQuery("SELECT p FROM Sample p WHERE UPPER(p.name) = UPPER(:name)");
		query.setParameter("name", name);
		query.setParameter("description", description);

		return query.getResultList();
	}

	
}
