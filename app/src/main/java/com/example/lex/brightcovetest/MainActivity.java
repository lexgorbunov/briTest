package com.example.lex.brightcovetest;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.brightcove.player.event.Event;
import com.brightcove.player.event.EventListener;
import com.brightcove.player.event.EventType;
import com.brightcove.player.media.DeliveryType;
import com.brightcove.player.model.Video;
import com.brightcove.player.view.BrightcoveExoPlayerVideoView;
import com.brightcove.player.view.BrightcovePlayer;

public class MainActivity extends BrightcovePlayer {

    private BrightcoveExoPlayerVideoView videoView;
    private int sizeKnownToken = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = (BrightcoveExoPlayerVideoView) findViewById(R.id.brightcove_video_view);
        findViewById(R.id.start_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                videoView.clear();
                videoView.add(Video.createVideo("https://staging-static.life.ru/posts/2017/01/961484/video/71585449ccb21f25ec65047e024da435.mp4", DeliveryType.MP4));
                if (sizeKnownToken != -1) {
                    videoView.getEventEmitter().off(EventType.VIDEO_SIZE_KNOWN, sizeKnownToken);
                    sizeKnownToken = -1;
                }
                sizeKnownToken = videoView.getEventEmitter().once(EventType.VIDEO_SIZE_KNOWN, new EventListener() {
                    @Override
                    public void processEvent(final Event event) {
                        final ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.height = videoView.getVideoHeight();
                        videoView.setLayoutParams(layoutParams);
                    }
                });
                videoView.start();
            }
        });
    }
}
