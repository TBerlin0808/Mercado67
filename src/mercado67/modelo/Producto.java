/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mercado67.modelo;

import java.util.ArrayList;
import java.util.List;

public class Producto {
    private int idProducto;
    private String nombre;
    private double precio;
    private int cantidad;
    private String descripcion;
    private int idVendedor; 
    private List<String> rutasFotos; 

    public Producto(int idProducto, String nombre, double precio, int cantidad, String descripcion, int idVendedor) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.idVendedor = idVendedor;
        this.rutasFotos = new ArrayList<>();
    }

  
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getIdVendedor() { return idVendedor; }
    public void setIdVendedor(int idVendedor) { this.idVendedor = idVendedor; }

    public List<String> getRutasFotos() { return rutasFotos; }
    public void agregarFoto(String rutaFoto) { this.rutasFotos.add(rutaFoto); }
}
