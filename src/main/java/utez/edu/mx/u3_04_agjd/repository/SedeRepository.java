package utez.edu.mx.u3_04_agjd.repository;

import utez.edu.mx.u3_04_agjd.model.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SedeRepository extends JpaRepository<Sede, UUID> {
    Optional<Sede> findByClaveSede(String claveSede);
    List<Sede> findByEstado(String estado);
    List<Sede> findByMunicipio(String municipio);
    List<Sede> findByEstadoAndMunicipio(String estado, String municipio);
}
