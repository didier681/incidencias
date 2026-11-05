import 'package:flutter/material.dart';

import '../servicios/servicio_incidencias.dart';
import 'pagina_solicitudes_servicio.dart';

class DetalleIncidenciaPagina extends StatefulWidget {
  const DetalleIncidenciaPagina({
    required this.token,
    required this.incidenciaId,
    this.rol = 'USUARIO',
    super.key,
  });

  final String token;
  final String incidenciaId;
  final String rol;

  @override
  State<DetalleIncidenciaPagina> createState() =>
      _DetalleIncidenciaPaginaState();
}

class _DetalleIncidenciaPaginaState extends State<DetalleIncidenciaPagina> {
  final ServicioIncidencias _servicio = ServicioIncidencias();

  late Future<Map<String, dynamic>> _detalle;

  @override
  void initState() {
    super.initState();
    _cargarDetalle();
  }

  void _cargarDetalle() {
    _detalle = _servicio.buscarDetalle(
      token: widget.token,
      incidenciaId: widget.incidenciaId,
    );
  }

  void _actualizarDetalle() {
    setState(_cargarDetalle);
  }

  Future<void> _abrirSolicitudes() async {
    await Navigator.of(context).push(
      MaterialPageRoute(
        builder: (_) => PaginaSolicitudesServicio(
          token: widget.token,
          incidenciaId: widget.incidenciaId,
          esAdministrador: widget.rol.toUpperCase() == 'ADMINISTRADOR',
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Detalle de incidencia'),
        actions: [
          IconButton(
            tooltip: 'Actualizar',
            onPressed: _actualizarDetalle,
            icon: const Icon(Icons.refresh),
          ),
        ],
      ),
      body: FutureBuilder<Map<String, dynamic>>(
        future: _detalle,
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
                      'No se pudo cargar el detalle:\n$mensaje',
                      textAlign: TextAlign.center,
                    ),
                    const SizedBox(height: 16),
                    FilledButton.icon(
                      onPressed: _actualizarDetalle,
                      icon: const Icon(Icons.refresh),
                      label: const Text('Intentar de nuevo'),
                    ),
                  ],
                ),
              ),
            );
          }

          final detalle = snapshot.data!;
          final titulo = detalle['titulo']?.toString() ?? 'Sin título';
          final estado = detalle['estado']?.toString() ?? 'Sin estado';
          final descripcion =
              detalle['descripcion']?.toString() ?? 'Sin descripción';
          final ubicacion =
              detalle['ubicacion']?.toString() ?? 'Sin ubicación';
          final fechaReporte =
              detalle['fechaReporte']?.toString().replaceFirst('T', ' ') ??
                  'Sin fecha';

          final historialJson = detalle['historialEstados'];
          final historial = historialJson is List
              ? historialJson
                  .map((elemento) => Map<String, dynamic>.from(elemento as Map))
                  .toList()
              : <Map<String, dynamic>>[];

          return ListView(
            padding: const EdgeInsets.all(16),
            children: [
              Text(
                titulo,
                style: Theme.of(context).textTheme.headlineSmall,
              ),
              const SizedBox(height: 12),
              Card(
                child: ListTile(
                  leading: const Icon(Icons.flag_outlined),
                  title: const Text('Estado actual'),
                  subtitle: Text(estado),
                ),
              ),
              Card(
                child: ListTile(
                  leading: const Icon(Icons.location_on_outlined),
                  title: const Text('Ubicación'),
                  subtitle: Text(ubicacion),
                ),
              ),
              Card(
                child: ListTile(
                  leading: const Icon(Icons.calendar_today_outlined),
                  title: const Text('Fecha del reporte'),
                  subtitle: Text(fechaReporte),
                ),
              ),
              const SizedBox(height: 12),
              Text(
                'Descripción',
                style: Theme.of(context).textTheme.titleMedium,
              ),
              const SizedBox(height: 4),
              Text(descripcion),
              const SizedBox(height: 16),
              Card(
                child: ListTile(
                  leading: const Icon(Icons.handyman_outlined),
                  title: const Text('Solicitudes de servicio'),
                  subtitle: const Text('Consultar o solicitar un servicio'),
                  trailing: const Icon(Icons.chevron_right),
                  onTap: _abrirSolicitudes,
                ),
              ),
              const SizedBox(height: 24),
              Text(
                'Historial de estados',
                style: Theme.of(context).textTheme.titleLarge,
              ),
              const SizedBox(height: 8),
              if (historial.isEmpty)
                const Card(
                  child: Padding(
                    padding: EdgeInsets.all(16),
                    child: Text('Esta incidencia todavía no tiene cambios.'),
                  ),
                )
              else
                ...historial.map((cambio) {
                  final anterior = cambio['estadoAnterior']?.toString();
                  final nuevo =
                      cambio['estadoNuevo']?.toString() ?? 'Sin estado';
                  final fecha = cambio['fechaCambio']
                          ?.toString()
                          .replaceFirst('T', ' ') ??
                      'Sin fecha';

                  final textoCambio = anterior == null
                      ? 'Incidencia reportada: $nuevo'
                      : '$anterior → $nuevo';

                  return Card(
                    child: ListTile(
                      leading: const Icon(Icons.history),
                      title: Text(textoCambio),
                      subtitle: Text(fecha),
                    ),
                  );
                }),
            ],
          );
        },
      ),
    );
  }
}