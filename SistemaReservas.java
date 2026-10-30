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
        // TÉCNICA UTILIZADA: Replace error code with exception
        // REFACTOR: Al aplicar esta técnica se realizaron los siguientes cambios:
        //   - Los códigos -1, -2 y -3 se reemplazaron por excepciones con un mensaje claro.
        //   - -1 (datos faltantes) y -2 (asientos inválidos) -> IllegalArgumentException.
        //   - -3 (asiento ocupado) -> IllegalStateException.
        //   - El método solo devuelve el id cuando la reserva se hizo bien.
        if (nombre == null || f == null) {
            throw new IllegalArgumentException("Faltan datos del cliente o de la función");
        }
        if (filas.length == 0 || filas.length != numeros.length) {
            throw new IllegalArgumentException("La lista de asientos no es válida");
        }
        // TÉCNICA UTILIZADA: Replace exception with test
        // REFACTOR: Al aplicar esta técnica se realizaron los siguientes cambios:
        //   - Se quitó el try/catch (IllegalStateException) que se usaba para controlar el flujo.
        //   - Ahora primero se revisan TODOS los asientos con f.estaLibre() y, solo si todos
        //     están libres, se marcan con f.marcarOcupado().
        //   - Esto corrige el bug de F7: si un asiento está ocupado no se marca ninguno,
        //     así que Luis ya no deja asientos bloqueados (único cambio de comportamiento permitido).
        for (int i = 0; i < filas.length; i++) {
            if (!f.estaLibre(filas[i], numeros[i])) {
                throw new IllegalStateException("Asiento ocupado: " + filas[i] + numeros[i]);
            }
        }
        List<String> lista = new ArrayList<>();
        for (int i = 0; i < filas.length; i++) {
            f.marcarOcupado(filas[i], numeros[i]);
            lista.add("" + filas[i] + numeros[i]);
        }
        Cliente c = new Cliente(nombre, email, socio);
        Reserva r = new Reserva(contador, c, f, lista);
        r.precio = calcularPrecio(r, socio ? "socio" : "general");
        db.put(contador, r);
        contador++;
        return r.id;
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

    // TÉCNICA UTILIZADA: Replace error code with exception
    // REFACTOR: Al aplicar esta técnica se realizaron los siguientes cambios:
    //   - cancelar() ya no devuelve 0, -1 o -2; ahora es void.
    //   - Si la reserva no existe lanza IllegalArgumentException.
    //   - Si ya estaba cancelada lanza IllegalStateException.
    public void cancelar(int id) {
        // TÉCNICA UTILIZADA: Encapsulate downcast
        // REFACTOR: se usa buscar(), que ya devuelve Reserva, en lugar de (Reserva) db.get(id).
        Reserva r = buscar(id);
        if (r == null) {
            throw new IllegalArgumentException("No existe la reserva " + id);
        }
        if (r.cancelada) {
            throw new IllegalStateException("La reserva " + id + " ya estaba cancelada");
        }
        for (String a : r.asientos) {
            // TÉCNICA UTILIZADA: Hide method
            // REFACTOR: ocupados ya es privada, así que se libera el asiento con liberar().
            r.funcion.liberar(a);
        }
        r.cancelar();
    }

    // Tecnica usada: Encapsulate downcast
    // Refactor: devuelve Reserva en lugar de Object; quien la usa ya no hace casteo.
    public Reserva buscar(int id) {
        return db.get(id);
    }
}
