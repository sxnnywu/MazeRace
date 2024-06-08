// IMPORTS ===============================================================

// Reading the maze text file
import java.io.*;
import java.nio.file.*;

// Arrays and arraylist functions, hashmap, scanner
import java.util.*;
import java.util.List;
import java.util.stream.*;

// Playing audios
import javax.sound.sampled.*;

//GUI elements
import javax.swing.*;
import javax.swing.Timer;

// Designing GUI elements
import java.awt.*;
import javax.swing.border.*;

// React to events and actions
import java.awt.event.*;
import javax.swing.event.*;

/*
Name: Sunny Wu
Due Date: May 26, 2024
Course: ICS3U1-04 - Mr. Fernandes
Description: This class creates a GUI frame for the MazeRace game
Major Skills: GUI, Arraylists, Reading and writing to files, playing sound 
*/

// Define the class
@SuppressWarnings("serial")
public class MazeRaceGUI extends JFrame implements ActionListener, KeyListener, MenuListener{
	
//	CREATE FIELDS =============================================================================

//	Constants -----------------------------------------------------------
	
//	Dimensions & sizing
	private final int CELL_SIZE = 25;
	private final int NUM_CELLS_WIDTH = 27;
	private final int NUM_CELLS_HEIGHT = 27; 	
	private final int NUM_COINS = 10; 
	
//	Images
	private final ImageIcon WALL = new ImageIcon("Images/red square.png");
	private final ImageIcon OUT_OF_BOUNDS = new ImageIcon("Images/black square.png");
	private final ImageIcon PATH = new ImageIcon("Images/grey square.png");
	private final ImageIcon COIN = new ImageIcon("Images/gold coin.gif");	
	private final ImageIcon[] MARIO = {new ImageIcon("Images/mario0.gif"), new ImageIcon("Images/mario1.gif"), 
			new ImageIcon("Images/mario2.gif"), new ImageIcon("Images/mario3.gif")};
	private final ImageIcon[] SONIC = {new ImageIcon("Images/sonic0.gif"), new ImageIcon("Images/sonic1.gif"), 
			new ImageIcon("Images/sonic2.gif"), new ImageIcon("Images/sonic3.gif")};
	
//	Audios
	private Map<File, Clip> audioClips = new HashMap<>(); // Keep up with the clips currently being played
	private final File COIN_FILE = new File("Audios/coin.wav");
	private final File THEME_FILE = new File("Audios/theme.wav");
	private final File GAME1_FILE = new File("Audios/game1.wav");
	private final File GAME2_FILE = new File("Audios/game2.wav");
	private final File VICTORY_FILE = new File("Audios/victory.wav");
	private final File TIME_OUT_FILE = new File("Audios/timeout.wav");
	
//	Start panel ---------------------------------------------------------
	private JPanel startPanel = new JPanel();
	
//	https://stackoverflow.com/questions/1090098/newline-in-jlabel to skip a line
//	https://stackoverflow.com/questions/6810581/how-to-center-the-text-in-a-jlabel
	private JLabel welcomeLabel = new JLabel("<html>Welcome to  <br/>MazeRace!<html>", SwingConstants.CENTER);	
	private JButton tutorialButton = new JButton("How the game works");
	private JButton playButton = new JButton("Play now");
	static String playerName;
	private static Clip startClip;
	
//	Character panel -----------------------------------------------------
	private JPanel characterPanel = new JPanel();
	
//	Description
	private JLabel characterLabel = new JLabel("<html>Choose your character <br/>to get started!<html>");
	
//	Mario
	private JButton marioButton = new JButton("Mario");
	ImageIcon unscaledMarioImage = new ImageIcon("Images/mario.png");
	ImageIcon scaledMarioImageIcon = new ImageIcon(unscaledMarioImage.getImage().getScaledInstance(130, 230, java.awt.Image.SCALE_SMOOTH));
	private JLabel marioImage = new JLabel(scaledMarioImageIcon);
	
//	Sonic
	private JButton sonicButton = new JButton("Sonic");
	ImageIcon unscaledSonicImage = new ImageIcon("Images/sonic.png");
	ImageIcon scaledSonicImageIcon = new ImageIcon(unscaledSonicImage.getImage().getScaledInstance(130, 230, java.awt.Image.SCALE_SMOOTH));
	private JLabel sonicImage = new JLabel(scaledSonicImageIcon);
	
//	Character chosen
	private String characterChoice;
	
//	Maze panel ------------------------------------------------------------
	private JPanel mazePanel = new JPanel();
	
