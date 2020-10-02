package lab6;

import java.util.concurrent.*;

public class MessageTest
{
	public static void main(String [] args)
	{
		MessageQueue newMessageQueue = new MessageQueue();
		Messenger newMessenger = new Messenger(newMessageQueue);
		Subscriber newSubscriber = new Subscriber(newMessageQueue);

		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(newMessenger);
		executor.execute(newSubscriber);
		Thread.yield();
		while (!executor.isTerminated()) {
			continue;
		}
		executor.shutdown();
	}
}
