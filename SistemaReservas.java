import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaReservas {
    // Tecnica usada: Encapsulate downcast
    // Refactor: Al aplicar esta tecnica se realizaron los siguientes cambios:
    //   db se tipo como Map<Integer, Reserva> en lugar de Map<Integer, Object>
    //   buscar() devuelve Reserva en lugar de Object
    //   Se eliminaron los casteos (Reserva) en cancelar() y en Main
    public Map<Integer, Reserva> db = new HashMap<>();
    public int contador = 1;

    // Nombre poco claro, muchos parámetros sueltos, flag sin uso y códigos de error
    public int hacer(String nombre, String email, boolean socio, Funcion f,
                     char[] filas, int[] numeros, boolean imprimir, String canal) {
        if (nombre == null || f == null) return -1;
        if (filas.length == 0 || filas.length != numeros.length) return -2;
        try {
            List<String> lista = new ArrayList<>();
            for (int i = 0; i < filas.length; i++) {
                // TÉCNICA UTILIZADA: Separate query from modifier
                // REFACTOR: Al aplicar esta técnica se realizaron los siguientes cambios:
                //   - En lugar de llamar a f.ocupar(), primero se pregunta con f.estaLibre()
                //     y después se marca con f.marcarOcupado().
                //   - Se conserva la excepción y el bug de F7 para no cambiar la salida (se corrigen en ISS-07).
                if (!f.estaLibre(filas[i], numeros[i])) {
                    throw new IllegalStateException("Asiento ocupado: " + filas[i] + numeros[i]);
                }
                f.marcarOcupado(filas[i], numeros[i]);   // si falla a mitad, los anteriores ya quedaron ocupados
                lista.add("" + filas[i] + numeros[i]);
            }
            Cliente c = new Cliente(nombre, email, socio);
            Reserva r = new Reserva(contador, c, f, lista);
            r.precio = calcularPrecio(r, socio ? "socio" : "general");
            db.put(contador, r);
            contador++;
            return r.id;
        } catch (IllegalStateException e) {
            return -3;
        }
    }

    // Tipo de tarifa como String y if/else
    public double calcularPrecio(Reserva r, String tipo) {
        double base = r.funcion.precio * r.asientos.size();
        if (tipo.equals("general")) {
            return base;
        } else if (tipo.equals("socio")) {
            return descuentoDel15(base);
        }
        return base;
    }

    public double descuentoDel15(double p) {
        return p - p * 0.15;
    }

    public double descuentoDel25(double p) {   // duplicado, sin uso
        return p - p * 0.25;
    }

    public int cancelar(int id) {
        // Tecnica usada: Encapsulate downcast
        // Refactor: se usa buscar(), que ya devuelve Reserva, en lugar de (Reserva) db.get(id).
        Reserva r = buscar(id);
        if (r == null) return -1;
        if (r.cancelada) return -2;
        for (String a : r.asientos) {
            // TÉCNICA UTILIZADA: Hide method
            // REFACTOR: ocupados ya es privada, así que se libera el asiento con liberar().
            r.funcion.liberar(a);
        }
        r.cancelar();
        return 0;
    }

    // Tecnica usada: Encapsulate downcast
    // Refactor: devuelve Reserva en lugar de Object; quien la usa ya no hace casteo.
    public Reserva buscar(int id) {
        return db.get(id);
    }
}
