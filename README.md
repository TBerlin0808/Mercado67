Mercado 67
Aplicación de escritorio desarrollada en JavaFX para comprar y vender productos. Proyecto de Programación 2, Universidad de Córdoba.

finalizado.

Integrantes
-Daniel Tomás Ardila Durango (TBerlin0808)

-Miguel Antonio Montes Argumedo

-Jesús Durango Padilla

Diseño en Figma

https://www.figma.com/design/u7d8pLwMAVfIWfhBZUGYSm/Interfaz-proyecto-final?node-id=0-1&t=mNpULiOKQwLCqYQG-1

Funcionalidades
-Inicio de sesión y registro de usuarios con roles (cliente y administrador).

-Catálogo "Explorar productos" con búsqueda, paginación y fotos.

-Detalle de producto con foto, descripción, vendedor, cantidad y navegación.

-Publicación de productos con hasta 3 fotos.

-Pantalla principal con productos destacados y fotos.

-Carrito de compras con cantidades y total.

-Registro de pedidos.

-Lista de deseos por usuario.

-Módulo "Mi cuenta": Mis datos, lista de deseos, historial de compras y cierre de sesión.

-Persistencia de datos en archivos JSON.

-Interfaz adaptable al tamaño de la ventana.

-Estructura de datos propia: Nodo y ListaEnlazada.

Persistencia
-data/usuarios.json

-data/productos.json

-data/pedidos.json

-data/deseos.json

-data/fotos/

Requisitos
-JDK 26

-JavaFX SDK 27.0.1 en C:\javafx-sdk

-NetBeans

Cómo ejecutar
1. Clonar el repositorio y abrir la carpeta Mercado67 en NetBeans.

2. Desmarcar Compile On Save en Properties > Build > Compiling.

3. Ejecutar Clean and Build.

4. Ejecutar Run (F6).

5. Clase principal: vista.MainVista.

Usuarios de prueba
-Administrador: admin@mercado67.com / admin123

-Cliente: cliente@mercado67.com / 1234

Estructura
-src/mercado67.modelo: modelo, servicios y estructura de datos.

-src/Controladores: controladores de las pantallas.

-src/vista: archivos FXML, MainVista y Navegacion.

-src/imagenes: recursos gráficos.

-data/: datos generados en ejecución.
