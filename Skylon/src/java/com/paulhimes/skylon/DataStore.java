package com.paulhimes.skylon;

import java.io.File;

import org.w3c.dom.Element;

import com.paulhimes.skylon.chatactions.ChatActions;
import com.paulhimes.skylon.logging.Logger;
import com.paulhimes.skylon.tools.XmlTools;
import com.paulhimes.skylon.xml.XmlParseException;

public class DataStore {

	private static final Logger logger = new Logger(DataStore.class);

	private static Actions actions;
	private static File saveFile;

	private DataStore() {

	}

	public static void loadActions(File file) {
		saveFile = file;

		try {
			Element actionsElement = XmlTools.getElementFromFile(saveFile, "actions");
			actions = Actions.parseXml(actionsElement);
		} catch (XmlParseException e) {
			logger.info("Failed to load actions from file: " + saveFile.getAbsolutePath());
		}

		// Create a new actions model and save to the given file, if no usable actions model was found.
		if (actions == null) {
			actions = new Actions();
			saveActions();
		}
	}

	public static void saveActions() {
		XmlTools.writeNodeToFile(actions, saveFile);
	}

	public static ChatActions getChatActions() {
		return actions.getChatActions();
	}
}