	static int level = 1;
	private Cell[][] maze = new Cell[NUM_CELLS_WIDTH][NUM_CELLS_HEIGHT]; // 2-dimensional cell array
	private Player player = new Player(MARIO[1]); // Player
	
//	Scoreboard panel ------------------------------------------------------
	private JPanel scoreboardPanel = new JPanel();
	
//	Timer
	private double time = 6000; 
	private Timer gameTimer = new Timer(10, this); 
	private JLabel timerLabel = new JLabel("60.00");
	private static double currentTime = 60;
	private static double highTime1 = 60;
	private static double highTime2 = 60;
	
//	Coins
	private int numCoinsCollected = 0; 
	private JLabel scoreLabel = new JLabel("0"); 
	private JLabel highscoreLabel = new JLabel("0");
	
//	Leaderboard panel -----------------------------------------------------
	private JPanel leaderboardPanel = new JPanel();
	
//	Labels & text
	private JLabel leadersLabel = new JLabel("LEVEL 1 TOP SCORES");
	private JLabel topScoreLabel = new JLabel();
	static String topScoreLabelText;
	private JLabel dynamicScoresArrayLabel = new JLabel();
	
//	Array list of all scores
//	https://www.w3schools.com/java/java_arraylist.asp
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static ArrayList<User> dynamicScoresArray = new ArrayList();
			
//	Menu bar 
	private JMenuBar menuBar = new JMenuBar();
	private JMenu homeMenu = new JMenu("Home");
	private JMenu leaderboardMenu = new JMenu("Leaderboard");
	
//	CONSTRUCTOR METHOD =========================================================================
	public MazeRaceGUI() {
		
//		Call the methods
		startPanelSetup(); // Set up the start panel
		characterPanelSetup(); // Set up the character panel
		scoreboardPanelSetup(); // Set up the scoreboard panel
		mazePanelSetup(level); // Set up the first maze panel
		leaderboardPanelSetup(); // Set up the leaderboard panel
		frameSetup(); // Set up the frame		
	}

//	START PANEL ================================================================================
	private void startPanelSetup() {

//		Set up the panel
		startPanel.setOpaque(true);
		startPanel.setBackground(Color.BLACK);
		startPanel.setBounds(0, 0, 690, 770); // Same size as the frame
		startPanel.setLayout(null); // Allow us to manually place objects on the panel
		
//		Welcome label
		welcomeLabel.setForeground(Color.WHITE);
		welcomeLabel.setFont(new Font("Comic Sans", Font.BOLD, 70));
		welcomeLabel.setBounds(20,100,690,170);
		startPanel.add(welcomeLabel);
		
//		Tutorial button
		tutorialButton.setBackground(Color.RED);
		tutorialButton.setForeground(Color.WHITE);
		tutorialButton.setFont(new Font("Comic Sans", Font.BOLD, 25));
		tutorialButton.setBorder(new LineBorder(Color.WHITE)); // https://stackoverflow.com/questions/33954698/jbutton-change-default-border
		tutorialButton.setBounds(200, 450, 300, 70);
		tutorialButton.addActionListener(this);
		startPanel.add(tutorialButton);
		
//		Play button
		playButton.setBackground(Color.RED);
		playButton.setForeground(Color.WHITE);
		playButton.setFont(new Font("Comic Sans", Font.BOLD, 25));
		playButton.setBorder(new LineBorder(Color.WHITE)); // https://stackoverflow.com/questions/33954698/jbutton-change-default-border
		playButton.setBounds(200, 550, 300, 70);
		playButton.addActionListener(this);
		startPanel.add(playButton);
		
		playThemeAudio();
	}

//	PLAY THEME AUDIO ===========================================================================
	private void playThemeAudio() {
		
//		https://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
		
//			Create an input stream
			AudioInputStream audioIn;
			
			try {
				audioIn = AudioSystem.getAudioInputStream(THEME_FILE);	
				
//				Get clip from file and open input stream
				startClip = AudioSystem.getClip();
				startClip.open(audioIn);
		
//				Play the clip
				startClip.start();
				
//			If there is a file input error, display the error
			} catch (Exception error) {
	            error.printStackTrace();
			}
	}

//	CHARACTER PANEL ============================================================================
	private void characterPanelSetup() {
		
//		Set up the panel
		characterPanel.setOpaque(true);
		characterPanel.setBackground(Color.BLACK);
		characterPanel.setBounds(0, 0, 690, 770); // Same size as the frame
		characterPanel.setLayout(null); // Allow us to manually place objects on the panel
		
//		Character label
		characterLabel.setForeground(Color.WHITE);
		characterLabel.setFont(new Font("Comic Sans", Font.BOLD, 30));
		characterLabel.setBounds(100, 60, 400, 200);
		characterPanel.add(characterLabel);
		
//		Mario image
		marioImage.setBounds(50, 250, 200, 400);
		characterPanel.add(marioImage);
		
//		Mario button
		marioButton.setBackground(Color.RED);
		marioButton.setForeground(Color.WHITE);
		marioButton.setFont(new Font("Comic Sans", Font.BOLD, 25));
		marioButton.setBounds(100, 600, 100, 50);
		marioButton.addActionListener(this);
		characterPanel.add(marioButton);

//		Sonic image
		sonicImage.setBounds(350, 250, 200, 400);
		characterPanel.add(sonicImage);
		
//		Sonic button
		sonicButton.setBackground(Color.RED);
		sonicButton.setForeground(Color.WHITE);
		sonicButton.setFont(new Font("Comic Sans", Font.BOLD, 25));
		sonicButton.setBounds(400, 600, 100, 50);
		sonicButton.addActionListener(this);
		characterPanel.add(sonicButton);		
	}
	
//  SCOREBOARD PANEL ===========================================================================
	private void scoreboardPanelSetup() {

//		Set up the panel
		scoreboardPanel.setOpaque(true); 
		scoreboardPanel.setBackground(Color.BLACK);
		scoreboardPanel.setBounds(0,0, CELL_SIZE * NUM_CELLS_WIDTH, 100);
		scoreboardPanel.setLayout(null); // Allow us to manually place objects on the panel
	
//		Menu Bar
		homeMenu.setForeground(Color.WHITE);
		homeMenu.addMenuListener(this);
		leaderboardMenu.setForeground(Color.WHITE);
		leaderboardMenu.addMenuListener(this);
		menuBar.setBackground(Color.BLACK);	
		menuBar.add(homeMenu);
		menuBar.add(leaderboardMenu);
		setJMenuBar(menuBar);
	
//		Score label
		scoreLabel.setForeground(Color.WHITE);
		scoreLabel.setBounds(scoreboardPanel.getWidth()/2, 0, 100, 25);
		scoreboardPanel.add(scoreLabel);
	
//		Timer label
		timerLabel.setForeground(Color.WHITE);
		timerLabel.setBounds(scoreboardPanel.getWidth()/2, scoreboardPanel.getHeight()/4, 100, 25);
		scoreboardPanel.add(timerLabel);
		
//		High score label
		highscoreLabel.setForeground(Color.WHITE);
		highscoreLabel.setBounds(scoreboardPanel.getWidth()/2, scoreboardPanel.getHeight()/2, 100, 25);
		scoreboardPanel.add(highscoreLabel);
	}

//	MAZE PANEL =================================================================================
	private void mazePanelSetup(int level) {

//		Clear the panel
//		https://stackoverflow.com/questions/7117332/dynamically-remove-component-from-jpanel
		mazePanel.removeAll();
		
//		Reset the time and score
		time = 6000;
		timerLabel.setText("60.00");
		numCoinsCollected = 0;
		scoreLabel.setText("0");
		
//		Set up the panel
		mazePanel.setBounds(0, 100, CELL_SIZE * NUM_CELLS_WIDTH, CELL_SIZE * NUM_CELLS_HEIGHT); // Set bounds
		mazePanel.setLayout(new GridLayout(NUM_CELLS_WIDTH, NUM_CELLS_HEIGHT)); // Use a grid layout
		
//		Call methods
		loadMaze(level);
		placeCoins();
		placePlayer();
	}
	
//	PLAY GAME AUDIO ============================================================================
	private void playAudio(File fileName) {
		
//	https://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
	
	       try {
//	            Check if the Clip is already in the map
	            if (!audioClips.containsKey(fileName)) {
	            	
//	            	Create input stream 
	                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileName);
	                Clip clip = AudioSystem.getClip();	            
	                clip.open(audioInputStream);
	                
//	                Put the clip in the hashmap
	                audioClips.put(fileName, clip);
	            }
	            
	            Clip clip = audioClips.get(fileName); // Get the clip from the map            
	            clip.start(); // Play the audio
	            
//	        If an error occurs, print the error
	        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException error) {
	            error.printStackTrace();
	        }
	}
	
