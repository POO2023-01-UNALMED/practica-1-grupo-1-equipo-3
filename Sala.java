public class Sala {
    private Funcion funcion;
    private int noDeSala;
    private Asiento[] asientos;

    private final int noFilas;
    private final int noColumnas;

    public Sala(Funcion funcion, int noDeSala, int noFilas, int noColumnas) {
        this.funcion = funcion;
        this.noDeSala = noDeSala;
        this.noFilas = noFilas;
        this.noColumnas = noColumnas;
        this.asientos = new Asiento[noFilas*noColumnas]; // 15 asientos por fila, 11 filas en total
        int numeroSilla = 1;
        for (int i = 0; i < this.noColumnas; i++) { // recorre por columna
            for (int j = 0; j < this.noFilas; j++) { // recorre por fila
                String tipo;
                if (j < 10) { // filas A-J son generales
                    tipo = "general";
                } else { // filas K-O son preferenciales
                    tipo = "preferencial";
                }
                String letra = Character.toString((char)('A' + i)); // obtiene la letra correspondiente a la columna
                asientos[i*this.getNoFilas() + j] = new Asiento(tipo, true, letra+numeroSilla); // crea el objeto Asiento con los valores correspondientes
                numeroSilla++; // aumenta el número de silla
            }
            numeroSilla = 1; // reinicia el número de silla al final de cada columna
        }
    }


    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public int getNoDeSala() {
        return noDeSala;
    }

    public void setNoDeSala(int noDeSala) {
        this.noDeSala = noDeSala;
    }

    public Asiento[] getAsientos() {
        return asientos;
    }

    public void setAsientos(Asiento[] asientos) {
        this.asientos = asientos;
    }

    public int getNoFilas() {
        return noFilas;
    }

    public int getNoColumnas() {
        return noColumnas;
    }
}
