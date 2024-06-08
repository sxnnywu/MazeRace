// IMPORTS =========================================
import javax.swing.ImageIcon;

/*
Name: Sunny Wu
Due Date: May 26, 2024
Course: ICS3U1-04 - Mr. Fernandes
Description: This class extends the cell object to create a player object
Major Skills: Object-oriented programming
*/

// Define the class
@SuppressWarnings("serial")
public class Player extends Cell {

//	CONSTRUCTOR METHOD =============================
	public Player(ImageIcon image) {		
		setIcon(image);
	}
	
//	MOVE THE PLAYER ================================
	public void move(int dRow, int dColumn) {		
		setRow(getRow() + dRow); // Add change in row
		setCol(getCol() + dColumn); // Add change in column
	}

} // End of class

