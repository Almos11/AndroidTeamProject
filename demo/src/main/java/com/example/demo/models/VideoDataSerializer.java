package com.example.demo.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class VideoDataSerializer extends JsonSerializer<VideoData> {
    @Override
    public void serialize(VideoData videoData, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", videoData.getId());
        jsonGenerator.writeStringField("author_name", videoData.getAuthor_name());
        jsonGenerator.writeNumberField("likes", videoData.getLikes());
        jsonGenerator.writeNumberField("comments", videoData.getComments());
        jsonGenerator.writeNumberField("views", videoData.getViews());
        jsonGenerator.writeNumberField("rating", videoData.getRating());
        jsonGenerator.writeEndObject();
    }
}

