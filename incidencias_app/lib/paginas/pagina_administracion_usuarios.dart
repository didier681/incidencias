import 'package:flutter/material.dart';

import '../servicios/servicio_administracion_usuarios.dart';

class PaginaAdministracionUsuarios extends StatefulWidget {
  const PaginaAdministracionUsuarios({
    required this.token,
    required this.usuarioActualId,
    super.key,
  });

  final String token;
  final String usuarioActualId;

  @override
  State<PaginaAdministracionUsuarios> createState() =>
      _PaginaAdministracionUsuariosState();
}

class _PaginaAdministracionUsuariosState
    extends State<PaginaAdministracionUsuarios> {
  final ServicioAdministracionUsuarios _servicio =
      ServicioAdministracionUsuarios();

  late Future<List<Map<String, dynamic>>> _usuarios;
  String? _usuarioActualizando;

  @override
  void initState() {
    super.initState();
    _cargarUsuarios();
  }

  void _cargarUsuarios() {
    _usuarios = _servicio.listar(widget.token);
  }

  void _actualizarLista() {
    setState(_cargarUsuarios);
  }

  Future<void> _cambiarEstado(
    String usuarioId,
    bool activo,
  ) async {
    setState(() {
      _usuarioActualizando = usuarioId;
    });

    try {
      await _servicio.cambiarEstado(
        token: widget.token,
        usuarioId: usuarioId,
        activo: activo,
      );

      if (!mounted) return;

      _actualizarLista();
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(
            activo ? 'Usuario activado.' : 'Usuario desactivado.',
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
          _usuarioActualizando = null;
        });
      }
    }
  }

  Future<void> _cambiarRol(
    String usuarioId,
    String nuevoRol,
  ) async {
    setState(() {
      _usuarioActualizando = usuarioId;
    });

    try {
      await _servicio.cambiarRol(
        token: widget.token,
        usuarioId: usuarioId,
        rol: nuevoRol,
      );

      if (!mounted) return;

      _actualizarLista();
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Rol cambiado a $nuevoRol.')),
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
          _usuarioActualizando = null;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Administrar usuarios'),
        actions: [
          IconButton(
            tooltip: 'Actualizar',
            onPressed: _actualizarLista,
            icon: const Icon(Icons.refresh),
          ),
        ],
      ),
      body: FutureBuilder<List<Map<String, dynamic>>>(
        future: _usuarios,
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
                      'No se pudieron cargar los usuarios:\n$mensaje',
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

          final usuarios = snapshot.data ?? [];

          if (usuarios.isEmpty) {
            return const Center(
              child: Text('No hay usuarios para mostrar.'),
            );
          }

          return ListView.builder(
            padding: const EdgeInsets.all(12),
            itemCount: usuarios.length,
            itemBuilder: (context, indice) {
              final usuario = usuarios[indice];
              final id = usuario['id']?.toString() ?? '';
              final nombre = usuario['nombre']?.toString() ?? 'Sin nombre';
              final correo = usuario['correo']?.toString() ?? '';
              final rol = usuario['rol']?.toString() ?? 'USUARIO';
              final activo = usuario['activo'] == true;
              final esUsuarioActual = id == widget.usuarioActualId;
              final actualizando = _usuarioActualizando == id;

              return Card(
                margin: const EdgeInsets.only(bottom: 12),
                child: Column(
                  children: [
                    ListTile(
                      leading: CircleAvatar(
                        child: Icon(
                          esUsuarioActual
                              ? Icons.admin_panel_settings
                              : Icons.person_outline,
                        ),
                      ),
                      title: Text(nombre),
                      subtitle: Text(
                        '$correo\nRol: $rol'
                        '${esUsuarioActual ? '\nTu cuenta' : ''}',
                      ),
                      isThreeLine: true,
                    ),
                    if (actualizando)
                      const LinearProgressIndicator(),
                    SwitchListTile(
                      title: const Text('Cuenta activa'),
                      value: activo,
                      onChanged: esUsuarioActual || actualizando || id.isEmpty
                          ? null
                          : (nuevoValor) => _cambiarEstado(id, nuevoValor),
                    ),
                    Padding(
                      padding: const EdgeInsets.fromLTRB(16, 0, 16, 12),
                      child: Row(
                        children: [
                          const Text('Rol'),
                          const SizedBox(width: 16),
                          Expanded(
                            child: DropdownButton<String>(
                              isExpanded: true,
                              value: rol == 'ADMINISTRADOR' || rol == 'USUARIO'
                                  ? rol
                                  : null,
                              hint: Text(rol),
                              items: const [
                                DropdownMenuItem(
                                  value: 'USUARIO',
                                  child: Text('USUARIO'),
                                ),
                                DropdownMenuItem(
                                  value: 'ADMINISTRADOR',
                                  child: Text('ADMINISTRADOR'),
                                ),
                              ],
                              onChanged:
                                  esUsuarioActual || actualizando || id.isEmpty
                                      ? null
                                      : (nuevoRol) {
                                          if (nuevoRol != null &&
                                              nuevoRol != rol) {
                                            _cambiarRol(id, nuevoRol);
                                          }
                                        },
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              );
            },
          );
        },
      ),
    );
  }
}