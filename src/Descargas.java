import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Date;

public class Descargas {
    String libro;
    String usuario;
    Date fechaDescarga;

    public Descargas() {
    }

    public Descargas(String libro, String usuario, String fechaDescarga) {
        this.libro = libro;
        this.usuario = usuario;
        this.fechaDescarga = new Date();
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
