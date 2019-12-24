//Copyright (C) 2019 Gnostech, Inc - All Rights Reserved.
package com.scavettapps.organizer.transcoding;

import com.scavettapps.organizer.files.StoredFile;
import com.scavettapps.organizer.media.MediaFile;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Gnostech Inc.
 */
public interface ITranscodingService {
   public File transcodeMediaFile(MediaFile file) throws TranscodingException;
}
