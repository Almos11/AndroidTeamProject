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
    private final ArrayList<File> videos;

    TestVideoFromServer testVideoFromServer;

    public VideoAdapterTest(TestVideoFromServer testVideoFromServer) {
        this.videos = new ArrayList<>();
        this.testVideoFromServer = testVideoFromServer;
    }



    @NonNull
    @Override
    public VideoAdapterTest.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_test_video_from_server, parent, false);
        return new VideoAdapterTest.VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapterTest.VideoViewHolder holder, int position) {
        if (position < videos.size()) {
            File videoPath = videos.get(position);
            holder.bindVideo(videoPath);
        } else {
            testVideoFromServer.getNextVideo(new TestVideoFromServer.FileCallback() {
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

        private final VideoView videoView;

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
