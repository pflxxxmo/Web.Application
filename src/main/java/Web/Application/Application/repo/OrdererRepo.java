package Web.Application.Application.repo;

import Web.Application.Application.domain.Orderer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdererRepo extends JpaRepository<Orderer,Long> {
}
