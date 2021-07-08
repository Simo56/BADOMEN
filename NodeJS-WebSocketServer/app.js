const serverPort = 8080;
const http = require("http");
const express = require("express");
const app = express();
const server = http.createServer(app);
const WebSocket = require("ws");
const websocketServer = new WebSocket.Server({ server });

//when a websocket connection is established
websocketServer.on("connection", (webSocketClient) => {
  //send feedback to the incoming connection
  webSocketClient.send('{ "connection" : "ok"}');

  //when a message is received
  webSocketClient.on("message", (message) => {
    console.log(message);
  });
});

app.get("/", (req, res) => {
  console.log("serving html file");
  res.sendFile(__dirname + "/index.html");
});

//start the web server
server.listen(serverPort, () => {
  console.log(`Websocket server started on port ` + serverPort);
});
