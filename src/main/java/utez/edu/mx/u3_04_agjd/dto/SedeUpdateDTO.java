package utez.edu.mx.u3_04_agjd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SedeUpdateDTO {
    
    @NotBlank(message = "El estado es obligatorio")
    @Size(min = 2, max = 50, message = "El estado debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El estado solo puede contener letras y espacios")
    private String estado;
    
    @NotBlank(message = "El municipio es obligatorio")
    @Size(min = 2, max = 50, message = "El municipio debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El municipio solo puede contener letras y espacios")
    private String municipio;
}
