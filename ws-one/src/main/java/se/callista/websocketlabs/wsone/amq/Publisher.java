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

	public Publisher() throws JMSException {
		this(DEFAULT_AMQ_URL, DEFAULT_AMQ_NOTIFY_TOPIC);
	}

	public Publisher(String activeMqUrl, String topicName) throws JMSException {
		super(activeMqUrl, topicName);
		producer = getSession().createProducer(getDestination());
	}

	public void publish(String message) throws JMSException {
		publish(message, DeliveryMode.NON_PERSISTENT);
	}

	public void publish(String message, int deliveryMode) throws JMSException {
		TextMessage textMessage = getSession().createTextMessage(message);
		textMessage.setJMSDeliveryMode(deliveryMode);
		producer.send(textMessage);
		LOG.debug("Message sent to subscribers: '{}'", message);
	}

	public void close() throws JMSException {
		producer.close();
		super.close();
	}
}
