# ComicManager (ComicApp)

Primer proyecto personal y autodidacta.

Aplicación de escritorio desarrollada en **Java + JavaFX** para la gestión de una colección de cómics.  
Permite **agregar, editar y eliminar** distintos tipos de publicaciones, persistiendo los cambios de forma local.

---

## Objetivo del proyecto

- Practicar **Programación Orientada a Objetos**
- Diseñar una arquitectura clara y escalable
- Implementar **persistencia local** sin frameworks externos
- Aprender mediante **prueba y error**

---

## Arquitectura

Patrón **MVC (Model–View–Controller)** con separación de responsabilidades:

### Model
- `Comic` (clase base)
- `Libro`
- `Evento`
- `TomoRecopilatorio`

### Controller
- Manejo de eventos de la UI
- Navegación principal de la aplicación

### Service
- Lógica de negocio
- Validaciones centralizadas

### Repository
- Acceso a datos
- Persistencia local

---

## Persistencia

- Persistencia **local mediante archivos JSON**
- Serialización y deserialización **manual**
- El archivo JSON actúa como **fuente única de verdad**
- Los datos se:
  - cargan al iniciar la aplicación
  - guardan automáticamente ante cualquier cambio

---

## Conceptos aplicados

- Programación Orientada a Objetos (POO)
- Implementación de interfaces
- Validaciones centralizadas
- Manejo de errores y feedback al usuario (IREP)
- Uso de `ArrayList` como colección base
- Separación de capas
- Aprendizaje iterativo (prueba y error)
- Primer acercamiento a UX/UI

---

## Conceptos aprendidos

- Diseño de lógica de negocio
- Persistencia de datos
- Delegación de responsabilidades
- Importancia del feedback visual
- Impacto del UX/UI en aplicaciones de escritorio

---

## Estado del proyecto

- Funcional
- Persistente
- Estable
- Versión: **v1.1**
- Base conceptual para proyectos posteriores
