package com.smurygin;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;


public class ReaderWLQueueMessages {

    public final static String JNDI_FACTORY="weblogic.jndi.WLInitialContextFactory";
    public final static String JMS_FACTORY="jms/Test-ConnectionFactory";
    public final static String QUEUE="jms/Test-Queue";
    public final static String USER_NAME="system";
    public final static String USER_PASSWORD="netcracker";
    public final static String URL="t3://devapp115co.netcracker.com:6330/";


    public static void main(String[] args) {
        Context jndiContext = null;

        System.out.println("started");

        //1. Create new JNDI context
        try {
            Hashtable properties = new Hashtable();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
            properties.put(Context.PROVIDER_URL, URL);
            properties.put(Context.SECURITY_PRINCIPAL, USER_NAME);
            properties.put(Context.SECURITY_CREDENTIALS, USER_PASSWORD);
            jndiContext = new InitialContext(properties);
            System.out.println("JNDI Context was initialized");
        } catch (NamingException e) {
            System.out.println("Could not create JNDI API context: " +
                    e.toString());
            System.exit(1);
        }

        // 2. Look up connection factory and queue.
        // If either doesn't exist - exit
        QueueConnectionFactory queueConnectionFactory = null;
        Queue queue = null;

        try {
            queueConnectionFactory =
                    (QueueConnectionFactory) jndiContext.lookup(JMS_FACTORY);
            queue = (Queue) jndiContext.lookup(QUEUE);
        } catch (Exception e) {
            System.out.println("JNDI API lookup failed: " + e.toString());
            e.printStackTrace();
            System.exit(1);
        }

        // 3. Create connection.
        // 4. Create session from connection; false means session is not transacted.
        // 5. Create receiver
        QueueConnection queueConnection = null;
        QueueReceiver queueReceiver  = null;
        QueueSession queueSession = null;
        TextMessage  message = null;

        try {
            queueConnection = queueConnectionFactory.createQueueConnection();
            queueSession = queueConnection.createQueueSession(false,
                    Session.AUTO_ACKNOWLEDGE);

            queueReceiver = queueSession.createReceiver(queue);
            queueConnection.start();

            System.out.println("Queue connection started");

            while (true) {
                Message m = queueReceiver.receive(1);
                if (m != null) {
                    if (m instanceof TextMessage) {
                        message = (TextMessage) m;
                        System.out.println("Reading message: " +
                                message.getText());
                    } else {
                        break;
                    }
                }
            }

        } catch (JMSException e) {
            System.out.println("Exception occurred: " +
                    e.toString());
            e.printStackTrace();

        } finally {
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException e) {}
        }
        }
        return;
     }
}
