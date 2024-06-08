/*
Name: Sunny Wu
Due Date: May 26, 2024
Course: ICS3U1-04 - Mr. Fernandes
Description: This class creates an object for the user
Major Skills: Object-oriented programming
*/

// Define the class
public class User {
	
//	Fields =======================================
	private String name;
	private double score;
	
//	Constructor method ===========================
	public User(String name, double score) {
		super();
		this.name = name;
		this.score = score;
	}

//	Get & set methods ============================
	
//	Name
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

//	Score
	public void setScore(double score) {
		
		if (score > 0 && score < 60)
			this.score = score;
		else 
			System.out.println("Score out of bounds.");
	}
	public double getScore() {
		return score;
	}

//	toString ======================================
	@Override
	public String toString() {
		return "User [name=" + name + ", score=" + score + "]";
	}
	
	
	
}
