package com.taihe.eggshell.base.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.chinaway.framework.swordfish.network.http.toolbox.ImageLoader;


/**
 * Image-Load的缓存设置
 */
public class BitmapCache implements ImageLoader.ImageCache {
	private LruCache<String, Bitmap> mCache;

	@SuppressLint("NewApi")
	public BitmapCache() {
		int maxSize = 10 * 1024 * 1024;
		mCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
	}

	@SuppressLint("NewApi")
	@Override
	public Bitmap getBitmap(String url) {
		return mCache.get(url);
	}

	@SuppressLint("NewApi")
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mCache.put(url, bitmap);
	}

}
