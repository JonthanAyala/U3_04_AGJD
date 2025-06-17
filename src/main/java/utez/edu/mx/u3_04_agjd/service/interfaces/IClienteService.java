package utez.edu.mx.u3_04_agjd.service.interfaces;

import utez.edu.mx.u3_04_agjd.model.Cliente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// PRINCIPIO SOLID: Interface Segregation Principle (ISP)
public interface IClienteService {
    List<Cliente> obtenerTodosLosClientes();
    Optional<Cliente> obtenerClientePorId(UUID id);
    Optional<Cliente> obtenerClientePorCorreo(String correoElectronico);
    Optional<Cliente> obtenerClientePorTelefono(String numeroTelefono);
    Cliente crearCliente(Cliente cliente);
    Optional<Cliente> actualizarCliente(UUID id, Cliente cliente);
    boolean eliminarCliente(UUID id);
}
