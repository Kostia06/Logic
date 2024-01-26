import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Math;

class Lexer{
    private String logic;
    private int index = 0;
    private int largest_op = 3;
    private char c = ' '; 
    private boolean declare = false;
    public static ArrayList<Operator> operators = new ArrayList<Operator>();
    public ArrayList<Object> objects = new ArrayList<Object>();
    public ArrayList<Object> ids = new ArrayList<Object>();

    public Lexer(String _logic){
        logic = _logic;
        ops_init();
        lex();
    }

    private boolean handle_op(){
        String str = "";
        // getting 
        for(int i = 0; i < Math.min(largest_op, logic.length()-index); i++){ str += logic.charAt(index+i); }
        // checking
        int str_len = str.length();
        for(int i = 0; i < str_len; i++){
            for(Operator op: operators){
                if(op.find(str)){ 
                    Object obj;
                    obj = new Object(str, op.type);
                    index += str.length();
                    c = logic.charAt(index-1);
                    objects.add(obj); 
                    return true; 
                }
            }
            str = str.substring(0, str.length()-1);
        }
        return false;
    }

    private boolean handle_id(){
        String str = "";
        while(index < logic.length() && Character.isLetterOrDigit(c)){
            c = logic.charAt(index++);
            if(!Character.isLetterOrDigit(c)){ break; }
            str += c;
        }
        if(str.length() > 0){
            Object obj = Object.find_object(ids, str);
            if(obj == null){
                obj = new Object(str, OP_TYPE.ID);
                ids.add(obj);
                if(declare){ 
                    if(index < logic.length()){ index--; }
                    declare = false;
                    return true;
                }
            }
            objects.add(obj);
            if(index < logic.length()){ index--; }
            return true; 
        }
        return false;
    } 

    private void lex(){
        while(index < logic.length()){
            c = logic.charAt(index);
            if(handle_op() || handle_id()){ continue; }
            else if(c == ' ' || c == '\t' || c == '\n'){ index++; continue; }
            else if(c == '\\'){ index += 2; }
            else if(c == '#'){ declare = true; index++; }
        }  
    }

    public void print_objects(){
        System.out.println(Main.PURPLE + "Objects:" + Main.RESET);
        for(Object obj: objects){ System.out.println(obj.name + " " + obj.type); }
    }


    public static final boolean find_and_apply(OP_TYPE type, Object left, Object right){
        for(Operator op: operators){
            if(op.type == type){ return op.apply(left, right); }
        }
        return false;
    }

    private void ops_init(){
        operators.add( Operator.new_op(
            OP_TYPE.OPEN_PAREN,
            new int[][]{ {-1,-1,-1}, {-1,-1,-1}, {-1,-1,-1}, {-1,-1,-1} },
            "("
        ));
        operators.add( Operator.new_op(
            OP_TYPE.CLOSE_PAREN,
            new int[][]{ {-1,-1,-1}, {-1,-1,-1}, {-1,-1,-1}, {-1,-1,-1} },
            ")"
        ));
        operators.add( Operator.new_op(
                OP_TYPE.NOT,
                new int[][]{ {-1, -1, -1}, {1, -1, -1}, {-1, -1, -1}, {-1, -1, -1} }, 
                "-", "~", "!", "not"
        ));
        operators.add( Operator.new_op(
                OP_TYPE.AND,
                new int[][]{ {1, 1, 1}, {1, 0, 0}, {0, 1, 0}, {0, 0, 0} }, 
                "&","&&", "^", "and"
        ));
        operators.add( Operator.new_op(
                OP_TYPE.OR,
                new int[][]{ {1, 1, 1}, {1, 0, 1}, {0, 1, 1}, {0, 0, 0} }, 
                "|", "||", "v", "V", "or"
        ));
        operators.add( Operator.new_op(
                OP_TYPE.XOR,
                new int[][]{ {1, 1, 0}, {1, 0, 1}, {0, 1, 1}, {0, 0, 0} }, 
                "0", "o", "O", "xor"
        ));
        operators.add( Operator.new_op(
                OP_TYPE.IF,
                new int[][]{ {1, 1, 1}, {1, 0, 0}, {0, 1, 1}, {0, 0, 1} },
                ">", "->", "if"
        ));
        operators.add( Operator.new_op(
                OP_TYPE.IFF,
                new int[][]{ {1, 1, 1}, {1, 0, 0}, {0, 1, 0}, {0, 0, 1} },
                "=", "<->", "<>", "iff"
        ));
    }
}
