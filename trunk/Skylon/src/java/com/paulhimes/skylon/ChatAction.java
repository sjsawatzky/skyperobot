package com.paulhimes.skylon;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public interface ChatAction {
	public void received(ChatMessage message) throws SkypeException;

	public void sent(ChatMessage message) throws SkypeException;

	public void registerCallback(ChatListener chatListener);

	public Node encodeXml(Document document);
}
