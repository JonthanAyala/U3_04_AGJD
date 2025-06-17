package utez.edu.mx.U3_04_AGJD.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "sedes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sede {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "clave_sede", unique = true, nullable = false)
    private String claveSede;
    
    @NotBlank(message = "El estado es obligatorio")
    @Size(min = 2, max = 50, message = "El estado debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El estado solo puede contener letras y espacios")
    @Column(nullable = false)
    private String estado;
    
    @NotBlank(message = "El municipio es obligatorio")
    @Size(min = 2, max = 50, message = "El municipio debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El municipio solo puede contener letras y espacios")
    @Column(nullable = false)
    private String municipio;
    
    @OneToMany(mappedBy = "sede", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Almacen> almacenes;
    
    @PrePersist
    public void generarClaveSede() {
        if (this.claveSede == null) {
            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
            String digitosAleatorios = String.format("%04d", new Random().nextInt(10000));
            this.claveSede = String.format("C%d-%s-%s", this.id != null ? this.id : 0, fecha, digitosAleatorios);
        }
    }
    
    public void actualizarClaveSede() {
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String digitosAleatorios = String.format("%04d", new Random().nextInt(10000));
        this.claveSede = String.format("C%s-%s-%s", this.id.toString().substring(0, 8), fecha, digitosAleatorios);
    }
}
