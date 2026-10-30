public class Main {
    public static void main(String[] args) {
        SistemaReservas s = new SistemaReservas();
        Funcion f = new Funcion("Interestelar", 3, "20:30", 12.0);

        // TÉCNICA UTILIZADA: Preserve whole object
        // REFACTOR: Se construye cada Cliente completo y se entrega a reservar().
        // TÉCNICA UTILIZADA: Replace error code with exception
        // REFACTOR: reservar() y cancelar() ya no devuelven códigos numéricos de error,
        //   así que Main atrapa las excepciones y muestra un mensaje controlado.
        Cliente anaCliente = new Cliente("Ana", "ana@mail.com", true);
        try {
            int r1 = s.reservar(anaCliente, f,
                    new SolicitudAsientos(new char[]{'F', 'F'}, new int[]{5, 6}), "App");
            System.out.println("Reserva Ana: " + r1);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Reserva Ana: Error - " + e.getMessage());
        }
        // Ya no queda F7 bloqueado: si F6 está ocupado no se marca ningún asiento
        Cliente luisCliente = new Cliente("Luis", "luis@mail.com", false);
        try {
            int r2 = s.reservar(luisCliente, f,
                    new SolicitudAsientos(new char[]{'F', 'F'}, new int[]{7, 6}), "Taquilla");
            System.out.println("Reserva Luis: " + r2);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Reserva Luis: Error - " + e.getMessage());
        }
        // TÉCNICA UTILIZADA: Hide method
        // REFACTOR: ocupados ya es privada, así que se lee con getOcupados().
        System.out.println("Ocupados: " + f.getOcupados());
        // Tecnica usada: Encapsulate downcast
        // Refactor: buscar() ya devuelve Reserva, así que se quitó el casteo (Reserva).
        Reserva ana = s.buscar(1);
        System.out.println("Precio Ana (socio): " + ana.precio);

        try {
            s.cancelar(1);
            System.out.println("Cancelar 1: OK");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Cancelar 1: Error - " + e.getMessage());
        }
        try {
            s.cancelar(99);
            System.out.println("Cancelar 99: OK");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Cancelar 99: Error - " + e.getMessage());
        }
    }
}
