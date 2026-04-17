package com.inventario.backend.service;

import com.inventario.backend.dto.SolicitudMovimiento;
import com.inventario.backend.model.MovimientoInventario;
import com.inventario.backend.model.Producto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovimientoInventarioService {

    private final List<MovimientoInventario> movimientos = new ArrayList<>();
    private Long contadorId = 1L;

    private final ProductoService productoService;

    public MovimientoInventarioService(ProductoService productoService) {
        this.productoService = productoService;
    }

    public MovimientoInventario registrarEntrada(SolicitudMovimiento solicitud) {
        Producto producto = productoService.aumentarStock(solicitud.getProductoId(), solicitud.getCantidad());

        if (producto == null) {
            throw new RuntimeException("No se pudo registrar la entrada");
        }

        MovimientoInventario movimiento = new MovimientoInventario(
                contadorId++,
                "ENTRADA",
                solicitud.getCantidad(),
                LocalDate.now(),
                solicitud.getObservacion(),
                solicitud.getProductoId()
        );

        movimientos.add(movimiento);
        return movimiento;
    }

    public MovimientoInventario registrarSalida(SolicitudMovimiento solicitud) {
        Producto producto = productoService.disminuirStock(solicitud.getProductoId(), solicitud.getCantidad());

        if (producto == null) {
            throw new RuntimeException("No se pudo registrar la salida");
        }

        MovimientoInventario movimiento = new MovimientoInventario(
                contadorId++,
                "SALIDA",
                solicitud.getCantidad(),
                LocalDate.now(),
                solicitud.getObservacion(),
                solicitud.getProductoId()
        );

        movimientos.add(movimiento);
        return movimiento;
    }

    public List<MovimientoInventario> listar() {
        return movimientos;
    }

    public List<MovimientoInventario> listarPorProducto(Long productoId) {
        List<MovimientoInventario> resultado = new ArrayList<>();

        for (MovimientoInventario movimiento : movimientos) {
            if (movimiento.getProductoId().equals(productoId)) {
                resultado.add(movimiento);
            }
        }

        return resultado;
    }
}