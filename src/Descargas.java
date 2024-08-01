import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Descargas {
    String libro;
    String usuario;
    String fechaDescarga;

    public Descargas() {
    }

    public Descargas(String libro, String usuario, String fechaDescarga) {
        this.libro = libro;
        this.usuario = usuario;
        this.fechaDescarga = generarFecha();
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFechaDescarga() {
        return fechaDescarga;
    }


    private String generarFecha() {

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date fecha = new Date();
        return formato.format(fecha);
    }

    public void guardar() {
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
            MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
            MongoCollection<Document> collection = database.getCollection("Descargas");

            Document documento = new Document("usuario", usuario)
                    .append("libro", libro)
                    .append("fechaDescarga", fechaDescarga);

            collection.insertOne(documento);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
