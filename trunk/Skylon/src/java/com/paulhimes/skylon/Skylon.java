package com.paulhimes.skylon;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Skylon {

	private TrayIcon trayIcon;

	public Skylon() {

		trayIcon = createTrayIcon();

		NerveCenter nerveCenter = new NerveCenter();
		nerveCenter.addChatActions(ChatActionLoader.loadActions());
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
			return ImageIO.read(getClass().getResourceAsStream(
					"/com/paulhimes/skylon/images/skylon-16b.png"));
		} catch (Exception e) {
			// Image not found. Create a backup.
			return createBackupTrayImage();
		}
	}

	private Image createBackupTrayImage() {
		int imageSize = 16;

		BufferedImage backupImage = new BufferedImage(imageSize, imageSize,
				BufferedImage.TYPE_INT_ARGB);
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Skylon();
	}
}
