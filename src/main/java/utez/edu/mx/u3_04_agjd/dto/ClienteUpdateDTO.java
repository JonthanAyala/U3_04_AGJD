package utez.edu.mx.u3_04_agjd.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteUpdateDTO {
    
    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras y espacios")
    private String nombreCompleto;
    
    @NotBlank(message = "El número de teléfono es obligatorio")
    @Pattern(regexp = "^\\d{10}$", message = "El número de teléfono debe tener exactamente 10 dígitos")
    private String numeroTelefono;
    
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    @Size(max = 100, message = "El correo no puede exceder 100 caracteres")
    private String correoElectronico;
}
