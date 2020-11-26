package com.prayer.times;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QiblahFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.qiblah, container, false);
		ImageView qiblahIcon = view.findViewById(R.id.qiblahNavIcon);
		TextView qiblahText = view.findViewById(R.id.qiblahNavText);
	
		/*
		 * Ideally, this would be part of CommonCode.setupNavivation
		 * but I was unable to get it to work there
		 */
		RelativeLayout salah_times = view.findViewById(R.id.salah_time);
		final Fragment fragment = this;
		salah_times.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				getActivity().getFragmentManager()
				        .beginTransaction()
						.remove(fragment)
						.commit();

				MainActivity activity = (MainActivity) getActivity();
				CommonCode.tintViews(activity.getIcon(), activity.getTextView());
			}
		});
		
		CommonCode.tintViews(qiblahIcon, qiblahText);
		return view;
	}
}