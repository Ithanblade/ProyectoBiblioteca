import java.util.Random; //para generar numeros aleatorios y estos usarlos en el ISBN que voy a generar en el metodo

public class Libro {
    String titulo;
    String autor;
    String genero;
    int numPaginas;
    String ISBN;

    public Libro() {
    }

    public Libro(String titulo, String autor, String genero, int numPaginas, String ISBN) {
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.numPaginas = numPaginas;
        this.ISBN = generarISBN(); //se iguala el ISBN a un metodo para generar el mismo
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getNumPaginas() {
        return numPaginas;
    }

    public void setNumPaginas(int numPaginas) {
        this.numPaginas = numPaginas;
    }

    public String getISBN() {
        return ISBN;
    }
    //no hay setter del ISBN ya que voy a generarlo mediante un metodo.


    //Metodo del ISBN
    private String generarISBN() {
        Random random = new Random();
        StringBuilder isbn = new StringBuilder("978"); //Tengo entendido que siempre empiezan con "978" o "979"

        for (int i = 0; i < 9; i++) {
            isbn.append(random.nextInt(10)); //se agregan otros 9 numeros al isbn, serían en total 12
        }

        //  Calcular el ultimo numero del ISBN
        int total = 0;
        for (int i = 0; i < isbn.length(); i++) {
            int digito = Character.getNumericValue(isbn.charAt(i)); //Me da el valor numerico del caracter en la posicion i
            if (i % 2 == 0) { //si la posicion es par o 0 el numero se suma al total directamente
                total += digito;
            } else { // si la posicion es impar se suma el producto de la posición por 3
                total += digito * 3;
            }
        }

        int digitoControl = 10 - (total % 10); //para el ultimo numero se resta 10 - el modulo de 10 del total, algo así como las unidades, si es 14 me devuelve 4
        if (digitoControl == 10) {//si el numero sale 10 entonces el numero será 0.
            digitoControl = 0;
        }

        isbn.append(digitoControl);
        return isbn.toString();  //retorno el ISBN como string
    }



}
