package mad.asign.financeTracker;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;



/**
 * This activity is used to show the interface to ask the user
 * to key in values of the expense to be inserted
 * 
 * @author RoboR
 *
 */
public class AddExpenseScreen extends Activity{
	
	/**
	 * Dialog that shows the date picker
	 */
	private DatePickerDialog dialog;	
	
	/**
	 * The current selected year
	 */
	private int year; 
	
	/**
	 * The current selected month
	 */
	private int month;
	
	/**
	 * The current selected day
	 */
	private int day;
	
	/**
	 * Id value for amount
	 */
	private final int AMOUNT_ID = 1;
	
	/**
	 * Id value for date
	 */
	private final int DATE_ID = 2;
	
	/**
	 * id value for name
	 */
	private final int NAME_ID = 3;
	
	/**
	 * id value for category
	 */
	private final int CATEGORY_ID = 4;
	
	/**
	 * id value for note
	 */
	private final int NOTE_ID = 5;
	
	/**
	 * The screen Height
	 */
	private int screenWidth;
	
	/**
	 * The main layout of this activity
	 */
	private LinearLayout mainLayout;
	
	
	
	protected void onCreate(Bundle savedInstanceState){		
		super.onCreate(savedInstanceState);
		
		//Compute the width
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);		
		screenWidth = metrics.widthPixels;

		mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
				
		addInputTable();
		addButtonRow();
		
		ScrollView scroll = new ScrollView(this);
		scroll.addView(mainLayout);
		
