package se.callista.websocketlabs.wsone.amq;

import static se.callista.websocketlabs.wsone.server.Constants.*;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Subscriber extends ActiveMQParent {

	private static final Logger LOG = LoggerFactory.getLogger(Subscriber.class);
	
	MessageConsumer subscriber = null;

	// Create a listener to process each received message
	static MessageListener defaultListener = new MessageListener() {
		public void onMessage(Message message) {
			try {
				TextMessage textMessage = (TextMessage) message;
				LOG.debug(
					"WS-One: Message received from : '{}', message: '{}', JMSDeliveryMode: {}",
					new Object[] {message.getJMSDestination(), textMessage.getText() , message.getJMSDeliveryMode()});
			} catch (JMSException je) {
				LOG.error(je.getMessage());
			}
 		}
	};
	
	public Subscriber() throws JMSException {
		this(DEFAULT_AMQ_URL, DEFAULT_AMQ_NOTIFY_TOPIC, defaultListener);
	}

	public Subscriber(String activeMqUrl, String topicName, MessageListener listener) throws JMSException {
		super(activeMqUrl, topicName);
		subscriber = getSession().createConsumer(getDestination());
		subscriber.setMessageListener(listener);		
	}

	public void close() throws JMSException {
		subscriber.close();
		super.close();
	}
	
}
