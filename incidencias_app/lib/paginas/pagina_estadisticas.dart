import 'package:flutter/material.dart';

import '../servicios/servicio_estadisticas.dart';

class PaginaEstadisticas extends StatefulWidget {
  const PaginaEstadisticas({
    required this.token,
    super.key,
  });

  final String token;

  @override
  State<PaginaEstadisticas> createState() => _PaginaEstadisticasState();
}

class _PaginaEstadisticasState extends State<PaginaEstadisticas> {
  final ServicioEstadisticas _servicio = ServicioEstadisticas();

  late Future<Map<String, dynamic>> _estadisticas;

  @override
  void initState() {
    super.initState();
    _cargarEstadisticas();
  }

  void _cargarEstadisticas() {
    _estadisticas = _servicio.obtenerEstadisticas(widget.token);
  }

  void _actualizar() {
    setState(_cargarEstadisticas);
  }

  int _entero(Map<String, dynamic> datos, String clave) {
    final valor = datos[clave];
    if (valor is num) return valor.toInt();
    return int.tryParse(valor?.toString() ?? '') ?? 0;
  }

  double _decimal(Map<String, dynamic> datos, String clave) {
    final valor = datos[clave];
    if (valor is num) return valor.toDouble();
    return double.tryParse(valor?.toString() ?? '') ?? 0;
  }

  Widget _tarjeta({
    required String titulo,
    required String valor,
    required IconData icono,
    required Color color,
  }) {
    return Card(
      child: ListTile(
        leading: CircleAvatar(
          backgroundColor: color.withValues(alpha: 0.12),
          child: Icon(icono, color: color),
        ),
        title: Text(titulo),
        subtitle: Text(
          valor,
          style: Theme.of(context).textTheme.titleLarge,
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Estadísticas'),
        actions: [
          IconButton(
            tooltip: 'Actualizar',
            onPressed: _actualizar,
            icon: const Icon(Icons.refresh),
          ),
        ],
      ),
      body: FutureBuilder<Map<String, dynamic>>(
        future: _estadisticas,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(
              child: CircularProgressIndicator(),
            );
          }

          if (snapshot.hasError) {
            final mensaje = snapshot.error
                .toString()
                .replaceFirst('Exception: ', '');

            return Center(
              child: Padding(
                padding: const EdgeInsets.all(24),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Text(
                      'No se pudieron cargar las estadísticas:\n$mensaje',
                      textAlign: TextAlign.center,
                    ),
                    const SizedBox(height: 16),
                    FilledButton.icon(
                      onPressed: _actualizar,
                      icon: const Icon(Icons.refresh),
                      label: const Text('Intentar de nuevo'),
                    ),
                  ],
                ),
              ),
            );
          }

          final datos = snapshot.data!;
          final promedio = _decimal(datos, 'tiempoPromedioResolucionHoras');

          return ListView(
            padding: const EdgeInsets.all(16),
            children: [
              Text(
                'Resumen de incidencias',
                style: Theme.of(context).textTheme.headlineSmall,
              ),
              const SizedBox(height: 12),
              _tarjeta(
                titulo: 'Total de incidencias',
                valor: '${_entero(datos, 'totalIncidencias')}',
                icono: Icons.summarize_outlined,
                color: const Color(0xFF1769AA),
              ),
              _tarjeta(
                titulo: 'Reportadas',
                valor: '${_entero(datos, 'reportadas')}',
                icono: Icons.fiber_new_outlined,
                color: Colors.blue,
              ),
              _tarjeta(
                titulo: 'En revisión',
                valor: '${_entero(datos, 'enRevision')}',
                icono: Icons.rate_review_outlined,
                color: Colors.orange,
              ),
              _tarjeta(
                titulo: 'En proceso',
                valor: '${_entero(datos, 'enProceso')}',
                icono: Icons.build_outlined,
                color: Colors.deepPurple,
              ),
              _tarjeta(
                titulo: 'Resueltas',
                valor: '${_entero(datos, 'resueltas')}',
                icono: Icons.check_circle_outline,
                color: Colors.green,
              ),
              _tarjeta(
                titulo: 'Rechazadas',
                valor: '${_entero(datos, 'rechazadas')}',
                icono: Icons.cancel_outlined,
                color: Colors.red,
              ),
              _tarjeta(
                titulo: 'Promedio de resolución',
                valor: '${promedio.toStringAsFixed(1)} horas',
                icono: Icons.timer_outlined,
                color: Colors.teal,
              ),
            ],
          );
        },
      ),
    );
  }
}