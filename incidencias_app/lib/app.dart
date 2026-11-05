import 'package:flutter/material.dart';

import 'paginas/inicio_sesion_pagina.dart';

class IncidenciasApp extends StatelessWidget {
  const IncidenciasApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Incidencias',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF1769AA),
        ),
        useMaterial3: true,
      ),
      home: const InicioSesionPagina(),
    );
  }
}