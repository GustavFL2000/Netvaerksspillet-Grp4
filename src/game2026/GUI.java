package game2026;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

public class GUI extends Application {

	public static final int size = 20; 
	public static final int scene_height = size * 20 + 100;
	public static final int scene_width = size * 20 + 200;

	public static Image image_floor;
	public static Image image_wall;
	public static Image hero_right,hero_left,hero_up,hero_down;

	public static Player me;
	public static List<Player> players = new ArrayList<Player>();

	Client client; //Dfinere en client

	private Label[][] fields;
	private TextArea scoreList;
	
	private  String[] board = {    // 20x20
			"wwwwwwwwwwwwwwwwwwww",
			"w        ww        w",
			"w w  w  www w  w  ww",
			"w w  w   ww w  w  ww",
			"w  w               w",
			"w w w w w w w  w  ww",
			"w w     www w  w  ww",
			"w w     w w w  w  ww",
			"w   w w  w  w  w   w",
			"w     w  w  w  w   w",
			"w ww ww        w  ww",
			"w  w w    w    w  ww",
			"w        ww w  w  ww",
			"w         w w  w  ww",
			"w        w     w  ww",
			"w  w              ww",
			"w  w www  w w  ww ww",
			"w w      ww w     ww",
			"w   w   ww  w      w",
			"wwwwwwwwwwwwwwwwwwww"
	};

    public GUI() throws IOException {
		client = new Client(this); //Opretter client når vi opretter gui
    }


