package org.opendolphin.swingexample;

import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientModelStore;
import org.opendolphin.core.client.comm.ClientConnector;
import org.opendolphin.core.client.comm.HttpClientConnector;
import org.opendolphin.core.comm.JsonCodec;

import javax.swing.*;
import java.awt.*;

public class ApplicationStarter {
    public static void main(String[] args) {
        final ClientDolphin clientDolphin = new ClientDolphin();
        clientDolphin.setClientModelStore(new ClientModelStore(clientDolphin));

        ClientConnector connector = createConnector(clientDolphin);
        connector.setUiThreadHandler(new SwingUiThreadHandler());
        clientDolphin.setClientConnector(connector);


		Runnable runnable = new Runnable() {
			public void run() {
				JFrame frame = new ApplicationFrame(clientDolphin).newComponent();

			}
		};
		EventQueue.invokeLater(runnable);
    }

    private static ClientConnector createConnector(ClientDolphin clientDolphin) {
        //running real client server mode.
        HttpClientConnector connector = new HttpClientConnector(clientDolphin, "http://localhost:8080/appContext/applicationServlet/");
        connector.setCodec(new JsonCodec());
        return connector;
    }

}
