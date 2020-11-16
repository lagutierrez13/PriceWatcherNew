package edu.utep.cs.cs4330.mypricewatchernew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
    Authors: Luis Gutierrez Antonio Zavala
    Class: CS4330
 */

public class MainActivity extends AppCompatActivity {
    private ItemDatabaseHelper dbHelper;
    private ItemAdapter itemAdapter;
    private ListView itemListView;
    private ImageButton sortListButton;
    private ImageButton filterListButton;
    private ImageButton addItemButton;
    private static final int EDIT = 0, DELETE = 1;
    int longClickedItemIndex;
    int clicked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new ItemDatabaseHelper(this);
        itemAdapter = new ItemAdapter(this, R.layout.item, dbHelper.allItems());
        sortListButton = findViewById(R.id.sortItemsButton);
        filterListButton = findViewById(R.id.filterItemsButton);
        addItemButton = findViewById(R.id.add_item);

        itemListView = findViewById(R.id.itemListView);
        itemListView.setAdapter(itemAdapter);
        registerForContextMenu(itemListView);

        // Application is started from data sharing (opened from browser)
        String action = getIntent().getAction();
        String type = getIntent().getType();

        if (Intent.ACTION_SEND.equalsIgnoreCase(action) && ("text/plain".equalsIgnoreCase(type))) {
            String url = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            Toast.makeText(this, "URL from browser: " + url, Toast.LENGTH_SHORT).show();

            FragmentManager fm = getSupportFragmentManager();
            AddItem addItemDialogFragment = AddItem.newInstance(url);
            addItemDialogFragment.show(fm, "fragment_add_name");
        }
        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickedItemIndex = position;
                return false;
            }
        });
    }

    //region Context Menu

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_item_list, menu);
        menu.setHeaderTitle("Save Options");
        menu.add(Menu.NONE, EDIT, menu.NONE, "Edit Save");
        menu.add(Menu.NONE, DELETE, menu.NONE, "Delete Save");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case EDIT:
                // TODO: Add edit code
                break;
            case DELETE:
                dbHelper.delete(item.getItemId());
                itemAdapter.getItem(longClickedItemIndex);
                itemAdapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }

    //endregion

    //region Action bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_quit:
                finish();
                System.exit(0);
                return true;
            case R.id.action_refresh:
<<<<<<< HEAD
                itemAdapter.notifyDataSetChanged();
                //update list view with the updated items

=======
                for(int i = 0; i < itemListView.getCount(); i++){
                    Item t = (Item) itemListView.getItemAtPosition(i);
                    t.refresh();
                    itemAdapter.getItem(i);
                    itemAdapter.notifyDataSetChanged();
                }
>>>>>>> dc5cb35ba1b03a85a2c8150e7f93dc4bbb868872
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    public void showAddItemDialog(View view) {
        FragmentManager fm = getSupportFragmentManager();
        AddItem addItemDialogFragment = AddItem.newInstance();
        addItemDialogFragment.show(fm, "fragment_add_name");
    }

    public void showEditItemDialog(Item item) {
        FragmentManager fm = getSupportFragmentManager();
        AddItem addItemDialogFragment = AddItem.newInstance(item);
        addItemDialogFragment.show(fm, "fragment_add_name");
    }

    public ItemDatabaseHelper getDbHelper() {
        return this.dbHelper;
    }

    public ItemAdapter getItemAdapter() {
        return this.itemAdapter;
    }

    public void sortListClick(View view) {
        clicked++;
        List<Item> list = dbHelper.allItems();
        Collections.sort(list, new Comparator<Item>(){
            @Override
            public int compare(Item item, Item t1) {
                // ## Ascending order
                if (clicked == 1) {
                    Toast.makeText(MainActivity.this, "Sorted A - Z", Toast.LENGTH_LONG).show();
                    return item.getName().compareToIgnoreCase(t1.getName());

                }
                if (clicked == 2) {
                    Toast.makeText(MainActivity.this, "Sorted Z - A", Toast.LENGTH_LONG).show();
                    return t1.getName().compareToIgnoreCase(item.getName());
                }
                if (clicked == 3) {
                    Toast.makeText(MainActivity.this, "Sorted $ - $$$", Toast.LENGTH_LONG).show();
                    return Double.compare(item.getCurrentPrice(), t1.getCurrentPrice());
                }
                if (clicked == 4) {
                    clicked = 0;
                    Toast.makeText(MainActivity.this, "Sorted $$$ - $", Toast.LENGTH_LONG).show();
                    return Double.compare(t1.getCurrentPrice(), item.getCurrentPrice());
                }
                
                return 0;
            }
        });
        ItemAdapter adapter = new ItemAdapter(this, R.layout.item, list);
        itemListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void filterListClick(View view) {
        List<Item> filterList = dbHelper.allItems();
// initialize the adapter with courseList instead of courses
        ItemAdapter adapter = new ItemAdapter(this, R.layout.item, filterList);

// this code inside the Button's onClick
        for (Item i : dbHelper.allItems()) {
            if (Double.parseDouble(i.getChange()) > 0) {
                adapter.add(i);
            }
        }
        itemListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "Filter to Only Items with Price Change", Toast.LENGTH_LONG).show();
    }
}