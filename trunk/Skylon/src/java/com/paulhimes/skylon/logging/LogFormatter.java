package com.paulhimes.skylon.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mma");

	@Override
	public String format(LogRecord record) {
		StringBuilder sb = new StringBuilder();
		sb.append(dateFormat.format(new Date(record.getMillis())));
		sb.append(" | ");
		sb.append(record.getLevel());
		sb.append(" | ");
		sb.append(record.getLoggerName());
		sb.append(" | ");
		sb.append(record.getMessage());
		sb.append("\n");
		return sb.toString();
	}
}
