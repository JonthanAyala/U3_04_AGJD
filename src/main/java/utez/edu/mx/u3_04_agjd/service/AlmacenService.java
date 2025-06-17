package utez.edu.mx.u3_04_agjd.service;

import utez.edu.mx.u3_04_agjd.model.Almacen;
import utez.edu.mx.u3_04_agjd.repository.AlmacenRepository;
import utez.edu.mx.u3_04_agjd.repository.SedeRepository;
import utez.edu.mx.u3_04_agjd.service.interfaces.IAlmacenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// PRINCIPIO SOLID: Single Responsibility Principle (SRP) - Solo maneja lógica de Almacenes
// PRINCIPIO SOLID: Dependency Inversion Principle (DIP) - Implementa interfaz
@Service
@RequiredArgsConstructor
@Transactional
public class AlmacenService implements IAlmacenService {
    
    private final AlmacenRepository almacenRepository;
    private final SedeRepository sedeRepository;
    
    @Override
    public List<Almacen> obtenerTodosLosAlmacenes() {
        return almacenRepository.findAll();
    }
    
    @Override
    public Optional<Almacen> obtenerAlmacenPorId(UUID id) {
        return almacenRepository.findById(id);
    }
    
    @Override
    public Optional<Almacen> obtenerAlmacenPorClave(String claveAlmacen) {
        return almacenRepository.findByClaveAlmacen(claveAlmacen);
    }
    
    @Override
    public List<Almacen> obtenerAlmacenesPorSede(UUID sedeId) {
        return almacenRepository.findBySede_Id(sedeId);
    }
    
    @Override
    public List<Almacen> obtenerAlmacenesPorTamano(Almacen.TamanoAlmacen tamano) {
        return almacenRepository.findByTamano(tamano);
    }
    
    @Override
    public Optional<Almacen> crearAlmacen(Almacen almacen) {
        return sedeRepository.findById(almacen.getSede().getId())
                .map(sede -> {
                    almacen.setSede(sede);
                    Almacen almacenGuardado = almacenRepository.save(almacen);
                    almacenGuardado.actualizarClaveAlmacen();
                    return almacenRepository.save(almacenGuardado);
                });
    }
    
    @Override
    public Optional<Almacen> actualizarAlmacen(UUID id, Almacen almacenActualizado) {
        return almacenRepository.findById(id)
                .map(almacen -> {
                    almacen.setPrecioVenta(almacenActualizado.getPrecioVenta());
                    almacen.setPrecioRenta(almacenActualizado.getPrecioRenta());
                    almacen.setTamano(almacenActualizado.getTamano());
                    return almacenRepository.save(almacen);
                });
    }
    
    @Override
    public boolean eliminarAlmacen(UUID id) {
        if (almacenRepository.existsById(id)) {
            almacenRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
