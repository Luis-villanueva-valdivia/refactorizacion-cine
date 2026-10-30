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
        if (cliente == null || cliente.nombre == null || f == null) return -1;
        if (!solicitud.esValida()) return -2;
        try {
            List<String> lista = new ArrayList<>();
            for (int i = 0; i < solicitud.cantidad(); i++) {
                // TÉCNICA UTILIZADA: Separate query from modifier
                // REFACTOR: Al aplicar esta técnica se realizaron los siguientes cambios:
                //   - En lugar de llamar a f.ocupar(), primero se pregunta con f.estaLibre()
                //     y después se marca con f.marcarOcupado().
                //   - Se conserva la excepción y el bug de F7 para no cambiar la salida (se corrigen en ISS-07).
                char fila = solicitud.obtenerFila(i);
                int numero = solicitud.obtenerNumero(i);
                if (!f.estaLibre(fila, numero)) {
                    throw new IllegalStateException("Asiento ocupado: " + fila + numero);
                }
                f.marcarOcupado(fila, numero);   // si falla a mitad, los anteriores ya quedaron ocupados
                lista.add("" + fila + numero);
            }
            Reserva r = new Reserva(contador, cliente, f, lista, canal);
            // Tecnica usada: Replace parameter with explicit methods
            // Refactor: se llama al metodo explicito segun el cliente, en lugar de pasar "socio"/"general".
            r.precio = cliente.socio ? calcularPrecioSocio(r) : calcularPrecioGeneral(r);
            db.put(contador, r);
            contador++;
            return r.id;
        } catch (IllegalStateException e) {
            return -3;
        }
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
