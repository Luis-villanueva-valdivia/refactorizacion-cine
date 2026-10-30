import java.util.List;

public class Reserva {
    public int id;
    public Cliente cliente;
    public Funcion funcion;
    public List<String> asientos;
    public double precio;
    public boolean cancelada;

    // TÉCNICA UTILIZADA: Add parameter
    // REFACTOR: Se agregó canal al constructor y se guarda en la Reserva,
    // porque SistemaReservas ya recibía ese dato pero lo ignoraba.
    public String canal;

    public Reserva(int id, Cliente cliente, Funcion funcion, List<String> asientos, String canal) {
        this.id = id;
        this.cliente = cliente;
        this.funcion = funcion;
        this.asientos = asientos;
        this.canal = canal;
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
