import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Funcion {
    public String titulo;
    public int sala;
    public String hora;
    public double precio;

    // TÉCNICA UTILIZADA: Hide method
    // REFACTOR: Al aplicar esta técnica se realizaron los siguientes cambios:
    //   - ocupados pasó de public a private, para que ninguna otra clase la modifique directo.
    //   - Se agregó getOcupados(), que devuelve una copia de solo lectura (para imprimir en Main).
    //   - Se agregó liberar(), que es la única forma de quitar un asiento (la usa cancelar()).
    private List<String> ocupados = new ArrayList<>();

    public Funcion(String titulo, int sala, String hora, double precio) {
        this.titulo = titulo;
        this.sala = sala;
        this.hora = hora;
        this.precio = precio;
    }

    // TÉCNICA UTILIZADA: Separate query from modifier
    // REFACTOR: Al aplicar esta técnica se realizaron los siguientes cambios:
    //   - ocupar() hacía dos cosas: revisaba si el asiento estaba libre y además lo marcaba.
    //   - Se dividió en estaLibre() (consulta: solo responde true/false, no cambia nada)
    //     y marcarOcupado() (modificador: solo agrega el asiento a la lista).
    //   - La validación que lanzaba la excepción se movió a SistemaReservas.reservar().
    public boolean estaLibre(char fila, int numero) {
        return !ocupados.contains("" + fila + numero);
    }

    public void marcarOcupado(char fila, int numero) {
        ocupados.add("" + fila + numero);
    }

    public List<String> getOcupados() {
        return Collections.unmodifiableList(ocupados);
    }

    public void liberar(String codigo) {
        ocupados.remove(codigo);
    }

    // TÉCNICA UTILIZADA: Remove setting method
    // REFACTOR: Se eliminó setPrecio(double) porque nadie lo usaba y el precio
    // de una función solo debe fijarse una vez, en el constructor.
}
