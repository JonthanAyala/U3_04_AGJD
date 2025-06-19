package utez.edu.mx.u3_04_agjd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "almacenes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "clave_almacen", unique = true, nullable = false)
    private String claveAlmacen = "A-00000000";

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de venta debe ser mayor a 0")
    @Column(name = "precio_venta", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioVenta;

    @NotNull(message = "El precio de renta es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de renta debe ser mayor a 0")
    @Column(name = "precio_renta", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioRenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TamanoAlmacen tamano;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sede_id", nullable = false)
    @JsonIgnore
    private Sede sede;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    @JsonIgnore
    private Cliente cliente;

    @JsonProperty("sedeId")
    public UUID getSedeId() {
        return this.sede != null ? this.sede.getId() : null;
    }

    @PrePersist
    public void inicializar() {
        if (this.fechaRegistro == null) {
            this.fechaRegistro = LocalDate.now();
        }
    }

    public void actualizarClaveAlmacen() {
        if (this.sede != null && this.sede.getClaveSede() != null) {
            this.claveAlmacen = String.format("%s-A%s", this.sede.getClaveSede(), this.id.toString().substring(0, 8));
        }
    }

    public enum TamanoAlmacen {
        G, M, P
    }
}
