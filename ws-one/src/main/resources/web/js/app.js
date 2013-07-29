(function() {
    var App = function() {
        var socket;
        

        if (!window.WebSocket) {
            window.WebSocket = window.MozWebSocket;
        }

        if (window.WebSocket) {
        	var hostname = location.hostname
        	socket = new Sock("ws://" + hostname + ":8080/websocket", appendWebSocketTextArea, appendDebugTextArea);
            new StompPush("ws://" + hostname + ":61614/stomp", "/topic/wsone.notify", appendStompNotificationTextArea, appendDebugTextArea);
            new MqttPush(hostname, "61614", "/wsone.notify", appendMqttNotificationTextArea, appendDebugTextArea);

        } else {
            alert("Your browser does not support Web Socket.");
        }

        function send(event) {
        	socket.send(event);
        }
        document.forms.inputform.addEventListener('submit', send, false);

        function appendWebSocketTextArea(newData) {
        	appendTextArea(newData, 'responseText');
        }

        function appendStompNotificationTextArea(newData) {
        	appendTextArea(newData, 'stompNotification');
        }

        function appendMqttNotificationTextArea(newData) {
        	appendTextArea(newData, 'mqttNotification');
        }

        function appendDebugTextArea(newData) {
        	appendTextArea(newData, 'debug');
        }

        function appendTextArea(newData, textAreaName) {
            var el = document.getElementById(textAreaName);
            el.value = getTs() + ": " + newData + '\n' + el.value;
        }

        function getTs() {
            
        	var ts = new Date();
            
            var h = ts.getHours();
            if (h < 10) h = '0' + h;
            
            var m = ts.getMinutes();
            if (m < 10) m = '0' + m;
            
            var s = ts.getSeconds();
            if (s < 10) s = '0' + s;

            var ms = ts.getMilliseconds();
            if (ms < 10) {
            	ms = '00' + ms;
            } else if (ms < 100) {
            	ms = '0' + ms;
            }
 
            return h + ":" + m + ":" + s + "." + ms;
        }
    }
    window.addEventListener('load', function() { new App(); }, false);
})();
