package utez.edu.mx.u3_04_agjd.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utez.edu.mx.u3_04_agjd.model.Almacen;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaDTO {
    @NotNull(message = "El cliente es obligatorio")
    private UUID cliente;
    @NotNull(message = "El usuario es obligatorio")
    private UUID almacen;
}
