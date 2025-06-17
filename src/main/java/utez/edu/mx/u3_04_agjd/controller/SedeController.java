package utez.edu.mx.u3_04_agjd.controller;

import utez.edu.mx.u3_04_agjd.model.Sede;
import utez.edu.mx.u3_04_agjd.service.interfaces.IAuditService;
import utez.edu.mx.u3_04_agjd.service.interfaces.ISedeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.u3_04_agjd.dto.SedeCreateDTO;
import utez.edu.mx.u3_04_agjd.dto.SedeUpdateDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sedes")
@RequiredArgsConstructor
public class SedeController {

    private final ISedeService sedeService;
    private final IAuditService auditService;

    @GetMapping
    public ResponseEntity<List<Sede>> obtenerTodasLasSedes() {
        List<Sede> sedes = sedeService.obtenerTodasLasSedes();
        return ResponseEntity.ok(sedes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sede> obtenerSedePorId(@PathVariable UUID id) {
        return sedeService.obtenerSedePorId(id)
                .map(sede -> ResponseEntity.ok(sede))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/clave/{claveSede}")
    public ResponseEntity<Sede> obtenerSedePorClave(@PathVariable String claveSede) {
        return sedeService.obtenerSedePorClave(claveSede)
                .map(sede -> ResponseEntity.ok(sede))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Sede>> obtenerSedesPorEstado(@PathVariable String estado) {
        List<Sede> sedes = sedeService.obtenerSedesPorEstado(estado);
        return ResponseEntity.ok(sedes);
    }

    @GetMapping("/municipio/{municipio}")
    public ResponseEntity<List<Sede>> obtenerSedesPorMunicipio(@PathVariable String municipio) {
        List<Sede> sedes = sedeService.obtenerSedesPorMunicipio(municipio);
        return ResponseEntity.ok(sedes);
    }

    @GetMapping("/ubicacion/{estado}/{municipio}")
    public ResponseEntity<List<Sede>> obtenerSedesPorEstadoYMunicipio(
            @PathVariable String estado, @PathVariable String municipio) {
        List<Sede> sedes = sedeService.obtenerSedesPorEstadoYMunicipio(estado, municipio);
        return ResponseEntity.ok(sedes);
    }

    @PostMapping
    public ResponseEntity<Sede> crearSede(@Valid @RequestBody SedeCreateDTO sedeDTO) {
        try {
            Sede sede = new Sede();
            sede.setEstado(sedeDTO.getEstado());
            sede.setMunicipio(sedeDTO.getMunicipio());

            Sede nuevaSede = sedeService.crearSede(sede);
            nuevaSede.actualizarClaveSede();
            sedeService.actualizarSede(nuevaSede.getId(), nuevaSede);

            auditService.logAction("CREATE", "Sede", nuevaSede.getId().toString(),
                    "Nueva sede creada: " + nuevaSede.getClaveSede());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSede);

        } catch (Exception e) {
            auditService.logAction("CREATE_FAILED", "Sede", null, "Error creando sede: " + e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sede> actualizarSede(@PathVariable UUID id, @Valid @RequestBody SedeUpdateDTO sedeDTO) {
        Sede sede = new Sede();
        sede.setEstado(sedeDTO.getEstado());
        sede.setMunicipio(sedeDTO.getMunicipio());

        return sedeService.actualizarSede(id, sede)
                .map(sedeActualizada -> {
                    auditService.logAction("UPDATE", "Sede", id.toString(),
                            "Sede actualizada: " + sedeActualizada.getClaveSede());
                    return ResponseEntity.ok(sedeActualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSede(@PathVariable UUID id) {
        if (sedeService.eliminarSede(id)) {
            auditService.logAction("DELETE", "Sede", id.toString(), "Sede eliminada");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
