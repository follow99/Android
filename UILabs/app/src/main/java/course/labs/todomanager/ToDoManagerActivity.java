package course.labs.todomanager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import course.labs.todomanager.ToDoItem.Priority;
import course.labs.todomanager.ToDoItem.Status;

public class ToDoManagerActivity extends ListActivity {


	private NotificationManager mNManager;
	private Notification notify1;
	Bitmap LargeBitmap = null;
	private static final int NOTIFYID_1 = 1;

	// Add a ToDoItem Request Code
	private static final int ADD_TODO_ITEM_REQUEST = 0;

	private static final String FILE_NAME = "TodoManagerActivityData.txt";
	private static final String TAG = "Lab-UserInterface";

	// IDs for menu items
	private static final int MENU_DELETE = Menu.FIRST;
	private static final int MENU_DUMP = Menu.FIRST + 1;
	private static final int MENU_SORT = Menu.FIRST + 2;
	private static final int MENU_SP = Menu.FIRST + 3;


	ToDoListAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
/*
				registerReceiver(
				new ThikReceiver(),
				new IntentFilter(Intent.ACTION_TIME_TICK));

		LargeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.iv_lc_icon);
		mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		bindView();*/

		setProgressBarVisibility(true);
		//setProgress(4500)

		// Create a new TodoListAdapter for this ListActivity's ListView
		mAdapter = new ToDoListAdapter(getApplicationContext());

		// Put divider between ToDoItems and FooterView
		getListView().setFooterDividersEnabled(true);
		 //getListView().setFooterDividersEnabled(true);
		//TODO - Inflate footerView for footer_view.xml file
		TextView footerView = (TextView) getLayoutInflater().inflate(R.layout.footer_view, null);
		//TODO - Add footerView to ListView
		getListView().addFooterView(footerView);
		//getListView().addFooterView(footerView);
		if (footerView != null) {
			footerView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					log("Entered footerView.OnClickListener.onClick()");

					//TODO - Attach Listener to FooterView. Implement onClick().
					Intent intent = new Intent(getApplicationContext(), AddToDoActivity.class);
					startActivityForResult(intent, ADD_TODO_ITEM_REQUEST);
				}

			});
		}


		//TODO - Attach the adapter to this ListActivity's ListView
		setListAdapter(mAdapter);

		//TODO set AlertDialog to delete item from listView on LongClick

		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
				final int position=i;
				new AlertDialog.Builder(ToDoManagerActivity.this)
						.setTitle("Delete")
						.setMessage("Do you want Delete this memo?")
						.setNegativeButton("Cancel",null)
						.setPositiveButton("Confirmed", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								mAdapter.remove(position);

							}
						})
						.show();
				return true;
			}
		});



	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		log("Entered onActivityResult()");

		// TODO - Check result code and request code.
		// If user submitted a new ToDoItem
		// Create a new ToDoItem from the data Intent
		// and then add it to the adapter
		if (requestCode == ADD_TODO_ITEM_REQUEST) {
			if (resultCode == RESULT_OK) {
				ToDoItem todoItem = new ToDoItem(data);
				mAdapter.add(todoItem);
			}
		}
	}

	// Do not modify below here

	@Override
	public void onResume() {
		super.onResume();

		// Load saved ToDoItems, if necessary

		if (mAdapter.getCount() == 0)
			loadItems();
		//

	}

	@Override
	protected void onPause() {
		super.onPause();

		// Save ToDoItems

		saveItems();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
		menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
		menu.add(Menu.NONE,MENU_SORT,Menu.NONE,"Sort by Date");
		menu.add(Menu.NONE,MENU_SP,Menu.NONE,"Sort by Priority");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_DELETE:
			mAdapter.clear();
			return true;
		case MENU_DUMP:
			dump();
			return true;

		case MENU_SORT:
        //TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            sortByDate();
            return true;
        case MENU_SP:
        //TODO  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            sortByPriority();
            return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void dump() {

		for (int i = 0; i < mAdapter.getCount(); i++) {
			String data = ((ToDoItem) mAdapter.getItem(i)).toLog();
			log("Item " + i + ": " + data.replace(ToDoItem.ITEM_SEP, ","));
		}

	}

    // comparator for Date sorting

    private ArrayList sort(ArrayList list){

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                ToDoItem p1 = (ToDoItem) o1;
                ToDoItem p2 = (ToDoItem) o2;
                //ToDoItem temp=p1.getDate().before(p2.getDate());
                return p1.getDate().compareTo(p2.getDate());
            }
        });
            return list;
    }


    //TODO sort by date
	private void sortByDate(){
            ArrayList<ToDoItem> list = new ArrayList<ToDoItem>();
            for (int i=0;i<mAdapter.getCount();i++){
                ToDoItem todoitem= (ToDoItem)mAdapter.getItem(i);
                list.add(todoitem);
            }
            list=sort(list);
            mAdapter.clear();
            for (ToDoItem x:list
                 ) {
                mAdapter.add(x);
            }
	}

    //TODO sort by date and Priority
    private void sortByPriority(){
        ArrayList<ToDoItem> list = new ArrayList<ToDoItem>();
        for (int i=0;i<mAdapter.getCount();i++){
            ToDoItem todoitem= (ToDoItem)mAdapter.getItem(i);
            list.add(todoitem);
        }
        ArrayList<ToDoItem> HIGH=new ArrayList<ToDoItem>();
        ArrayList<ToDoItem> MED=new ArrayList<ToDoItem>();
        ArrayList<ToDoItem> LOW=new ArrayList<ToDoItem>();
        for (ToDoItem h:list
             ) {
            if (h.getPriority().toString().equals("HIGH")) {
                HIGH.add(h);
            }
        }
            for (ToDoItem m:list
                 ) {
                if (m.getPriority().toString().equals("MED")) {
                    MED.add(m);
                }
            }
            for (ToDoItem l:list
                     ) {
                if (l.getPriority().toString().equals("LOW")) {
                    LOW.add(l);
                }
            }
				// seperate to 3 list for different sorting requirment us in future say sort by HIGH/LOW/MED
                list.clear();
                list.addAll(sort(HIGH));
                list.addAll(sort(MED));
                list.addAll(sort(LOW));
                mAdapter.clear();
                for (ToDoItem x:list
                        ) {
                    mAdapter.add(x);
                }
     }


	// Load stored ToDoItems
	private void loadItems() {
		BufferedReader reader = null;
		try {
			FileInputStream fis = openFileInput(FILE_NAME);
			reader = new BufferedReader(new InputStreamReader(fis));

			String title = null;
			String priority = null;
			String status = null;
			Date date = null;

			while (null != (title = reader.readLine())) {
				priority = reader.readLine();
				status = reader.readLine();
				date = ToDoItem.FORMAT.parse(reader.readLine());
				mAdapter.add(new ToDoItem(title, Priority.valueOf(priority),
						Status.valueOf(status), date));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Save ToDoItems to file
	private void saveItems() {
		PrintWriter writer = null;
		try {
			FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					fos)));

			for (int idx = 0; idx < mAdapter.getCount(); idx++) {

				writer.println(mAdapter.getItem(idx));

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != writer) {
				writer.close();
			}
		}
	}

	private void log(String msg) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(TAG, msg);
	}

}

