import 'package:flutter_test/flutter_test.dart';
import 'package:incidencias_app/app.dart';

void main() {
  testWidgets('muestra la pantalla de inicio de sesión', (
    WidgetTester tester,
  ) async {
    await tester.pumpWidget(const IncidenciasApp());

    expect(find.text('Incidencias'), findsOneWidget);
    expect(find.text('Iniciar sesión'), findsOneWidget);
  });
}