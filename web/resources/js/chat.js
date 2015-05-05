/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var socket = new WebSocket("ws://192.168.57.98:37198/socket/chat");
socket.onmessage = onMessage;

function onMessage(event) {
    var messageObj = JSON.parse(event.data);
    console.log(messageObj);
    
    var paragraph = document.createElement("p");
    paragraph.appendChild(document.createTextNode(messageObj.message));
    document.getElementById("messageArea").appendChild(paragraph);
}

/**
 * Submits the form
 */
function formSubmit() {
    var form = document.getElementById("chatForm");
    var message = form.elements["chatMessage"].value;
    document.getElementById("chatForm").reset();
    sendMessage(message);
}

/**
 * Sends a message via websocket
 * 
 * @param message
 */
function sendMessage(message) {
    var MessageAction = {
        message: message
    };
    socket.send(JSON.stringify(MessageAction));
}