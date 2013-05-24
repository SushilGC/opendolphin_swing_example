package org.opendolphin.swingexample;

import org.opendolphin.core.comm.DefaultInMemoryConfig;

import javax.swing.*;
import java.awt.*;

public class ApplicationInMemoryStarter {

    public static void main(String[] args) throws Exception {
        final DefaultInMemoryConfig config = new DefaultInMemoryConfig();
        config.registerDefaultActions();
        config.getClientDolphin().getClientConnector().setUiThreadHandler(new SwingUiThreadHandler());
        registerApplicationActions(config);

		Runnable runnable = new Runnable() {
			public void run() {
				JFrame frame = new ApplicationFrame(config.getClientDolphin()).newComponent();

			}
		};
		EventQueue.invokeLater(runnable);
    }

    private static void registerApplicationActions(DefaultInMemoryConfig config) {
        config.getServerDolphin().register(new ApplicationDirector());
    }

}
