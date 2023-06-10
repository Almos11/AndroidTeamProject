package com.example.client;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private final ArrayList<File> videos;

    VideoView testVideoFromServer;

    public VideoAdapter(VideoView testVideoFromServer) {
        this.videos = new ArrayList<>();
        this.testVideoFromServer = testVideoFromServer;
    }



    @NonNull
    @Override
    public VideoAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_view, parent, false);
        return new VideoAdapter.VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.VideoViewHolder holder, int position) {
        if (position < videos.size()) {
            File videoPath = videos.get(position);
            holder.bindVideo(videoPath);
        } else {
            testVideoFromServer.getNextVideo(new VideoView.FileCallback() {
                @Override
                public void onFileReceived(File videoFile) {
                    videos.add(videoFile);
                    holder.bindVideo(videoFile);
                }
            }, position);
        }
    }

    @Override
    public int getItemCount() {
        return /*videos.size()*/ 10000;
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {

        private final android.widget.VideoView videoView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
        }

        public void bindVideo(File video) {
            videoView.setVideoURI(Uri.fromFile(video));
            videoView.setOnCompletionListener(mediaPlayer -> videoView.start());
            videoView.start();
        }
    }
}
