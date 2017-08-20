var client = null;

// messages defined in websocket config
client = Stomp.over(new SockJS('/register'));
client.connect({}, function (frame) {
    var subscribeTo = '/index';
    client.subscribe(subscribeTo, function (response) {
        displayTimer(JSON.parse(response.body));
    });
});

function displayTimer(timer) {
    var paragraph = document.createElement("p");
    var textNode = document.createTextNode("No information available");
    var timeFrom = getTimeFrom(timer.spawnStart);
    if (timeFrom.includes("-")) {
        textNode = document.createTextNode("(" + timer.name + ") Start time has passed! Estimated: " + timer.spawnEstimated);
    } else {
        textNode = document.createTextNode("(" + timer.name + ") Starting in:" + timeFrom + " Estimated: " + timer.spawnEstimated);
    }
    paragraph.appendChild(textNode);
    var children = document.getElementById("timers").children;
    for (var i = 0; i < children.length; i++) {
        var tableChild = children[i];
        if (tableChild.innerHTML.includes(timer.name)) {
            document.getElementById("timers").children[i] = paragraph;
            return;
        }
    }
    document.getElementById("timers").insertBefore(paragraph, document.getElementById("timers").firstChild);
}

function getTimeFrom(time) {
    var now = new Date();
    var hour = /(1?\d):/g.exec(time)[1];
    var minute = /(\d\d)$/g.exec(time)[1];
    var hourDifference = hour - now.getHours();
    if (hourDifference < -8) {
        hourDifference += 24;
    }
    var minuteDifference = minute - now.getMinutes();
    if (minuteDifference < 0) {
        minuteDifference += 60;
        hourDifference -= 1;
    }

    return hourDifference + ":" + minuteDifference;
}

// does not work in opera :/ -- this is also triggered
// on some old IEs when clicking anchor links
window.onbeforeunload = function () {
    client.disconnect();
    client.close();
};