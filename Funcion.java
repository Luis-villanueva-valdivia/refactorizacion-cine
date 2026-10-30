import java.util.ArrayList;
import java.util.List;

public class Funcion {
    public String titulo;
    public int sala;
    public String hora;
    public double precio;
    public List<String> ocupados = new ArrayList<>();

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
    //   - La validación que lanzaba la excepción se movió a SistemaReservas.hacer(),
    //     para que la salida siga siendo idéntica (el try/catch se quita en ISS-07).
    public boolean estaLibre(char fila, int numero) {
        return !ocupados.contains("" + fila + numero);
    }

    public void marcarOcupado(char fila, int numero) {
        ocupados.add("" + fila + numero);
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