//	STOP GAME AUDIO ============================================================================
	private void stopAudio(File fileName) {
		
//	https://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
		
//	    Get the clip from the map
        Clip clip = audioClips.get(fileName);
        
//      If the clip is currently playing
        if (clip != null) {
        	
            clip.stop(); // Stop the clip
            clip.close();  // Close the clip 
            audioClips.remove(fileName); // Remove the clip from the hashmap
        }	
	}

// 	LOAD MAZE ==================================================================================
	private void loadMaze(int level) {
	
		int row = 0;
		char[] line;

		try {
		
//			Create a scanner to read the file
			Scanner inputFile = new Scanner(new File ("Maze/maze"+level+".txt")); 
		
//			Keep reading the file until no more data left to read
			while (inputFile.hasNext()) {
			
//				Convert all characters into an array of characters
				line = inputFile.nextLine().toCharArray();
				
//				Fill up each cell
				for (int column = 0; column < line.length; column++) {			
					fillCell(line[column], row, column);
				}
				
				row++; // Go to the next row
			}
		
//			Close the file
			inputFile.close();
		
//		If there is an error while reading the file, show the error
		} catch (FileNotFoundException error){
		System.out.println(error); 
		}
	}

//	PLACE COINS ================================================================================
	private void placeCoins() {
		
//		Go through each coin
		for (int coin = 1; coin <= NUM_COINS; coin++) {
			
			Cell cell = findEmptyCell(); // Find an empty cell 
			maze[cell.getRow()][cell.getCol()].setIcon(COIN); // Place the coin on the empty cell
		}
	}

