package utez.edu.mx.u3_04_agjd.controller;

import utez.edu.mx.u3_04_agjd.dto.ClienteCreateDTO;
import utez.edu.mx.u3_04_agjd.dto.ClienteUpdateDTO;
import utez.edu.mx.u3_04_agjd.model.Cliente;
import utez.edu.mx.u3_04_agjd.service.interfaces.IClienteService;
import utez.edu.mx.u3_04_agjd.service.interfaces.IAuditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final IClienteService clienteService;
    private final IAuditService auditService;

    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerTodosLosClientes() {
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable UUID id) {
        return clienteService.obtenerClientePorId(id)
                .map(cliente -> ResponseEntity.ok(cliente))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/correo/{correoElectronico}")
    public ResponseEntity<Cliente> obtenerClientePorCorreo(@PathVariable String correoElectronico) {
        return clienteService.obtenerClientePorCorreo(correoElectronico)
                .map(cliente -> ResponseEntity.ok(cliente))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/telefono/{numeroTelefono}")
    public ResponseEntity<Cliente> obtenerClientePorTelefono(@PathVariable String numeroTelefono) {
        return clienteService.obtenerClientePorTelefono(numeroTelefono)
                .map(cliente -> ResponseEntity.ok(cliente))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody ClienteCreateDTO clienteDTO) {
        try {
            Cliente cliente = new Cliente();
            cliente.setNombreCompleto(clienteDTO.getNombreCompleto());
            cliente.setNumeroTelefono(clienteDTO.getNumeroTelefono());
            cliente.setCorreoElectronico(clienteDTO.getCorreoElectronico());

            Cliente nuevoCliente = clienteService.crearCliente(cliente);
            auditService.logAction("CREATE", "Cliente", nuevoCliente.getId().toString(),
                    "Nuevo cliente creado: " + nuevoCliente.getNombreCompleto());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
        } catch (Exception e) {
            auditService.logAction("CREATE_FAILED", "Cliente", null, "Error creando cliente: " + e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable UUID id, @Valid @RequestBody ClienteUpdateDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setNombreCompleto(clienteDTO.getNombreCompleto());
        cliente.setNumeroTelefono(clienteDTO.getNumeroTelefono());
        cliente.setCorreoElectronico(clienteDTO.getCorreoElectronico());

        return clienteService.actualizarCliente(id, cliente)
                .map(clienteActualizado -> {
                    auditService.logAction("UPDATE", "Cliente", id.toString(),
                            "Cliente actualizado: " + clienteActualizado.getNombreCompleto());
                    return ResponseEntity.ok(clienteActualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable UUID id) {
        if (clienteService.eliminarCliente(id)) {
            auditService.logAction("DELETE", "Cliente", id.toString(), "Cliente eliminado");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
