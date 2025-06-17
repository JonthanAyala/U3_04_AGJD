package utez.edu.mx.U3_04_AGJD.controller;

import utez.edu.mx.U3_04_AGJD.model.Almacen;
import utez.edu.mx.U3_04_AGJD.model.Cliente;
import utez.edu.mx.U3_04_AGJD.model.Sede;
import utez.edu.mx.U3_04_AGJD.service.interfaces.IAlmacenService;
import utez.edu.mx.U3_04_AGJD.service.interfaces.IAuditService;
import utez.edu.mx.U3_04_AGJD.service.interfaces.IClienteService;
import utez.edu.mx.U3_04_AGJD.service.interfaces.ISedeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AlmacenesController {
    
    private final ISedeService sedeService;
    private final IAlmacenService almacenService;
    private final IClienteService clienteService;
    private final IAuditService auditService;
    
    // ==================== SEDES ====================
    
    @GetMapping("/sedes")
    public ResponseEntity<List<Sede>> obtenerTodasLasSedes() {
        List<Sede> sedes = sedeService.obtenerTodasLasSedes();
        return ResponseEntity.ok(sedes);
    }
    
    @GetMapping("/sedes/{id}")
    public ResponseEntity<Sede> obtenerSedePorId(@PathVariable UUID id) {
        return sedeService.obtenerSedePorId(id)
                .map(sede -> ResponseEntity.ok(sede))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/sedes/clave/{claveSede}")
    public ResponseEntity<Sede> obtenerSedePorClave(@PathVariable String claveSede) {
        return sedeService.obtenerSedePorClave(claveSede)
                .map(sede -> ResponseEntity.ok(sede))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/sedes/estado/{estado}")
    public ResponseEntity<List<Sede>> obtenerSedesPorEstado(@PathVariable String estado) {
        List<Sede> sedes = sedeService.obtenerSedesPorEstado(estado);
        return ResponseEntity.ok(sedes);
    }
    
    @PostMapping("/sedes")
    public ResponseEntity<Sede> crearSede(@Valid @RequestBody Sede sede) {
        try {
            Sede nuevaSede = sedeService.crearSede(sede);
            auditService.logAction("CREATE", "Sede", nuevaSede.getId().toString(), 
                    "Nueva sede creada: " + nuevaSede.getClaveSede());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSede);
        } catch (Exception e) {
            auditService.logAction("CREATE_FAILED", "Sede", null, "Error creando sede: " + e.getMessage());
            throw e;
        }
    }
    
    @PutMapping("/sedes/{id}")
    public ResponseEntity<Sede> actualizarSede(@PathVariable UUID id, @Valid @RequestBody Sede sede) {
        return sedeService.actualizarSede(id, sede)
                .map(sedeActualizada -> {
                    auditService.logAction("UPDATE", "Sede", id.toString(), 
                            "Sede actualizada: " + sedeActualizada.getClaveSede());
                    return ResponseEntity.ok(sedeActualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/sedes/{id}")
    public ResponseEntity<Void> eliminarSede(@PathVariable UUID id) {
        if (sedeService.eliminarSede(id)) {
            auditService.logAction("DELETE", "Sede", id.toString(), "Sede eliminada");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    // ==================== ALMACENES ====================
    
    @GetMapping("/almacenes")
    public ResponseEntity<List<Almacen>> obtenerTodosLosAlmacenes() {
        List<Almacen> almacenes = almacenService.obtenerTodosLosAlmacenes();
        return ResponseEntity.ok(almacenes);
    }
    
    @GetMapping("/almacenes/{id}")
    public ResponseEntity<Almacen> obtenerAlmacenPorId(@PathVariable UUID id) {
        return almacenService.obtenerAlmacenPorId(id)
                .map(almacen -> ResponseEntity.ok(almacen))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/almacenes/clave/{claveAlmacen}")
    public ResponseEntity<Almacen> obtenerAlmacenPorClave(@PathVariable String claveAlmacen) {
        return almacenService.obtenerAlmacenPorClave(claveAlmacen)
                .map(almacen -> ResponseEntity.ok(almacen))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/almacenes/sede/{sedeId}")
    public ResponseEntity<List<Almacen>> obtenerAlmacenesPorSede(@PathVariable UUID sedeId) {
        List<Almacen> almacenes = almacenService.obtenerAlmacenesPorSede(sedeId);
        return ResponseEntity.ok(almacenes);
    }
    
    @GetMapping("/almacenes/tamano/{tamano}")
    public ResponseEntity<List<Almacen>> obtenerAlmacenesPorTamano(@PathVariable Almacen.TamanoAlmacen tamano) {
        List<Almacen> almacenes = almacenService.obtenerAlmacenesPorTamano(tamano);
        return ResponseEntity.ok(almacenes);
    }
    
    @PostMapping("/almacenes")
    public ResponseEntity<Almacen> crearAlmacen(@Valid @RequestBody Almacen almacen) {
        return almacenService.crearAlmacen(almacen)
                .map(nuevoAlmacen -> {
                    auditService.logAction("CREATE", "Almacen", nuevoAlmacen.getId().toString(), 
                            "Nuevo almacén creado: " + nuevoAlmacen.getClaveAlmacen());
                    return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAlmacen);
                })
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @PutMapping("/almacenes/{id}")
    public ResponseEntity<Almacen> actualizarAlmacen(@PathVariable UUID id, @Valid @RequestBody Almacen almacen) {
        return almacenService.actualizarAlmacen(id, almacen)
                .map(almacenActualizado -> {
                    auditService.logAction("UPDATE", "Almacen", id.toString(), 
                            "Almacén actualizado: " + almacenActualizado.getClaveAlmacen());
                    return ResponseEntity.ok(almacenActualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/almacenes/{id}")
    public ResponseEntity<Void> eliminarAlmacen(@PathVariable UUID id) {
        if (almacenService.eliminarAlmacen(id)) {
            auditService.logAction("DELETE", "Almacen", id.toString(), "Almacén eliminado");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    // ==================== CLIENTES ====================
    
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> obtenerTodosLosClientes() {
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/clientes/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable UUID id) {
        return clienteService.obtenerClientePorId(id)
                .map(cliente -> ResponseEntity.ok(cliente))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/clientes/correo/{correoElectronico}")
    public ResponseEntity<Cliente> obtenerClientePorCorreo(@PathVariable String correoElectronico) {
        return clienteService.obtenerClientePorCorreo(correoElectronico)
                .map(cliente -> ResponseEntity.ok(cliente))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/clientes/telefono/{numeroTelefono}")
    public ResponseEntity<Cliente> obtenerClientePorTelefono(@PathVariable String numeroTelefono) {
        return clienteService.obtenerClientePorTelefono(numeroTelefono)
                .map(cliente -> ResponseEntity.ok(cliente))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/clientes")
    public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody Cliente cliente) {
        try {
            Cliente nuevoCliente = clienteService.crearCliente(cliente);
            auditService.logAction("CREATE", "Cliente", nuevoCliente.getId().toString(), 
                    "Nuevo cliente creado: " + nuevoCliente.getNombreCompleto());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
        } catch (Exception e) {
            auditService.logAction("CREATE_FAILED", "Cliente", null, "Error creando cliente: " + e.getMessage());
            throw e;
        }
    }
    
    @PutMapping("/clientes/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable UUID id, @Valid @RequestBody Cliente cliente) {
        return clienteService.actualizarCliente(id, cliente)
                .map(clienteActualizado -> {
                    auditService.logAction("UPDATE", "Cliente", id.toString(), 
                            "Cliente actualizado: " + clienteActualizado.getNombreCompleto());
                    return ResponseEntity.ok(clienteActualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable UUID id) {
        if (clienteService.eliminarCliente(id)) {
            auditService.logAction("DELETE", "Cliente", id.toString(), "Cliente eliminado");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
