package com.deliveryninja;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventServer {

    Map<String, SearchEvent> searchMap = new ConcurrentHashMap<>();
    Map<String, DownloadEvent> downloadMap = new ConcurrentHashMap<>();

    public void addSearchEvent(String name){
        searchMap.put(name, SearchEvent.WAITING);
    }

    public void startSearch(String name){
        searchMap.put(name, SearchEvent.SEARCHING);
    }

    public void completeSearch(String name){
        searchMap.put(name, SearchEvent.COMPLETE);
    }

    public void printSearchMap(){
        searchMap.forEach((k, v) -> System.out.println("Search: " + k + " -> " + v.name()));
    }

    public void addDownloadEvent(String name){
        downloadMap.put(name, DownloadEvent.WAITING);
    }

    public void startDownload(String name){
        downloadMap.put(name, DownloadEvent.DOWNLOADING);
    }

    public void completeDownload(String name){
        downloadMap.put(name, DownloadEvent.COMPLETE);
    }

    public void printDownloadMap(){
        downloadMap.forEach((k, v) -> System.out.println("Download: " + k + " -> " + v.name()));
    }

    public enum SearchEvent {
        WAITING,
        SEARCHING,
        COMPLETE
    }

    public enum DownloadEvent {
        WAITING,
        DOWNLOADING,
        COMPLETE
    }
}
