package com.taihe.eggshell.base;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import android.widget.RemoteViews;

import com.taihe.eggshell.R;

public class MyNotification {

	private Context context;
	private RemoteViews contentView;
	private NotificationManager manager;
	private Notification notification;

	public MyNotification(Context context) {

		this.context = context;
		manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public void createNotification() {

		notification = new Notification();
		notification.icon = R.drawable.ic_launcher;
		notification.tickerText = "开始下载";
		// 通知栏里面的布局
		contentView = new RemoteViews(context.getPackageName(),
				R.layout.notification);
		notification.contentView = contentView;

		manager.notify(1, notification);
	}

	public NotificationManager getManager() {
		return manager;
	}

	public void changeProgressStatus(int process) {
//		int cur = process * 100 / 10;
		System.out.println("-----------------------进度"+process);
		notification.contentView.setProgressBar(R.id.progressBar, 100, process,
				false);
		notification.contentView.setTextViewText(R.id.progressTv, "当前进度" + process
				+ "%");
		manager.notify(1, notification);
	}
}
