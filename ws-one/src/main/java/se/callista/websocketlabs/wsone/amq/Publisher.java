package se.callista.websocketlabs.wsone.amq;

import static se.callista.websocketlabs.wsone.server.Constants.*;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Publisher extends ActiveMQParent {

	private static final Logger LOG = LoggerFactory.getLogger(Publisher.class);

	MessageProducer producer = null;

	public Publisher() {
		this(DEFAULT_AMQ_URL, DEFAULT_AMQ_NOTIFY_TOPIC);
	}

	public Publisher(String activeMqUrl, String topicName) {
		super(activeMqUrl, topicName);
		try {
			producer = getSession().createProducer(getDestination());
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	public void publish(String message) {
		publish(message, DeliveryMode.NON_PERSISTENT);
	}

	public void publish(String message, int deliveryMode) {
		try {
			TextMessage textMessage = getSession().createTextMessage(message);
			textMessage.setJMSDeliveryMode(deliveryMode);
			producer.send(textMessage);
			LOG.debug("Message sent to subscribers: '{}'", message);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	public void close() {
		try {
			producer.close();
			super.close();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}
}
