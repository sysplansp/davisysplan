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

package teste;

import com.github.frkr.ismparser.ISMFileParser;
import com.github.frkr.ismparser.ISMFileParserUtil;

import java.io.BufferedReader;

public class JogoParse {

    public static void main(String... args) throws Exception {
        System.out.println("Abrir arquivo 'teste.txt'");
        try(BufferedReader readerArquivo = ISMFileParserUtil.fileReader("teste.txt")) {
            System.out.println("Parse de arquivo");
            try (ISMFileParser parser = new ISMFileParser(new JogoFileType(readerArquivo,System.out))) {
                    parser.open();
            }
            System.out.println("Termino do aplicativo");
        }
    }

}
