package utez.edu.mx.u3_04_agjd.service.interfaces;

import utez.edu.mx.u3_04_agjd.model.Sede;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// PRINCIPIO SOLID: Interface Segregation Principle (ISP)
public interface ISedeService {
    List<Sede> obtenerTodasLasSedes();
    Optional<Sede> obtenerSedePorId(UUID id);
    Optional<Sede> obtenerSedePorClave(String claveSede);
    List<Sede> obtenerSedesPorEstado(String estado);
    List<Sede> obtenerSedesPorMunicipio(String municipio);
    List<Sede> obtenerSedesPorEstadoYMunicipio(String estado, String municipio);
    Sede crearSede(Sede sede);
    Optional<Sede> actualizarSede(UUID id, Sede sede);
    boolean eliminarSede(UUID id);
}
