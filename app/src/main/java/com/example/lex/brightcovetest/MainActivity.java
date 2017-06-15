package com.example.lex.brightcovetest;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.brightcove.player.analytics.Analytics;
import com.brightcove.player.event.Event;
import com.brightcove.player.event.EventListener;
import com.brightcove.player.event.EventType;
import com.brightcove.player.media.DeliveryType;
import com.brightcove.player.model.Video;
import com.brightcove.player.view.BrightcoveExoPlayerVideoView;
import com.brightcove.player.view.BrightcovePlayer;

public class MainActivity extends BrightcovePlayer {
    private Button startBtn;
    private int sizeKnownToken = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPlayer();

        startBtn = (Button) findViewById(R.id.start_btn);

        final ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                brightcoveVideoView.clear();
                brightcoveVideoView.add(Video.createVideo("https://staging-static.life.ru/posts/2017/01/961484/video/71585449ccb21f25ec65047e024da435.mp4", DeliveryType.MP4));
                unbindEvents();
                sizeKnownToken = brightcoveVideoView.getEventEmitter().once(EventType.VIDEO_SIZE_KNOWN, new EventListener() {
                    @Override
                    public void processEvent(final Event event) {
                        layoutParams.height = brightcoveVideoView.getVideoHeight();
                        brightcoveVideoView.setLayoutParams(layoutParams);
                    }
                });
                brightcoveVideoView.start();
            }
        });
    }

    private void initPlayer() {
        brightcoveVideoView = (BrightcoveExoPlayerVideoView) findViewById(R.id.brightcove_video_view);
    }

    private void unbindEvents() {
        if (sizeKnownToken != -1) {
            brightcoveVideoView.getEventEmitter().off(EventType.VIDEO_SIZE_KNOWN, sizeKnownToken);
            sizeKnownToken = -1;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindEvents();
        final Analytics analytics = brightcoveVideoView.getAnalytics();
        analytics.getEventEmitter().off();
        analytics.removeListeners();
        brightcoveVideoView.clear();
    }
}
