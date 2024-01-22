import java.util.ArrayList;
import java.lang.Math;


enum TokenType{
    ID, RESULT, 
    OPEN_PAREN, CLOSE_PAREN,
    NOT,
    AND, OR, XOR,
    NOTHING
};

class Keyword{
    public char value;
    public TokenType  type;
    public Keyword(char value, TokenType type){
        this.value = value;
        this.type = type;
    }
};


class Object{
    ArrayList<Boolean> table;
    boolean result;
    String value;
    TokenType type;

    public Object(String value, TokenType type){
        this.value = value;
        this.type = type;
        table = new ArrayList<Boolean>();
    }

    void create_table(int size, int increment){
        boolean item = true;
        int count = 0;
        double max = size / increment;
        for(int i = 0; i < size; i++){
            if(count >= max){ 
                item = !item;
                count = 0;
            }
            count++;
            table.add(item);
        }
    }

    void set_result(int index){ 
        if(type == TokenType.ID){ result = table.get(index); }
    }

    boolean get_value(){ return result; }

}


class Lexer{
    ArrayList<Keyword> keywords = new ArrayList<Keyword>();
    ArrayList<Object> objects = new ArrayList<Object>();
    ArrayList<Object> identifiers = new ArrayList<Object>();
    public String GREEN = "\033[1m\033[32m";
    public String RED = "\033[1m\033[31m";
    public String PURPLE = "\033[1m\033[35m";
    public String RESET = "\u001B[0m";

    void keywords_init(){
        // PARAN
        keywords.add(new Keyword('(', TokenType.OPEN_PAREN));
        keywords.add(new Keyword(')', TokenType.CLOSE_PAREN));
        // NOT
        keywords.add(new Keyword('~', TokenType.NOT));
        keywords.add(new Keyword('-', TokenType.NOT));
        // AND
        keywords.add(new Keyword('&', TokenType.AND));
        keywords.add(new Keyword('^', TokenType.AND));
        // OR
        keywords.add(new Keyword('|', TokenType.OR));
        keywords.add(new Keyword('v', TokenType.OR));
        keywords.add(new Keyword('V', TokenType.OR));
        // XOR
        keywords.add(new Keyword('o', TokenType.XOR));
        keywords.add(new Keyword('O', TokenType.XOR));
        keywords.add(new Keyword('0', TokenType.XOR));
        // NOTHING
        keywords.add(new Keyword('\0', TokenType.NOTHING));
    }

    TokenType is_keyword(char c){
        for(Keyword keyword: keywords){ if(c == keyword.value){ return keyword.type; } }
        return TokenType.NOTHING;
    }

    void objects_init(String code){
        int i = 0;
        while(i < code.length()){
            char c = code.charAt(i);
            // check if c is a keyword
            if(is_keyword(c) != TokenType.NOTHING){
                Object obj = new Object(Character.toString(c), is_keyword(c));
                objects.add(obj);
                i++;
            }
            // check if c is a space
            else if(c == ' '){ i++; }
            // check if c is a identifier
            else{
                String id = "";
                while(i < code.length()){
                    c = code.charAt(i);        
                    if(is_keyword(c) != TokenType.NOTHING || c == ' '){ break; }
                    id += c;
                    i++;
                }
                boolean found = false;
                Object object_found = new Object("", TokenType.NOTHING);
                for(Object identifier: identifiers){
                    if(identifier.value.equals(id)){ 
                        found = true; 
                        object_found = identifier;
                        break; 
                    }
                }
                if(!found){ 
                    Object obj = new Object(id, TokenType.ID);
                    identifiers.add(obj); 
                    objects.add(obj);
                }
                else{ objects.add(object_found); }
            }

        }
                
    }
        
    void create_table(){
        int size = (int)Math.pow(2, identifiers.size());
        int increment = 2;
        for(int i = 0; i < identifiers.size(); i++){
            Object obj = identifiers.get(i);
            obj.create_table(size, increment);
            increment *= 2;
        }
    }
    
    public void print_objects(){
        for(Object obj: objects){
            System.out.println(obj.value + "\t" + obj.type);
        }
    }


    public Lexer(String code){
        System.out.println(PURPLE);
        System.out.println("\tLexing: " + code);
        System.out.println(RESET);
        keywords_init(); 
        objects_init(code);
        create_table();
    }
}