//	FIND EMPTY CELL ============================================================================ 
	private Cell findEmptyCell() {
	
		Cell cell = new Cell();
		
		do {
			cell.setRow((int)(Math.random() * 24) + 2); // Random row
			cell.setCol((int)(Math.random() * 24) + 2); // Random column
			
		} while (maze[cell.getRow()][cell.getCol()].getIcon() != PATH);
		
		return cell;
	}

//  PLACE PLAYER ===============================================================================
	private void placePlayer() {
	
//		Find an empty cell
		Cell cell = findEmptyCell();
		
//		Place player on the empty cell
		player.setRow(cell.getRow());
		player.setCol(cell.getCol());
		
//		Add the player image to the empty cell
		maze[cell.getRow()][cell.getCol()].setIcon(player.getIcon());
	}

//	FILL CELL ==================================================================================
	private void fillCell(char character, int row, int column) {
	
//		Fill each row
		maze[row][column] = new Cell(row, column);
	
//		Wall 
		if (character == 'W')
			maze[row][column].setIcon(WALL);
	
//		Out of bounds
		else if (character == 'X')
			maze[row][column].setIcon(OUT_OF_BOUNDS);
	
//		Path
		else if (character == '.')
			maze[row][column].setIcon(PATH);
	
//		Add new cell to panel
		mazePanel.add(maze[row][column]);
	}

//	LEADER BOARD PANEL =========================================================================
	private void leaderboardPanelSetup() {
		
//		Set up the panel
		leaderboardPanel.setOpaque(true);
		leaderboardPanel.setBackground(Color.BLACK); // Background colour
		leaderboardPanel.setBounds(0, 0, 690, 770);  // Sizing 
		leaderboardPanel.setLayout(null);  // Allow us to manually position objects
		
//		Leaders label
		leadersLabel.setForeground(Color.WHITE);
		leadersLabel.setFont(new Font("Comic Sans", Font.BOLD, 40));
		leadersLabel.setBounds(100, 60, 500, 80);
		leaderboardPanel.add(leadersLabel);
		
//		Top scorer label
		topScoreLabel.setForeground(Color.WHITE);
		topScoreLabel.setFont(new Font("Comic Sans", Font.BOLD, 35));
		topScoreLabel.setBounds(100, 200, 200, 80);
		leaderboardPanel.add(topScoreLabel);
		
//		Top scores label
		dynamicScoresArrayLabel.setForeground(Color.WHITE);
		dynamicScoresArrayLabel.setFont(new Font("Comic Sans", Font.PLAIN, 30));
		dynamicScoresArrayLabel.setBounds(180, 330, 500, 400);
		leaderboardPanel.add(dynamicScoresArrayLabel);
	}
	
