package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Developer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DeveloperRepositoryWithBagRelationships {
    Optional<Developer> fetchBagRelationships(Optional<Developer> developer);

    List<Developer> fetchBagRelationships(List<Developer> developers);

    Page<Developer> fetchBagRelationships(Page<Developer> developers);
}
