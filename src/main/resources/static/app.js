var stompClient = null;

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
    //var socket = new SockJS('/websocket');
    //stompClient = Stomp.over(socket);
    accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ik5UZG1aak00WkRrM05qWTBZemM1TW1abU9EZ3dNVEUzTVdZd05ERTVNV1JsWkRnNE56YzRaQT09In0.eyJhdWQiOiJodHRwOlwvXC9vcmcud3NvMi5hcGltZ3RcL2dhdGV3YXkiLCJzdWIiOiJhZG1pbkBjYXJib24uc3VwZXIiLCJhcHBsaWNhdGlvbiI6eyJvd25lciI6ImFkbWluIiwidGllciI6IlVubGltaXRlZCIsIm5hbWUiOiJNeVBsYWlyQXBwIiwiaWQiOjIsInV1aWQiOm51bGx9LCJzY29wZSI6ImFtX2FwcGxpY2F0aW9uX3Njb3BlIGRlZmF1bHQiLCJpc3MiOiJodHRwczpcL1wvamF2YWRldi5pbnRlc3lzLml0Ojk0NDNcL29hdXRoMlwvdG9rZW4iLCJ0aWVySW5mbyI6eyJCcm9uemUiOnsic3RvcE9uUXVvdGFSZWFjaCI6dHJ1ZSwic3Bpa2VBcnJlc3RMaW1pdCI6MCwic3Bpa2VBcnJlc3RVbml0IjpudWxsfSwiR29sZCI6eyJzdG9wT25RdW90YVJlYWNoIjp0cnVlLCJzcGlrZUFycmVzdExpbWl0IjowLCJzcGlrZUFycmVzdFVuaXQiOm51bGx9LCJVbmxpbWl0ZWQiOnsic3RvcE9uUXVvdGFSZWFjaCI6dHJ1ZSwic3Bpa2VBcnJlc3RMaW1pdCI6MCwic3Bpa2VBcnJlc3RVbml0IjpudWxsfX0sImtleXR5cGUiOiJQUk9EVUNUSU9OIiwic3Vic2NyaWJlZEFQSXMiOlt7InN1YnNjcmliZXJUZW5hbnREb21haW4iOiJjYXJib24uc3VwZXIiLCJuYW1lIjoiUGxhaXJVWEFwaSIsImNvbnRleHQiOiJcL3BsYWlyXC8wLjE4LjMiLCJwdWJsaXNoZXIiOiJhZG1pbiIsInZlcnNpb24iOiIwLjE4LjMiLCJzdWJzY3JpcHRpb25UaWVyIjoiQnJvbnplIn0seyJzdWJzY3JpYmVyVGVuYW50RG9tYWluIjoiY2FyYm9uLnN1cGVyIiwibmFtZSI6IlBsYWlyRGV2VXRpbHMiLCJjb250ZXh0IjoiXC9wbGFpci1kZXYiLCJwdWJsaXNoZXIiOiJhZG1pbiIsInZlcnNpb24iOiIxLjAuMCIsInN1YnNjcmlwdGlvblRpZXIiOiJHb2xkIn0seyJzdWJzY3JpYmVyVGVuYW50RG9tYWluIjoiY2FyYm9uLnN1cGVyIiwibmFtZSI6Ik15V2Vic29ja2V0QVBJIiwiY29udGV4dCI6Ilwvd3NvY2tldFwvMS4wLjAiLCJwdWJsaXNoZXIiOiJhZG1pbiIsInZlcnNpb24iOiIxLjAuMCIsInN1YnNjcmlwdGlvblRpZXIiOiJHb2xkIn0seyJzdWJzY3JpYmVyVGVuYW50RG9tYWluIjoiY2FyYm9uLnN1cGVyIiwibmFtZSI6IkNoYXQiLCJjb250ZXh0IjoiXC9jaGF0XC8xLjAuMCIsInB1Ymxpc2hlciI6ImFkbWluIiwidmVyc2lvbiI6IjEuMC4wIiwic3Vic2NyaXB0aW9uVGllciI6IlVubGltaXRlZCJ9XSwiY29uc3VtZXJLZXkiOiIwaFp2ZU9kbHVTcTN2bHZ1TTZvWTd5OHBjem9hIiwiZXhwIjoxNTg2MjUyNzA2LCJpYXQiOjE1ODYyNDkxMDYsImp0aSI6IjVmY2E3MTVhLTdhOWYtNDJhZi05OWRmLWE4Y2I4NThmMzZiMSJ9.sED69SEfSietJb_dQOyruevzl100uqE9lc0qDbKAnP2ooyv8J2_xYPaY1IUwTUx7_0_OvucGaxFB_ycL8XO9-8PTwTf_aXllUcaHI8unyeF_XE1O8Rmqd_ETts-AZaVqjRu1dBHsV-unfcqTJZ74GNg8fGZB7ui94wS-RqfU18GrVultUgL3_pmTuTaG8Ih67e8ckPilIuLI_GvI-YgPXjK9WXwO1LZ4nhDFon8JS1IUTRy9QPhvHhrhFko4hdGKlFOgZD4ECfLAbJ8tCjxLj3nUNNynrlyXvpujE09u4TQT0V_pJR4RvubzK8PP-JnLp3GDKJQTVimKTASt8Qa3VA";
    stompClient = new Stomp.client('ws://localhost:9099/chat/1.0.0?access_token=' + accessToken);

    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/messages', function (greeting) {
            console.log("Received on /topic/messages - ", greeting);
            showGreeting(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/user/topic/errors', function (message) {
            alert("Error " + message.body);
        });

        stompClient.subscribe("/user/queue/errors", function(message) {
            alert("Error " + message.body);
        });

        stompClient.subscribe('/user/queue/chat', function (greeting) {
            console.log("Received on /user/queue/chat - ", greeting);
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
    $("#name").html("");
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function sendPrivateMessage() {
    stompClient.send("/app/chat/user-" + $( "#user").val(), {}, JSON.stringify({'name': $("#private-message").val()}));
    $("#user").html("");
    $("#private-message").html("");
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

