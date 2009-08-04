package com.paulhimes.skylon;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public interface ChatAction {
	public void received(ChatMessage message) throws SkypeException;

	public void sent(ChatMessage message) throws SkypeException;
}
