package com.udacity.popularmovies.model;

import java.util.List;

public class MovieVideoDetails {

    //{"id":18,"results":[{"id":"578e58b4c3a36817480052d0","iso_639_1":"en","iso_3166_1":"US","key":"ZH4islqoA8o","name":"The Fifth Element (1997) - Theatrical Trailer in HD (Fan Remaster)","site":"YouTube","size":1080,"type":"Trailer"},{"id":"578e4e63c3a3685acb013412","iso_639_1":"en","iso_3166_1":"US","key":"7-9mTiBawSM","name":"The Fifth Element - Trailer","site":"YouTube","size":1080,"type":"Clip"}]}
    private int id;
    private List<MovieVideo> movieVideos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieVideo> getMovieVideos() {
        return movieVideos;
    }

    public void setMovieVideos(List<MovieVideo> movieVideos) {
        this.movieVideos = movieVideos;
    }

    public String toString(){
        return "id: "+id+", movievideos: "+movieVideos+", video count:"+(movieVideos!=null?movieVideos.size():0);
    }

    public static class MovieVideo {
        String id;
        String iso_6391;
        String iso_3166_1;
        String key;
        String name;
        String site;
        int size; //allowed values - 360,480,720,1080
        String type; //allowed values trailer,teaser,clip,featurette

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIso_6391() {
            return iso_6391;
        }

        public void setIso_6391(String iso_6391) {
            this.iso_6391 = iso_6391;
        }

        public String getIso_3166_1() {
            return iso_3166_1;
        }

        public void setIso_3166_1(String iso_3166_1) {
            this.iso_3166_1 = iso_3166_1;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

}