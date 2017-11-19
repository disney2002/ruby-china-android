package com.testerhome.android.listener;


import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;

import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;
import com.testerhome.android.EmptyActivity;
import com.testerhome.android.MainActivity;
import com.testerhome.android.R;

/**
 * Created by xushizhao on 17/2/23.
 */

public class Button_Listener implements View.OnClickListener{

    private MainActivity activity;
    private String text;

    public Button_Listener(MainActivity activity,String text) {
        this.activity = activity;
        this.text = text;

    }




    @Override
    public void onClick(View v) {

        TurbolinksView turbolinksView = (TurbolinksView)activity.findViewById(R.id.turbolinks_view);
        TurbolinksSession.getDefault(activity).setDebugLoggingEnabled(true);
        WebSettings webSettings = TurbolinksSession.getDefault(activity).getWebView().getSettings();
        webSettings.setUserAgentString("turbolinks-app, testerhome, official, android");

        String url = "";
        if(text.equals("话题")){
            url = "https://testerhome.com/topics";
        }else if (text.equals("知识库")){
            url = "https://testerhome.com/wiki";
        }
        TurbolinksSession.getDefault(activity)
                .activity(activity)
                .adapter(activity)
                .view(turbolinksView)
                .visit(url);

    }


}
