package com.example.designhubzwebgltest;

import androidx.appcompat.app.AppCompatActivity;

import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class GeckoViewActivity extends AppCompatActivity {
    org.mozilla.geckoview.GeckoView geckoView;
    GeckoSession geckoSession;
    GeckoRuntime geckoRuntime;
    String url;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String url = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        init();
        // opens a new session (this MUST be before "setSession" or it WILL crash)
        geckoSession.open(geckoRuntime);
        // sets the geckoview session
        geckoView.setSession(geckoSession);
        // loads your url
        geckoSession.loadUri(url);
    }
    // initialize variables
    private void init(){
        geckoView = findViewById(R.id.geckoview);
        geckoSession = new GeckoSession();
        final GeckoPermissionDelegate permission = new GeckoPermissionDelegate();
        geckoSession.setPermissionDelegate(permission);
        final BasicGeckoViewPrompt prompt = new BasicGeckoViewPrompt(this);

        geckoSession.setPromptDelegate(prompt);
        geckoRuntime = GeckoRuntime.create(this);
        geckoRuntime.getSettings().setRemoteDebuggingEnabled(true);
        geckoRuntime.getSettings().setConsoleOutputEnabled(true);
        geckoRuntime.getSettings().setJavaScriptEnabled(true);
        url = "https://google.com";
    }
    private class GeckoPromptDelegate implements GeckoSession.PromptDelegate {
        private final Activity mActivity;
        public GeckoPromptDelegate(final Activity activity) {
            mActivity = activity;
        }
        @Override
        public GeckoResult<PromptResponse> onAlertPrompt(final GeckoSession session,
                                                         final AlertPrompt prompt) {
            final Activity activity = mActivity;
            if (activity == null) {
                return GeckoResult.fromValue(prompt.dismiss());
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                    .setTitle(prompt.title)
                    .setMessage(prompt.message)
                    .setPositiveButton(android.R.string.ok, /* onClickListener */ null);
            GeckoResult<PromptResponse> res = new GeckoResult<PromptResponse>();
            createStandardDialog(builder, prompt, res).show();
            return res;
        }
        private AlertDialog createStandardDialog(final AlertDialog.Builder builder,
                                                 final BasePrompt prompt,
                                                 final GeckoResult<PromptResponse> response) {
            final AlertDialog dialog = builder.create();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(final DialogInterface dialog) {
                    if (!prompt.isComplete()) {
                        response.complete(prompt.dismiss());
                    }
                }
            });
            return dialog;
        }
    }
    private class GeckoPermissionDelegate implements GeckoSession.PermissionDelegate {
        private Callback mCallback;


//        @RequiresApi(api = Build.VERSION_CODES.M)
//        @Override
//        public void onAndroidPermissionsRequest(final GeckoSession session,
//                                                final String[] permissions,
//                                                final Callback callback) {
//            mCallback = callback;
//            requestPermissions(permissions, MY_CAMERA_REQUEST_CODE);
//        }
//
//        @Override
//        public void onContentPermissionRequest(final GeckoSession session,
//                                               final String uri,
//                                               final int type, final Callback callback) {
//            mCallback = callback;
//        }
        @Override
        public void onMediaPermissionRequest(
                GeckoSession session,
                String uri,
                MediaSource[] video,
                MediaSource[] audio,
                MediaCallback callback)
        {

            callback.grant(video[0], null);

        }


    }
}