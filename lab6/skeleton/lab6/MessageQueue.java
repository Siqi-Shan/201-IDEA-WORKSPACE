package lab6;

import java.util.*;

public class MessageQueue
{
	private ArrayList<String> messages;

	public MessageQueue()
	{
		this.messages = new ArrayList<String>();
	}

	public void addMessage(String s)
	{
		this.messages.add(s);
	}

	public String getMessage()
	{
		if (this.messages.size() > 0)
		{
			return this.messages.remove(0);
		}
		return "";
	}
}
