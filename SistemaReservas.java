import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaReservas {
    public Map<Integer, Object> db = new HashMap<>();
    public int contador = 1;

    // Nombre poco claro, muchos parámetros sueltos, flag sin uso y códigos de error
    public int hacer(String nombre, String email, boolean socio, Funcion f,
                     char[] filas, int[] numeros, boolean imprimir, String canal) {
        if (nombre == null || f == null) return -1;
        if (filas.length == 0 || filas.length != numeros.length) return -2;
        // TÉCNICA UTILIZADA: Replace exception with test
        // REFACTOR: Al aplicar esta técnica se realizaron los siguientes cambios:
        //   - Se quitó el try/catch (IllegalStateException) que se usaba para controlar el flujo.
        //   - Ahora primero se revisan TODOS los asientos con f.estaLibre() y, solo si todos
        //     están libres, se marcan con f.marcarOcupado().
        //   - Esto corrige el bug de F7: si un asiento está ocupado no se marca ninguno,
        //     así que Luis ya no deja asientos bloqueados (único cambio de comportamiento permitido).
        for (int i = 0; i < filas.length; i++) {
            if (!f.estaLibre(filas[i], numeros[i])) {
                return -3;
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

    // Downcast repetido en cada llamada
    public int cancelar(int id) {
        Reserva r = (Reserva) db.get(id);
        if (r == null) return -1;
        if (r.cancelada) return -2;
        for (String a : r.asientos) {
            // TÉCNICA UTILIZADA: Hide method
            // REFACTOR: ocupados ya es privada, así que se libera el asiento con liberar().
            r.funcion.liberar(a);
        }
        r.setCancelada(true);
        return 0;
    }

    public Object buscar(int id) {
        return db.get(id);
    }
}
