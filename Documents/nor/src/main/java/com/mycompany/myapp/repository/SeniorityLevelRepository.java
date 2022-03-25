package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SeniorityLevel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SeniorityLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeniorityLevelRepository extends JpaRepository<SeniorityLevel, Long> {}
