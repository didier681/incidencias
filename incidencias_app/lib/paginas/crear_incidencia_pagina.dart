import 'package:flutter/material.dart';

import '../servicios/servicio_incidencias.dart';

class CrearIncidenciaPagina extends StatefulWidget {
  const CrearIncidenciaPagina({
    required this.token,
    super.key,
  });

  final String token;

  @override
  State<CrearIncidenciaPagina> createState() =>
      _CrearIncidenciaPaginaState();
}

class _CrearIncidenciaPaginaState extends State<CrearIncidenciaPagina> {
  final _formulario = GlobalKey<FormState>();
  final _tituloControlador = TextEditingController();
  final _descripcionControlador = TextEditingController();
  final _ubicacionControlador = TextEditingController();
  final _servicio = ServicioIncidencias();

  late Future<List<Map<String, dynamic>>> _categorias;
  String? _categoriaId;
  bool _guardando = false;

  @override
  void initState() {
    super.initState();
    _categorias = _servicio.listarCategorias(widget.token);
  }

  @override
  void dispose() {
    _tituloControlador.dispose();
    _descripcionControlador.dispose();
    _ubicacionControlador.dispose();
    super.dispose();
  }

  Future<void> _crearIncidencia() async {
    if (!_formulario.currentState!.validate()) {
      return;
    }

    final categoriaId = _categoriaId;
    if (categoriaId == null) {
      return;
    }

    setState(() {
      _guardando = true;
    });

    try {
      await _servicio.crearIncidencia(
        token: widget.token,
        categoriaId: categoriaId,
        titulo: _tituloControlador.text.trim(),
        descripcion: _descripcionControlador.text.trim(),
        ubicacion: _ubicacionControlador.text.trim(),
      );

      if (!mounted) return;
      Navigator.of(context).pop(true);
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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Reportar incidencia'),
      ),
      body: FutureBuilder<List<Map<String, dynamic>>>(
        future: _categorias,
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
                      'No se pudieron cargar las categorías:\n$mensaje',
                      textAlign: TextAlign.center,
                    ),
                    const SizedBox(height: 16),
                    FilledButton(
                      onPressed: () {
                        setState(() {
                          _categorias =
                              _servicio.listarCategorias(widget.token);
                        });
                      },
                      child: const Text('Intentar de nuevo'),
                    ),
                  ],
                ),
              ),
            );
          }

          final categorias = snapshot.data ?? [];

          if (categorias.isEmpty) {
            return const Center(
              child: Padding(
                padding: EdgeInsets.all(24),
                child: Text('No hay categorías activas disponibles.'),
              ),
            );
          }

          return SingleChildScrollView(
            padding: const EdgeInsets.all(20),
            child: Form(
              key: _formulario,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  DropdownButtonFormField<String>(
                    initialValue: _categoriaId,
                    decoration: const InputDecoration(
                      labelText: 'Categoría',
                      border: OutlineInputBorder(),
                    ),
                    items: categorias.map((categoria) {
                      return DropdownMenuItem<String>(
                        value: categoria['id'].toString(),
                        child: Text(
                          categoria['nombre']?.toString() ??
                              'Categoría sin nombre',
                        ),
                      );
                    }).toList(),
                    onChanged: _guardando
                        ? null
                        : (valor) {
                            setState(() {
                              _categoriaId = valor;
                            });
                          },
                    validator: (valor) {
                      if (valor == null) {
                        return 'Selecciona una categoría';
                      }
                      return null;
                    },
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    controller: _tituloControlador,
                    maxLength: 120,
                    decoration: const InputDecoration(
                      labelText: 'Título',
                      border: OutlineInputBorder(),
                    ),
                    validator: (valor) {
                      if (valor == null || valor.trim().isEmpty) {
                        return 'Escribe un título';
                      }
                      return null;
                    },
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    controller: _descripcionControlador,
                    maxLines: 4,
                    decoration: const InputDecoration(
                      labelText: 'Descripción',
                      alignLabelWithHint: true,
                      border: OutlineInputBorder(),
                    ),
                    validator: (valor) {
                      if (valor == null || valor.trim().isEmpty) {
                        return 'Escribe una descripción';
                      }
                      return null;
                    },
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    controller: _ubicacionControlador,
                    decoration: const InputDecoration(
                      labelText: 'Ubicación',
                      prefixIcon: Icon(Icons.location_on_outlined),
                      border: OutlineInputBorder(),
                    ),
                    validator: (valor) {
                      if (valor == null || valor.trim().isEmpty) {
                        return 'Escribe la ubicación';
                      }
                      return null;
                    },
                  ),
                  const SizedBox(height: 24),
                  SizedBox(
                    height: 52,
                    child: FilledButton(
                      onPressed: _guardando ? null : _crearIncidencia,
                      child: _guardando
                          ? const SizedBox(
                              width: 22,
                              height: 22,
                              child: CircularProgressIndicator(
                                strokeWidth: 2,
                              ),
                            )
                          : const Text('Enviar incidencia'),
                    ),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}