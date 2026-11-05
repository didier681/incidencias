import 'package:flutter/material.dart';

import '../servicios/servicio_autenticacion.dart';

class RegistroUsuarioPagina extends StatefulWidget {
  const RegistroUsuarioPagina({super.key});

  @override
  State<RegistroUsuarioPagina> createState() => _RegistroUsuarioPaginaState();
}

class _RegistroUsuarioPaginaState extends State<RegistroUsuarioPagina> {
  final _formulario = GlobalKey<FormState>();
  final _nombreControlador = TextEditingController();
  final _correoControlador = TextEditingController();
  final _contrasenaControlador = TextEditingController();
  final _confirmarControlador = TextEditingController();
  final _servicio = ServicioAutenticacion();

  bool _mostrarContrasena = false;
  bool _guardando = false;

  @override
  void dispose() {
    _nombreControlador.dispose();
    _correoControlador.dispose();
    _contrasenaControlador.dispose();
    _confirmarControlador.dispose();
    super.dispose();
  }

  Future<void> _registrar() async {
    if (!_formulario.currentState!.validate()) {
      return;
    }

    setState(() {
      _guardando = true;
    });

    try {
      await _servicio.registrar(
        nombre: _nombreControlador.text.trim(),
        correo: _correoControlador.text.trim(),
        contrasena: _contrasenaControlador.text,
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
        title: const Text('Crear cuenta'),
      ),
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
                      Icons.person_add_alt_1,
                      size: 64,
                      color: Color(0xFF1769AA),
                    ),
                    const SizedBox(height: 16),
                    Text(
                      'Regístrate',
                      textAlign: TextAlign.center,
                      style: Theme.of(context).textTheme.headlineMedium,
                    ),
                    const SizedBox(height: 24),
                    TextFormField(
                      controller: _nombreControlador,
                      textCapitalization: TextCapitalization.words,
                      decoration: const InputDecoration(
                        labelText: 'Nombre',
                        prefixIcon: Icon(Icons.person_outline),
                        border: OutlineInputBorder(),
                      ),
                      validator: (valor) {
                        if (valor == null || valor.trim().isEmpty) {
                          return 'Escribe tu nombre';
                        }
                        return null;
                      },
                    ),
                    const SizedBox(height: 16),
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
                          return 'Escribe una contraseña';
                        }
                        return null;
                      },
                    ),
                    const SizedBox(height: 16),
                    TextFormField(
                      controller: _confirmarControlador,
                      obscureText: !_mostrarContrasena,
                      decoration: const InputDecoration(
                        labelText: 'Confirma la contraseña',
                        prefixIcon: Icon(Icons.lock_reset_outlined),
                        border: OutlineInputBorder(),
                      ),
                      validator: (valor) {
                        if (valor == null || valor.isEmpty) {
                          return 'Confirma tu contraseña';
                        }
                        if (valor != _contrasenaControlador.text) {
                          return 'Las contraseñas no coinciden';
                        }
                        return null;
                      },
                    ),
                    const SizedBox(height: 24),
                    SizedBox(
                      height: 52,
                      child: FilledButton(
                        onPressed: _guardando ? null : _registrar,
                        child: _guardando
                            ? const SizedBox(
                                width: 22,
                                height: 22,
                                child: CircularProgressIndicator(
                                  strokeWidth: 2,
                                ),
                              )
                            : const Text('Crear cuenta'),
                      ),
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