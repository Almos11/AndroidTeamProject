package com.example.client;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;

public class VideoAdapterTest extends RecyclerView.Adapter<VideoAdapterTest.VideoViewHolder> {
    private ArrayList<byte[]> videoBytes;

    public VideoAdapterTest(ArrayList<byte[]> videoBytes) {
        this.videoBytes = videoBytes;
    }

    @NonNull
    @Override
    public VideoAdapterTest.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_view, parent, false);
        return new VideoAdapterTest.VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapterTest.VideoViewHolder holder, int position) {
        byte[] videoData = videoBytes.get(position);
        holder.bindVideo(videoData);
    }

    @Override
    public int getItemCount() {
        return videoBytes.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {

        private final VideoView videoView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
        }

        public void bindVideo(byte[] videoData) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(videoData);
            videoView.setVideoURI(null);
            videoView.setVideoPath("");
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
            });
            videoView.requestFocus();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    videoView.start();
                }
            });
            videoView.start();
        }
    }
}
