package com.prayer.times;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class SalahTimesWidgetProvider extends AppWidgetProvider
{
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	
		for (int appWidgetId : appWidgetIds)
		{
			RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		
			rv.setEmptyView(R.id.widget_list, R.id.empty_view);
		}
	}
}