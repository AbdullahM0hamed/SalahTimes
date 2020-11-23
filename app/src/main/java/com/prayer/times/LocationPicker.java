package com.prayer.times;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.view.KeyEvent;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.text.*;

public class LocationPicker extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);
	
		this.setFinishOnTouchOutside(false);

		final EditText search = findViewById(R.id.addressSearch);
	
		TextWatcher watcher = new TextWatcher()
		{		
			@Override
			public void onTextChanged(CharSequence text, int start, int count, int after)
			{
				try
				{
					setAddresses(search.getText().toString());
				}
				catch (IOException e)
				{}
			}
		
			@Override
			public void beforeTextChanged(CharSequence text, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable editable) {}
		};
	
		search.addTextChangedListener(watcher);
	}

	void setAddresses(String address) throws IOException
	{
		ListView addressList = findViewById(R.id.addressList);
		final ArrayList<long[]> coordinates = new ArrayList<long[]>();

		OnItemClickListener itemListener = new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Intent data = new Intent();
				long[] chosenCoordinates = coordinates.get(position);
				data.putExtra("latitude", chosenCoordinates[0]);
				data.putExtra("longitude", chosenCoordinates[1]);
				setResult(RESULT_OK, data);
				finish();
			}
		};

		addressList.setOnItemClickListener(itemListener);
		Geocoder geoCoder = new Geocoder(this);

		List<Address> addresses = geoCoder.getFromLocationName(address, 20);

		ArrayList<String> cityList = new ArrayList<String>();

		for (Address foundAddress : addresses)
		{
			String cityName = foundAddress.getLocality() + ", " + foundAddress.getCountryName();

			if (!cityList.contains(cityName))
			{
				cityList.add(cityName);
				coordinates.add(new long[] {(long) foundAddress.getLatitude(), (long) foundAddress.getLongitude()});
			}
		}

		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, cityList);
		addressList.setAdapter(adapter);
	}
}