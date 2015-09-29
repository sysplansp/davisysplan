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

public class ISMFileParser extends ISM {

    private final FileType ft;

    public ISMFileParser(FileType ft) {
        super(ft);
        this.ft=ft;
    }

    @Override
    public void open() {
        Scanner scanner=ft.useScanner();
        super.open();
        String buf=null;
        try {
            while ((buf=ft.next(scanner)) != null) {
                ft.on(buf);
            }
        } catch (NoSuchElementException e) {
        }
    }

}

/*
TODO Todo list:

Gramatica // https://docs.oracle.com/javase/specs/jls/se6/html/syntax.html#18.1
http://docs.oracle.com/javase/7/docs/api/java/util/ArrayDeque.html //

Monitorar arquivos não é requisito
Monitorar "TAIL" não é requisito
Criar um gerenciador básico de máquina de estado
Separação de comandos
Baseado em Interfaces
Após entregar fazer GIT de arquivos XML, CNAB, JSON, CSV, JAVA
Atestar performance
Procurar MASTERDAO
Uma máquina de estado pode validar estruturas e identificar posição exata de erros.

 READLINE - É MELHOR PARA CRIAR OUTPUT DE DEBUG
 ESTADO DE LINHA
 SCOPOS

 */
