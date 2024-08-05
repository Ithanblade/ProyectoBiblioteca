import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Date;

public class Descargas {
    private String libro;
    private String usuario;
    private Date fechaDescarga;

    // Constructor por defecto
    public Descargas() {
    }

    // Constructor con parámetros
    public Descargas(String libro, String usuario, String fechaDescarga) {
        this.libro = libro;
        this.usuario = usuario;
        this.fechaDescarga = new Date(); // Inicializa con la fecha y hora actual
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

    public Date getFechaDescarga() {
        return fechaDescarga;
    }

    // Método para guardar la información de la descarga en la base de datos
    public void guardar() {
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
            MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
            MongoCollection<Document> collection = database.getCollection("Descargas");

            // Crea un documento con la información de la descarga
            Document documento = new Document("usuario", usuario)
                    .append("libro", libro)
                    .append("fechaDescarga", fechaDescarga);

            // Inserta el documento en la colección de descargas
            collection.insertOne(documento);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
