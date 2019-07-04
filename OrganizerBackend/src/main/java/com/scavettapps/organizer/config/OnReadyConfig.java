package com.scavettapps.organizer.config;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class OnReadyConfig {
	
	@EventListener({ApplicationReadyEvent.class})
	void applicationReadyEvent() {
	    System.out.println("Application started ... launching browser now");
	    //Browse("http://localhost:8080/");
	}

	public static void Browse(String url) {
	    if(Desktop.isDesktopSupported()){
	        Desktop desktop = Desktop.getDesktop();
	        try {
	            desktop.browse(new URI(url));
	        } catch (IOException | URISyntaxException e) {
	            e.printStackTrace();
	        }
	    }else{
	        Runtime runtime = Runtime.getRuntime();
	        try {
	            runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
}