//	FRAME ======================================================================================
	private void frameSetup() {

//		Set up the frame
		setTitle("Sunny's MazeRace");
		setSize(mazePanel.getWidth() + 15, mazePanel.getHeight() + scoreboardPanel.getHeight() + 65); // Extra pixels for the border
		setLayout(null); // Allow us to manually place the objects on the frame
	
//		Add the panels to the frame
		add(startPanel);
		add(characterPanel);
		add(scoreboardPanel);
		add(mazePanel);
		add(leaderboardPanel);
		
//		Only show start panel
		startPanel.setVisible(true);
		characterPanel.setVisible(false);
		scoreboardPanel.setVisible(false);
		mazePanel.setVisible(false);
		leaderboardPanel.setVisible(false);
	
//		Activate the key listener
		setFocusable(true);
		addKeyListener(this);
		requestFocusInWindow();
		
//		Read scores.txt file to update the current top scorers
		loadScores();
		updateLeaderboard();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the program
		setResizable(false); // Don't let the size be altered
		setVisible(true); // Show the frame
	}
	
//	ACTION LISTENER METHOD =====================================================================
	@Override
	public void actionPerformed(ActionEvent event) {
		
//		If the user clicks the tutorial button
		if (event.getSource() == tutorialButton) {
			
//			Display a pop-up message explaining the rules of the game
//			https://www.geeksforgeeks.org/java-joptionpane/
			
			JOptionPane.showMessageDialog(null, "To win mazerace, use the 4 arrow keys to travel around the maze \n "
				+ "and collect all of the coins before the timer runs out!");
		}
		
//		If the user clicks the play button
		if (event.getSource() == playButton) {
			
//			Ask user for their initials
//			https://www.geeksforgeeks.org/java-joptionpane/
			
			playerName = JOptionPane.showInputDialog("Enter your initials:");
			
			startPanel.setVisible(false); // Close the start panel
			characterPanel.setVisible(true); // Display the character panel
		}
		
//		If the user clicks the mario button
		if (event.getSource() == marioButton) 
			setCharacter("mario", MARIO[1]);
		
//		If the user clicks the sonic button
		else if (event.getSource() == sonicButton) 
			setCharacter("sonic", SONIC[1]);
		
//		If the timer ticks 
		if (event.getSource() == gameTimer) {	
			
			time--; // Decrease time by 1
			timerLabel.setText(Double.toString(time/100)); // Reset timerLabel text
			
//			If time runs out before user collects all coins
			if (time == 0) 
				timeOut();
		}
	}
	
//	SET CHARACTER =============================================================================
	public void setCharacter(String character, ImageIcon image) {
		
//		Ask user if they are sure
//		https://www.geeksforgeeks.org/java-joptionpane/
		
		int choice = JOptionPane.showConfirmDialog(null, "Play "+character+"?", 
                character.toUpperCase()+" CHOSEN", JOptionPane.YES_NO_OPTION); 
		
		
//		If yes
		if (choice == JOptionPane.YES_OPTION) {
	
			characterChoice = character;
			player.setIcon(image);
			
//			Play game music
			if (level == 1)
				playAudio(GAME1_FILE);
			else
				playAudio(GAME2_FILE);
			
//			Close character panel and show maze panel + scoreboard panel
			mazePanelSetup(level);
			characterPanel.setVisible(false);
			startClip.stop();		
			mazePanel.setVisible(true);
			scoreboardPanel.setVisible(true);
			
//			Start the timer
			gameTimer.start();
		}
	}
	
