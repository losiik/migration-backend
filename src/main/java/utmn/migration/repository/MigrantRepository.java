package utmn.migration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utmn.migration.entity.Migrant;
import utmn.migration.entity.User;

import java.util.Optional;

@Repository
public interface MigrantRepository extends JpaRepository<Migrant, Long> {
    Optional<Migrant> findByUser(User user);
    Optional<Migrant> findByUserId(Long userId);
}
