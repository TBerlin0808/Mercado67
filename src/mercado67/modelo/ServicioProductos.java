/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mercado67.modelo;

import java.util.ArrayList;
import java.util.List;

public class ServicioProductos {
        private final List<Producto> productos = new ArrayList<>();
     public ServicioProductos() {
        productos.add(new Producto(1, "Audifonos", 85000, 10, "Audifonos inalambricos", 1));
        productos.add(new Producto(2, "Mouse gamer", 60000, 15, "Mouse con luces RGB", 1));
        productos.add(new Producto(3, "Teclado mecanico", 120000, 8, "Teclado con switches rojos", 1));
        productos.add(new Producto(4, "Cuaderno", 8000, 50, "Cuaderno de 100 hojas", 1));
        productos.add(new Producto(5, "Mochila", 95000, 12, "Mochila resistente al agua", 1));
        productos.add(new Producto(6, "Botella termica", 35000, 20, "Botella de acero 750 ml", 1));
    }
         public List<Producto> listarProductos() {
        return productos;
    }
}
