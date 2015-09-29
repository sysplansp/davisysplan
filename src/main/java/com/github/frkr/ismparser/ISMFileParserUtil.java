/*
 * Copyright (c) 2015. Davi Saranszky Mesquita
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 */

package com.github.frkr.ismparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Shortcut Class with usual commands
 */
public final class ISMFileParserUtil {

    /**
     * {@link Charset#forName(String)}
     * {@link System#getProperty(String, String)} Property "file.encoding" with default "UTF-8"
     */
    public final static Charset CHARSET = Charset.forName(System.getProperty("file.encoding","UTF-8"));

    /**
     * @param file {@link Paths#get(String, String...)}
     * @return {@link Files#newBufferedReader(Path, Charset)}
     * @throws IOException {@link Files#newBufferedReader(Path, Charset)}
     */
    public final static BufferedReader fileReader(String file) throws IOException {
        return Files.newBufferedReader(Paths.get(file), CHARSET);
    }
    
}
