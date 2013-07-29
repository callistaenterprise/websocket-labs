function Sock (url, displayMessage, displayLog) {

	var socket;

	// this allows to display logs directly on the web page
    var log = function(str) {
    	displayLog("WS-LOG: " + str);
    };  
    var logError = function(str) {
    	displayLog("WS-ERROR: " + str);
    };  

	log("Connects to WebSocket: " + url);

	socket = new WebSocket(url);
    socket.onopen = onopen;
    socket.onmessage = onmessage;
    socket.onclose = onclose;
    socket.onerror = onerror;

    function onopen(event) {
        log("Connected to WebSocket");
    }

    function onmessage(event) {
    	displayMessage(event.data);
    }
    function onclose(event) {
        log("Web Socket closed");
    }
    function onerror(event) {
        logError("ERROR: Web Socket problem: data = " + event.data + ", name = " + event.name + ", message = " + event.message);
    }

    this.send = function(event) {
    	log("Send Web Socket Request");
        event.preventDefault();
        if (window.WebSocket) {
            if (socket.readyState == WebSocket.OPEN) {
                socket.send(event.target.message.value);
            } else {
                alert("The socket is not open.");
            }
        }
    }
}