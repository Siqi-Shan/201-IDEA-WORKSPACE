package lab6;

import java.util.concurrent.*;

public class Messenger extends Thread
{
    private MessageQueue MQ;

    public Messenger(MessageQueue MQ) {
        this.MQ = MQ;
    }

    public void run() {
        for (int i = 0; i < 20; i++) {
            String toInsert = "message #" + i;
            this.MQ.addMessage(toInsert);

            Util.print(Util.getDate() + " Messenger - insert " + "\"" + toInsert + "\"");

            try {
                Thread.sleep(ThreadLocalRandom.current().nextLong(0,1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
