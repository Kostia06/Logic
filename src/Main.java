

public class Main{
    public static void main(String[] args){
        String logic = "(x1 & x2) ^ x3 v x4";
        Lexer lexer = new Lexer(logic);
        Parser parser = new Parser(lexer.objects);
    }
}
