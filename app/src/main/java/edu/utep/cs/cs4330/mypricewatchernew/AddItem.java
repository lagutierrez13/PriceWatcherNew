package edu.utep.cs.cs4330.mypricewatchernew;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
    Authors: Luis Gutierrez Antonio Zavala
    Class: CS4330
 */

public class AddItem extends DialogFragment {
    private EditText nameEditText;
    private EditText urlEditText;
    private EditText sourceEditText;
    private Button saveButton;
    private Button cancelButton;

    public AddItem() {
        // Required empty public constructor
    }

    public void setUrlEditText(String url) {
        this.urlEditText.setText(url);
    }

    public static AddItem newInstance() {
        AddItem frag = new AddItem();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    public static AddItem newInstance(String url) {
        AddItem frag = new AddItem();
        Bundle args = new Bundle();
        args.putString("url", url);
        frag.setArguments(args);
        return frag;
    }

    public static AddItem newInstance(Item item) {
        AddItem frag = new AddItem();
        Bundle args = new Bundle();
        args.putInt("id", item.getId());
        args.putString("name", item.getName());
        args.putDouble("init_price", item.getInitPrice());
        args.putDouble("curr_price", item.getCurrentPrice());
        args.putString("url", item.getUrl());
        args.putString("source_name", item.getSourceName());
        args.putString("change", item.getChange());
        args.putString("date_added", item.getDateAdded());
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get fields from view
        nameEditText = (EditText) view.findViewById(R.id.nameEditText);
        urlEditText = (EditText) view.findViewById(R.id.urlEditText);
        sourceEditText = (EditText) view.findViewById(R.id.sourceEditText);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);

        // Check if bundle has passed arguments
        nameEditText.setText(getArguments().getString("name"));
        urlEditText.setText(getArguments().getString("url"));
        sourceEditText.setText(getArguments().getString("source_name"));

        if (getArguments().getString("name") != null) {
            saveButton.setOnClickListener(v -> {
                editItem((getArguments().getInt("id")), getArguments().getString("name"), getArguments().getDouble("init_price"), getArguments().getDouble("curr_price"), getArguments().getString("url"), getArguments().getString("source_name"), getArguments().getString("date_added"));
            });
        } else {
            saveButton.setOnClickListener(v -> {
                addItem(nameEditText.getText().toString(), urlEditText.getText().toString(), sourceEditText.getText().toString());
            });
        }

        cancelButton.setOnClickListener(v -> {
            dismiss();
        });

        // Show soft keyboard automatically and request focus to field
        nameEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_item, container);
    }

    public void addItem(String name, String url, String source) {
        if (!name.isEmpty() && !url.isEmpty() && !source.isEmpty()) {
            Item item = new Item(name, url, source);
            ((MainActivity) getActivity()).getDbHelper().addItem(item);
            ((MainActivity) getActivity()).getItemAdapter().add(item);
            ((MainActivity) getActivity()).getItemAdapter().notifyDataSetChanged();
            Toast.makeText(getActivity(), "Added item: " + item.getName(), Toast.LENGTH_SHORT).show();
            dismiss();
        } else {
            Toast.makeText(getActivity(), "Fill out all required fields to add item", Toast.LENGTH_SHORT).show();
        }
    }

    public void editItem(int id, String name, double init, double curr, String url, String source, String added) {
        if (!name.isEmpty() && !url.isEmpty() && !source.isEmpty()) {
            Item item = new Item(id, name, init, curr, url, source, added);
            ((MainActivity) getActivity()).getDbHelper().update(id, item);
            ((MainActivity) getActivity()).getItemAdapter().notifyDataSetChanged();
            Toast.makeText(getActivity(), "Edited item: " + item.getName(), Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }
}