package se.kth.nylun.bamba.androidapp;

import se.kth.nylun.bamba.androidapp.model.db.ShoppingListDB;
import se.kth.nylun.bamba.androidapp.model.handler.ShoppingListHandler;
import se.kth.nylun.bamba.androidapp.view.ShoppingListAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ShoppingListActivity extends Activity{

	private ShoppingListHandler shoppingList;
	private ShoppingListDB db;
	private ListView listView;
	private ShoppingListAdapter adapter;
	private Button finishButton;
	private TextView emptyText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_list);
		
		//Set up DB
		db = new ShoppingListDB(this);
		
		//Set up button
		finishButton = (Button) findViewById(R.id.shoppingFinished_btn);
		finishButton.setOnClickListener(new OnFinishedClickListener());
		
		
		//Set up shopping list
		shoppingList = ShoppingListHandler.getInstance();
		
		listView = (ListView) findViewById(R.id.shoppingListView);
		listView.setItemsCanFocus(false);
		
		emptyText = (TextView) findViewById(R.id.emptyText);
		listView.setEmptyView(emptyText);
		
		adapter = shoppingList.getAdapter(getBaseContext());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new ShoppingListItemClickListener());
		listView.setOnItemLongClickListener(new ShoppingListItemLongClickListener());
		
		adapter.notifyDataSetChanged();
		
	}
	
	private class OnFinishedClickListener  implements OnClickListener{

		@Override
		public void onClick(View v) {
			
			shoppingList.finishShopping(adapter.getCheckedItems(),
										adapter.getUnCheckedItems(),
										db);
			finish();
		}
		
	}
	
	private class ShoppingListItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> adapterView, View v, int pos,
				long id) {
			
			adapter.toggleItem(pos);
			adapter.notifyDataSetChanged();
		}
		
		
	}
	
	private class ShoppingListItemLongClickListener implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> adpaterView, View v,
				int pos, long id) {
			
			shoppingList.removeItemAndSave(pos, db);
			adapter.notifyDataSetChanged();
			showToast("Borttaget!");
			
			return true;
		}
		
	}
	
	private void showToast(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
		}
	
}