    // -------------------------------------------
	// | Maze: (0,0)              | Score: (1,0) |
	// |-----------------------------------------|
	// | boardGrid (0,1)          | scorelist    |
	// |                          | (1,1)        |
	// -------------------------------------------

	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(0, 10, 0, 10));

			Text mazeLabel = new Text("Maze:");
			mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	
			Text scoreLabel = new Text("Score:");
			scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			scoreList = new TextArea();
			
			GridPane boardGrid = new GridPane();

			image_wall  = new Image(getClass().getResourceAsStream("Image/wall4.png"),size,size,false,false);
			image_floor = new Image(getClass().getResourceAsStream("Image/floor1.png"),size,size,false,false);

			hero_right  = new Image(getClass().getResourceAsStream("Image/heroRight.png"),size,size,false,false);
			hero_left   = new Image(getClass().getResourceAsStream("Image/heroLeft.png"),size,size,false,false);
			hero_up     = new Image(getClass().getResourceAsStream("Image/heroUp.png"),size,size,false,false);
			hero_down   = new Image(getClass().getResourceAsStream("Image/heroDown.png"),size,size,false,false);

			fields = new Label[20][20];
			for (int j=0; j<20; j++) {
				for (int i=0; i<20; i++) {
					switch (board[j].charAt(i)) {
					case 'w':
						fields[i][j] = new Label("", new ImageView(image_wall));
						break;
					case ' ':					
						fields[i][j] = new Label("", new ImageView(image_floor));
						break;
					default: throw new Exception("Illegal field value: "+board[j].charAt(i) );
					}
					boardGrid.add(fields[i][j], i, j);
				}
			}
			scoreList.setEditable(false);
			
			
			grid.add(mazeLabel,  0, 0); 
			grid.add(scoreLabel, 1, 0); 
			grid.add(boardGrid,  0, 1);
			grid.add(scoreList,  1, 1);
						
			Scene scene = new Scene(grid,scene_width,scene_height);
			primaryStage.setScene(scene);
			primaryStage.show();

			scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				switch (event.getCode()) {
				case UP:
                    try {
                        playerMoved(0,-1,"up");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
				case DOWN:
                    try {
                        playerMoved(0,+1,"down");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
				case LEFT:
                    try {
                        playerMoved(-1,0,"left");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
				case RIGHT:
                    try {
                        playerMoved(+1,0,"right");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
				default: break;
				}
			});

			// Setting up standard player
			int[] position = getRandomPosition();
			me = addPlayer("Gustav", position[0], position[1], "up");

			scoreList.setText(getScoreList());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Player addPlayer(String navn, int x, int y, String direction) {
		Player player = new Player(navn, x, y, direction);
		players.add(player);

		if (direction.equalsIgnoreCase("right")) {
			fields[x][y].setGraphic(new ImageView(hero_right));
		}
		if (direction.equalsIgnoreCase("left")) {
			fields[x][y].setGraphic(new ImageView(hero_left));
		}
		if (direction.equalsIgnoreCase("up")) {
			fields[x][y].setGraphic(new ImageView(hero_up));
		}
		if (direction.equalsIgnoreCase("down")) {
			fields[x][y].setGraphic(new ImageView(hero_down));
		}

		return player;
	}
	public void playerMoved(int delta_x, int delta_y, String direction) throws IOException {
		//Vi sender en message om at player er rykket gennem client
		me.direction = direction;
		int x = me.getXpos(),y = me.getYpos();

		if (board[y+delta_y].charAt(x+delta_x)=='w') {
			me.addPoints(-1);
			client.pointMessage(me.name,me.point);
		} 
		else {
			Player p = getPlayerAt(x+delta_x,y+delta_y);
			if (p!=null) {
              me.addPoints(10);
			  client.pointMessage(me.name,me.point);
              p.addPoints(-10);
			  client.pointMessage(p.name,p.point);
			} else {
				me.addPoints(1);
				client.pointMessage(me.name,me.point);
			
				fields[x][y].setGraphic(new ImageView(image_floor));
				x+=delta_x;
				y+=delta_y;

				if (direction.equals("right")) {
					fields[x][y].setGraphic(new ImageView(hero_right));
					client.movedMessage(x,y,"RIGHT", me.name,delta_x,delta_y);
				};
				if (direction.equals("left")) {
					fields[x][y].setGraphic(new ImageView(hero_left));
					client.movedMessage(x,y,"LEFT", me.name,delta_x,delta_y);
				};
				if (direction.equals("up")) {
					fields[x][y].setGraphic(new ImageView(hero_up));
					client.movedMessage(x,y,"UP",me.name,delta_x,delta_y);
				};
				if (direction.equals("down")) {
					fields[x][y].setGraphic(new ImageView(hero_down));
					client.movedMessage(x,y,"DOWN", me.name,delta_x,delta_y);
				};

				me.setXpos(x);
				me.setYpos(y);

			}
		}
		scoreList.setText(getScoreList());
	}

	public void moveOtherPlayer(int delta_x, int delta_y, String direction, String navn, int x, int y) {
		for (Player player : players) {
			if(player.name.equals(navn)){
				int oldX = player.getXpos();
				int oldY = player.getYpos();

				fields[oldX][oldY].setGraphic(new ImageView(image_floor));

				if (direction.equalsIgnoreCase("right")) {
					fields[x][y].setGraphic(new ImageView(hero_right));
				}
				if (direction.equalsIgnoreCase("left")) {
					fields[x][y].setGraphic(new ImageView(hero_left));
				}
				if (direction.equalsIgnoreCase("up")) {
					fields[x][y].setGraphic(new ImageView(hero_up));
				}
				if (direction.equalsIgnoreCase("down")) {
					fields[x][y].setGraphic(new ImageView(hero_down));
				}

				player.setXpos(x);
				player.setYpos(y);


			}
		}

		// Spilleren findes ikke endnu
		addPlayer(navn, x, y, direction);
	}
	public String getScoreList() {
		StringBuffer b = new StringBuffer(100);
		for (Player p : players) {
			b.append(p+"\r\n");
		}
		return b.toString();
	}

	public int[] getRandomPosition() {
		int x;
		int y;

		do {
			x = (int) (Math.random() * 20);
			y = (int) (Math.random() * 20);
		} while (board[y].charAt(x) == 'w' || getPlayerAt(x, y) != null);

		return new int[]{x, y};
	}

	public Player getPlayerAt(int x, int y) {
		for (Player p : players) {
			if (p.getXpos()==x && p.getYpos()==y) {
				return p;
			}
		}
		return null;
	}

	
}

