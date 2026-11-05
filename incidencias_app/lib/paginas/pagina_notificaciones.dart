import 'package:flutter/material.dart';

import '../servicios/servicio_notificaciones.dart';
import 'detalle_incidencia_pagina.dart';

class PaginaNotificaciones extends StatefulWidget {
  const PaginaNotificaciones({
    required this.token,
    super.key,
  });

  final String token;

  @override
  State<PaginaNotificaciones> createState() => _PaginaNotificacionesState();
}

class _PaginaNotificacionesState extends State<PaginaNotificaciones> {
  final ServicioNotificaciones _servicio = ServicioNotificaciones();

  late Future<List<Map<String, dynamic>>> _notificaciones;

  @override
  void initState() {
    super.initState();
    _cargarNotificaciones();
  }

  void _cargarNotificaciones() {
    _notificaciones = _servicio.listarPropias(widget.token);
  }

  void _actualizarLista() {
    setState(_cargarNotificaciones);
  }

  Future<void> _abrirNotificacion(
    Map<String, dynamic> notificacion,
  ) async {
    final notificacionId = notificacion['id']?.toString();
    final incidenciaId = notificacion['incidenciaId']?.toString();
    final leida = notificacion['leida'] == true;

    try {
      if (!leida && notificacionId != null && notificacionId.isNotEmpty) {
        await _servicio.marcarComoLeida(
          token: widget.token,
          notificacionId: notificacionId,
        );

        if (mounted) {
          _actualizarLista();
        }
      }

      if (!mounted) return;

      if (incidenciaId == null || incidenciaId.isEmpty) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text(
              'Esta notificación no tiene una incidencia asociada.',
            ),
          ),
        );
        return;
      }

      await Navigator.of(context).push(
        MaterialPageRoute(
          builder: (_) => DetalleIncidenciaPagina(
            token: widget.token,
            incidenciaId: incidenciaId,
          ),
        ),
      );
    } catch (error) {
      if (!mounted) return;

      final mensaje = error.toString().replaceFirst('Exception: ', '');
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(mensaje)),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Notificaciones'),
        actions: [
          IconButton(
            tooltip: 'Actualizar',
            onPressed: _actualizarLista,
            icon: const Icon(Icons.refresh),
          ),
        ],
      ),
      body: FutureBuilder<List<Map<String, dynamic>>>(
        future: _notificaciones,
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
                      'No se pudieron cargar las notificaciones:\n$mensaje',
                      textAlign: TextAlign.center,
                    ),
                    const SizedBox(height: 16),
                    FilledButton.icon(
                      onPressed: _actualizarLista,
                      icon: const Icon(Icons.refresh),
                      label: const Text('Intentar de nuevo'),
                    ),
                  ],
                ),
              ),
            );
          }

          final notificaciones = snapshot.data ?? [];

          if (notificaciones.isEmpty) {
            return const Center(
              child: Padding(
                padding: EdgeInsets.all(24),
                child: Text('No tienes notificaciones.'),
              ),
            );
          }

          return ListView.builder(
            padding: const EdgeInsets.all(12),
            itemCount: notificaciones.length,
            itemBuilder: (context, indice) {
              final notificacion = notificaciones[indice];
              final mensaje =
                  notificacion['mensaje']?.toString() ?? 'Notificación';
              final fecha = notificacion['fechaCreacion']
                      ?.toString()
                      .replaceFirst('T', ' ') ??
                  '';
              final leida = notificacion['leida'] == true;

              return Card(
                child: ListTile(
                  onTap: () => _abrirNotificacion(notificacion),
                  leading: Icon(
                    leida
                        ? Icons.notifications_none
                        : Icons.notifications_active,
                    color: leida ? Colors.grey : const Color(0xFF1769AA),
                  ),
                  title: Text(
                    mensaje,
                    style: TextStyle(
                      fontWeight: leida ? FontWeight.normal : FontWeight.bold,
                    ),
                  ),
                  subtitle: Text(fecha),
                  trailing: leida
                      ? const Icon(Icons.check, color: Colors.green)
                      : const Icon(
                          Icons.circle,
                          size: 12,
                          color: Colors.blue,
                        ),
                ),
              );
            },
          );
        },
      ),
    );
  }
}