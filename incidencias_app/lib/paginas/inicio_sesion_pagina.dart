import 'package:flutter/material.dart';

import '../servicios/servicio_autenticacion.dart';
import 'pagina_incidencias.dart';
import 'registro_usuario_pagina.dart';

class InicioSesionPagina extends StatefulWidget {
  const InicioSesionPagina({super.key});

  @override
  State<InicioSesionPagina> createState() => _InicioSesionPaginaState();
}

class _InicioSesionPaginaState extends State<InicioSesionPagina> {
  final _formulario = GlobalKey<FormState>();
  final _correoControlador = TextEditingController();
  final _contrasenaControlador = TextEditingController();
  final _servicioAutenticacion = ServicioAutenticacion();

  bool _mostrarContrasena = false;
  bool _cargando = false;

  @override
  void dispose() {
    _correoControlador.dispose();
    _contrasenaControlador.dispose();
    super.dispose();
  }

  Future<void> _iniciarSesion() async {
    if (!_formulario.currentState!.validate()) {
      return;
    }

    setState(() {
      _cargando = true;
    });

    try {
      final sesion = await _servicioAutenticacion.iniciarSesion(
        correo: _correoControlador.text.trim(),
        contrasena: _contrasenaControlador.text,
      );

      if (!mounted) return;

      final usuario = sesion['usuario'];
      final nombre = usuario is Map
          ? usuario['nombre']?.toString() ?? 'usuario'
          : 'usuario';
      final rol = usuario is Map
          ? usuario['rol']?.toString() ?? 'USUARIO'
          : 'USUARIO';
      final usuarioId =
          usuario is Map ? usuario['id']?.toString() : null;
      final token = sesion['token']?.toString();

      if (token == null || token.isEmpty) {
        throw Exception('El servidor no devolvió un token de acceso.');
      }

      if (usuarioId == null || usuarioId.isEmpty) {
        throw Exception(
          'El servidor no devolvió el identificador del usuario.',
        );
      }

      Navigator.of(context).pushReplacement(
        MaterialPageRoute(
          builder: (_) => PaginaIncidencias(
            nombre: nombre,
            token: token,
            rol: rol,
            usuarioId: usuarioId,
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
          _cargando = false;
        });
      }
    }
  }

  Future<void> _abrirRegistro() async {
    final registrado = await Navigator.of(context).push<bool>(
      MaterialPageRoute(
        builder: (_) => const RegistroUsuarioPagina(),
      ),
    );

    if (!mounted) return;

    if (registrado == true) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Cuenta creada. Ahora puedes iniciar sesión.'),
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF4F7FB),
      body: SafeArea(
        child: Center(
          child: SingleChildScrollView(
            padding: const EdgeInsets.all(24),
            child: ConstrainedBox(
              constraints: const BoxConstraints(maxWidth: 420),
              child: Form(
                key: _formulario,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    const Icon(
                      Icons.report_problem_outlined,
                      size: 72,
                      color: Color(0xFF1769AA),
                    ),
                    const SizedBox(height: 20),
                    const Text(
                      'Incidencias',
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 30,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 8),
                    const Text(
                      'Inicia sesión para continuar',
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 16,
                        color: Colors.black54,
                      ),
                    ),
                    const SizedBox(height: 32),
                    TextFormField(
                      controller: _correoControlador,
                      keyboardType: TextInputType.emailAddress,
                      decoration: const InputDecoration(
                        labelText: 'Correo electrónico',
                        prefixIcon: Icon(Icons.email_outlined),
                        border: OutlineInputBorder(),
                      ),
                      validator: (valor) {
                        if (valor == null || valor.trim().isEmpty) {
                          return 'Escribe tu correo electrónico';
                        }
                        if (!valor.contains('@')) {
                          return 'Escribe un correo válido';
                        }
                        return null;
                      },
                    ),
                    const SizedBox(height: 16),
                    TextFormField(
                      controller: _contrasenaControlador,
                      obscureText: !_mostrarContrasena,
                      decoration: InputDecoration(
                        labelText: 'Contraseña',
                        prefixIcon: const Icon(Icons.lock_outline),
                        border: const OutlineInputBorder(),
                        suffixIcon: IconButton(
                          tooltip: _mostrarContrasena
                              ? 'Ocultar contraseña'
                              : 'Mostrar contraseña',
                          icon: Icon(
                            _mostrarContrasena
                                ? Icons.visibility_off
                                : Icons.visibility,
                          ),
                          onPressed: () {
                            setState(() {
                              _mostrarContrasena = !_mostrarContrasena;
                            });
                          },
                        ),
                      ),
                      validator: (valor) {
                        if (valor == null || valor.isEmpty) {
                          return 'Escribe tu contraseña';
                        }
                        return null;
                      },
                    ),
                    const SizedBox(height: 24),
                    SizedBox(
                      height: 52,
                      child: FilledButton(
                        onPressed: _cargando ? null : _iniciarSesion,
                        child: _cargando
                            ? const SizedBox(
                                width: 22,
                                height: 22,
                                child: CircularProgressIndicator(
                                  strokeWidth: 2,
                                ),
                              )
                            : const Text(
                                'Iniciar sesión',
                                style: TextStyle(fontSize: 16),
                              ),
                      ),
                    ),
                    const SizedBox(height: 12),
                    TextButton(
                      onPressed: _cargando ? null : _abrirRegistro,
                      child: const Text('¿No tienes cuenta? Crear cuenta'),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}