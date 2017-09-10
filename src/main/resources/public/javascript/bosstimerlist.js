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
        paragraph.appendChild(document.createElement("br"));
        paragraph.appendChild(document.createTextNode(textArray[i]));
    }
    var children = document.getElementById("timers").children;
    for (var i = 0; i < children.length; i++) {
        var tableChild = children[i];
        if (tableChild.innerHTML.includes(timer.name)) {
            var row = document.createElement("div");
            row.setAttribute("class", "col-6 col-lg-3");
            row.appendChild(paragraph);
            row.appendChild(createProgressBar(timer));
            document.getElementById("timers").replaceChild(row, tableChild);
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

function createProgressBar(timer) {
    var percent = makePercentagesFromTime(timer.lastSpawn, timer.spawnStart);
    var percentToEstimate = makePercentagesFromTime(timer.spawnStart, timer.spawnEstimated);
    var progress = document.createElement("div");
    progress.className = "progress";
    var progressBar = document.createElement("div");
    if (percent >= 99) {
        var progressToEstimate = document.createElement("div")
        progressToEstimate.setAttribute("class", "progress-bar bg-danger");
        progressToEstimate.setAttribute("role", "progressbar");
        progressToEstimate.setAttribute("aria-valuenow", percentToEstimate);
        progressToEstimate.setAttribute("aria-valuemin", "0");
        progressToEstimate.setAttribute("aria-valuemax", "100");
        progressToEstimate.setAttribute("style", "width: " + percentToEstimate + "%");
        progress.appendChild(progressToEstimate);

        progressBar.setAttribute("class", "progress-bar bg-warning");
    } else {
        progressBar.setAttribute("class", "progress-bar bg-success");
    }
    progressBar.setAttribute("role", "progressbar");
    progressBar.setAttribute("aria-valuenow", (percent - percentToEstimate));
    progressBar.setAttribute("aria-valuemin", "0");
    progressBar.setAttribute("aria-valuemax", "100");
    progressBar.setAttribute("style", "width: " + (percent - percentToEstimate) + "%");
    progress.appendChild(progressBar);
    return progress;
}

function makePercentagesFromTime(start, end) {
    var lastTime = new Date(start);
    var now = new Date();
    var coming = new Date(end);

    var diff = coming - lastTime;
    var atm = now - lastTime;
    return Math.max(0, Math.min(Math.floor((atm / diff) * 100), 100));
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

client.debug = function (str) {
};

// does not work in opera :/ -- this is also triggered
// on some old IEs when clicking anchor links
window.onbeforeunload = function () {
    client.disconnect();
    client.close();
};