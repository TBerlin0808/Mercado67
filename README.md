Mercado 67

Aplicacion de escritorio hecha en JavaFX para comprar y vender productos. Proyecto de Programacion 2, Universidad de Cordoba.

Integrantes

- Daniel Tomás Ardila Durango (TBerlin0808)
- Miguel Antonio Montes Argumedo
- Jesus Durango Padilla

Diseno en Figma

https://www.figma.com/design/u7d8pLwMAVfIWfhBZUGYSm/Interfaz-proyecto-final?node-id=0-1&t=mNpULiOKQwLCqYQG-1

Estado del avance

- Login con validaciones y rol de administrador.
- Registro de usuarios.
- Catalogo de productos con pantalla Explorar productos.
- Estructura de datos propia: Nodo y ListaEnlazada.

Requisitos

- JDK 26
- JavaFX SDK 27.0.1 ubicado en C:\javafx-sdk
- NetBeans con la libreria llamada exactamente JavaFX (Tools > Libraries) agregada en Properties > Libraries > Compile

Como ejecutar

1. Clonar el repositorio y abrir la carpeta Mercado67 en NetBeans.
2. En Properties > Build > Compiling, desmarcar Compile On Save.
3. Ejecutar Clean and Build y luego Run (F6).
4. Clase principal: vista.MainVista

Usuarios de prueba

- Administrador: admin@mercado67.com / admin123
- Cliente: cliente@mercado67.com / 1234

Estructura

- src/mercado67.modelo: clases del modelo (Usuario, Producto, ListaEnlazada, servicios)
- src/Controladores: controladores de las pantallas
- src/vista: pantallas FXML y MainVista
