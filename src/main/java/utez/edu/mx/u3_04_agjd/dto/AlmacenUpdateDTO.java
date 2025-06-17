package utez.edu.mx.u3_04_agjd.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utez.edu.mx.u3_04_agjd.model.Almacen;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlmacenUpdateDTO {

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de venta debe ser mayor a 0")
    private BigDecimal precioVenta;

    @NotNull(message = "El precio de renta es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de renta debe ser mayor a 0")
    private BigDecimal precioRenta;

    @NotNull(message = "El tamaño es obligatorio")
    private Almacen.TamanoAlmacen tamano;
}
