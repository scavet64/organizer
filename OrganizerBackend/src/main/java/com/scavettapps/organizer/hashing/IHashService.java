package com.scavettapps.organizer.hashing;

import java.io.File;
import java.io.IOException;

public interface IHashService {

   public String getHash(File file) throws IOException;
}
