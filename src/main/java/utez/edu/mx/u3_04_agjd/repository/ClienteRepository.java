package utez.edu.mx.U3_04_AGJD.repository;

import utez.edu.mx.U3_04_AGJD.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    Optional<Cliente> findByCorreoElectronico(String correoElectronico);
    Optional<Cliente> findByNumeroTelefono(String numeroTelefono);
}
