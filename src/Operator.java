import java.util.ArrayList;

enum OP_TYPE{
    OPEN_PAREN, CLOSE_PAREN,
    NOT, 
    OP_START,
        AND, OR, XOR,
        IF, IF_AND_ONLY_IF, 
    OP_END,
    ID, PAREN,
    NONE
}


class Operator{
    ArrayList<String> names;
    OP_TYPE type;
    int[][] binary;

    public Operator(ArrayList<String> _names, int[][] _binary, OP_TYPE _type){
        this.names = _names;
        this.type = _type;
        this.binary = _binary;
    }

    public static final Operator new_op(OP_TYPE _type, int[][] _binary, String... _names){
        ArrayList<String> names_list = new ArrayList<String>();
        for(String name: _names){ names_list.add(name); }
        return new Operator(names_list,_binary, _type);
    }

    public boolean find(String str){
        for(String name: names){ if(name.equals(str)){ return true; } }
        return false;
    }

    boolean apply(Object left, Object right){
        for(int i = 0; i < binary.length; i++){
            if(left.result == (binary[i][0] == 1) && right.result == (binary[i][1] == 1)){
                return binary[i][2] == 1;
            }
        }
        return false;
    }
}
