package utez.edu.mx.u3_04_agjd.model;

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
    
    @Column(name = "clave_sede", unique = true, nullable = false) // Default 0
    private String claveSede = "C-00000000-0000-0000-0000-000000000000";
    
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



    public void actualizarClaveSede() {
        if (this.id != null) {
            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
            String digitosAleatorios = String.format("%04d", new Random().nextInt(10000));

            // Versión más simple y segura
            this.claveSede = "C-" + this.id.toString().substring(0, 8) + "-" + fecha + "-" + digitosAleatorios;
        }
    }
}
