package fr.niromash.partyblock.rmq;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RabbitURI {
    private String username;
    private String password;
    private String host;

    public RabbitURI(String uri) throws UnknownHostException {
        String _uri = uri.substring(7);

        String[] url = _uri.split("@");
        String[] auth = url[0].split(":");

        if (url.length == 2) {
            this.username = !auth[0].equals("") ? auth[0] : null;
            this.password = !auth[1].equals("") ? auth[1] : null;
            this.host = InetAddress.getByName(url[1]).getHostAddress();
        }else {
            this.host = InetAddress.getByName(_uri).getHostAddress();;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }
}