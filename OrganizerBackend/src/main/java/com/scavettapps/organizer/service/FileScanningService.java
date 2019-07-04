package com.scavettapps.organizer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scavettapps.organizer.entity.File;
import com.scavettapps.organizer.repository.FileRepository;
import com.scavettapps.organizer.repository.ScanLocationRepository;

@Service
public class FileScanningService {

	@Autowired
	private ScanLocationRepository scanLocationRepository;
	
	@Autowired
	private FileRepository fileRepository;
	
	public List<File> scanLocationForFiles(String path) {
		List<File> newFiles = new ArrayList<>();
		
		Collection<java.io.File> files = FileUtils.listFiles(new java.io.File(path), new RegexFileFilter("^(.*?)"), DirectoryFileFilter.DIRECTORY);
		
		for (java.io.File file: files) {
			newFiles.add(new File(UUID.randomUUID().toString(), file.getName()));
		}
		
		return newFiles;
	}
	
}
