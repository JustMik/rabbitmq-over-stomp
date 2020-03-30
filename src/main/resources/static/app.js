var stompClient = null;
var group = '0';
var id = 1;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
        $("#user-chat").show();
    }
    else {
        $("#conversation").hide();
        $("#user-chat").hide();
    }
    $("#greetings").html("");
    $("#user-message").html("");
}

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });

        stompClient.subscribe("/user/queue/errors", function(message) {
            alert("Error " + message.body);
        });

        stompClient.subscribe('/user/queue/chat', function (greeting) {
            showPrivateMessage(JSON.parse(greeting.body).content);
        });


    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/messages", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function sendPrivateMessage() {
    stompClient.send("/app/chat/user-" + $( "#user").val(), {}, JSON.stringify({'name': $("#private-message").val()}));
}

function showPrivateMessage(message) {
    $("#user-message").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });

    $( "#send" ).click(function() { sendName(); });
    $( "#send-private-message").click(function () { sendPrivateMessage();})
});

