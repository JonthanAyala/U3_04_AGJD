package utez.edu.mx.u3_04_agjd.repository;

import utez.edu.mx.u3_04_agjd.model.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, UUID> {
    Optional<Almacen> findByClaveAlmacen(String claveAlmacen);
    List<Almacen> findBySede_Id(UUID sedeId);
    List<Almacen> findByTamano(Almacen.TamanoAlmacen tamano);
}
