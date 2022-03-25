package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Developer;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class DeveloperRepositoryWithBagRelationshipsImpl implements DeveloperRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Developer> fetchBagRelationships(Optional<Developer> developer) {
        return developer.map(this::fetchSkills);
    }

    @Override
    public Page<Developer> fetchBagRelationships(Page<Developer> developers) {
        return new PageImpl<>(fetchBagRelationships(developers.getContent()), developers.getPageable(), developers.getTotalElements());
    }

    @Override
    public List<Developer> fetchBagRelationships(List<Developer> developers) {
        return Optional.of(developers).map(this::fetchSkills).get();
    }

    Developer fetchSkills(Developer result) {
        return entityManager
            .createQuery(
                "select developer from Developer developer left join fetch developer.skills where developer is :developer",
                Developer.class
            )
            .setParameter("developer", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Developer> fetchSkills(List<Developer> developers) {
        return entityManager
            .createQuery(
                "select distinct developer from Developer developer left join fetch developer.skills where developer in :developers",
                Developer.class
            )
            .setParameter("developers", developers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
