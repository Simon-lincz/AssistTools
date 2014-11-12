package com.fornut.assisttools.models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fornut.assisttools.R;

public class RootCommand {

	private String TAG = "AssistTools-" + RootCommand.class.getSimpleName();

	static RootCommand sInstance;

	Context mContext;
	Resources mResources;
	Process mSuProcess;
	DataOutputStream mCmdExec;
	DataInputStream mCmdResult;
	DataInputStream mCmdError;
	boolean mGetRoot = false;
	String mCmdTmp = null;
	Toast mRootToast;

	public static RootCommand getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new RootCommand(context);
		}
		return sInstance;
	}

	public RootCommand(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mResources = context.getResources();
		checkRoot();
	}

	void checkRoot() {
		if (mGetRoot) {
			return;
		}
		AsyncTask<Void, Integer, Void> tryToGetRoot = new AsyncTask<Void, Integer, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				Runtime runtime = Runtime.getRuntime();
				DataOutputStream mCmdExec = null;
				DataInputStream mCmdResult = null, mCmdError = null;
				Process mSuProcess = null;
				try {
					mSuProcess = runtime.exec("su");
					String line;
					boolean getErrorLog = false;
					mCmdExec = new DataOutputStream(
							mSuProcess.getOutputStream());
					mCmdExec.writeBytes("echo checkroot" + '\n');
					mCmdExec.flush();
					mCmdExec.writeBytes("exit" + '\n');
					mCmdExec.flush();
					mCmdResult = new DataInputStream(
							mSuProcess.getInputStream());
					while ((line = mCmdResult.readLine()) != null) {
						Log.d(TAG, "Result >  " + line);
					}
					mCmdError = new DataInputStream(mSuProcess.getErrorStream());
					while ((line = mCmdError.readLine()) != null) {
						Log.d(TAG, "Error >  " + line);
						getErrorLog = true;
					}
					mSuProcess.waitFor();
					mGetRoot = !getErrorLog;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (mSuProcess != null) {
						mSuProcess.destroy();
					}
					if (mCmdExec != null) {
						try {
							mCmdExec.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mCmdExec = null;
					}
					if (mCmdResult != null) {
						try {
							mCmdResult.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mCmdResult = null;
					}
					if (mCmdError != null) {
						try {
							mCmdError.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mCmdError = null;
					}
				}
				Log.d(TAG, "checkRoot " + mGetRoot);
				if (mGetRoot) {
					publishProgress(0);
					initRoot();
					if (mCmdTmp != null) {
						execCommand(mCmdTmp);
						mCmdTmp = null;
					}
				} else {
					publishProgress(-1);
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
				int result = values[0].intValue();
				if (result == -1) {
					showToast(mResources
							.getString(R.string.toast_msg_fail_to_get_root));
				} else if (result == 0) {
					showToast(mResources
							.getString(R.string.toast_msg_succeed_to_get_root));
				}
			}
		};
		tryToGetRoot.execute();
	}

	void initRoot() {
		if (mGetRoot) {
			try {
				mSuProcess = Runtime.getRuntime().exec("su");
				mCmdExec = new DataOutputStream(mSuProcess.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void showToast(String msg) {
		if (mRootToast != null) {
			mRootToast.cancel();
		}
		mRootToast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
		mRootToast.show();
	}

	public void execCommand(String cmd) {
		if (!mGetRoot) {
			showToast(mResources.getString(R.string.toast_msg_no_root_permission));
			mCmdTmp = cmd;
			checkRoot();
			return;
		}
		Log.d(TAG, "execCommand " + cmd);
		try {
			mCmdExec.writeBytes(cmd + '\n');
			mCmdExec.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
