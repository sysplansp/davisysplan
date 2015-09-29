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

import com.github.frkr.ismparser.FileType;
import com.github.frkr.ismparser.Scope;
import com.github.frkr.ismparser.State;

import java.io.PrintStream;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JogoFileType implements FileType {

    private Reader conteudo;
    private PrintStream saida;
    private int line = 0;

    public JogoFileType(Reader conteudo, PrintStream saida) {
        this.conteudo = conteudo;
        this.saida = saida;
    }

    @Override
    public Scanner useScanner() {
        return new Scanner(conteudo);
    }

    @Override
    public String next(Scanner s) throws NoSuchElementException, IllegalStateException {
        return s.next();
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public State open() {
        return this;
    }

    @Override
    public State on(String s) {
        return this.currentState.on(s);
    }

    private List<String> timeStamp = new ArrayList<>();

    private State<String> stateInicial = new State<String>() {
        @Override
        public State open() {
            line++;
            currentState = new Match();
            return this;
        }

        @Override
        public State on(String buf) {
            if (buf.equals("-")) {
                this.open();
            } else {
                timeStamp.add(buf);
            }
            return this;
        }

        @Override
        public void close() throws Exception {
        }
    };
    private State<String> currentState= stateInicial;
    private Match currentMatch=null;

    private State<String> stateNewMatch1 = new State<String>() {
        @Override
        public State open() {
            return null;
        }

        @Override
        public State on(String buf) {
            if (buf.equals("match")) {
                currentState = stateNewMatch2;
            } else {
                logMatch("palavra 'match' nao encontrada");
                currentState = stateInicial;
                timeStamp.clear();
            }
            return this;
        }

        @Override
        public void close() throws Exception {

        }
    };

    private State<String> stateNewMatch2 = new State<String>() {
        @Override
        public State open() {
            return null;
        }

        @Override
        public State on(String buf) {
            try {
                currentMatch.matchDescr.append(" partida ");
                currentMatch.matchDescr.append(buf);
                currentMatch.matchDescr.append(" iniciou.");
                currentState = stateMatchCommand;
                timeStamp.clear();
                currentMatch.matchNumber=Long.parseLong(buf);
            } catch (Exception e) {
                logMatch("formato de numero de 'match' inválido");
                timeStamp.clear();
                currentState = stateInicial;
            }
            return this;
        }

        @Override
        public void close() throws Exception {

        }
    };

    private State<String> stateNewMatch3 = new State<String>() {
        @Override
        public State open() {
            return null;
        }

        @Override
        public State on(String buf) {
            try {
                currentMatch.matchDescr.append(" terminou ");
                currentMatch.matchDescr.append(currentMatch.matchNumber);
                currentMatch.matchDescr.append(" ");
                currentMatch.matchDescr.append(timeStamp.get(timeStamp.size()-2));
                currentMatch.matchDescr.append(" ");
                currentMatch.matchDescr.append(timeStamp.get(timeStamp.size()-1));
                try {
                    Date end=newSDF(new StringBuilder(timeStamp.get(timeStamp.size() - 2)).append(" ").append(timeStamp.get(timeStamp.size() - 1)).toString());
                    currentMatch.matchDescr.append(" duração (mim) ");
                    currentMatch.matchDescr.append( (end.getTime() - currentMatch.matchDateTime.getTime()) / (1000 * 60) );
                } catch (Exception e) {
                    logMatch("formato de hora inválido");
                }
                if (currentMatch.matchNumber != Long.parseLong(buf)) {
                    logMatch("match de numero invalido");
                }
            } catch (Exception e) {
                logMatch("formato de numero de 'match' inválido");
            }
            currentMatch.close();
            timeStamp.clear();
            currentState = stateInicial;
            return this;
        }

        @Override
        public void close() throws Exception {
            currentMatch.close();
        }
    };


    private State<String> stateMatchCommand = new State<String>() {
        @Override
        public State open() {
            return this;
        }

        @Override
        public State on(String s) {
            if (s.equals("-")) {
                line++;
                currentState = stateMatchCommand2;
            } else {
                timeStamp.add(s);
            }
            return this;
        }

        @Override
        public void close() throws Exception {

        }
    };

    private State<String> stateMatchCommand2 = new State<String>() {
        @Override
        public State open() {
            return this;
        }

        @Override
        public State on(String s) {
            if (s.equals("<WORLD>")) {
                currentState = stateMatchCommand;
                timeStamp.clear();
            } else if (s.equals("Match")) {
                currentState = stateNewMatch3;
            } else {
                Player player = currentMatch.players.get(s);
                if (player==null) {
                    player=new Player(s);
                    currentMatch.players.put(s,player);
                }
                currentMatch.player1=player;
                try {
                    currentMatch.playerDate = newSDF(new StringBuilder(timeStamp.get(timeStamp.size() - 2)).append(" ").append(timeStamp.get(timeStamp.size() - 1)).toString());
                } catch (Exception e) {
                    logMatch("formato de hora inválido");
                    timeStamp.clear();
                    currentState=stateMatchCommand;
                }
                currentState = stateMatchCommand3;
            }
            return this;
        }

        @Override
        public void close() throws Exception {

        }
    };

    private State<String> stateMatchCommand3 = new State<String>() {
        @Override
        public State open() {
            return null;
        }

        @Override
        public State on(String s) {
            if (s.equals("killed")) {
                currentState = stateMatchCommand4;
            } else {
                currentState = stateMatchCommand;
                timeStamp.clear();
            }
            return this;
        }

        @Override
        public void close() throws Exception {

        }
    };

    private State<String> stateMatchCommand4 = new State<String>() {
        @Override
        public State open() {
            return null;
        }

        @Override
        public State on(String s) {
            Player player = currentMatch.players.get(s);
            if (player==null) {
                player=new Player(s);
                currentMatch.players.put(s,player);
            }
            currentMatch.player2 = player;
            currentState = stateMatchCommand5;
            return this;
        }

        @Override
        public void close() throws Exception {

        }
    };

    private State<String> stateMatchCommand5 = new State<String>() {
        @Override
        public State open() {
            return null;
        }

        @Override
        public State on(String s) {
            if (s.equals("using")) {
                currentState = stateMatchCommand6;
            } else {
                logMatch("erro na contrucao da kill");
                currentState = stateMatchCommand;
                timeStamp.clear();
            }
            return this;
        }

        @Override
        public void close() throws Exception {

        }
    };

    private State<String> stateMatchCommand6 = new State<String>() {
        @Override
        public State open() {
            return null;
        }

        @Override
        public State on(String s) {
            currentMatch.player1.kill(s);
            currentMatch.player2.death();
            timeStamp.clear();
            currentState=stateMatchCommand;
            return null;
        }

        @Override
        public void close() throws Exception {

        }
    };

    private class Player implements Scope<String>, Comparable<Player> {

        Date openSpree = null;
        int kill=0;
        int death=0;
        int spree=0;
        int spreeMaior=0;
        boolean spreePremio=false;

        String name;
        Player(String name) {
            this.name=name;
        }

        @Override
        public State open() {
            return null;
        }

        @Override
        public State on(String s) {
            return null;
        }

        @Override
        public void close() {
            StringBuilder b=new StringBuilder();
            b.append(name).append(" matou ").append(kill).append(" morreu ").append(death).append(". Maior spree ").append(spreeMaior).append(".");
            if (spreePremio) {
                b.append(" Ganhou o premio de maior spree.");
            }
            if (death==0) {
                b.append(" Ganhou o premio por nao morrer.");
            }
            b.append("\n\tArmas preferidas:");
            Map<String, Integer> ordenar = sortByValue(armazem);
            for (Map.Entry<String, Integer> arma: ordenar.entrySet()) {
                b.append("\n\tArma: ").append(arma.getKey()).append(": ").append(arma.getValue());
            }
            saida.println(b.toString());
        }

        @Override
        public int compareTo(Player o) {
            int r=Integer.compare(kill,o.kill);
            if (r==0) {
                r=Integer.compare(death,o.death);
                if (r==0) {
                    return name.compareTo(o.name);
                }
            }
            return r*-1;
        }

        public void kill(String s) {
            if (spree==0) {
                openSpree=currentMatch.playerDate;
            }
            spree++;
            kill++;
            if (spree > spreeMaior) {
                spreeMaior=spree;
            }
            Integer arma = armazem.get(s);
            if (arma==null) {
                arma=0;
            }
            armazem.put(s,++arma);
            if (!spreePremio && spree>=5) {
                if ( ((currentMatch.playerDate.getTime() - openSpree.getTime()) / (1000*60)) <= 1) {
                    spreePremio=true;
                }
            }
        }

        private Map<String,Integer> armazem = new HashMap<>();

        public void death() {
            death++;
            if (spree > spreeMaior) {
                spreeMaior=spree;
            }
            spree=0;
        }
    }

    private class Match implements Scope<String> {

        StringBuilder matchDescr = new StringBuilder();
        StringBuilder matchError = new StringBuilder();

        Player player1 = null;
        Player player2 = null;
        Map<String,Player> players = new HashMap<>();
        Date playerDate = null;

        Date matchDateTime=null;
        long matchNumber=0;

        @Override
        public State open() {
            currentMatch=this;
            currentState = stateNewMatch1;
            this.matchDescr.append(timeStamp.get(timeStamp.size()-2));
            this.matchDescr.append(" ");
            this.matchDescr.append(timeStamp.get(timeStamp.size()-1));
            try {
                this.matchDateTime=newSDF(new StringBuilder(timeStamp.get(timeStamp.size() - 2)).append(" ").append(timeStamp.get(timeStamp.size() - 1)).toString());
            } catch (Exception e) {
                logMatch("formato de hora inválido");
            }
            timeStamp.clear();
            return this;
        }

        @Override
        public State on(String buf) {
            if (buf.equals("New")) {
                this.open();
            } else {
                timeStamp.clear();
                currentState = stateInicial;
            }
            return this;
        }

        @Override
        public void close() {
            saida.println(matchDescr.toString());
            SortedSet<Player> rank = new TreeSet<>(players.values());
            for (Player p:rank) {
                p.close();
            }
            saida.println(matchError.toString());
        }
    }

    private void logMatch(String log) {
        currentMatch.matchError.append("\n(linha ");
        currentMatch.matchError.append(line);
        currentMatch.matchError.append(": ");
        currentMatch.matchError.append(log);
        currentMatch.matchError.append(")");
    }

    private static Date newSDF(String date) throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date);
    }

    // copiado da internet
    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            @Override
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

}
