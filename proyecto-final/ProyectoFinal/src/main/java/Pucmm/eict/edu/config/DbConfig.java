package Pucmm.eict.edu.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class DbConfig {
    private static Datastore datastore;

    private DbConfig(){};

    public static synchronized Datastore getDatastore() {
        if (datastore == null) {
            String uri = System.getenv("DB_URL");
            if (uri == null || uri.isBlank()) {
                throw new IllegalStateException(
                        "Falta la variable de entorno MONGO_URI. " +
                                "Ejemplo: mongodb+srv://usuario:clave@cluster.xxxxx.mongodb.net/?retryWrites=true&w=majority");
            }

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(uri))
                    .applyToConnectionPoolSettings(b -> b.maxSize(50))
                    .build();

            MongoClient client = MongoClients.create(settings);


            datastore = Morphia.createDatastore(client);

            System.out.println("[Morphia] Conectado usando morphia-config.properties");
        }
        return datastore;
    }

}
