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
    var textArray = createTimerText(timer);
    for (i = 0; i < textArray.length; i++) {
        paragraph.appendChild(document.createTextNode(textArray[i]));
        paragraph.appendChild(document.createElement("br"));
    }
    var children = document.getElementById("timers").children;
    for (var i = 0; i < children.length; i++) {
        var tableChild = children[i];
        if (tableChild.innerHTML.includes(timer.name)) {
            document.getElementById("timers").replaceChild(paragraph, tableChild);
            return;
        }
    }
    document.getElementById("timers").insertBefore(paragraph, document.getElementById("timers").firstChild);
}

function createTimerText(timer) {
    var timeFromStart = getTimeFrom(timer.spawnStart);
    var timeFromEstimate = getTimeFrom(timer.spawnEstimated);
    if (timeFromEstimate.includes("-")) {
        return ["(" + timer.name + ") ", "Both times have passed! Any moment now! (or maybe our server is just broken)"];
    } else if (timeFromStart.includes("-")) {
        return ["(" + timer.name + ") ", "Start time has passed! Estimated to arrive in: " + timeFromEstimate, "Estimate: " + new Date(timer.spawnEstimated).toTimeString()];
    } else {
        return ["(" + timer.name + ") ", "Starting in: " + timeFromStart + " Estimated to arrive in: " + timeFromEstimate, "Estimate: " + new Date(timer.spawnEstimated).toTimeString()];
    }
}

function getTimeFrom(time) {
    var now = new Date();
    var then = new Date(time);
    var diff = then - now;
    var seconds = diff / 1000;
    var minutes = seconds / 60;
    var hours = Math.floor(minutes / 60);

    return hours + ":" + Math.floor(minutes % 60);
}

// does not work in opera :/ -- this is also triggered
// on some old IEs when clicking anchor links
window.onbeforeunload = function () {
    client.disconnect();
    client.close();
};