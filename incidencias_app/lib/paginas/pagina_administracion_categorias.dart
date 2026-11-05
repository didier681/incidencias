import 'package:flutter/material.dart';

import '../servicios/servicio_categorias.dart';

class PaginaAdministracionCategorias extends StatefulWidget {
  const PaginaAdministracionCategorias({
    required this.token,
    super.key,
  });

  final String token;

  @override
  State<PaginaAdministracionCategorias> createState() =>
      _PaginaAdministracionCategoriasState();
}

class _PaginaAdministracionCategoriasState
    extends State<PaginaAdministracionCategorias> {
  final ServicioCategorias _servicio = ServicioCategorias();

  late Future<List<Map<String, dynamic>>> _categorias;
  bool _procesando = false;

  @override
  void initState() {
    super.initState();
    _cargarCategorias();
  }

  void _cargarCategorias() {
    _categorias = _servicio.listar(widget.token);
  }

  void _actualizarLista() {
    setState(_cargarCategorias);
  }

  Future<void> _mostrarFormulario({
    Map<String, dynamic>? categoria,
  }) async {
    final formulario = GlobalKey<FormState>();
    final nombreControlador = TextEditingController(
      text: categoria?['nombre']?.toString() ?? '',
    );
    final descripcionControlador = TextEditingController(
      text: categoria?['descripcion']?.toString() ?? '',
    );

    final datos = await showDialog<Map<String, String>>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text(
            categoria == null ? 'Nueva categoría' : 'Editar categoría',
          ),
          content: Form(
            key: formulario,
            child: SingleChildScrollView(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  TextFormField(
                    controller: nombreControlador,
                    maxLength: 100,
                    decoration: const InputDecoration(
                      labelText: 'Nombre',
                      border: OutlineInputBorder(),
                    ),
                    validator: (valor) {
                      if (valor == null || valor.trim().isEmpty) {
                        return 'Escribe el nombre de la categoría';
                      }
                      return null;
                    },
                  ),
                  const SizedBox(height: 12),
                  TextFormField(
                    controller: descripcionControlador,
                    maxLength: 500,
                    maxLines: 3,
                    decoration: const InputDecoration(
                      labelText: 'Descripción',
                      alignLabelWithHint: true,
                      border: OutlineInputBorder(),
                    ),
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
                  'nombre': nombreControlador.text.trim(),
                  'descripcion': descripcionControlador.text.trim(),
                });
              },
              child: const Text('Guardar'),
            ),
          ],
        );
      },
    );

    nombreControlador.dispose();
    descripcionControlador.dispose();

    if (datos == null) return;

    setState(() {
      _procesando = true;
    });

    try {
      if (categoria == null) {
        await _servicio.crear(
          token: widget.token,
          nombre: datos['nombre']!,
          descripcion: datos['descripcion']!,
        );
      } else {
        await _servicio.actualizar(
          token: widget.token,
          categoriaId: categoria['id'].toString(),
          nombre: datos['nombre']!,
          descripcion: datos['descripcion']!,
        );
      }

      if (!mounted) return;

      _actualizarLista();
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(
            categoria == null
                ? 'Categoría creada.'
                : 'Categoría actualizada.',
          ),
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
          _procesando = false;
        });
      }
    }
  }

  Future<void> _desactivarCategoria(
    Map<String, dynamic> categoria,
  ) async {
    final confirmar = await showDialog<bool>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Desactivar categoría'),
          content: Text(
            '¿Quieres desactivar "${categoria['nombre']}"?',
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context, false),
              child: const Text('Cancelar'),
            ),
            FilledButton(
              onPressed: () => Navigator.pop(context, true),
              child: const Text('Desactivar'),
            ),
          ],
        );
      },
    );

    if (confirmar != true) return;

    setState(() {
      _procesando = true;
    });

    try {
      await _servicio.desactivar(
        token: widget.token,
        categoriaId: categoria['id'].toString(),
      );

      if (!mounted) return;

      _actualizarLista();
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Categoría desactivada.')),
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
          _procesando = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Administrar categorías'),
        actions: [
          IconButton(
            tooltip: 'Actualizar',
            onPressed: _actualizarLista,
            icon: const Icon(Icons.refresh),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: _procesando ? null : () => _mostrarFormulario(),
        icon: const Icon(Icons.add),
        label: const Text('Nueva categoría'),
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

          final categorias = snapshot.data ?? [];

          if (categorias.isEmpty) {
            return const Center(
              child: Text('Todavía no hay categorías.'),
            );
          }

          return ListView.builder(
            padding: const EdgeInsets.all(12),
            itemCount: categorias.length,
            itemBuilder: (context, indice) {
              final categoria = categorias[indice];
              final nombre =
                  categoria['nombre']?.toString() ?? 'Sin nombre';
              final descripcion =
                  categoria['descripcion']?.toString() ?? '';
              final activa = categoria['activa'] == true;

              return Card(
                child: ListTile(
                  leading: Icon(
                    Icons.category_outlined,
                    color: activa ? Colors.blue : Colors.grey,
                  ),
                  title: Text(nombre),
                  subtitle: Text(
                    '${activa ? 'Activa' : 'Inactiva'}'
                    '${descripcion.isEmpty ? '' : '\n$descripcion'}',
                  ),
                  isThreeLine: descripcion.isNotEmpty,
                  trailing: PopupMenuButton<String>(
                    enabled: !_procesando && activa,
                    onSelected: (opcion) {
                      if (opcion == 'editar') {
                        _mostrarFormulario(categoria: categoria);
                      } else if (opcion == 'desactivar') {
                        _desactivarCategoria(categoria);
                      }
                    },
                    itemBuilder: (context) => const [
                      PopupMenuItem(
                        value: 'editar',
                        child: Text('Editar'),
                      ),
                      PopupMenuItem(
                        value: 'desactivar',
                        child: Text('Desactivar'),
                      ),
                    ],
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