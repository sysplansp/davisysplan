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

/**
 * Generic State
 */
public interface State<T> extends AutoCloseable {

    State open();
    State on(T s);

}
