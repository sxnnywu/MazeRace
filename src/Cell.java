// IMPORTS ==========================================
import javax.swing.JLabel;
/*
Name: Sunny Wu
Due Date: May 26, 2024
Course: ICS3U1-04 - Mr. Fernandes
Description: This class creates a cell object
Major Skills: Object-oriented programming
*/

// Define the class
@SuppressWarnings("serial")
public class Cell extends JLabel {
	
//	Fields ===========================================
	private int row;
	private int col;
	
//	Empty constructor method =========================
	public Cell() {		
	}
	
//	Constructor method ===============================
	public Cell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}

//	Set & Get methods ================================
	
//	Row
	public void setRow(int row) {
		this.row = row;
	}
	public int getRow() {
		return row;
	}

//	Column
	public void setCol(int col) {
		this.col = col;
	}
	public int getCol() {
		return col;
	}

//	toString method ===================================
	@Override
	public String toString() {
		return "Cell [row=" + row + ", col=" + col + "]";
	}
	
} // End of class