//	TIME OUT ===================================================================================
	public void timeOut() {
		
		playAudio(TIME_OUT_FILE);
		
		int choice = JOptionPane.showConfirmDialog(this, "So close! Continue playing?", 
				"TIME OUT!", JOptionPane.YES_NO_OPTION);
		
//		If user chooses no, hide this panel and go to start panel
		if (choice == JOptionPane.NO_OPTION) {
			scoreboardPanel.setVisible(false);
			mazePanel.setVisible(false);
			startPanel.setVisible(true);
		}
		
//		If user chooses yes, reset the timer and coins collected and replace the coins and player
		else 
			resetGame(level);
	}
	
//	RESET GAME ================================================================================
	private void resetGame(int level) {
	
//		Reset time + coins
		timerLabel.setText("60.00");
		time = 6000;
		gameTimer.start();
		numCoinsCollected = 0;
		scoreLabel.setText("0");
		
//		Play music
		if (level == 1)
			playAudio(GAME1_FILE);
		else
			playAudio(GAME2_FILE);
		
//		Replace coins
		placeCoins();
		
//		Remove the player from its current location and set its new location
		maze[player.getRow()][player.getCol()].setIcon(PATH);
		placePlayer();
	}

//	KEY LISTENER METHOD ========================================================================
	@Override
	public void keyPressed(KeyEvent key) {
		
//		If the user presses up AND the cell above is a path, change player direction and move the player one cell up 
		if(key.getKeyCode() == KeyEvent.VK_UP && 
				maze[player.getRow() - 1][player.getCol()].getIcon() != WALL) {
	
			if (characterChoice == "mario")
				player.setIcon(MARIO[0]);
			else
				player.setIcon(SONIC[0]);
			movePlayer(-1,0, level); 
		}
		
//		If the user presses down AND the cell below is a path, change player direction and move the player one cell down 
		else if(key.getKeyCode() == KeyEvent.VK_DOWN && 
				maze[player.getRow() + 1][player.getCol()].getIcon() != WALL) {
			
			if (characterChoice == "mario")
				player.setIcon(MARIO[2]);
			else
				player.setIcon(SONIC[2]);
			movePlayer(1,0, level); 
		}
		
//		If the user presses left AND the cell left is a path, change player direction and move the player one cell left 
		else if(key.getKeyCode() == KeyEvent.VK_LEFT && 
				maze[player.getRow()][player.getCol() - 1].getIcon() != WALL) {
			
			if (characterChoice == "mario")
				player.setIcon(MARIO[3]);
			else
				player.setIcon(SONIC[3]);
			movePlayer(0,-1, level); 
		}
		
//		If the user presses right AND the cell right is a path, change player direction and move the player one cell right 
		else if(key.getKeyCode() == KeyEvent.VK_RIGHT && 
				maze[player.getRow()][player.getCol() + 1].getIcon() != WALL) {
			
			if (characterChoice == "mario")
				player.setIcon(MARIO[1]);
			else
				player.setIcon(SONIC[1]);
			movePlayer(0,1, level); 
		}
	}

//	MOVE PLAYER ================================================================================
	private void movePlayer(int dRow, int dColumn, int level) {
		
//		Remove the player from its current location
		maze[player.getRow()][player.getCol()].setIcon(PATH);
		
//		If the new location is on a coin, increase score by 1 and reset scoreLabel text
		if (maze[player.getRow() + dRow][player.getCol() + dColumn].getIcon() == COIN) {
			playAudio(COIN_FILE);
			numCoinsCollected++; 
			scoreLabel.setText(Integer.toString(numCoinsCollected));
		}
		
//		Add player to its new location
		player.move(dRow, dColumn);
		maze[player.getRow()][player.getCol()].setIcon(player.getIcon());
		
//		If player collects all the coins
		if (numCoinsCollected == NUM_COINS) {
			victoryAction();
			
//			If user beats the high time, display new high score
			if (level == 1 && currentTime < highTime1) {
				highTime1 = ((6000-time)/100);
				highscoreLabel.setText(Double.toString(highTime1));
			}
			else if (level == 2 && currentTime < highTime2) {
				highTime2 = ((6000-time)/100);
				highscoreLabel.setText(Double.toString(highTime2));
			}
		}
	}

