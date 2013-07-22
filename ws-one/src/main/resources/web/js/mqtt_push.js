function MqttPush (host, port, topic, displayMessage, displayLog) {

	var client;
    
    // this allows to display logs directly on the web page
    var log = function(str) {
    	displayLog("MQTT-LOG: " + str);
    };  
    var logError = function(str) {
    	displayLog("MQTT-ERROR: " + str);
    };  

    // the client is notified when it is connected to the server.
    var onConnect = function(frame) {
    	log("Connected to MQTT, subscribe to " + topic);
        client.subscribe(topic);
    };  

	var onMessageArrived = function(message) {
		displayMessage(message.payloadString)
	};

	var onConnectionLost = function(responseObject) {
		if (responseObject.errorCode !== 0) {
			logError(client.clientId + ", Error code: " + responseObject.errorCode);
		} else {
			logError(client.clientId + ", Unknown Error");
		}
	};

    var onFailure = function(failure) {
    	logError("Failure: " + failure.errorMessage);
	};

	var clientId = generateClientId();
	
	log("MqttPush connects to: " + host + ":" + port + " (" + clientId + ")");

    client = new Messaging.Client(host, Number(port), clientId);

    client.onConnect = onConnect;
    client.onMessageArrived = onMessageArrived;
    client.onConnectionLost = onConnectionLost;            

    client.connect({onSuccess:onConnect, onFailure:onFailure}); 

    function generateClientId() {
	  return Math.floor((1 + Math.random()) * 0x10000000000).toString(16).substring(1);
	}
}