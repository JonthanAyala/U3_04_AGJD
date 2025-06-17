package utez.edu.mx.u3_04_agjd.service;

import utez.edu.mx.u3_04_agjd.model.Cliente;
import utez.edu.mx.u3_04_agjd.repository.ClienteRepository;
import utez.edu.mx.u3_04_agjd.service.interfaces.IClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// PRINCIPIO SOLID: Single Responsibility Principle (SRP) - Solo maneja lógica de Clientes
// PRINCIPIO SOLID: Dependency Inversion Principle (DIP) - Implementa interfaz
@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService implements IClienteService {
    
    private final ClienteRepository clienteRepository;
    
    @Override
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }
    
    @Override
    public Optional<Cliente> obtenerClientePorId(UUID id) {
        return clienteRepository.findById(id);
    }
    
    @Override
    public Optional<Cliente> obtenerClientePorCorreo(String correoElectronico) {
        return clienteRepository.findByCorreoElectronico(correoElectronico);
    }
    
    @Override
    public Optional<Cliente> obtenerClientePorTelefono(String numeroTelefono) {
        return clienteRepository.findByNumeroTelefono(numeroTelefono);
    }
    
    @Override
    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    @Override
    public Optional<Cliente> actualizarCliente(UUID id, Cliente clienteActualizado) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNombreCompleto(clienteActualizado.getNombreCompleto());
                    cliente.setNumeroTelefono(clienteActualizado.getNumeroTelefono());
                    cliente.setCorreoElectronico(clienteActualizado.getCorreoElectronico());
                    return clienteRepository.save(cliente);
                });
    }
    
    @Override
    public boolean eliminarCliente(UUID id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