//	VICTORY ====================================================================================
	private void victoryAction() {
		
//		Calculate user's time
		currentTime = ((6000-time)/100);
	
//		Stop the timer
		gameTimer.stop();
		
//		Play victory sound
		stopAudio(GAME1_FILE);
		playAudio(VICTORY_FILE);
		
//		Update scores
		storeScores();
		updateLeaderboard();
		
		int choice = JOptionPane.showConfirmDialog(this, "Your time: "+currentTime+" secs. Continue playing?", 
				"VICTORY!", JOptionPane.YES_NO_OPTION);
		
//		If user chooses no, hide this panel and go to start panel
		if (choice == JOptionPane.NO_OPTION) {
			scoreboardPanel.setVisible(false);
			mazePanel.setVisible(false);
			startPanel.setVisible(true);
		}
		
//		If user chooses yes
		else {
			
//			If the player takes less than 10 seconds, level up
			if (level == 1 && currentTime <= 10) 
				levelupAction();
			
//			Otherwise, restart the game
			else
				resetGame(level);
		}
	}
	
//	TOP 5 SCORES ===============================================================================
	private void storeScores() {
		
//		https://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it
		
//	    Specify the file path and name
	    Path filePath = Paths.get("Scores/scores.txt");

//	    Clear the file 
	    try {
	        Files.write(filePath, Collections.emptyList());
	        
//	    Print any errors that occur
	    } catch (IOException e) {
	        System.out.println("An error occurred while clearing the file.");
	        e.printStackTrace();
	    }
	    
//	    Sort the array list
	    updateScores();
	    
//		Store top score in a txt file
		storeTopScore();

//	    Write the top 5 scorers to the file
	    try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
	    	
	        for (int index = 0; index < 5; index++) {
	        	
	        	User user = dynamicScoresArray.get(index);
	        	
	            writer.write(user.getName() + ": " + user.getScore());
	            writer.newLine(); // Skip a line
	        }
	        
//	    Print any errors that occur
	    } catch (IOException e) {
	        System.out.println("An error occurred while writing to the file.");
	        e.printStackTrace();
	    }

	}
	
//	STORE TOP SCORE ============================================================================
	private void storeTopScore() {
		
//	    Specify the file path and name
	    Path filePath = Paths.get("Scores/top score.txt");
	    
//	    Write the top score to the file
	    try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.WRITE)) {
	    	
	    	writer.write(dynamicScoresArray.get(0).getName() + ": " + dynamicScoresArray.get(0).getScore());
	        
//	    Print any errors that occur
	    } catch (IOException e) {
	        System.out.println("An error occurred while writing to the file.");
	        e.printStackTrace();
	    }
		
	}
	
//	LOAD SCORES ================================================================================
	private void loadScores() {
		
//	https://sentry.io/answers/how-to-split-a-string-in-java/#:~:text=The%20easiest%20way%20to%20split,split()%20method.&text=In%20the%20example%20above%2C%20we,more%20%3A%20characters%20by%20using%20%3A%2B%20.
		
		try {
			
//			Create a scanner to read the file
			Scanner inputFile = new Scanner(new File ("Scores/scores.txt")); 
	
//			Keep reading the file until no more data left to read
			while (inputFile.hasNext()) {
			
//				Split each line into an array made of the user's initials and score
//				https://sentry.io/answers/how-to-split-a-string-in-java/#:~:text=The%20easiest%20way%20to%20split,split()%20method.&text=In%20the%20example%20above%2C%20we,more%20%3A%20characters%20by%20using%20%3A%2B%20.
				String scoreRecord[] = inputFile.nextLine().split(": ");
				
//				Create a user for the current line 
				User user = new User(scoreRecord[0], Double.valueOf(scoreRecord[1]));
				
//				Add this user to the full list of scores
				dynamicScoresArray.add(user);
			}
		
//			Close the file
			inputFile.close();
		
//		If there is an error while reading the file, show the error
		} catch (FileNotFoundException error){
		System.out.println(error); 
		}
	}
	
