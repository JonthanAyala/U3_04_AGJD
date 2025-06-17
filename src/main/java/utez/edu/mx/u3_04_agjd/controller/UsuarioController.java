package utez.edu.mx.u3_04_agjd.controller;

import utez.edu.mx.u3_04_agjd.dto.UsuarioCreateDTO;
import utez.edu.mx.u3_04_agjd.dto.UsuarioUpdateDTO;
import utez.edu.mx.u3_04_agjd.model.Usuario;
import utez.edu.mx.u3_04_agjd.service.interfaces.IAuditService;
import utez.edu.mx.u3_04_agjd.service.interfaces.IUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final IUsuarioService usuarioService;
    private final IAuditService auditService;

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable UUID id) {
        return usuarioService.obtenerUsuarioPorId(id)
                .map(usuario -> ResponseEntity.ok(usuario))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Usuario> obtenerUsuarioPorUsername(@PathVariable String username) {
        return usuarioService.obtenerUsuarioPorUsername(username)
                .map(usuario -> ResponseEntity.ok(usuario))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> obtenerUsuariosPorRol(@PathVariable String rol) {
        List<Usuario> usuarios = usuarioService.obtenerUsuariosPorRol(rol);
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioDTO) {
        try {
            // Validar que el rol sea válido
            if (!usuarioDTO.getRol().equals("ADMIN") && !usuarioDTO.getRol().equals("EMPLEADO")) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Rol inválido. Solo se permiten: ADMIN, EMPLEADO");
                return ResponseEntity.badRequest().body(error);
            }

            // Verificar si el username ya existe
            if (usuarioService.existeUsername(usuarioDTO.getUsername())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El nombre de usuario ya existe");
                return ResponseEntity.badRequest().body(error);
            }

            Usuario usuario = new Usuario();
            usuario.setUsername(usuarioDTO.getUsername());
            usuario.setPassword(usuarioDTO.getPassword());
            usuario.setRol(usuarioDTO.getRol());

            Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
            auditService.logAction("CREATE", "Usuario", nuevoUsuario.getId().toString(),
                    "Nuevo usuario creado: " + nuevoUsuario.getUsername() + " con rol: " + nuevoUsuario.getRol());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (Exception e) {
            auditService.logAction("CREATE_FAILED", "Usuario", null, "Error creando usuario: " + e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable UUID id, @Valid @RequestBody UsuarioUpdateDTO usuarioDTO) {
        try {
            // Validar que el rol sea válido
            if (!usuarioDTO.getRol().equals("ADMIN") && !usuarioDTO.getRol().equals("EMPLEADO")) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Rol inválido. Solo se permiten: ADMIN, EMPLEADO");
                return ResponseEntity.badRequest().body(error);
            }

            Usuario usuario = new Usuario();
            usuario.setUsername(usuarioDTO.getUsername());
            if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().trim().isEmpty()) {
                usuario.setPassword(usuarioDTO.getPassword());
            }
            usuario.setRol(usuarioDTO.getRol());
            usuario.setActivo(usuarioDTO.getActivo() != null ? usuarioDTO.getActivo() : true);

            return usuarioService.actualizarUsuario(id, usuario)
                    .map(usuarioActualizado -> {
                        auditService.logAction("UPDATE", "Usuario", id.toString(),
                                "Usuario actualizado: " + usuarioActualizado.getUsername());
                        return ResponseEntity.ok(usuarioActualizado);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            auditService.logAction("UPDATE_FAILED", "Usuario", id.toString(), "Error actualizando usuario: " + e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable UUID id) {
        if (usuarioService.eliminarUsuario(id)) {
            auditService.logAction("DELETE", "Usuario", id.toString(), "Usuario eliminado");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<Usuario> cambiarEstadoUsuario(@PathVariable UUID id) {
        return usuarioService.obtenerUsuarioPorId(id)
                .map(usuario -> {
                    usuario.setActivo(!usuario.getActivo());
                    return usuarioService.actualizarUsuario(id, usuario)
                            .map(usuarioActualizado -> {
                                String accion = usuarioActualizado.getActivo() ? "ACTIVATED" : "DEACTIVATED";
                                auditService.logAction(accion, "Usuario", id.toString(),
                                        "Usuario " + (usuarioActualizado.getActivo() ? "activado" : "desactivado"));
                                return ResponseEntity.ok(usuarioActualizado);
                            })
                            .orElse(ResponseEntity.internalServerError().build());
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