		setContentView(scroll);
	}

	
	
	/**
	 * Insert the table layout that consists of the label and input field
	 */
	private void addInputTable(){
		InputFilter [] filters = new InputFilter[1];

		TableLayout inputTable = new TableLayout(this);
		
		TableRow.LayoutParams paramLabel = new TableRow.LayoutParams( (int)(screenWidth * 0.4), TableRow.LayoutParams.MATCH_PARENT);		
		TableRow.LayoutParams paramInput = new TableRow.LayoutParams( (int)(screenWidth * 0.6) - 40, 
										TableRow.LayoutParams.MATCH_PARENT);

		int textWidth = (int) (screenWidth * 0.3);
		float textSize = 15f;
		
		//Amount
		TableRow amountRow = new TableRow(this);
		amountRow.setPadding(0, 0, 0, 15);

		TextView amountText = new TextView(this);
		amountText.setText("Amount");
		amountText.setTextSize(textSize);
		amountText.setWidth(textWidth);
		amountText.setPadding(20, 20, 20, 20);
		amountText.setGravity(Gravity.CENTER);
		amountText.setBackgroundColor(Color.rgb(128, 128, 255));
		
		LinearLayout amountTextLayout = new LinearLayout(this);
		amountTextLayout.setGravity(Gravity.CENTER);
		amountTextLayout.setLayoutParams(paramLabel);
		amountTextLayout.addView(amountText);
		amountRow.addView(amountTextLayout);
		
		EditText amountInput = new EditText(this);
		amountInput.setId(AMOUNT_ID);
		filters[0] = new InputFilter.LengthFilter(10);
		amountInput.setFilters(filters);
		amountInput.setLayoutParams(paramInput);
		amountInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		amountRow.addView(amountInput);
		
		inputTable.addView(amountRow);
		
		
		//Date
		TableRow dateRow = new TableRow(this);
		dateRow.setPadding(0, 0, 0, 15);
		
		TextView dateText = new TextView(this);
		dateText.setTextSize(textSize);
		dateText.setWidth(textWidth);
		dateText.setText("Date");
		dateText.setPadding(20, 20, 20, 20);		
		dateText.setGravity(Gravity.CENTER);
		dateText.setBackgroundColor(Color.rgb(128, 128, 255));

		LinearLayout dateTextLayout = new LinearLayout(this);
		dateTextLayout.setGravity(Gravity.CENTER);
		dateTextLayout.setLayoutParams(paramLabel);
		dateTextLayout.addView(dateText);
		dateRow.addView(dateTextLayout);
		
		Calendar calendar = Calendar.getInstance();
		 
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		
		dialog = new DatePickerDialog(this, new PickDate(), year, month, day);		

		Button dateBut = new Button(this);
		dateBut.setPadding(0, 0, 0, 0);
		dateBut.setLayoutParams(paramInput);
		dateBut.setId(DATE_ID);
		dateBut.setText(day + "-" + (month + 1) + "-" + year);		
		dateBut.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
                dialog = new DatePickerDialog(v.getContext(), new PickDate(), year, month, day);

                dialog.updateDate(year, month, day);                                
                dialog.show();    				
			}
		});
		dateRow.addView(dateBut);
		
		inputTable.addView(dateRow);	
		
		
		//Name
		TableRow nameRow = new TableRow(this);
		nameRow.setPadding(0, 0, 0, 15);
		
		TextView nameText = new TextView(this);
		nameText.setText("Name");
		nameText.setTextSize(textSize);
		nameText.setWidth(textWidth);
		nameText.setPadding(20, 20, 20, 20);
		nameText.setGravity(Gravity.CENTER);
		nameText.setBackgroundColor(Color.rgb(128, 128, 255));
		
		LinearLayout nameTextLayout = new LinearLayout(this);
		nameTextLayout.setGravity(Gravity.CENTER);
		nameTextLayout.setLayoutParams(paramLabel);
		nameTextLayout.addView(nameText);
		nameRow.addView(nameTextLayout);
		
		EditText nameInput = new EditText(this);
		nameInput.setId(NAME_ID);
		nameInput.setLayoutParams(paramInput);
		filters[0] = new InputFilter.LengthFilter(40);
		nameInput.setFilters(filters);
		nameInput.setLines(1);
		nameRow.addView(nameInput);
		
		inputTable.addView(nameRow);
		
		
		//Category
		TableRow categoryRow = new TableRow(this);
		categoryRow.setPadding(0, 0, 0, 15);
		
		TextView categoryText = new TextView(this);
		categoryText.setText("Category");
		categoryText.setTextSize(textSize);
		categoryText.setWidth(textWidth);
		categoryText.setPadding(20, 20, 20, 20);
		categoryText.setGravity(Gravity.CENTER);
		categoryText.setBackgroundColor(Color.rgb(128, 128, 255));
		
		LinearLayout categoryTextLayout = new LinearLayout(this);
		categoryTextLayout.setGravity(Gravity.CENTER);
		categoryTextLayout.setLayoutParams(paramLabel);
		categoryTextLayout.addView(categoryText);
		categoryRow.addView(categoryTextLayout);
				
		String [] spinnerList = getSpinnerList();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 
																spinnerList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final Spinner spinner = new Spinner(this);
		spinner.setId(CATEGORY_ID);
		spinner.setLayoutParams(paramInput);
		spinner.setAdapter(adapter);		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			public void onItemSelected(final AdapterView<?> parent, final View view, int pos, long id) {
		        String selected = parent.getItemAtPosition(pos).toString();
		        
		        if(selected == "Add Category"){
		        	//To add a New Category
		        	final EditText categoryInput = new EditText(view.getContext());

		        	AlertDialog.Builder addAlert = new AlertDialog.Builder(view.getContext());
		        	addAlert.setTitle("Add New Category");
		        	addAlert.setMessage("Input");		        	
		        	addAlert.setView(categoryInput);		        	
		        	addAlert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
							SharedPreferences preference = getSharedPreferences(FinanceTracker.PREF_FILE_NAME, Context.MODE_PRIVATE);													
							SharedPreferences.Editor editor = preference.edit();
							
							String categories = preference.getString(FinanceTracker.PREF_MAIN_CATEGORY, null);	
							
							String [] c = categories.split(",");
							String input = categoryInput.getText().toString();
							boolean exist = false;
							
							for(int i = 0; i < c.length; i++){
								if( c[i].equalsIgnoreCase(input) ){
									exist = true;
									Toast.makeText(AddExpenseScreen.this, "Category Aready Exist", Toast.LENGTH_SHORT).show();									
									break;
								}
							}
							
							//Prevent only whitespace input
							if(input.trim().length() <= 0){
								exist = true;
								Toast.makeText(AddExpenseScreen.this, "Category should not left blank", Toast.LENGTH_SHORT).show();
							}
							
							if(!exist){
								categories += "," + input;
								
								editor.putString(FinanceTracker.PREF_MAIN_CATEGORY, categories);
								editor.commit();
		
								String [] spinnerList = getSpinnerList();
								ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, spinnerList);
								adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								spinner.setAdapter(adapter);	
								
								Toast.makeText(AddExpenseScreen.this, input + " has added into Category", Toast.LENGTH_SHORT).show();									
							}
						}
					});
		        	
		        	addAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();							
						}
					});
		        	
		        	addAlert.show();
			        parent.setSelection(0);
		        }		        
		    }
			
			 public void onNothingSelected(AdapterView parent) {
			        // Do nothing
			    }
		});		
		categoryRow.addView(spinner);	
		inputTable.addView(categoryRow);
		
		
		//Notes
		TableRow noteRow = new TableRow(this);
		noteRow.setPadding(0, 0, 0, 15);
		
		TextView noteText = new TextView(this);
		noteText.setText("Note");
		noteText.setTextSize(textSize);
		noteText.setWidth(textWidth);
		noteText.setPadding(20, 20, 20, 20);
		noteText.setGravity(Gravity.CENTER);
		noteText.setBackgroundColor(Color.rgb(128, 128, 255));
		
		LinearLayout noteTextLayout = new LinearLayout(this);
		noteTextLayout.setGravity(Gravity.CENTER);
		noteTextLayout.setLayoutParams(paramLabel);
		noteTextLayout.addView(noteText);
		noteRow.addView(noteTextLayout);
		
		EditText noteInput = new EditText(this);
		noteInput.setId(NOTE_ID);
		noteInput.setLayoutParams(paramInput);
		noteInput.setLines(3);
		filters[0] = new InputFilter.LengthFilter(255);
		noteInput.setFilters(filters);
		noteRow.addView(noteInput);
		
		inputTable.setPadding(20, 20, 20, 20);
		inputTable.addView(noteRow);
		
		
		mainLayout.addView(inputTable);
	}
	
	
	
	/**
	 * The row that has the add and clear buttons
	 */
	private void addButtonRow(){
		LinearLayout buttonRowLayout = new LinearLayout(this);
		buttonRowLayout.setPadding(0, 0, 0, 30);
		buttonRowLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonRowLayout.setGravity(Gravity.CENTER);
		
		//Add Button
		LinearLayout addLayout = new LinearLayout(this);
		addLayout.setPadding(0, 0, 50, 0);
		
		int butWidth = 150;
		
		Button addBut = new Button(this);
		addBut.setWidth(butWidth);
		addBut.setText("Add");
		addBut.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
				DatabaseHandler db = new DatabaseHandler(v.getContext());
				
				EditText amountText = (EditText) findViewById(AMOUNT_ID);
				EditText nameText = (EditText) findViewById(NAME_ID);	
				Spinner spinner = (Spinner) findViewById(CATEGORY_ID);
				EditText noteText = (EditText) findViewById(NOTE_ID);
								
				Expense newExpense = new Expense();
				
				//Check for empty amount
				if(amountText.getText().toString().equalsIgnoreCase("")){
					Toast.makeText(AddExpenseScreen.this, "Amount is needed to be inserted.", Toast.LENGTH_SHORT).show();
				}else{
					//insert the expense
					newExpense.setAmount(amountText.getText().toString() );					
					newExpense.setDate(year, (month + 1), day);
					newExpense.setName(nameText.getText().toString() );
					newExpense.setMainCategory(spinner.getSelectedItem().toString() );
					newExpense.setNotes(noteText.getText().toString() );

					db.addExpense(newExpense);
					
					Intent backToHome = new Intent(AddExpenseScreen.this, HomeScreen.class);
					startActivity(backToHome);
					
					Toast.makeText(AddExpenseScreen.this, "Record Succesfully Inserted.", Toast.LENGTH_SHORT).show();
					finish();
				}
				db.close();
			}
		});
		addLayout.addView(addBut);
		
		buttonRowLayout.addView(addLayout);

		
		//Clear Button
		LinearLayout clearLayout = new LinearLayout(this);
		clearLayout.setPadding(50, 0, 0, 0);
		
		Button clearBut = new Button(this);		
		clearBut.setWidth(butWidth);
		clearBut.setText("Clear");
		clearBut.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
				EditText amountText = (EditText) findViewById(AMOUNT_ID);
				amountText.setText("");
				
				EditText nameText = (EditText) findViewById(NAME_ID);
				nameText.setText("");	
				
				EditText noteText = (EditText) findViewById(NOTE_ID);
				noteText.setText("");	
			}
		});
		clearLayout.addView(clearBut);		
		
		buttonRowLayout.addView(clearLayout);
		
		mainLayout.addView(buttonRowLayout);		
	}
	
	
	
	/**
	 * Get the category that is store in the preference
	 * 
	 * @return the category in an String array
	 */
	private String [] getCategory(){
		SharedPreferences preference = getSharedPreferences(FinanceTracker.PREF_FILE_NAME, Context.MODE_PRIVATE);

		String str = preference.getString(FinanceTracker.PREF_MAIN_CATEGORY, null);

		String [] categories = str.split(",");
				
		return categories;		
	}

	
	
	/**
	 * Get the String array that is for the spinner (category)
	 * Has an extra selection for Add Category
	 * 
	 * @return The string array for the spinnerList
	 */
	private String [] getSpinnerList(){
		String [] categories = getCategory();

		String [] categoryList = new String [categories.length + 1];		
		
		for(int i = 0; i < categories.length; i++){
			categoryList[i] = categories[i];
		}
		
		categoryList[categoryList.length - 1] = "Add Category";
		
		return categoryList;
	}
	
	
	
	/**
	 * Set the date that has selected
	 * @param year
	 * @param month
	 * @param day
	 */
	private void setSelectedDate(int year, int month, int day){
		this.year = year;
		this.month = month;
		this.day = day;
	}

	
	
	/**
	 * The handler when the date is chosen from the date picker 
	 * It will updates the selected date and also changes the
	 * value of the date button
	 *   
	 * @author RoboR
	 *
	 */
	private class PickDate implements DatePickerDialog.OnDateSetListener{

		public void onDateSet(DatePicker view, int year, int month, int day) {
			
			setSelectedDate(year, month, day);
			
	        Button but = (Button) findViewById(DATE_ID);
	        but.setText(day + "-" + (month + 1) + "-"  + year);
	        dialog.hide();
		}		
	}
}