//	UPDATE SCORES ==============================================================================
	private void updateScores() {
		
		User user = new User(playerName, currentTime);
		boolean exists = false; // Whether the user's initials are already stored

//		Check each user in the arraylist to see if they are already stored
//		https://ioflood.com/blog/length-of-arraylist-java/#:~:text=The%20length%20of%20an%20ArrayList%20in%20Java%20can%20be%20found,elements%20present%20in%20the%20ArrayList.&text=In%20this%20example%2C%20we%20create,element%20'Hello'%20to%20it.
		for (int index = 0; index < dynamicScoresArray.size(); index++){ 
			
//			If the user is already stored in the arraylist
			if (dynamicScoresArray.get(index).getName().equals(user.getName())) {
				
//				If their new score is better than their previous score, store the new score
				if (user.getScore() < dynamicScoresArray.get(index).getScore()) {
					
					dynamicScoresArray.get(index).setScore(user.getScore());
					exists = true;
				}	
			}	
		}
		
//		If the user's initials are not already stored, add them to the arraylist
		if (!exists) 
			dynamicScoresArray.add(user);
		
//		Sort the array list based on times in descending order
//		https://stackoverflow.com/questions/16252269/how-to-sort-a-list-arraylist
		dynamicScoresArray.sort(Comparator.comparing(User::getScore));
		
	}
	
//	UPDATE LEADERBOARD =========================================================================
	private void updateLeaderboard() {
		
//		Top score label ----------------------------------------
		topScoreLabelText = String.format("%s: %.2f", dynamicScoresArray.get(0).getName(),
				dynamicScoresArray.get(0).getScore());
		topScoreLabel.setText(topScoreLabelText);
		
//		Top 5 scores table -------------------------------------
		
//	    Create a StringBuilder to build the leaderboard string using html
	    StringBuilder leaderboard = new StringBuilder();

//	    Table header
	    leaderboard.append("<html><table border='1'><tr><th>Rank</th><th>Initials</th><th>Score</th></tr>");

//	    Top 5 scorers
	    for (int index = 0; index < 5; index++) {
	    	
	        User user = dynamicScoresArray.get(index);
	        leaderboard.append("<tr><td>" + (index + 1) + "</td><td>" + user.getName() + "</td><td>" + user.getScore() + "</td></tr>");
	    }

//	    Table footer
	    leaderboard.append("</table></html>");

	    dynamicScoresArrayLabel.setText(leaderboard.toString());
	    
	    leaderboardPanel.setVisible(false);
	}
	
//	LEVEL UP ===================================================================================
	private void levelupAction() {
		
//		Stop the timer
		gameTimer.stop();
		
//		Show message
		JOptionPane.showMessageDialog(null, "Congratulations! You reached a time of "+currentTime+" seconds. "
				+ "Time to move on to Level 2!");
		
//		Reset coins
		numCoinsCollected = 0;
		scoreLabel.setText("0");
		highscoreLabel.setText(Double.toString(highTime2));

//		Reset timer
		time = 6000;
		timerLabel.setText(Double.toString(time/100));
		gameTimer.start();
		
//		Update level
		level++;
		
//		Change music
		stopAudio(GAME1_FILE);
		playAudio(GAME2_FILE);
		
//		Show the new maze
	    mazePanel.setVisible(false);
		mazePanelSetup(level);
		add(mazePanel);
		mazePanel.setVisible(true);
	}
	
//	Unused
	@Override
	public void keyTyped(KeyEvent e) {
	}
//	Unused
	@Override
	public void keyReleased(KeyEvent e) {
	}

//	MENU LISTENER METHOD =======================================================================
	@Override
	public void menuSelected(MenuEvent menu) {
		
//		If user selects quitMenu, close this frame and open startFrame
		if (menu.getSource() == homeMenu) {
			mazePanel.setVisible(false);
			scoreboardPanel.setVisible(false);
			stopAudio(GAME1_FILE);
			stopAudio(GAME2_FILE);
			
			startPanel.setVisible(true);
		}
	
//		If user selects leaderboardMenu, close all panels and display the leaderboard panel 
		if (menu.getSource() == leaderboardMenu) {
			
			startPanel.setVisible(false);
			characterPanel.setVisible(false);
			scoreboardPanel.setVisible(false);
			mazePanel.setVisible(false);
			leaderboardPanel.setVisible(true);
		}
	}
	
//	Unused
	@Override	
	public void menuDeselected(MenuEvent e) {
	}
//	Unused
	@Override	
	public void menuCanceled(MenuEvent e) {
		// TODO Auto-generated method stub
	}
	
} // End of class
