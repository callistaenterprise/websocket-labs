package se.callista.websocketlabs.wsone.amq;

import static se.callista.websocketlabs.wsone.server.Constants.*;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ActiveMQParent {

	private static final Logger LOG = LoggerFactory.getLogger(ActiveMQParent.class);
    public static void runActiveMQBroker() throws Exception {
    	BrokerService broker = new BrokerService();

    	// configure the broker
    	broker.addConnector(DEFAULT_AMQ_WS_URL);

    	long storeLimit = 1024*1024*1024; // 1 GB for store and temp usage should be ok for these tests...
    	broker.getSystemUsage().getStoreUsage().setLimit(storeLimit);
    	broker.getSystemUsage().getTempUsage().setLimit(storeLimit);
    	LOG.info("WS-One: ActiveMQ StoreUsage set to: " + broker.getSystemUsage().getStoreUsage().getLimit() + " bytes.");
    	LOG.info("WS-One: ActiveMQ TempUsage set to: " + broker.getSystemUsage().getTempUsage().getLimit() + " bytes.");

    	// start the broker
    	broker.start();
        LOG.info("WS-One: ActiveMQ started with connector-urls: " + DEFAULT_AMQ_URL + ", " + DEFAULT_AMQ_WS_URL);
    }
    
	private ConnectionFactory connectionFactory = null;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;

	public ActiveMQParent(String activeMqUrl, String topicName) throws JMSException {
		connectionFactory = new ActiveMQConnectionFactory(activeMqUrl);
		connection = connectionFactory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createTopic(topicName);
	}
	
	public Session getSession() {
		return session;
	}
	
	public Destination getDestination() {
		return destination;
	}

	public void close() throws JMSException {
		connection.stop();
		connection.close();
	}

}
