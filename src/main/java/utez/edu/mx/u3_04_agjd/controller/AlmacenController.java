package utez.edu.mx.u3_04_agjd.controller;

import utez.edu.mx.u3_04_agjd.dto.AlmacenCreateDTO;
import utez.edu.mx.u3_04_agjd.dto.AlmacenUpdateDTO;
import utez.edu.mx.u3_04_agjd.dto.VentaDTO;
import utez.edu.mx.u3_04_agjd.model.Almacen;
import utez.edu.mx.u3_04_agjd.model.Cliente;
import utez.edu.mx.u3_04_agjd.model.Sede;
import utez.edu.mx.u3_04_agjd.service.ClienteService;
import utez.edu.mx.u3_04_agjd.service.interfaces.IAlmacenService;
import utez.edu.mx.u3_04_agjd.service.interfaces.IAuditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.u3_04_agjd.service.interfaces.ISedeService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/almacenes")
@RequiredArgsConstructor
public class AlmacenController {

    private final IAlmacenService almacenService;
    private final IAuditService auditService;
    private final ISedeService sedeService;
    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Almacen>> obtenerTodosLosAlmacenes() {
        List<Almacen> almacenes = almacenService.obtenerTodosLosAlmacenes();
        return ResponseEntity.ok(almacenes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Almacen> obtenerAlmacenPorId(@PathVariable UUID id) {
        return almacenService.obtenerAlmacenPorId(id)
                .map(almacen -> ResponseEntity.ok(almacen))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/clave/{claveAlmacen}")
    public ResponseEntity<Almacen> obtenerAlmacenPorClave(@PathVariable String claveAlmacen) {
        return almacenService.obtenerAlmacenPorClave(claveAlmacen)
                .map(almacen -> ResponseEntity.ok(almacen))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sede/{sedeId}")
    public ResponseEntity<List<Almacen>> obtenerAlmacenesPorSede(@PathVariable UUID sedeId) {
        List<Almacen> almacenes = almacenService.obtenerAlmacenesPorSede(sedeId);
        return ResponseEntity.ok(almacenes);
    }

    @GetMapping("/tamano/{tamano}")
    public ResponseEntity<List<Almacen>> obtenerAlmacenesPorTamano(@PathVariable Almacen.TamanoAlmacen tamano) {
        List<Almacen> almacenes = almacenService.obtenerAlmacenesPorTamano(tamano);
        return ResponseEntity.ok(almacenes);
    }

    @PostMapping
    public ResponseEntity<Almacen> crearAlmacen(@Valid @RequestBody AlmacenCreateDTO almacenDTO) {
        try {
            Optional<Sede> sedeOpt = sedeService.obtenerSedePorId(almacenDTO.getSedeId());
            if (sedeOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Almacen almacen = new Almacen();
            almacen.setPrecioVenta(almacenDTO.getPrecioVenta());
            almacen.setPrecioRenta(almacenDTO.getPrecioRenta());
            almacen.setTamano(almacenDTO.getTamano());
            almacen.setSede(sedeOpt.get());

            Optional<Almacen> nuevoAlmacenOpt = almacenService.crearAlmacen(almacen);
            if (nuevoAlmacenOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            Almacen nuevoAlmacen = nuevoAlmacenOpt.get();
            nuevoAlmacen.actualizarClaveAlmacen();
            almacenService.actualizarAlmacen(nuevoAlmacen.getId(), nuevoAlmacen);

            auditService.logAction("CREATE", "Almacen", nuevoAlmacen.getId().toString(),
                    "Almacén creado: " + nuevoAlmacen.getClaveAlmacen());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAlmacen);
        } catch (Exception e) {
            auditService.logAction("CREATE_FAILED", "Almacen", null, "Error creando almacen: " + e.getMessage());
            throw e;
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Almacen> actualizarAlmacen(@PathVariable UUID id, @Valid @RequestBody AlmacenUpdateDTO almacenDTO) {
        Almacen almacen = new Almacen();
        almacen.setPrecioVenta(almacenDTO.getPrecioVenta());
        almacen.setPrecioRenta(almacenDTO.getPrecioRenta());
        almacen.setTamano(almacenDTO.getTamano());

        return almacenService.actualizarAlmacen(id, almacen)
                .map(almacenActualizado -> {
                    auditService.logAction("UPDATE", "Almacen", id.toString(),
                            "Almacén actualizado: " + almacenActualizado.getClaveAlmacen());
                    return ResponseEntity.ok(almacenActualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlmacen(@PathVariable UUID id) {
        if (almacenService.eliminarAlmacen(id)) {
            auditService.logAction("DELETE", "Almacen", id.toString(), "Almacén eliminado");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/venta")
    public ResponseEntity<Almacen> venderAlmacen(@RequestBody VentaDTO ventaDTO) {
        Optional<Almacen> almacenOpt = almacenService.obtenerAlmacenPorId(ventaDTO.getAlmacen());
        if (almacenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); //agrega un mensaje de error más descriptivo
        }

        if (almacenOpt.get().getCliente() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorId(ventaDTO.getCliente());
        if (clienteOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        almacenOpt.get().setCliente(clienteOpt.get());
        almacenService.actualizarAlmacen(ventaDTO.getAlmacen(), almacenOpt.get());

        auditService.logAction("SELL", "Almacen", ventaDTO.getAlmacen().toString(), "Almacén vendido a cliente: " + ventaDTO.getCliente().toString());
        return ResponseEntity.status(HttpStatus.OK).body(almacenOpt.get());
    }

}
