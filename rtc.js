
var recording = false;
var ws = new WebSocket("ws://127.0.0.1:8080");

ws.onopen = function () {
   console.log("Openened connection to websocket");
};

ws.onmessage = function(e) {
  $("#message").val($("#message").val() + e.data + "\n\n");
};

function callback(stream) {
    var context = new AudioContext();
    var mediaStreamSource = context.createMediaStreamSource(stream);
    rec = new Recorder(mediaStreamSource);
}

$(document).ready(function() {
    navigator.webkitGetUserMedia({audio:true}, callback, function(e) {
      alert('Error getting audio');
      console.log(e);
    });

    $('#record').click(function() {
      if (recording) {
        ws.send("Stopped Recording.");
        recording = false;
        rec.stop();
        rec.clear();
      } else {
        ws.send("Now Recording...");
        recording = true;
        rec.record();
      }
    });

    $('#clear').click(function() {
      $("#message").val("");
    });

    $('#stop').click(function() {
      recording = false;
      rec.stop();
      rec.clear();
      ws.send("STOP");
    });

});
