package utmn.migration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utmn.migration.entity.Patent;

import java.util.Optional;

@Repository
public interface PatentRepository extends JpaRepository<Patent, Long> {
    Optional<Patent> findByMigrantId(Long migrantId);
    void deleteByMigrantId(Long migrantId);
}
