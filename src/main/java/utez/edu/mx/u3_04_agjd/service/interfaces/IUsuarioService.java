package utez.edu.mx.u3_04_agjd.service.interfaces;

import utez.edu.mx.u3_04_agjd.model.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// PRINCIPIO SOLID: Interface Segregation Principle (ISP)
public interface IUsuarioService {
    List<Usuario> obtenerTodosLosUsuarios();
    Optional<Usuario> obtenerUsuarioPorId(UUID id);
    Optional<Usuario> obtenerUsuarioPorUsername(String username);
    List<Usuario> obtenerUsuariosPorRol(String rol);
    Usuario crearUsuario(Usuario usuario);
    Optional<Usuario> actualizarUsuario(UUID id, Usuario usuario);
    boolean eliminarUsuario(UUID id);
    boolean existeUsername(String username);
    void actualizarUltimoAcceso(String username);
}
