package mad.asign.financeTracker;


/**
 * This class is use to store the expense value
 * 
 * @author RoboR
 *
 */
public class Expense {
	/**
	 * The primary key of the expense that is linked to the database
	 */
	private int idKey; 
	
	/**
	 * The value of the amount, placed in a string
	 */
	private String amount;


	/**
	 * The data of the expense, integer value, format : YYYYMMDD
	 * Stored as a whole string for easier comparison
	 */
	private int date;
	
	/**
	 * The mainCategory of the expense, placed in an character array
	 * of maximum 20 characters
	 */
	private char [] mainCategory;
	
	/**
	 * The name of the expense, placed in an character array of 40 characters 
	 */
	private char [] name;
	
	/**
	 * The extra notes of the expense, placed in an character array of 255 characters
	 */
	private char [] notes;
	

	
	/**
	 * Initialize the expense with no value
	 */
	public Expense(){
		amount = "0.00";
		date = 0;
		name = new char [40];
		mainCategory = new char [20];
		notes = new char [255];
	}
	
	
	/**
	 * Initialize the expense with the parameters provided
	 * 
	 * @param key	id key that linked with the database
	 * @param amount	amount of the expense
	 * @param year	the date in terms of year
	 * @param month	the date in terms of month
	 * @param day	the date in terms of day
	 * @param name	the name of the expense
	 * @param mainCategory	the category of the expense
	 * @param notes	extra notes for the expense
	 */
	public Expense(int key, String amount, int year, int month, int day, String name, String mainCategory, String notes){
		this.idKey = key;
		this.amount = amount;		

		//Format the date to the format of YYYYMMDD
		String date = String.valueOf(year);		
		if(month < 10){
			date += "0";
		}		
		date += String.valueOf(month);
		
		if(day < 10){
			date += "0";
		}
		date += String.valueOf(day);		
		this.date = Integer.parseInt(date);
		
		//Insert the name		
		if(name != null){
			if(name.length() > 40){
				name = name.substring(0, 40);
			}
			this.name = name.toCharArray();
		}
		
		//Insert the category
		if(mainCategory.length() > 40){
			mainCategory = mainCategory.substring(0, 40);
		}
		this.mainCategory = mainCategory.toCharArray();
				
		//insert the notes
		if(notes != null){
			if(notes.length() > 255){
				notes = notes.substring(0, 40);
			}
			this.notes = notes.toCharArray();
		}
	}
	

	
	/**
	 * Setter for id key
	 * 
	 * @param key value of id key, integer
	 */
	public void setIdKey(int key){
		this.idKey = key;
	}
	
	
	
	/**
	 * Getter for the key value
	 * 
	 * @return integer of the key value
	 */
	public String getIdKey(){
		return String.valueOf(this.idKey);
	}
	
	
	
	/**
	 * Setter for the expense amount
	 * 
	 * @param amount value of the expense amount
	 */
	public void setAmount(String amount){		
		this.amount = amount;
	}
	
	
	/**
	 * Getter for the expense amount
	 * 	
	 * @return value of amount in String
	 */
	public String getAmount(){		
		return this.amount;
	}
	
	
	
	/**
	 * Setter for the date, the date will be format into integer
	 * of format YYYYMMDD 
	 * 
	 * @param year	year of the expense
	 * @param month	month of the expense
	 * @param day	day of the expense
	 */
	public void setDate(int year, int month, int day){
		String date = String.valueOf(year);
		
		if(month < 10){
			date += "0";
		}		
		date += String.valueOf(month);
		
		if(day < 10){
			date += "0";
		}
		date += String.valueOf(day);

		this.date = Integer.parseInt(date);
	}
	
	
	/**
	 * Getter for expense date
	 * 
	 * @return the date in integer, format : YYYYMMDD
	 */
	public String getDate(){
		return String.valueOf(this.date);
	}



	/**
	 * Setter for the name
	 * 
	 * @param name the value of the name in string, not more than 40 characters
	 */
	public void setName(String name){
		if(name != null){			
			if(name.length() <= 40){
				this.name = name.toCharArray();
			}
		}else{
			this.name = null;
		}
	}	

	
	
	/**
	 * Getter for the name
	 * 
	 * @return returns the name in string.
	 */
	public String getName(){					   
		if(this.name == null){
			return null;
		}else{
			return String.valueOf(this.name);
		}
	}
	
	
	
	/**
	 * Setter for the category of the expense.
	 * String that is not more than 20 characters;
	 * 
	 * @param mainCategory category in String format
	 */
	public void setMainCategory(String mainCategory){
		if(mainCategory.length() <= 20){
			this.mainCategory = mainCategory.toCharArray();
		}
	}
	
	
	/**
	 * Getter for the category
	 * 
	 * @return category in String
	 */
	public String getMainCategory(){
		return String.valueOf(this.mainCategory);
	}
	
	
	
	/**
	 * Setter for the extra notes of the expenses
	 * 
	 * @param notes String not more than 255 characters
	 */
	public void setNotes(String notes){
		if(notes != null){
			if(notes.length() <= 255){
				this.notes = notes.toCharArray();
			}
		}else{
			this.notes = null;
		}
	}
	
	
	
	/**
	 * Getter for the notes
	 * 
	 * @return the expense notes in String
	 */
	public String getNotes(){
		if(this.notes == null){
			return null;
		}else{			
			return String.valueOf(this.notes);
		}
	}	
}
