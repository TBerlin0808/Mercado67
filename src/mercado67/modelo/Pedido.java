/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mercado67.modelo;

import java.time.LocalDate;
import java.util.List;

public class Pedido {

    private int idPedido;
    private int idComprador;
    private LocalDate fecha;
    private String estado;
    private double total;
    private List<ItemCarrito> itemsComprados;

    public Pedido(int idPedido, int idComprador, LocalDate fecha, String estado, List<ItemCarrito> itemsComprados) {
        this.idPedido = idPedido;
        this.idComprador = idComprador;
        this.fecha = fecha;
        this.estado = estado;
        this.itemsComprados = itemsComprados;
        this.total = calcularTotal();
    }

    private double calcularTotal() {
        double suma = 0;
        for (ItemCarrito item : itemsComprados) {
            suma += item.calcularSubtotal();
        }
        return suma;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdComprador() {
        return idComprador;
    }

    public void setIdComprador(int idComprador) {
        this.idComprador = idComprador;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public List<ItemCarrito> getItemsComprados() {
        return itemsComprados;
    }

    public void setItemsComprados(List<ItemCarrito> itemsComprados) {
        this.itemsComprados = itemsComprados;
        this.total = calcularTotal();
    }
}
