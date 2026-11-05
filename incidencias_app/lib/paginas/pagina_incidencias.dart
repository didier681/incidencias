import 'package:flutter/material.dart';

import '../servicios/servicio_incidencias.dart';
import 'crear_incidencia_pagina.dart';
import 'detalle_incidencia_pagina.dart';
import 'pagina_administracion_categorias.dart';
import 'pagina_administracion_incidencias.dart';
import 'pagina_administracion_usuarios.dart';
import 'pagina_estadisticas.dart';
import 'pagina_notificaciones.dart';

class PaginaIncidencias extends StatefulWidget {
  const PaginaIncidencias({
    required this.nombre,
    required this.token,
    required this.rol,
    required this.usuarioId,
    super.key,
  });

  final String nombre;
  final String token;
  final String rol;
  final String usuarioId;

  @override
  State<PaginaIncidencias> createState() => _PaginaIncidenciasState();
}

class _PaginaIncidenciasState extends State<PaginaIncidencias> {
  final ServicioIncidencias _servicio = ServicioIncidencias();

  late Future<List<Map<String, dynamic>>> _incidencias;

  @override
  void initState() {
    super.initState();
    _cargarIncidencias();
  }

  void _cargarIncidencias() {
    _incidencias = _servicio.listarPropias(widget.token);
  }

  void _actualizarLista() {
    setState(_cargarIncidencias);
  }

  Future<void> _abrirFormulario() async {
    final creada = await Navigator.of(context).push<bool>(
      MaterialPageRoute(
        builder: (_) => CrearIncidenciaPagina(token: widget.token),
      ),
    );

    if (!mounted) return;

    if (creada == true) {
      _actualizarLista();

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Incidencia creada correctamente.'),
        ),
      );
    }
  }

  Future<void> _abrirDetalle(String incidenciaId) async {
    await Navigator.of(context).push(
      MaterialPageRoute(
        builder: (_) => DetalleIncidenciaPagina(
          token: widget.token,
          incidenciaId: incidenciaId,
          rol: widget.rol,
        ),
      ),
    );
  }

  Future<void> _abrirNotificaciones() async {
    await Navigator.of(context).push(
      MaterialPageRoute(
        builder: (_) => PaginaNotificaciones(token: widget.token),
      ),
    );
  }

  Future<void> _abrirAdministracionIncidencias() async {
    await Navigator.of(context).push(
      MaterialPageRoute(
        builder: (_) => PaginaAdministracionIncidencias(
          token: widget.token,
        ),
      ),
    );
  }

  Future<void> _abrirAdministracionCategorias() async {
    await Navigator.of(context).push(
      MaterialPageRoute(
        builder: (_) => PaginaAdministracionCategorias(
          token: widget.token,
        ),
      ),
    );
  }

  Future<void> _abrirAdministracionUsuarios() async {
    await Navigator.of(context).push(
      MaterialPageRoute(
        builder: (_) => PaginaAdministracionUsuarios(
          token: widget.token,
          usuarioActualId: widget.usuarioId,
        ),
      ),
    );
  }

  Future<void> _abrirEstadisticas() async {
    await Navigator.of(context).push(
      MaterialPageRoute(
        builder: (_) => PaginaEstadisticas(token: widget.token),
      ),
    );
  }

  void _seleccionarOpcionAdministracion(String opcion) {
    switch (opcion) {
      case 'incidencias':
        _abrirAdministracionIncidencias();
        break;
      case 'categorias':
        _abrirAdministracionCategorias();
        break;
      case 'usuarios':
        _abrirAdministracionUsuarios();
        break;
      case 'estadisticas':
        _abrirEstadisticas();
        break;
    }
  }

  @override
  Widget build(BuildContext context) {
    final esAdministrador = widget.rol.toUpperCase() == 'ADMINISTRADOR';

    return Scaffold(
      appBar: AppBar(
        title: const Text('Mis incidencias'),
        actions: [
          if (esAdministrador)
            PopupMenuButton<String>(
              tooltip: 'Administración',
              icon: const Icon(Icons.admin_panel_settings_outlined),
              onSelected: _seleccionarOpcionAdministracion,
              itemBuilder: (context) => const [
                PopupMenuItem(
                  value: 'incidencias',
                  child: Text('Administrar incidencias'),
                ),
                PopupMenuItem(
                  value: 'categorias',
                  child: Text('Administrar categorías'),
                ),
                PopupMenuItem(
                  value: 'usuarios',
                  child: Text('Administrar usuarios'),
                ),
                PopupMenuItem(
                  value: 'estadisticas',
                  child: Text('Estadísticas'),
                ),
              ],
            ),
          IconButton(
            tooltip: 'Notificaciones',
            onPressed: _abrirNotificaciones,
            icon: const Icon(Icons.notifications_outlined),
          ),
          IconButton(
            tooltip: 'Actualizar',
            onPressed: _actualizarLista,
            icon: const Icon(Icons.refresh),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: _abrirFormulario,
        icon: const Icon(Icons.add),
        label: const Text('Reportar'),
      ),
      body: FutureBuilder<List<Map<String, dynamic>>>(
        future: _incidencias,
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
                    const Icon(
                      Icons.error_outline,
                      size: 48,
                      color: Colors.red,
                    ),
                    const SizedBox(height: 12),
                    Text(
                      'No se pudieron cargar las incidencias:\n$mensaje',
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

          final incidencias = snapshot.data ?? [];

          return ListView(
            padding: const EdgeInsets.all(16),
            children: [
              Text(
                'Hola, ${widget.nombre}',
                style: Theme.of(context).textTheme.headlineSmall,
              ),
              const SizedBox(height: 8),
              const Text('Estas son tus incidencias reportadas.'),
              const SizedBox(height: 16),
              if (incidencias.isEmpty)
                const Card(
                  child: Padding(
                    padding: EdgeInsets.all(20),
                    child: Text('Todavía no tienes incidencias.'),
                  ),
                )
              else
                ...incidencias.map((incidencia) {
                  final titulo =
                      incidencia['titulo']?.toString() ?? 'Sin título';
                  final estado =
                      incidencia['estado']?.toString() ?? 'Sin estado';
                  final ubicacion =
                      incidencia['ubicacion']?.toString() ?? 'Sin ubicación';
                  final descripcion =
                      incidencia['descripcion']?.toString() ?? '';

                  return Card(
                    margin: const EdgeInsets.only(bottom: 12),
                    child: ListTile(
                      onTap: () => _abrirDetalle(
                        incidencia['id'].toString(),
                      ),
                      leading: const Icon(
                        Icons.report_problem_outlined,
                        color: Color(0xFF1769AA),
                      ),
                      title: Text(titulo),
                      subtitle: Text(
                        '$estado\n$ubicacion'
                        '${descripcion.isEmpty ? '' : '\n$descripcion'}',
                      ),
                      isThreeLine: descripcion.isNotEmpty,
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