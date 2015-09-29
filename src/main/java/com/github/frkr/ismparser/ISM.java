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

public class ISM implements AutoCloseable {

    private final State s;

    public ISM(State s) {
        this.s=s;
    }

    @Override
    public void close() throws Exception {
        s.close();
    }

    public void open() {
        s.open();
    }

}
