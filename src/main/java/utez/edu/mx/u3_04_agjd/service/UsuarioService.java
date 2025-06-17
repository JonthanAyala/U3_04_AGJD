package utez.edu.mx.u3_04_agjd.service;

import utez.edu.mx.u3_04_agjd.model.Usuario;
import utez.edu.mx.u3_04_agjd.repository.UsuarioRepository;
import utez.edu.mx.u3_04_agjd.service.interfaces.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// PRINCIPIO SOLID: Single Responsibility Principle (SRP) - Solo maneja lógica de Usuarios
// PRINCIPIO SOLID: Dependency Inversion Principle (DIP) - Implementa interfaz
@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(UUID id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public List<Usuario> obtenerUsuariosPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        // Cifrar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> actualizarUsuario(UUID id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setUsername(usuarioActualizado.getUsername());
                    // Solo actualizar contraseña si se proporciona una nueva
                    if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
                        usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
                    }
                    usuario.setRol(usuarioActualizado.getRol());
                    usuario.setActivo(usuarioActualizado.getActivo());
                    return usuarioRepository.save(usuario);
                });
    }

    @Override
    public boolean eliminarUsuario(UUID id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean existeUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    public void actualizarUltimoAcceso(String username) {
        usuarioRepository.findByUsername(username)
                .ifPresent(usuario -> {
                    usuario.actualizarUltimoAcceso();
                    usuarioRepository.save(usuario);
                });
    }
}
