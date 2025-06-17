package utez.edu.mx.U3_04_AGJD.service;

import utez.edu.mx.U3_04_AGJD.model.Sede;
import utez.edu.mx.U3_04_AGJD.repository.SedeRepository;
import utez.edu.mx.U3_04_AGJD.service.interfaces.ISedeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// PRINCIPIO SOLID: Single Responsibility Principle (SRP) - Solo maneja lógica de Sedes
// PRINCIPIO SOLID: Dependency Inversion Principle (DIP) - Implementa interfaz
@Service
@RequiredArgsConstructor
@Transactional
public class SedeService implements ISedeService {
    
    private final SedeRepository sedeRepository;
    
    @Override
    public List<Sede> obtenerTodasLasSedes() {
        return sedeRepository.findAll();
    }
    
    @Override
    public Optional<Sede> obtenerSedePorId(UUID id) {
        return sedeRepository.findById(id);
    }
    
    @Override
    public Optional<Sede> obtenerSedePorClave(String claveSede) {
        return sedeRepository.findByClaveSede(claveSede);
    }
    
    @Override
    public List<Sede> obtenerSedesPorEstado(String estado) {
        return sedeRepository.findByEstado(estado);
    }
    
    @Override
    public List<Sede> obtenerSedesPorMunicipio(String municipio) {
        return sedeRepository.findByMunicipio(municipio);
    }
    
    @Override
    public List<Sede> obtenerSedesPorEstadoYMunicipio(String estado, String municipio) {
        return sedeRepository.findByEstadoAndMunicipio(estado, municipio);
    }
    
    @Override
    public Sede crearSede(Sede sede) {
        Sede sedeGuardada = sedeRepository.save(sede);
        sedeGuardada.actualizarClaveSede();
        return sedeRepository.save(sedeGuardada);
    }
    
    @Override
    public Optional<Sede> actualizarSede(UUID id, Sede sedeActualizada) {
        return sedeRepository.findById(id)
                .map(sede -> {
                    sede.setEstado(sedeActualizada.getEstado());
                    sede.setMunicipio(sedeActualizada.getMunicipio());
                    return sedeRepository.save(sede);
                });
    }
    
    @Override
    public boolean eliminarSede(UUID id) {
        if (sedeRepository.existsById(id)) {
            sedeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
