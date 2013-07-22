function StompPush (url, topic, displayMessage, displayLog) {

	var client;
    
	// this allows to display logs directly on the web page
    var log = function(str) {
    	displayLog("STOMP-LOG: " + str);
    };  
    var logError = function(str) {
    	displayLog("STOMP-ERROR: " + str);
    };  

    // the client is notified when it is connected to the server.
    var onconnect = function(frame) {
      log("Connected to Stomp, subscribe to " + topic)

      client.subscribe(topic, function(message) {
    	  displayMessage(message.body);
      });
    };

	log("StompPush connects to: " + url);
    client = Stomp.client(url);
    
    client.debug = function(str) {
//    	log(str);
    };

    client.connect("", "", onconnect);
}