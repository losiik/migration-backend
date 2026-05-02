package utmn.migration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utmn.migration.entity.Citizenship;

import java.util.List;
import java.util.Optional;

@Repository
public interface CitizenshipRepository extends JpaRepository<Citizenship, Long> {
    List<Citizenship> findAllByOrderByCountryAsc();
    Optional<Citizenship> findByCodeIgnoreCase(String code);
}
