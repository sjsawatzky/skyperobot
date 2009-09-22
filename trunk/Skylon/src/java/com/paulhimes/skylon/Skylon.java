package com.paulhimes.skylon;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.paulhimes.skylon.gui.EditActionsWindow;
import com.paulhimes.skylon.logging.Logger;

public class Skylon {

	@SuppressWarnings("unused")
	private Logger logger = new Logger(getClass());
	private TrayIcon trayIcon;

	public Skylon() {
		trayIcon = createTrayIcon();

		File actionsFile = new File(System.getProperty("user.dir"), "skylonProfile.xml");
		DataStore.loadActions(actionsFile);

		new NerveCenter(trayIcon);
	}

	private TrayIcon createTrayIcon() {
		if (SystemTray.isSupported()) {
			// get the SystemTray instance
			SystemTray tray = SystemTray.getSystemTray();

			TrayIcon trayIcon = new TrayIcon(createTrayImage(), "Skylon", createTrayMenu());

			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				return null;
			}

			return trayIcon;
		} else {
			return null;
		}
	}

	private Image createTrayImage() {
		try {
			return ImageIO.read(getClass().getResourceAsStream("/com/paulhimes/skylon/images/skylon-16.png"));
		} catch (Exception e) {
			// Image not found. Create a backup.
			return createBackupTrayImage();
		}
	}

	private Image createBackupTrayImage() {
		int imageSize = 16;

		BufferedImage backupImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = backupImage.createGraphics();
		g2.setPaint(Color.DARK_GRAY);
		g2.fillRect(0, 0, imageSize, imageSize);
		g2.setPaint(Color.RED);
		g2.fillOval(imageSize / 4, imageSize / 4, imageSize / 2, imageSize / 2);
		g2.dispose();
		return backupImage;
	}

	private PopupMenu createTrayMenu() {
		// create a menu
		PopupMenu popup = new PopupMenu();

		// Create EditActions item
		MenuItem editActions = new MenuItem("Edit Actions");
		editActions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editActions();
			}
		});
		popup.add(editActions);

		// Create quit item
		MenuItem quit = new MenuItem("Quit");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		popup.add(quit);

		return popup;
	}

	private void editActions() {
		new EditActionsWindow();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Skylon();
	}
}
