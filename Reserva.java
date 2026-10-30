import java.util.List;

public class Reserva {
    public int id;
    public Cliente cliente;
    public Funcion funcion;
    public List<String> asientos;
    public double precio;
    public boolean cancelada;

    public Reserva(int id, Cliente cliente, Funcion funcion, List<String> asientos) {
        this.id = id;
        this.cliente = cliente;
        this.funcion = funcion;
        this.asientos = asientos;
    }

    //Tecnica utilizada: Remove setting method
    /* Refactor: Al aplicar esta tecnica se realizaron los siguientes cambios:
    Eliminamos setCancelada(boolean), debido a que esta accion permitia descancelar una reserva
    Se agrego cancelar(), permite marcar la reserva como cancelada
     */
    public void cancelar() {
        this.cancelada = true;
    }
}