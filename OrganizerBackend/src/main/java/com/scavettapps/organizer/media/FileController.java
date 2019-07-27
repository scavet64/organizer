package com.scavettapps.organizer.media;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scavettapps.organizer.core.response.DataResponse;
import com.scavettapps.organizer.core.response.Response;
import com.scavettapps.organizer.core.entity.DuplicateMediaFilePath;
import com.scavettapps.organizer.core.repository.DuplicateMediaFilePathRepository;
import com.scavettapps.organizer.scanner.FileScanningService;

@RestController
public class FileController {

   @Autowired
   private FileRepository fileRepo;

   @Autowired
   private FileScanningService fileScanningService;
   
   @Autowired
   private DuplicateMediaFilePathRepository duplicateMediaFilePathRepository;

   @RequestMapping("/file")
   public long getTest() {
      return fileRepo.count();
   }

   @RequestMapping("/all")
   public Response getAll() {
      return new DataResponse(fileRepo.findAll());
   }

   @RequestMapping("/scan")
   public Response scan(@RequestParam String path) throws InterruptedException, ExecutionException {
      return new DataResponse(fileScanningService.scanLocationForFiles(path));
   }

   @RequestMapping("/add")
   public Response addTest() {
      MediaFile test = new MediaFile(UUID.randomUUID().toString(), "name");
      test = fileRepo.save(test);
      return new DataResponse(test);
   }

//   @RequestMapping("/file/folder")
//   public Response findFilesInFolder(@RequestParam String folderName) {
//      return new DataResponse(this.fileRepo.findAllByFolder_Path(folderName));
//   }

   @RequestMapping("/file/dupes")
   public Response findDuplicates() {
      return new DataResponse(this.duplicateMediaFilePathRepository.findAll());
   }
   
   @RequestMapping("/file/dupes2")
   public Response findDuplicates2() {
      return new DataResponse(this.fileRepo.findAllByDuplicatePathsNotEmpty());
   }

}
