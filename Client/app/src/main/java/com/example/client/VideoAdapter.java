package com.example.client;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private final ArrayList<Video> videos;

    public String currentVideoInfo;


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
            Video video = videos.get(position);
            holder.bindVideo(video.videoFile);
            currentVideoInfo = video.jsonInfo;
        } else {
            testVideoFromServer.getNextVideo(new VideoView.FileCallback() {
                @Override
                public void onFileReceived(File videoFile, String jsonData) {
                    videos.add(new Video(jsonData, videoFile));
                    currentVideoInfo = jsonData;
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



    private class Video {
        String jsonInfo;
        File videoFile;
        Video(String jsonInfo, File videoFile) {
            this.jsonInfo = jsonInfo;
            this.videoFile = videoFile;
        }
    }

}
