# Analysis.md

> **Análisis Funcional del Proyecto**
>
> Este documento es el primer paso del desarrollo.
>
> Su objetivo es transformar una consigna escrita (generalmente un relato o caso de uso)
> en una especificación funcional clara que servirá de base para la arquitectura y la implementación.
>
> **No contiene código.**
>
> Toda la información aquí documentada debe provenir de la consigna.
> Si algo no está explícito, la IA puede inferirlo únicamente cuando sea necesario y deberá indicarlo como una suposición.

---

# Instrucciones para la IA

Antes de completar este documento:

1. Leer completamente la consigna.
2. No escribir código.
3. No diseñar componentes todavía.
4. No pensar en Angular ni Spring Boot.
5. Analizar únicamente el dominio del problema.
6. Identificar reglas explícitas e implícitas.
7. Si realizas una inferencia, justificarla.

---

# 1. Resumen de la Consigna

Explicar en pocas líneas qué problema resuelve la aplicación.

---

# 2. Objetivo del Sistema

¿Cuál es el objetivo principal?

¿Qué debe poder hacer el usuario?

---

# 3. Dominio del Problema

Describir el contexto.

Ejemplos:

- Juego
- Gestión
- Biblioteca
- Clínica
- Restaurante
- Banco
- E-commerce

Explicar cómo funciona el dominio.

---

# 4. Actores

Identificar todos los actores.

Completar la tabla.

| Actor | Descripción | Acciones |
|--------|-------------|----------|

---

# 5. Entidades del Dominio

Listar todas las entidades mencionadas o implícitas.

Ejemplo.

| Entidad | Descripción |
|----------|-------------|

Para cada entidad indicar.

## Nombre

Descripción

Responsabilidad

Relaciones

Observaciones

---

# 6. Reglas de Negocio

Extraer todas las reglas.

Separarlas entre.

## Reglas explícitas

(Reglas escritas literalmente.)

-

-

-

## Reglas implícitas

(Reglas deducidas.)

-

-

-

---

# 7. Casos de Uso

Listar todos.

Ejemplo.

| Caso de Uso | Actor |
|-------------|-------|

Para cada uno indicar.

## Objetivo

Precondiciones

Flujo principal

Flujos alternativos

Resultado esperado

---

# 8. Flujo General del Usuario

Representar el recorrido.

Ejemplo.

```
Inicio

↓

Selecciona jugador

↓

Comienza partida

↓

Realiza acciones

↓

Finaliza partida
```

---

# 9. Objetos que Manipula el Usuario

Listar.

Ejemplo.

- Jugador
- Carta
- Pedido
- Producto
- Vehículo

---

# 10. Acciones Disponibles

Listar todas las acciones.

Ejemplo.

- Crear
- Editar
- Eliminar
- Buscar
- Tirar dado
- Mover ficha
- Comprar
- Vender

---

# 11. Estados del Sistema

Listar los estados importantes.

Ejemplo.

- Esperando jugador
- Jugando
- Pausado
- Finalizado
- Error

---

# 12. Eventos Importantes

Identificar eventos.

Ejemplo.

- Inicio de partida
- Fin del turno
- Tirada de dado
- Compra realizada
- Usuario eliminado

---

# 13. Reglas Temporales

Si existen.

Ejemplo.

- Un turno dura...
- No puede realizarse una acción antes de...
- El jugador debe...

---

# 14. Restricciones

Listar todas.

Ejemplo.

- Máximo de jugadores.
- Cantidad mínima.
- Límites.
- Validaciones.

---

# 15. Validaciones

¿Qué debe validarse?

Ejemplo.

Campos obligatorios.

Valores mínimos.

Valores máximos.

Estados válidos.

---

# 16. Datos que Maneja el Sistema

Identificar toda la información.

Ejemplo.

Jugador

- nombre
- puntaje
- posición

Carta

- tipo
- ataque
- energía

---

# 17. Relaciones entre Entidades

Describir.

Ejemplo.

Jugador

↓

tiene muchas

↓

Cartas

---

# 18. Acciones Automáticas

Identificar procesos automáticos.

Ejemplo.

- Calcular puntaje.
- Avanzar turno.
- Actualizar tablero.
- Validar ganador.

---

# 19. Casos Especiales

Listar.

Ejemplo.

- Empates.
- Sin jugadores.
- Sin stock.
- Tablero completo.

---

# 20. Posibles Errores

Identificar errores funcionales.

Ejemplo.

- Movimiento inválido.
- Acción fuera de turno.
- Datos incompletos.

---

# 21. Información que Debe Mostrar la Interfaz

No diseñar todavía.

Solo listar.

Ejemplo.

- Nombre jugador
- Puntaje
- Turno
- Tiempo restante
- Estado

---

# 22. Información que Debe Solicitar la Interfaz

Listar.

Ejemplo.

- Nombre
- Cantidad
- Confirmaciones
- Selecciones

---

# 23. Funcionalidades Principales

Ordenarlas por prioridad.

| Prioridad | Funcionalidad |
|-----------|---------------|

---

# 24. Funcionalidades Secundarias

Listarlas.

---

# 25. Funcionalidades Opcionales

Si existen.

---

# 26. Ambigüedades Detectadas

Toda consigna suele tener zonas grises.

Listarlas.

Para cada una indicar.

Ambigüedad.

Suposición realizada.

Justificación.

---

# 27. Suposiciones

Documentar cualquier decisión tomada porque la consigna no lo especifica.

---

# 28. Glosario

Definir términos propios del dominio.

Ejemplo.

Turno

Partida

Inventario

Escudería

Circuito

Paciente

Reserva

---

# 29. Resumen Ejecutivo

Realizar un resumen del análisis.

Debe responder:

- ¿Qué hace el sistema?
- ¿Quién lo usa?
- ¿Qué objetos existen?
- ¿Qué reglas importantes hay?
- ¿Qué funcionalidades tendrá?

---

# 30. Preparación para la Arquitectura

La IA debe responder.

¿Cuáles serán probablemente las entidades del proyecto?

¿Cuáles serán probablemente los modelos?

¿Cuáles serán probablemente los servicios?

¿Cuáles serán probablemente las pantallas?

¿Cuáles serán probablemente los componentes?

**No diseñarlos todavía.**

Solo listarlos.

---

# Checklist Final

Antes de continuar verificar.

- [ ] Toda la consigna fue analizada.
- [ ] Todos los actores fueron identificados.
- [ ] Todas las entidades fueron identificadas.
- [ ] Todas las reglas fueron documentadas.
- [ ] Todos los casos de uso fueron identificados.
- [ ] Todas las restricciones fueron documentadas.
- [ ] Todas las validaciones fueron documentadas.
- [ ] Todas las ambigüedades fueron registradas.
- [ ] Todas las suposiciones fueron justificadas.
- [ ] El dominio del problema quedó completamente comprendido.

---

# Criterios de Calidad

El documento debe cumplir:

- No contener código.
- No contener decisiones de Angular.
- No contener decisiones de Spring Boot.
- No hablar de componentes.
- No hablar de servicios.
- No hablar de arquitectura.
- No hablar de diseño visual.
- Centrarse únicamente en comprender el problema de negocio.

Solo cuando este documento esté completo podrá comenzar la etapa de arquitectura.
