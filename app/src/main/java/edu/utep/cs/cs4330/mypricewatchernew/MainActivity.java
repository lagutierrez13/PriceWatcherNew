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
    }

    //region Context Menu

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_item_list, menu);
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
                itemAdapter.getItem(info.position);
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
                for(int i = 0; i < itemListView.getCount(); i++){
                    Item t = (Item) itemListView.getItemAtPosition(i);
                    t.refresh();
                    itemAdapter.getItem(i);
                    itemAdapter.notifyDataSetChanged();
                }
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
    }

    public void filterListClick(View view) {
    }
}