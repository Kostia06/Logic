import java.io.BufferedReader;
import java.io.FileReader;


class Main{
    public static final String RESET = "\033[1m\033[0m";
    public static final String PURPLE = "\033[1m\033[35m";
    public static final String RED = "\033[1m\033[31m";
    public static final String GREEN = "\033[1m\033[32m";

    public static void main(String[] args){
        if(args.length != 1){ System.out.println("Usage: java Main <filename>"); return; }
        String logic = read(args[0]);
        Lexer lexer = new Lexer(logic);
        Parser parser = new Parser(lexer.objects, lexer.ids);
    }

    public static final String read(String filename){
        String logic = "";
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = "";
            while((line = reader.readLine()) != null){
                logic += line;
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        return logic;
    }
}
