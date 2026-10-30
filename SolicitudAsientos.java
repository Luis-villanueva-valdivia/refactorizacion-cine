// TÉCNICA UTILIZADA: Introduce parameter object
// REFACTOR: Se agrupan filas y números en un solo objeto para representar la solicitud de asientos.
public class SolicitudAsientos {
    private final char[] filas;
    private final int[] numeros;

    public SolicitudAsientos(char[] filas, int[] numeros) {
        this.filas = filas;
        this.numeros = numeros;
    }

    public boolean esValida() {
        return filas.length != 0 && filas.length == numeros.length;
    }

    public int cantidad() {
        return filas.length;
    }

    public char obtenerFila(int indice) {
        return filas[indice];
    }

    public int obtenerNumero(int indice) {
        return numeros[indice];
    }
}
