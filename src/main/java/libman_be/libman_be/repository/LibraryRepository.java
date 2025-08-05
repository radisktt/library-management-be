package libman_be.libman_be.repository;

import libman_be.libman_be.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Long> {
    Library findByName(String name);
}
