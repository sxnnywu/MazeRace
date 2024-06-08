/*
-- PROJECT HEADER --

Name: Sunny Wu
Due Date: May 26, 2024
Course: ICS3U1-04 - Mr. Fernandes
Title: MazeRace

Description: Mazerace is a comprehensive GUI application where users enter their initials, choose between Mario and Sonic, 
			 and use their arrow keys to race around a maze to collect all of the randomly placed coins before a 1-minute timer.
			 When they complete Level 1 within 10 seconds, they can move on to Level 2, featuring a harder maze to navigate.
			 Top 5 scores are recorded in the scores.txt file and displayed on a leaderboard. The #1 score is recorded in the
			 top score.txt file and also displayed on the leaderboard.

Major Skills:
* Java Swing GUI
* Arrays 
* Arraylists
* Object-oriented programming
* Reading + writing to files
* Playing sound

Added Features:
* Get player image to face the proper direction as they move
* Accurate timing (tenths of seconds)
* High score recorded at the top
* Menubar with options to go home + see leaderboard
* Seperate opening screen
* Seperate screen for users to choose their character (Mario or Sonic)
* Music for opening screen and both maze levels & sound effects when user collects coins
* Sound effects when users win and lose
* Second level with a harder maze
* Initials recorded in top score.txt for the highest scorer
* Initials and scores of top 5 players recorded in scores.txt
* User object created to store initials and scores

Areas of Concern / Features I'd add:
* If user presses x on the pop-up message asking for their initials, the next screen still shows up
* Add background image
* More levels
* Create leaderboard for more levels
* More characters
* Power ups & portals
* 2-player options
* More testing of different scenarios in general
*/

// Define the class
public class MazeRaceApplication {
	
//	Main method
	public static void main(String[] args) {
		
		new MazeRaceGUI();
	}
} // End of class
