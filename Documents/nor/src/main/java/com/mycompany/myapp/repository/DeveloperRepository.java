package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Developer;
import com.mycompany.myapp.domain.Skill;
import com.mycompany.myapp.projections.AmountDevByAge;
import com.mycompany.myapp.projections.AmountDevsBySkill;
import com.mycompany.myapp.service.dto.DevsBySkillDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Developer entity.
 */
@Repository
public interface DeveloperRepository extends DeveloperRepositoryWithBagRelationships, JpaRepository<Developer, Long> {
    default Optional<Developer> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Developer> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Developer> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    @Query("SELECT COUNT(d) FROM Developer d WHERE d.age=?1")
    Integer countDevsByAge(Integer age);

    @Query(value = "SELECT COUNT(*) FROM rel_developer__skill WHERE skill_id=?1", nativeQuery = true)
    Integer countDevsBySkill(Long skillId);

    // promedio de edad de los devs por skills

    @Query(value = "SELECT AVG(d.age) FROM developer d INNER JOIN rel_developer__skill ds on d.id = ds.developer_id INNER JOIN skill s on ds.skill_id = s.id WHERE s.id=:skillId", nativeQuery = true)
    Float avgAgeDevsBySkill(@Param("skillId") Long skillId);

    @Query(value = "Select developer.id, developer.name, developer.age from developer  inner join rel_developer__skills on developer.id = rel_developer__skills.developer_id  where rel_developer__skills.skills_id =:skillId", nativeQuery = true)
    List<Developer> listDevsBySkillId(@Param("skillId") Long skillId);

    @Query(value = "SELECT age, count(age) count FROM developer GROUP BY age", nativeQuery = true)
    List<AmountDevByAge> getNumberOfDevsByAge();

    // @Query("SELECT d, s FROM Developer d JOIN d.skills s")
    // List<Object[]> devsWithSkill();

    @Query("SELECT s.name AS skillName, COUNT(d.id) AS count FROM Developer d JOIN d.skills s GROUP BY s")
    List<AmountDevsBySkill> getCountDevsBySkill();
}
