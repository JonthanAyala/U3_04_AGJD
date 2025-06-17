package utez.edu.mx.U3_04_AGJD.repository;

import utez.edu.mx.U3_04_AGJD.model.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, UUID> {
    Optional<Almacen> findByClaveAlmacen(String claveAlmacen);
    List<Almacen> findBySedeId(UUID sedeId);
    List<Almacen> findByTamano(Almacen.TamanoAlmacen tamano);
}
