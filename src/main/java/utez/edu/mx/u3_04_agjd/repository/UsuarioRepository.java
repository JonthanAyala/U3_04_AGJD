package utez.edu.mx.u3_04_agjd.repository;

import utez.edu.mx.u3_04_agjd.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByUsername(String username);
    List<Usuario> findByRol(String rol);
    List<Usuario> findByActivo(Boolean activo);
    boolean existsByUsername(String username);
}
