# 📱 Tarea Final - Desarrollo de Aplicaciones Móviles

Este proyecto es una aplicación móvil desarrollada en **Android Studio** utilizando **Kotlin**, **Jetpack Compose** y **Retrofit**. Permite visualizar cartas de Digimon, gestionar autenticación de usuario y recibir notificaciones.

---

## Tecnologías Utilizadas

- **Kotlin** - Lenguaje de programación principal.
- **Jetpack Compose** - Para la interfaz de usuario.
- **Retrofit** - Para el consumo de API.
- **Firebase Authentication** - Para el inicio de sesión y registro.
- **Jetpack Navigation** - Para la navegación entre pantallas.

---

## Funcionalidades Principales

1. **Inicio de Sesión y Registro**
   - Permite crear una cuenta y autenticarse con Firebase.
   - Manejo de errores en la autenticación.

2. **Visualización de Cartas de Digimon**
   - Obtiene datos desde una API usando Retrofit.
   - Muestra las cartas en un `LazyVerticalGrid`.
   - Permite abrir un `Dialog` con detalles de cada carta.

3. **Notificaciones**
   - Usa `Handler` para enviar notificaciones cada minuto.
   - Se detienen cuando el usuario consulta la informació de una carta.

4. **Arquitectura MVVM**
   - `DigimonRepository.kt` maneja la lógica de datos.
   - `DigimonApi.kt` define los endpoints de Retrofit.
   - `MainActivity.kt` implementa la vista principal.

---

## Mejoras Futuras

1. **Opcion de guardar en favoritos y de construccion de mazo**
    - Se usara Firebase Firestore

2. **Mantener la cuenta abierta y cerrarla**

