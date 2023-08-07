# Remote Access Tool (RAT) - WebSocket Server

This repository contains a Remote Access Tool (RAT) implemented using Java and Spring WebSocket for handling connections from an infected APK. The tool allows you to remotely manage various functionalities on the infected device, such as handling messages, calls, and contacts.

## Functionality

The RAT provides the following main functionalities:

1. **Message Handling**: Collects and stores text messages received from the infected device.

2. **Call Handling**: Collects and stores call data including contact name, phone number, and call duration.

3. **Contact Handling**: Collects and stores contact information from the infected device.

4. **Camera Handling**: Supports handling camera data (currently not fully implemented).

## Getting Started

To set up and run the RAT server, follow these steps:

1. Clone the repository to your local machine:
```markdown
git clone https://github.com/your-username/rat-websocket-server.git
```

2. Make sure you have Java and Maven installed.

3. Navigate to the project directory:
```bash
cd rat-websocket-server
```

4. Build the project using Maven:
```markdown
mvn clean install
```

5. Run the RAT server:
```bash
java -jar target/rat-websocket-server.jar
```

6. The server should now be running and ready to handle WebSocket connections from the infected APK.

## Configuration

The RAT server creates and stores collected data in text files. By default, the data is stored in the `Files` directory in the project root. You can modify the file paths and storage behavior by editing the `WebSocketHandler` class.

## Usage

The RAT server listens for WebSocket connections from the infected APK. The APK should be configured to connect to the server's WebSocket endpoint. Once connected, the server will handle various commands and data sent by the APK.

## Disclaimer

This project is provided for educational and experimental purposes only. Unauthorized access to devices and systems is illegal. Please ensure that you have the necessary permissions before using or modifying this tool. The developers of this tool are not responsible for any misuse or unlawful activities.

## Contributing

Contributions to this project are welcome! If you find any bugs, have suggestions, or want to improve the code, feel free to open issues or submit pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Note**: This project is intended to raise awareness about cybersecurity and the importance of protecting devices from unauthorized access. It should not be used for any malicious purposes.

For questions or inquiries, please contact [Your Name](tosatto.simonepio@gmail.com).
