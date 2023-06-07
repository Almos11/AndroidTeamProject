package com.example.client;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class VideoAdapterTest extends RecyclerView.Adapter<VideoAdapterTest.VideoViewHolder> {
    private ArrayList<File> videos;

    public VideoAdapterTest(ArrayList<File> videos) {
        this.videos = videos;
    }

    @NonNull
    @Override
    public VideoAdapterTest.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_view, parent, false);
        return new VideoAdapterTest.VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapterTest.VideoViewHolder holder, int position) {
        File videoPath = videos.get(position);
        videos.add(videos.get(0));
        holder.bindVideo(videoPath);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {

        private final VideoView videoView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
        }

        public void bindVideo(File video) {
            videoView.setVideoURI(Uri.fromFile(video));
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
