package com.scavettapps.organizer.folder;

//@Mapper
//TODO: Get MapStruct working. Not sure why its not right now
public class RootFolderMapper {

   public RootFolder folderToRootFolder(Folder folder) {
      return RootFolder.builder()
         .id(folder.getId())
         .folderName(folder.getFolderName())
         .children(folder.getFiles().size() + folder.getFolders().size())
         .build();
   }
}
