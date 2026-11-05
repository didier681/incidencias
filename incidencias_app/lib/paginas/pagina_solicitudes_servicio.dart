import 'package:flutter/material.dart';

import '../servicios/servicio_solicitudes_servicio.dart';

class PaginaSolicitudesServicio extends StatefulWidget {
  const PaginaSolicitudesServicio({
    required this.token,
    required this.incidenciaId,
    this.esAdministrador = false,
    super.key,
  });

  final String token;
  final String incidenciaId;
  final bool esAdministrador;

  @override
  State<PaginaSolicitudesServicio> createState() =>
      _PaginaSolicitudesServicioState();
}

class _PaginaSolicitudesServicioState
    extends State<PaginaSolicitudesServicio> {
  final ServicioSolicitudesServicio _servicio =
      ServicioSolicitudesServicio();

  static const List<String> _estados = [
    'PENDIENTE',
    'ASIGNADA',
    'EN_ATENCION',
    'COMPLETADA',
    'CANCELADA',
  ];

  late Future<List<Map<String, dynamic>>> _solicitudes;
  bool _guardando = false;
  String? _solicitudActualizando;

  @override
  void initState() {
    super.initState();
    _cargarSolicitudes();
  }

  void _cargarSolicitudes() {
    _solicitudes = _servicio.listarPorIncidencia(
      token: widget.token,
      incidenciaId: widget.incidenciaId,
    );
  }

  void _actualizarLista() {
    setState(_cargarSolicitudes);
  }

  Future<void> _crearSolicitud() async {
    final formulario = GlobalKey<FormState>();
    final tipoControlador = TextEditingController();
    final descripcionControlador = TextEditingController();

    final datos = await showDialog<Map<String, String>>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Nueva solicitud de servicio'),
          content: Form(
            key: formulario,
            child: SingleChildScrollView(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  TextFormField(
                    controller: tipoControlador,
                    decoration: const InputDecoration(
                      labelText: 'Tipo de servicio',
                      hintText: 'Ej. Reparación de luminaria',
                      border: OutlineInputBorder(),
                    ),
                    validator: (valor) {
                      if (valor == null || valor.trim().isEmpty) {
                        return 'Escribe el tipo de servicio';
                      }
                      return null;
                    },
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    controller: descripcionControlador,
                    maxLines: 3,
                    decoration: const InputDecoration(
                      labelText: 'Descripción',
                      border: OutlineInputBorder(),
                    ),
                    validator: (valor) {
                      if (valor == null || valor.trim().isEmpty) {
                        return 'Escribe una descripción';
                      }
                      return null;
                    },
                  ),
                ],
              ),
            ),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('Cancelar'),
            ),
            FilledButton(
              onPressed: () {
                if (!formulario.currentState!.validate()) return;

                Navigator.pop(context, {
                  'tipoServicio': tipoControlador.text.trim(),
                  'descripcion': descripcionControlador.text.trim(),
                });
              },
              child: const Text('Enviar'),
            ),
          ],
        );
      },
    );

    tipoControlador.dispose();
    descripcionControlador.dispose();

    if (datos == null) return;

    setState(() {
      _guardando = true;
    });

    try {
      await _servicio.crear(
        token: widget.token,
        incidenciaId: widget.incidenciaId,
        tipoServicio: datos['tipoServicio']!,
        descripcion: datos['descripcion']!,
      );

      if (!mounted) return;

      _actualizarLista();
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Solicitud de servicio creada correctamente.'),
        ),
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
          _guardando = false;
        });
      }
    }
  }

  Future<void> _cambiarEstado(
    String solicitudId,
    String nuevoEstado,
  ) async {
    setState(() {
      _solicitudActualizando = solicitudId;
    });

    try {
      await _servicio.cambiarEstado(
        token: widget.token,
        incidenciaId: widget.incidenciaId,
        solicitudId: solicitudId,
        estado: nuevoEstado,
      );

      if (!mounted) return;

      _actualizarLista();
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Solicitud actualizada a $nuevoEstado.')),
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
          _solicitudActualizando = null;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Solicitudes de servicio'),
        actions: [
          IconButton(
            tooltip: 'Actualizar',
            onPressed: _actualizarLista,
            icon: const Icon(Icons.refresh),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: _guardando ? null : _crearSolicitud,
        icon: _guardando
            ? const SizedBox(
                width: 20,
                height: 20,
                child: CircularProgressIndicator(strokeWidth: 2),
              )
            : const Icon(Icons.add),
        label: const Text('Solicitar servicio'),
      ),
      body: FutureBuilder<List<Map<String, dynamic>>>(
        future: _solicitudes,
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
                      'No se pudieron cargar las solicitudes:\n$mensaje',
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

          final solicitudes = snapshot.data ?? [];

          if (solicitudes.isEmpty) {
            return const Center(
              child: Padding(
                padding: EdgeInsets.all(24),
                child: Text('Esta incidencia todavía no tiene solicitudes.'),
              ),
            );
          }

          return ListView.builder(
            padding: const EdgeInsets.all(12),
            itemCount: solicitudes.length,
            itemBuilder: (context, indice) {
              final solicitud = solicitudes[indice];
              final id = solicitud['id']?.toString() ?? '';
              final tipo =
                  solicitud['tipoServicio']?.toString() ?? 'Servicio';
              final descripcion =
                  solicitud['descripcion']?.toString() ?? '';
              final estado =
                  solicitud['estado']?.toString() ?? 'PENDIENTE';
              final fecha = solicitud['fechaSolicitud']
                      ?.toString()
                      .replaceFirst('T', ' ') ??
                  '';
              final estadoValido =
                  _estados.contains(estado) ? estado : null;
              final actualizando = _solicitudActualizando == id;

              return Card(
                child: ListTile(
                  leading: const Icon(
                    Icons.handyman_outlined,
                    color: Color(0xFF1769AA),
                  ),
                  title: Text(tipo),
                  subtitle: Text(
                    '$estado'
                    '${fecha.isEmpty ? '' : '\n$fecha'}'
                    '${descripcion.isEmpty ? '' : '\n$descripcion'}',
                  ),
                  isThreeLine: descripcion.isNotEmpty,
                  trailing: widget.esAdministrador
                      ? SizedBox(
                          width: 145,
                          child: actualizando
                              ? const Center(
                                  child: CircularProgressIndicator(
                                    strokeWidth: 2,
                                  ),
                                )
                              : DropdownButton<String>(
                                  isExpanded: true,
                                  value: estadoValido,
                                  hint: Text(estado),
                                  items: _estados.map((opcion) {
                                    return DropdownMenuItem<String>(
                                      value: opcion,
                                      child: Text(
                                        opcion,
                                        overflow: TextOverflow.ellipsis,
                                      ),
                                    );
                                  }).toList(),
                                  onChanged: id.isEmpty
                                      ? null
                                      : (nuevoEstado) {
                                          if (nuevoEstado != null &&
                                              nuevoEstado != estado) {
                                            _cambiarEstado(id, nuevoEstado);
                                          }
                                        },
                                ),
                        )
                      : null,
                ),
              );
            },
          );
        },
      ),
    );
  }
}