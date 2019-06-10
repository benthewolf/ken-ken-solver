package kenkensolver;

import java.io.*;
import java.util.*;
import kenkensolver.Constraint.METHOD;

public class Parser {

    private File rawFile;

    private final Map<String,METHOD> rules = Map.ofEntries(
            Map.entry("*", METHOD.MULTIPLY),
            Map.entry("#",METHOD.EQUALTO),
            Map.entry("/",METHOD.DIVIDE),
            Map.entry("+", METHOD.ADD),
            Map.entry("-", METHOD.SUBTRACT)
    );



    public Parser(String path){
        this.rawFile = new File(path);
    }
    public Parser(File file){
        this.rawFile = file;
    }

    public KenBoard parse(){

        int line = 0;
        HashMap<String, Constraint> map = new HashMap<String, Constraint>();
        try(
        DataInputStream reader = new DataInputStream(new FileInputStream(this.rawFile))
        ){

            Scanner scan =  new Scanner(reader);

            while (scan.hasNextLine()){
                if (line == 0){
                    line = Integer.parseInt(scan.nextLine());
                    continue;
                }

                String[] currentLine = scan.nextLine().split(" ");
                int result = Integer.parseInt(currentLine[0]);
                METHOD method = this.rules.get(currentLine[1]);
                Cage cage = new Cage(result, method);


                for (int a = 2; a < currentLine.length; a+=2){
                    cage.setSize(cage.getSize() + 1);
                    String currentcoords = currentLine[a] + currentLine[a+1];
                    map.put(currentcoords, new Constraint(method, currentcoords, result,line, cage));
                }

            }

            return new KenBoard(line, map);

        }

        catch (IOException e){
            System.out.println(e.getMessage());
            //System.out.println(e.getStackTrace());
        }

        return new KenBoard(line, map);
    }

}
