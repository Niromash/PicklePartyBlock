package fr.niromash.partyblock.rmq;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class RabbitManager {

    private final String rabbitUri;
    private Connection connection;
    private Channel channel;
    private final String QUEUE_NAME = "PartyBlock";

    public RabbitManager(String rabbitUri) {
        this.rabbitUri = rabbitUri;
    }

    public void connect() throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException, IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(rabbitUri);
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void sendMessage(String routeName) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("routeName", routeName);
        channel.basicPublish("", QUEUE_NAME, null, new Gson().toJson(jsonObject).getBytes());
    }

    public void sendMessage(String routeName, List<HashMap<String, String>> object) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("routeName", routeName);
        for (HashMap<String, String> hashMap : object) hashMap.forEach(jsonObject::addProperty);
        channel.basicPublish("", QUEUE_NAME, null, new Gson().toJson(jsonObject).getBytes());
    }

    public void disconnect() throws IOException, TimeoutException {
        connection.close();
        channel.close();
    }
}
