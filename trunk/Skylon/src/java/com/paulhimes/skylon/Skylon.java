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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.paulhimes.skylon.tools.XmlTools;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class Skylon {

	private TrayIcon trayIcon;

	public Skylon() {

		trayIcon = createTrayIcon();

		NerveCenter nerveCenter = new NerveCenter(trayIcon);

		File actionsFile = new File(System.getProperty("user.dir"), "skylonProfile.xml");
		Actions actions = loadActions(actionsFile);
		if (actions == null) {
			// Create a new empty Actions object.
			actions = new Actions();
			writeNodeToFile(actions, actionsFile);
		}

		nerveCenter.addChatActions(actions.getChatActions());
	}

	private void writeNodeToFile(XmlModel model, File file) {
		// Create a new emply Actions file.
		Document document = null;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		Node node = model.encodeXml(document);

		document.appendChild(node);

		try {
			FileOutputStream out = new FileOutputStream(file);
			OutputFormat format = new OutputFormat(document);
			format.setIndenting(true);
			XMLSerializer output = new XMLSerializer(out, format);
			output.serialize(document);
			out.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private Actions loadActions(File file) {
		Actions actions = null;
		try {
			Element actionsElement = XmlTools.getElementFromFile(file, "actions");
			actions = Actions.parseXml(actionsElement);
		} catch (XmlParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return actions;
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
