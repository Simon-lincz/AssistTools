package com.fornut.assisttools.models;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

public class KeySimulator {

	static boolean DBG = true;
	private String TAG = "AssistTools-"+KeySimulator.class.getSimpleName();

	Context mContext;
	AudioManager mAudioManager;
	private static KeySimulator sInstance;

	public static KeySimulator getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new KeySimulator(context);
		}
		return sInstance;
	}

	public KeySimulator(Context context) {
		// TODO Auto-generated constructor stub
		sInstance = this;
		mContext = context;
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
	}

	public void setVolumeUP(){
		mAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
	}

	public void setVolumeDown(){
		mAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
	}

	public void GoHomeLauncher(){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
		DropzoneManager.getInstance(mContext).showWhiteDot();
	}
}