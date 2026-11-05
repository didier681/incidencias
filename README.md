# Incidencias

## Requisitos
- JDK 17 o superior
- PostgreSQL
- Flutter SDK

## Base de datos
1. Crear la base de datos en PostgreSQL:
   CREATE DATABASE incidencias_db;
2. Editar incidencias-api/src/main/resources/application.properties
   y poner tu usuario y contraseña de PostgreSQL.

## Backend
cd incidencias-api
mvnw.cmd spring-boot:run

El backend queda en http://localhost:8080

## Frontend
Cambiar _urlBase (en incidencias_app/lib/) según cómo se pruebe:
- Emulador Android: http://10.0.2.2:8080
- Chrome/Windows: http://localhost:8080
- Celular físico: http://IP_DE_TU_PC:8080

cd incidencias_app
flutter pub get
flutter run