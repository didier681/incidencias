import 'package:flutter/material.dart';

import '../servicios/servicio_categorias.dart';
import '../servicios/servicio_incidencias.dart';

class PaginaAdministracionIncidencias extends StatefulWidget {
  const PaginaAdministracionIncidencias({
    required this.token,
    super.key,
  });

  final String token;

  @override
  State<PaginaAdministracionIncidencias> createState() =>
      _PaginaAdministracionIncidenciasState();
}

class _PaginaAdministracionIncidenciasState
    extends State<PaginaAdministracionIncidencias> {
  final ServicioIncidencias _servicioIncidencias = ServicioIncidencias();
  final ServicioCategorias _servicioCategorias = ServicioCategorias();

  static const List<String> _estados = [
    'REPORTADA',
    'EN_REVISION',
    'EN_PROCESO',
    'RESUELTA',
    'RECHAZADA',
  ];

  late Future<List<Map<String, dynamic>>> _incidencias;
  late Future<List<Map<String, dynamic>>> _categorias;

  String? _estadoSeleccionado;
  String? _categoriaSeleccionada;
  String? _incidenciaActualizando;

  @override
  void initState() {
    super.initState();
    _categorias = _servicioCategorias.listar(widget.token);
    _cargarIncidencias();
  }

  void _cargarIncidencias() {
    _incidencias = _servicioIncidencias.listarAdministracion(
      widget.token,
      estado: _estadoSeleccionado,
      categoriaId: _categoriaSeleccionada,
    );
  }

  void _actualizarLista() {
    setState(_cargarIncidencias);
  }

  void _cambiarFiltroEstado(String? estado) {
    setState(() {
      _estadoSeleccionado = estado;
      _cargarIncidencias();
    });
  }

  void _cambiarFiltroCategoria(String? categoriaId) {
    setState(() {
      _categoriaSeleccionada = categoriaId;
      _cargarIncidencias();
    });
  }

  void _limpiarFiltros() {
    setState(() {
      _estadoSeleccionado = null;
      _categoriaSeleccionada = null;
      _cargarIncidencias();
    });
  }

  Future<void> _cambiarEstado(
    String incidenciaId,
    String nuevoEstado,
  ) async {
    setState(() {
      _incidenciaActualizando = incidenciaId;
    });

    try {
      await _servicioIncidencias.cambiarEstado(
        token: widget.token,
        incidenciaId: incidenciaId,
        estado: nuevoEstado,
      );

      if (!mounted) return;

      _actualizarLista();
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Estado cambiado a $nuevoEstado.')),
      );
    } catch (error) {
      if (!mounted) return;

      final mensaje = error.toString().replaceFirst('Exception: ', '');
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(mensaje)),
      );
    } finally {
      if (mounted) {
        setState(() {
          _incidenciaActualizando = null;
        });
      }
    }
  }

  Widget _construirFiltros() {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Text(
              'Filtrar incidencias',
              style: Theme.of(context).textTheme.titleMedium,
            ),
            const SizedBox(height: 12),
            DropdownButtonFormField<String>(
              initialValue: _estadoSeleccionado,
              decoration: const InputDecoration(
                labelText: 'Estado',
                border: OutlineInputBorder(),
              ),
              items: [
                const DropdownMenuItem<String>(
                  value: null,
                  child: Text('Todos los estados'),
                ),
                ..._estados.map(
                  (estado) => DropdownMenuItem<String>(
                    value: estado,
                    child: Text(estado),
                  ),
                ),
              ],
              onChanged: _cambiarFiltroEstado,
            ),
            const SizedBox(height: 12),
            FutureBuilder<List<Map<String, dynamic>>>(
              future: _categorias,
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return const LinearProgressIndicator();
                }

                if (snapshot.hasError) {
                  return const Text(
                    'No se pudieron cargar las categorías para filtrar.',
                  );
                }

                final categorias = snapshot.data ?? [];

                return DropdownButtonFormField<String>(
                  initialValue: _categoriaSeleccionada,
                  decoration: const InputDecoration(
                    labelText: 'Categoría',
                    border: OutlineInputBorder(),
                  ),
                  items: [
                    const DropdownMenuItem<String>(
                      value: null,
                      child: Text('Todas las categorías'),
                    ),
                    ...categorias.map(
                      (categoria) => DropdownMenuItem<String>(
                        value: categoria['id'].toString(),
                        child: Text(
                          categoria['nombre']?.toString() ??
                              'Categoría sin nombre',
                        ),
                      ),
                    ),
                  ],
                  onChanged: _cambiarFiltroCategoria,
                );
              },
            ),
            if (_estadoSeleccionado != null ||
                _categoriaSeleccionada != null) ...[
              const SizedBox(height: 8),
              Align(
                alignment: Alignment.centerRight,
                child: TextButton(
                  onPressed: _limpiarFiltros,
                  child: const Text('Limpiar filtros'),
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Administrar incidencias'),
        actions: [
          IconButton(
            tooltip: 'Actualizar',
            onPressed: _actualizarLista,
            icon: const Icon(Icons.refresh),
          ),
        ],
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
            padding: const EdgeInsets.all(12),
            children: [
              _construirFiltros(),
              if (incidencias.isEmpty)
                const Padding(
                  padding: EdgeInsets.all(24),
                  child: Center(
                    child: Text('No hay incidencias con esos filtros.'),
                  ),
                )
              else
                ...incidencias.map((incidencia) {
                  final id = incidencia['id']?.toString() ?? '';
                  final titulo =
                      incidencia['titulo']?.toString() ?? 'Sin título';
                  final estado =
                      incidencia['estado']?.toString() ?? 'REPORTADA';
                  final ubicacion =
                      incidencia['ubicacion']?.toString() ?? 'Sin ubicación';
                  final descripcion =
                      incidencia['descripcion']?.toString() ?? '';

                  final estadoValido =
                      _estados.contains(estado) ? estado : null;
                  final actualizando = _incidenciaActualizando == id;

                  return Card(
                    margin: const EdgeInsets.only(bottom: 12),
                    child: Padding(
                      padding: const EdgeInsets.all(12),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            titulo,
                            style: Theme.of(context).textTheme.titleMedium,
                          ),
                          const SizedBox(height: 6),
                          Text('Ubicación: $ubicacion'),
                          if (descripcion.isNotEmpty) ...[
                            const SizedBox(height: 4),
                            Text(descripcion),
                          ],
                          const SizedBox(height: 12),
                          Row(
                            children: [
                              const Text('Estado:'),
                              const SizedBox(width: 12),
                              Expanded(
                                child: DropdownButton<String>(
                                  isExpanded: true,
                                  value: estadoValido,
                                  hint: Text(estado),
                                  items: _estados.map((opcion) {
                                    return DropdownMenuItem<String>(
                                      value: opcion,
                                      child: Text(opcion),
                                    );
                                  }).toList(),
                                  onChanged: actualizando || id.isEmpty
                                      ? null
                                      : (nuevoEstado) {
                                          if (nuevoEstado != null &&
                                              nuevoEstado != estado) {
                                            _cambiarEstado(id, nuevoEstado);
                                          }
                                        },
                                ),
                              ),
                              if (actualizando) ...[
                                const SizedBox(width: 8),
                                const SizedBox(
                                  width: 20,
                                  height: 20,
                                  child: CircularProgressIndicator(
                                    strokeWidth: 2,
                                  ),
                                ),
                              ],
                            ],
                          ),
                        ],
                      ),
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