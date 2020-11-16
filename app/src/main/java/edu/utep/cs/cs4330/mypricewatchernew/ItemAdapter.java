package edu.utep.cs.cs4330.mypricewatchernew;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import java.util.List;

/*
    Authors: Luis Gutierrez Antonio Zavala
    Class: CS4330
 */

public class ItemAdapter extends ArrayAdapter<Item> {
    public ItemAdapter(Context context, int resourceId, List<Item> items) {
        super(context, resourceId, items);
    }

    public interface ItemClickListener {
        void itemClicked(Item item);
    }

    private ItemClickListener listener;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        // Lookup view for data population
        TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView priceTextView = (TextView) convertView.findViewById(R.id.priceTextView);
        TextView changeTextView = (TextView) convertView.findViewById(R.id.changeTextView);
        TextView addedTextView = (TextView) convertView.findViewById(R.id.addedTextView);
        ImageButton launchButton = (ImageButton) convertView.findViewById(R.id.launchUrlButton);
        Button editButton = (Button) convertView.findViewById(R.id.editButton);
        Button deleteButton = (Button) convertView.findViewById(R.id.deleteButton);

        // Launch URL on browser
        launchButton.setOnClickListener(v -> {
            Uri site = Uri.parse(item.getUrl());
            if (!item.getUrl().startsWith("http://") && !item.getUrl().startsWith("https://")) {
                site = Uri.parse("http://" + item.getUrl());
            }
            Toast.makeText(v.getContext(), "Opening: " + site, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_VIEW, site);
            getContext().startActivity(intent);
            if (listener != null) {
                listener.itemClicked(item);
            }
        });

        editButton.setOnClickListener(v -> {
            ((MainActivity) getContext()).showEditItemDialog(item);
        });

        deleteButton.setOnClickListener(v -> {
            ((MainActivity) getContext()).getDbHelper().delete(item.getId());
            ((MainActivity) getContext()).getItemAdapter().getItem(position);
            ((MainActivity) getContext()).getItemAdapter().notifyDataSetChanged();
        });

        // Populate the data into the template view using the data object
        nameTextView.setText("Name: "+ item.getName());
        priceTextView.setText(String.valueOf("Price: " + item.getCurrentPrice()));
        changeTextView.setText("Change: " + item.getChange());
        addedTextView.setText("Added: " + item.getDateAdded());
        // Return the completed view to render on screen
        return convertView;
    }
}
