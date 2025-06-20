package utez.edu.mx.u3_04_agjd.repository;

import utez.edu.mx.u3_04_agjd.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByEntidad(String entidad);
    List<AuditLog> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    List<AuditLog> findByAccionAndEntidad(String accion, String entidad);
}
