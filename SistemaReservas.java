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

    // TÉCNICA UTILIZADA: Rename method
    // REFACTOR: Se renombró hacer() a reservar() porque el nuevo nombre describe claramente la operación.
    // TÉCNICA UTILIZADA: Remove parameter
    // REFACTOR: Se eliminó boolean imprimir porque no se utilizaba ni modificaba el resultado.
    // TÉCNICA UTILIZADA: Preserve whole object
    // REFACTOR: Se recibe Cliente completo en lugar de nombre, email y socio por separado,
    // manteniendo agrupados los datos que pertenecen al cliente.
    // TÉCNICA UTILIZADA: Introduce parameter object
    // REFACTOR: Se reemplazaron los arreglos paralelos filas y numeros por SolicitudAsientos,
    // agrupando los datos de los asientos en un único parámetro.
    public int reservar(Cliente cliente, Funcion f, SolicitudAsientos solicitud, String canal) {
        // TÉCNICA UTILIZADA: Replace error code with exception
        // REFACTOR: Al aplicar esta técnica se realizaron los siguientes cambios:
        //   - Los códigos -1, -2 y -3 se reemplazaron por excepciones con un mensaje claro.
        //   - -1 (datos faltantes) y -2 (asientos inválidos) -> IllegalArgumentException.
        //   - -3 (asiento ocupado) -> IllegalStateException.
        //   - El método solo devuelve el id cuando la reserva se hizo bien.
        if (cliente == null || cliente.nombre == null || f == null) {
            throw new IllegalArgumentException("Faltan datos del cliente o de la función");
        }
        if (!solicitud.esValida()) {
            throw new IllegalArgumentException("La lista de asientos no es válida");
        }
        // TÉCNICA UTILIZADA: Replace exception with test
        // REFACTOR: Al aplicar esta técnica se realizaron los siguientes cambios:
        //   - Se quitó el try/catch (IllegalStateException) que se usaba para controlar el flujo.
        //   - Ahora primero se revisan TODOS los asientos con f.estaLibre() y, solo si todos
        //     están libres, se marcan con f.marcarOcupado().
        //   - Esto corrige el bug de F7: si un asiento está ocupado no se marca ninguno,
        //     así que Luis ya no deja asientos bloqueados (único cambio de comportamiento permitido).
        for (int i = 0; i < solicitud.cantidad(); i++) {
            char fila = solicitud.obtenerFila(i);
            int numero = solicitud.obtenerNumero(i);
            if (!f.estaLibre(fila, numero)) {
                throw new IllegalStateException("Asiento ocupado: " + fila + numero);
            }
        }
        List<String> lista = new ArrayList<>();
        for (int i = 0; i < solicitud.cantidad(); i++) {
            char fila = solicitud.obtenerFila(i);
            int numero = solicitud.obtenerNumero(i);
            f.marcarOcupado(fila, numero);
            lista.add("" + fila + numero);
        }
        Reserva r = new Reserva(contador, cliente, f, lista, canal);
        // Tecnica usada: Replace parameter with explicit methods
        // Refactor: se llama al metodo explicito segun el cliente, en lugar de pasar "socio"/"general".
        r.precio = cliente.socio ? calcularPrecioSocio(r) : calcularPrecioGeneral(r);
        db.put(contador, r);
        contador++;
        return r.id;
    }
    // Tecnica usada: Replace parameter with explicit methods
    // Refactor: Al aplicar esta tecnica se realizaron los siguientes cambios:
    //   - Se elimino calcularPrecio(Reserva r, String tipo) y su if/else con textos.
    //   - Se crearon calcularPrecioGeneral(r) y calcularPrecioSocio(r).
    public double calcularPrecioGeneral(Reserva r) {
        return precioBase(r);
    }
    public double calcularPrecioSocio(Reserva r) {
        // Tecnica usada: Parameterize method
        // Refactor: se usa aplicarDescuento() con 0.15 en lugar de descuentoDel15().
        return aplicarDescuento(precioBase(r), 0.15);
    }
    // Tecnica usada: Replace parameter with method
    // Refactor: Al aplicar esta tecnica se realizaron los siguientes cambios:
    //   - Se creo precioBase(r), que obtiene el precio de r.funcion y r.asientos.
    //   - Los metodos de precio ya no dependen de un precio calculado por fuera.
    public double precioBase(Reserva r) {
        return r.funcion.precio * r.asientos.size();
    }
    // Tecnica usada: Parameterize method
    // Refactor: Al aplicar esta tecnica se realizaron los siguientes cambios:
    //   - descuentoDel15() y descuentoDel25() se reemplazaron por aplicarDescuento(precio, porcentaje).
    //   - El porcentaje se pasa como parametro (0.15 = 15%).
    public double aplicarDescuento(double precio, double porcentaje) {
        return precio - precio * porcentaje;
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
