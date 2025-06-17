package utez.edu.mx.u3_04_agjd.service.interfaces;

import utez.edu.mx.u3_04_agjd.model.Almacen;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// PRINCIPIO SOLID: Interface Segregation Principle (ISP)
public interface IAlmacenService {
    List<Almacen> obtenerTodosLosAlmacenes();
    Optional<Almacen> obtenerAlmacenPorId(UUID id);
    Optional<Almacen> obtenerAlmacenPorClave(String claveAlmacen);
    List<Almacen> obtenerAlmacenesPorSede(UUID sedeId);
    List<Almacen> obtenerAlmacenesPorTamano(Almacen.TamanoAlmacen tamano);
    Optional<Almacen> crearAlmacen(Almacen almacen);
    Optional<Almacen> actualizarAlmacen(UUID id, Almacen almacen);
    boolean eliminarAlmacen(UUID id);
}
