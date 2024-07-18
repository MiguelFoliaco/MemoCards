package com.foliaco.memocards.modules.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.foliaco.memocards.R
import com.foliaco.memocards.modules.home.model.FirebaseModel
import com.foliaco.memocards.modules.home.model.Memos
import com.foliaco.memocards.modules.widgets.CardWidget.Companion.ACTION_BUTTON_CLICK
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementation of App Widget functionality.
 */
@AndroidEntryPoint
class CardWidget : AppWidgetProvider() {

    @Inject
    lateinit var firebase: FirebaseModel

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
    ) {

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, firebase)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == ACTION_BUTTON_CLICK) {
            val appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
            // Realiza la acción deseada cuando se hace clic en el botón
            val appWidgetManager = AppWidgetManager.getInstance(context)
            updateAppWidget(context, appWidgetManager, appWidgetId, firebase)
        }
    }

    companion object {
        const val ACTION_BUTTON_CLICK =
            "com.foliaco.memocards.modules.widgets.cardwidget.ACTION_BUTTON_CLICK"
    }
}

internal fun updateAppWidget(
    context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, firebase: FirebaseModel
) {
    val intent = Intent(context, CardWidget::class.java).apply {
        action = ACTION_BUTTON_CLICK
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        appWidgetId,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val scope = CoroutineScope(Dispatchers.Main)
    val memos = mutableListOf<Memos>()

    scope.launch {

        val result = firebase.getAllCardsWidgets().await()
        for (doc in result.documents) {
            var item = Memos(
                id = doc.id,
                lenguajeId = doc["lenguajeId"].toString(),
                key = doc["key"].toString(),
                nivel = doc["nivel"].toString(),
                reading_on = (doc["reading_on"] as String?).orEmpty(),
                reading_kun = (doc["reading_kun"] as String?).orEmpty(),
                value = doc["value"].toString(),
                reading = (doc["reading"] as String?).orEmpty(),
                widget = (doc["widget"] ?: false) as Boolean
            )
            memos.add(item)
        }

        val random = memos.indices.random()
        var item = memos[random]
        val views = RemoteViews(context.packageName, R.layout.card_widget)

        views.setTextViewText(R.id.key_value, item.key)
        views.setTextViewText(R.id.reading, item.reading.orEmpty())
        views.setTextViewText(R.id.reading_on, item.reading_on.orEmpty())
        views.setTextViewText(R.id.reading_kun, item.reading_kun.orEmpty())
        views.setTextViewText(R.id.missing, item.value)


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
        views.setOnClickPendingIntent(R.id.button_image, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }.start()


}
