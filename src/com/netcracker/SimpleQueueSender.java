package com.netcracker;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class SimpleQueueSender {

    public final static String JNDI_FACTORY="weblogic.jndi.WLInitialContextFactory";
    public final static String JMS_FACTORY="jms/Test-ConnectionFactory";
    public final static String QUEUE="jms/Test-Queue";
    public final static String USER_NAME="system";
    public final static String USER_PASSWORD="netcracker";
    public final static String URL="t3://devapp115co.netcracker.com:6330/";


/*  for local weblogic server
    public final static String USER_NAME="weblogic";
    public final static String USER_PASSWORD="Welcome1";
    public final static String URL="t3://localhost:8001/?";
*/

    public static void main(String[] args) {
        String                  queueName = QUEUE;
        Context                 jndiContext = null;
        QueueConnectionFactory  queueConnectionFactory = null;
        QueueConnection         queueConnection = null;
        QueueSession            queueSession = null;
        Queue                   queue = null;
        QueueSender             queueSender = null;
        TextMessage             message = null;

        /*
         * Create a JNDI API InitialContext object if none exists
         * yet.
         */
            // create InitialContext
            Hashtable properties = new Hashtable();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
            // NOTE: The port number of the server is provided in the next line,
            //       followed by the userid and password on the next two lines.
            properties.put(Context.PROVIDER_URL, URL);
            properties.put(Context.SECURITY_PRINCIPAL, USER_NAME);
            properties.put(Context.SECURITY_CREDENTIALS, USER_PASSWORD);
        try {
            jndiContext = new InitialContext(properties);
        } catch (NamingException e) {
            System.out.println("Could not create JNDI API " +
                    "context: " + e.toString());
            System.exit(1);
        }

        /*
         * Look up connection factory and queue.  If either does
         * not exist, exit.
         */
        try {
            queueConnectionFactory = (QueueConnectionFactory)
                    jndiContext.lookup(JMS_FACTORY);
            queue = (Queue) jndiContext.lookup(queueName);
        } catch (NamingException e) {
            System.out.println("JNDI API lookup failed: " +
                    e.toString());
            System.exit(1);
        }

        /*
         * Create connection.
         * Create session from connection; false means session is
         * not transacted.
         * Create sender and text message.
         * Send messages, varying text slightly.
         * Send end-of-messages message.
         * Finally, close connection.
         */
        try {
            queueConnection =
                    queueConnectionFactory.createQueueConnection();
            queueSession =
                    queueConnection.createQueueSession(false,
                            Session.AUTO_ACKNOWLEDGE);
            queueSender = queueSession.createSender(queue);
            message = queueSession.createTextMessage();
            for (int i = 0; i < 1; i++) {
                message.setText("This is message " + (i + 1));
                System.out.println("Sending message: " +
                        message.getText());
                queueSender.send(message);
            }

            /*
             * Send a non-text control message indicating end of
             * messages.
             */
            queueSender.send(queueSession.createMessage());
        } catch (JMSException e) {
            System.out.println("Exception occurred: " +
                    e.toString());
        } finally {
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException e) {}
            }
        }
    }
}