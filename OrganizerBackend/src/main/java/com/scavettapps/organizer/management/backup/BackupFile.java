package com.scavettapps.organizer.management.backup;

import com.scavettapps.organizer.folder.Folder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BackupFile {
   private Set<Folder> folder;
}
