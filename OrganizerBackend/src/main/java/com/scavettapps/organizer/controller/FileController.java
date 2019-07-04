package com.scavettapps.organizer.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scavettapps.organizer.controller.response.DataResponse;
import com.scavettapps.organizer.controller.response.Response;
import com.scavettapps.organizer.entity.File;
import com.scavettapps.organizer.repository.FileRepository;
import com.scavettapps.organizer.service.FileScanningService;

@RestController
public class FileController {
	
	@Autowired
	private FileRepository fileRepo;
	
	@Autowired
	private FileScanningService fileScanningService;
	
	@RequestMapping("/file")
	public long getTest() {
		return fileRepo.count();
	}
	
	@RequestMapping("/all")
	public Response getAll() {
		return new DataResponse(fileRepo.findAll());
	}
	
	@RequestMapping("/scan")
	public Response scan(@RequestParam String path) {
		return new DataResponse(fileScanningService.scanLocationForFiles(path));
	}
	
	@RequestMapping("/add")
	public Response addTest() {
		File test = new File(UUID.randomUUID().toString(), "name");
		test = fileRepo.save(test);
		return new DataResponse(test);
	}

}
