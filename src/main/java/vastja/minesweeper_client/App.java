package vastja.minesweeper_client;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vastja.minesweeper_client.controllers.GameController;
import vastja.minesweeper_client.controllers.IController;
import vastja.minesweeper_client.utils.Client;
import javafx.fxml.FXMLLoader;
import javafx.application.Platform;

public class App extends Application {
	
	private static final String MAIN_MENU_FXML = "/vastja/minesweeper_client/resources/fxml/MainMenu.fxml";
	private static final String GAME_FXML = "/vastja/minesweeper_client/resources/fxml/Game.fxml";
	private static final String DISCON_FXML = "/vastja/minesweeper_client/resources/fxml/Disconnected.fxml";
	private static final String REC_REF_FXML = "/vastja/minesweeper_client/resources/fxml/ReconnectRefused.fxml";
	
	private static Stage primaryStage;
	
	private static GameController gameController;
	private static IController controller;
	
	private static boolean inGame = false;
	
	private static boolean reconnectToGame = false;
	private static int gameId = -1;
	private static String gameCode = null;
	
	@Override
	public void start(Stage stage) throws IOException {
		
		primaryStage = stage;
		primaryStage.setTitle("Minesweeper");

		Client client = Client.getConnection();
		Thread clientThread = new Thread(client);
		clientThread.setDaemon(true);
		clientThread.start();
		
		if (reconnectToGame) {
			game(gameCode, gameId);
			gameController.reconnected();
		}
		else {
			mainMenu();
		}
		
	}
	
	public static void main(String[] args) {
		
		try {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("--help") || args[0].equalsIgnoreCase("-h")) {
					System.out.println("USAGE: Minesweeper_client.jar <ip-adress> <port> [gameId] [gameCode]");
				}
				else {
					System.out.println("USAGE: Minesweeper_client.jar <ip-adress> <port> [gameId] [gameCode]");
				}
				exitApp();
			}
			else if (args.length == 2) {
				Client.setIpAdress(args[0]);
				Client.setPort(Integer.valueOf(args[1]));
				launch(args);
			}
			else if (args.length == 4) {
				Client.setIpAdress(args[0]);
				Client.setPort(Integer.valueOf(args[1]));
				gameId = Integer.valueOf(args[2]);
				gameCode = args[3];
				reconnectToGame = true;
				launch(args);
			}
			else {
				System.out.println("USAGE: Minesweeper_client.jar <ip-adress> <port> [gameId] [gameCode]");
				exitApp();
			}
		}
		catch (Exception e) {
			System.err.println("Error during application start.");
			System.out.println("USAGE: Minesweeper_client.jar <ip-adress> <port> [gameId] [gameCode]");
			exitApp();
		}
		
	}
	
	public static void mainMenu() {
		
		try {
			gameController = null;
			FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(MAIN_MENU_FXML));
			Parent mainMenu = fxmlLoader.load();
			controller = fxmlLoader.getController();
			
			Scene scene = new Scene(mainMenu, 900, 600);
	
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void game(String gameCode, int id) {
		
		try {
			
			FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(GAME_FXML));
			Parent game = fxmlLoader.load();
			gameController = (GameController) fxmlLoader.getController();
			controller = fxmlLoader.getController();
			
			gameController.setGameCode(gameCode);
			gameController.setGameId(id);
			
			gameController.refreshGamePropertieslabels();
			
			Scene scene = new Scene(game, 900, 600);

			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void disconnected() {
		
		try {
			
			Client.getConnection().disconnect();
			
			gameController = null;
			
			FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(DISCON_FXML));
			Parent disconnected = fxmlLoader.load();
			controller = fxmlLoader.getController();
			
			Scene scene = new Scene(disconnected, 900, 600);

			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
public static void recRefused() {
		
		try {
			
			gameController = null;
			
			
			FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(REC_REF_FXML));
			Parent disconnected = fxmlLoader.load();
			controller = fxmlLoader.getController();
			
			Scene scene = new Scene(disconnected, 900, 600);

			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static GameController getGameController() {
		return gameController;
	}
	
	public static IController getController() {
		return controller;
	}
	
	public static boolean isInGame() {
		return inGame;
	};
	
	public static void setInGame(boolean ig) {
		inGame = ig;
	}
	
	public static void exitApp() {
		Client.getConnection().disconnect();
		Platform.exit();
	}
		
}
