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

import java.util.NoSuchElementException;
import java.util.Scanner;

public interface FileType extends State<String> {

    /**
     * Method for instancing a new Scanner. Use with method {@link Scanner#useDelimiter(String)} if necessary
     * @return A (new) instance of Scanner
     */
    Scanner useScanner();

    /**
     * How will read the next command. Using with method {@link Scanner#nextLine()} will produce a "Line Reader Type of Machine"
     * @param s The Scanner
     * @return Result of methods like {@link Scanner#next()}
     * @throws NoSuchElementException Termination
     * @throws IllegalStateException Channel is closed
     */
    String next(Scanner s) throws NoSuchElementException, IllegalStateException;

}
