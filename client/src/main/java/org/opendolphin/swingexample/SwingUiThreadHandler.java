package org.opendolphin.swingexample;

import org.opendolphin.core.client.comm.UiThreadHandler;

import java.awt.*;

public class SwingUiThreadHandler implements UiThreadHandler {
	@Override
	public void executeInsideUiThread(final Runnable runnable) {
		EventQueue.invokeLater(runnable);
	}
}
