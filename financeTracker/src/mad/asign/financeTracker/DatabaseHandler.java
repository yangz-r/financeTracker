package mad.asign.financeTracker;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



/**
 * This is use to obtain the database of the application
 * and database operations are also done with this class
 * 
 * @author RoboR
 *
 */
public class DatabaseHandler extends SQLiteOpenHelper{
	
	/**
	 * A constant String that shows all categories are included
	 */
	public static final String FIND_ALL = "all_included";
	
	/**
	 * The version of the database
	 */
	private static final int DATABASE_VERSION = 1;
	
	/**
	 * The name of the database
	 */
	private static final String DATABASE_NAME = "records_database.db";
	
	/**
	 * The table name that use to store the expense records
	 */
	public static final String EXPENSE_TABLE_NAME = "expense_record_table";
	
	/**
	 * The primary key for the expense table
	 */
	public static final String EXPENSE_KEY_ROWID = "rowId";
	
	/**
	 * The amount that indicates how much is spend in an expense 
	 */
	public static final String EXPENSE_AMOUNT = "amount";
	
	/**
	 * The date of the expense
	 */
	public static final String EXPENSE_DATE = "date";
	
	/**
	 * The name of the item for the expense
	 */
	public static final String EXPENSE_NAME = "item_name";
	
	/**
	 * The main category that the expense belong to
	 */
	public static final String EXPENSE_MAIN_CATEGORY = "main_category";
	
	/**
	 * Extra notes for the expense
	 */
	public static final String EXPENSE_NOTES = "notes";
	
	
	/**
	 * Constructor when creating the database
	 * 
	 * @param context the current context
	 */
	DatabaseHandler(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	/**
	 * When the database is first created, the expense table will be inserted
	 * to the database.
	 */
	@Override
	public void onCreate(SQLiteDatabase db){
		try{		
			
			String sqlCreateTable = "CREATE TABLE " + EXPENSE_TABLE_NAME 
									+ "(" + EXPENSE_KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
									+ EXPENSE_AMOUNT + " VARCHAR(20)," 
									+ EXPENSE_DATE + " INTEGER,"
									+ EXPENSE_NAME + " VARCHAR(40)," 
									+ EXPENSE_MAIN_CATEGORY + " VARCHAR(20)," 
									+ EXPENSE_NOTES + " VARCHAR(255)" + ");";
			
			db.execSQL(sqlCreateTable);

			}catch(Exception e){				
				Log.v("sql", e.getMessage().toString() );
		}		
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS " + EXPENSE_TABLE_NAME);

		// Create tables again
		onCreate(db);
	}
	
	
	/**
	 * To close the database
	 */
	public void close(){
		SQLiteDatabase db = this.getReadableDatabase();

		db.close();
	}
	
	
	
	/**
	 * To add an expense record into the database
	 * 
	 * @param expense expense to be store
	 */
	public void addExpense(Expense expense){
		try{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues value = new ContentValues();
			
			value.put(EXPENSE_AMOUNT, expense.getAmount() );
			value.put(EXPENSE_DATE, expense.getDate() );
			value.put(EXPENSE_NAME, expense.getName() );
			value.put(EXPENSE_MAIN_CATEGORY, expense.getMainCategory() );
			value.put(EXPENSE_NOTES, expense.getNotes() );

			db.insert(EXPENSE_TABLE_NAME, null, value);

		}catch(Exception e){
			Log.v("sql", e.getMessage() );
		}

	}
	

	
	/**
	 * To obtain query results from the database
	 * 
	 * @param columns columns to be returned
	 * @param selection the selection string
	 * @param selectionArgs the selection arguments
	 * @param groupBy group the results by
	 * @param having having clause
	 * @param orderBy order the results By
	 * 
	 * @return cursor of the query results
	 */
	public Cursor query(String [] columns, String selection, String [] selectionArgs, String groupBy, String having, String orderBy){
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor c = db.query(EXPENSE_TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
		
		return c;				
	}
	
	
	/**
	 * To obtain the database
	 * 
	 * @return return the database of the application
	 */
	public SQLiteDatabase getDatabase(){
		SQLiteDatabase db = this.getWritableDatabase();

		return db;		
	}

	



}
