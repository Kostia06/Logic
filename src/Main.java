import java.io.BufferedReader;
import java.io.FileReader;


class Main{

    public static final String RESET = "\033[1m\033[0m";
    public static final String PURPLE = "\033[1m\033[35m";
    public static final String RED = "\033[1m\033[31m";
    public static final String GREEN = "\033[1m\033[32m";

    public static char TRUE = 'T';
    public static char FALSE = 'F';
    public static boolean binary = false;
    public static String logic = "";
        

    public static void main(String[] args){
        if(args.length != 1){ System.out.println("Usage: java Main <filename>"); return; }
        read(args[0]);
        if(binary){ TRUE = '1'; FALSE = '0'; }

        Lexer lexer = new Lexer(logic);
        if(lexer.objects.size() == 0){ return; }
        Parser parser = new Parser(lexer.objects, lexer.ids);
        Show show = new Show(lexer.objects, lexer.ids, parser.result_table);
    }

    public static final void read(String filename){
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
    }
}
