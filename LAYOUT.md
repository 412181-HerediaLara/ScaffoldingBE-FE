# Layout.md

> **Especificación de Layout y UX**
>
> Este documento define la estructura visual completa de la aplicación.
>
> **NO contiene código.**
>
> Su objetivo es que cualquier desarrollador o IA pueda reconstruir exactamente la interfaz sin necesidad de ver un diseño en Figma.
>
> Si existen imágenes de referencia, este documento debe describirlas completamente.

---

# Instrucciones para la IA

Antes de completar este documento debes:

1. Leer completamente la consigna.
2. Leer `design.md`.
3. Leer `sdd.md`.
4. Leer `architecture.md`.
5. Analizar todas las pantallas descritas en la consigna.
6. Si existen imágenes de referencia, describirlas en texto.
7. No generar código.
8. No inventar pantallas que no sean necesarias.
9. Mantener consistencia entre todas las pantallas.
10. Justificar cualquier decisión que no esté explícita.

---

# 1. Objetivo del Layout

Describir la experiencia general de la aplicación.

Completar:

- Objetivo principal
- Tipo de aplicación
- Público objetivo
- Prioridad visual
- Información más importante para el usuario

---

# 2. Distribución General

Seleccionar una.

- [ ] Dashboard
- [ ] Sidebar + Contenido
- [ ] Pantalla completa
- [ ] Juego
- [ ] Wizard
- [ ] Formularios
- [ ] Otro

Explicar.

---

# 3. Grid Principal

Describir la estructura.

Ejemplo.

```
┌───────────────────────────────────────────────┐
│ Header                                        │
├──────────────┬───────────────────────┬────────┤
│ Sidebar      │ Contenido             │ Panel  │
│              │                       │        │
│              │                       │        │
├──────────────┴───────────────────────┴────────┤
│ Footer                                        │
└───────────────────────────────────────────────┘
```

Describir:

- Header
- Sidebar
- Contenido
- Paneles
- Footer

---

# 4. Responsive

Definir comportamiento.

Desktop

Tablet

Mobile

Para cada uno indicar:

- Componentes que desaparecen
- Componentes que cambian de tamaño
- Paneles colapsables
- Scroll
- Prioridad visual

---

# 5. Navegación

Describir.

Ejemplo.

```
Inicio

↓

Menú

↓

Pantalla Principal

↓

Detalle

↓

Configuración
```

Indicar:

- Navegación principal
- Navegación secundaria
- Accesos rápidos

---

# 6. Pantallas

La IA debe listar todas las pantallas.

---

# Plantilla de Pantalla

## Nombre

Objetivo.

### Wireframe

Representar utilizando ASCII.

Ejemplo.

```
┌─────────────────────────────┐
│ Header                      │
├────────────┬────────────────┤
│ Sidebar    │                │
│            │ Contenido      │
│            │                │
├────────────┴────────────────┤
│ Footer                      │
└─────────────────────────────┘
```

---

### Componentes

Listar todos.

Ejemplo.

- Header
- Sidebar
- Board
- Toolbar
- Card
- Footer

---

### Distribución

Describir exactamente.

Ejemplo.

Header ocupa todo el ancho.

Sidebar fija.

Board ocupa el espacio restante.

Footer siempre visible.

---

### Acciones

Listar todas.

Ejemplo.

- Crear
- Editar
- Eliminar
- Buscar
- Tirar dado
- Seleccionar ficha

---

### Estados

Ejemplo.

- Vacío
- Cargando
- Error
- Información
- Confirmación

---

### Responsive

Describir cómo cambia.

---

# 7. Componentes Visuales

Para cada componente completar.

---

## Nombre

Responsabilidad.

Ubicación.

Tamaño.

Contenido.

Estados.

Interacciones.

Responsive.

---

# 8. Formularios

Si existen.

Para cada formulario indicar.

Campos.

Validaciones.

Botones.

Mensajes.

Errores.

---

# 9. Tablas

Si existen.

Columnas.

Orden.

Filtros.

Paginación.

Acciones.

Estados.

---

# 10. Tablero (si aplica)

Completar.

Tipo de tablero.

Distribución.

Cantidad de casillas.

Orientación.

Escalado.

Zoom.

Scroll.

Tamaño.

Animaciones.

Estados.

---

# 11. Cartas (si aplica)

Tamaño.

Ubicación.

Animaciones.

Estados.

Acciones.

---

# 12. Dados (si aplica)

Ubicación.

Cantidad.

Animación.

Estados.

Botón lanzar.

Resultado.

---

# 13. Fichas (si aplica)

Cantidad.

Colores.

Selección.

Movimiento.

Animaciones.

Estados.

---

# 14. Paneles Laterales

Describir.

Contenido.

Tamaño.

Colapsable.

Scroll.

---

# 15. Barra Superior

Contenido.

Logo.

Título.

Botones.

Información.

Estados.

---

# 16. Footer

Contenido.

Acciones.

Información.

---

# 17. Diálogos

Listar.

Confirmación.

Información.

Advertencia.

Error.

Éxito.

---

# 18. Notificaciones

Toast.

Snackbar.

Alertas.

Mensajes.

---

# 19. Loading

Dónde aparece.

Spinner.

Skeleton.

Overlay.

---

# 20. Estados Vacíos

Definir.

Sin datos.

Sin jugadores.

Sin resultados.

Sin elementos.

---

# 21. Estados de Error

Errores de red.

Errores del servidor.

Errores de validación.

Errores inesperados.

---

# 22. Animaciones

Describir.

Entrada.

Salida.

Hover.

Click.

Carga.

Movimiento.

Duración.

---

# 23. Accesibilidad

Focus.

ARIA.

Teclado.

Contraste.

Lectores de pantalla.

---

# 24. Consistencia

Todos los botones iguales.

Todas las tarjetas iguales.

Todos los paneles iguales.

Espaciado consistente.

Alineación consistente.

---

# 25. Prioridad Visual

Indicar qué debe captar primero la atención del usuario.

Orden.

1.

2.

3.

4.

---

# 26. Reglas de UX

La IA debe respetar.

- No ocultar acciones importantes.
- Máximo dos clics para acciones frecuentes.
- Mantener contexto del usuario.
- Evitar cambios bruscos de layout.
- Mostrar feedback inmediato.
- Evitar scroll innecesario.
- Mantener alineación consistente.

---

# 27. Observaciones

La IA debe documentar cualquier decisión tomada.

---

# Checklist

Antes de implementar verificar.

- [ ] Todas las pantallas definidas.
- [ ] Todos los componentes identificados.
- [ ] Navegación definida.
- [ ] Responsive definido.
- [ ] Estados definidos.
- [ ] Formularios definidos.
- [ ] Tablas definidas.
- [ ] Tablero definido (si aplica).
- [ ] Paneles definidos.
- [ ] UX consistente.
- [ ] Layout consistente.
- [ ] No existen contradicciones con architecture.md.
- [ ] No existen contradicciones con design.md.
- [ ] No existen contradicciones con sdd.md.
