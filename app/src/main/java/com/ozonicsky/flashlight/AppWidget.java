package com.ozonicsky.flashlight;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class AppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setImageViewResource(R.id.lightButton, R.drawable.on);

        appWidgetManager.updateAppWidget(appWidgetId, views);

        views.setOnClickPendingIntent(R.id.lightButton, clickButton(context));

        pushWidgetUpdate(context, views);
    }

    @Override
    public void onEnabled(Context context) {
        AppWidgetIntentReceiver.setup(context);
    }

    @Override
    public void onDisabled(Context context) {
        AppWidgetIntentReceiver.off();
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, AppWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }

    public static PendingIntent clickButton(Context context) {
        Intent intent = new Intent();
        intent.setAction("SWITCH_LIGHT");
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}

