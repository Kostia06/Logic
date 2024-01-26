import java.util.ArrayList;
import java.lang.Math;

class Result{
    ArrayList<Boolean> table; 
    boolean result;
    String row_op = "";
    String row_result = "";

    Result(String row_op, int size){
        this.row_op = row_op;
        for(int i = 0; i < size; i++){ row_result += " "; }
    }
    
    private String addChar(String str, char ch, int index) {
        char[] chars = str.toCharArray();
        chars[index] = ch;
        return String.valueOf(chars);  
    }

    public void apply(boolean value, int index){
        row_result = addChar(row_result, value ? Main.TRUE : Main.FALSE, index);
    }

    public static int get_mid(int size){ 
        if(size == 1){ return 0; }
        if(size == 2){ return 0; }
        return (int)Math.floor(size/2); 
    }

    
}

class ResultRow{
    public ArrayList<Result> results;
    public ArrayList<Boolean> table;
    public boolean result;

    public ResultRow(ArrayList<Result> _results, boolean _result){
        this.results = _results;
        this.result = _result;
        table = new ArrayList<Boolean>();
    }

    public void apply(boolean value){ table.add(value); }
}


class Parser{
    
    private Table table;
    private ArrayList<String> ops = new ArrayList<String>();    
    private ArrayList<Result> results; 
    ArrayList<ResultRow> result_table = new ArrayList<ResultRow>();


    public Parser(ArrayList<Object> objects, ArrayList<Object> ids){
        int row_size = (int)Math.pow(2,ids.size());
        table = new Table(ids.size(), row_size);

        for(int i = 0; i < row_size; i++){
            results = new ArrayList<Result>();
            ArrayList<Boolean> table_values =  table.apply(i);

            ArrayList<Object> objects_copy = copy(objects, ids, table_values);

            ResultRow result_row = new ResultRow(results, parse_tokens(objects_copy).result);
            for(Boolean value: table_values){ result_row.apply(value); }
            result_table.add(result_row);
        }
    }



    private Object parse_tokens(ArrayList<Object> objects){
        int index = 0;
        while(index < objects.size()){
            Object obj = objects.get(index);
            if(obj.type == OP_TYPE.OPEN_PAREN){
                index = handle_paren(objects, index);
            }
            else if(OP_TYPE.OP_START.ordinal() <= obj.type.ordinal() && obj.type.ordinal() <= OP_TYPE.OP_END.ordinal()){
                index = handle_op(objects, index);
            }
            else if(objects.get(index).type == OP_TYPE.ID){
                index++;
            }
            else if(objects.get(index).type == OP_TYPE.NOT){
                index = handle_not(objects, index);
            }
        }
        return objects.get(0);
    }


    private int handle_paren(ArrayList<Object> objects, int index){
        ArrayList<Object> children = new ArrayList<Object>();
        int count = 1;
        objects.remove(index);
        while(index < objects.size()){
            if(objects.get(index).type == OP_TYPE.CLOSE_PAREN){ count--; }
            else if(objects.get(index).type == OP_TYPE.OPEN_PAREN){ count++; }
            if(count == 0){ break; }
            children.add(objects.get(index));
            objects.remove(index);
        }

        if(objects.get(index).type == OP_TYPE.CLOSE_PAREN){
            Object obj = parse_tokens(children);
            objects.set(index, obj );
            index = 0;
        }
        else{
            System.out.println(Main.RED + "Error: Parenthesis mismatch" + Main.RESET);
            System.exit(1);
        }
        return index;
    }

    private int handle_op(ArrayList<Object> objects, int index){
        if(index == 0 || index == objects.size()-1){ 
            System.out.println(Main.RED + "Error: Operator \"" + objects.get(index).name +"\" has no values to operate on" + Main.RESET);
            System.exit(1);
        }
        Object left = objects.get(index-1);
        Object op = objects.get(index);
        Object right = objects.get(index+1);
        if(right.type != OP_TYPE.ID){ return index+1; }
        Object obj = new Object("("+left.name + " " + op.name + " "+ right.name+")", OP_TYPE.ID);
        obj.result = Lexer.find_and_apply(op.type, left, right);
       
        String operation = "("+left.name + " " + op.name + " " + right.name + ")";
        
        Result result = new Result(operation, operation.length());
        int index1 = Result.get_mid(left.name.length())+1;
        result.apply(left.result, index1);
        int index2 = Result.get_mid(op.name.length())+2 + left.name.length();
        result.apply(obj.result, index2);
        int index3 = Result.get_mid(right.name.length())+3 + left.name.length() + op.name.length();
        result.apply(right.result, index3);
        result.result = obj.result;
        results.add(result);

        objects.set(index-1, obj);
        objects.remove(index);
        objects.remove(index);
        return Math.max(index - 2,0);
    }

    private int handle_not(ArrayList<Object> objects, int index){
        if(index+1 >= objects.size()){ 
            System.out.println(Main.RED + "Error: Operator \"" + objects.get(index).name +"\" has no values to operate on" + Main.RESET);
            System.exit(1);
        }
        Object op = objects.get(index);
        Object right = objects.get(index+1);
        if(right.type != OP_TYPE.ID){ return index+1; }
        right.result = !right.result;

        String operation = op.name + right.name;
        int index1 = Result.get_mid(op.name.length());
        int index2 = Result.get_mid(right.name.length()+op.name.length());
        if(index2 == 0){ index2 = 1; }
        
        Result result = new Result(operation, operation.length());
        right.add_not(op.name);

        objects.remove(index);
        return Math.max(index-2,0); 
    }


    ArrayList<Object> copy(ArrayList<Object> objects, ArrayList<Object> ids, ArrayList<Boolean> values){
        ArrayList<Object> objects_copy = new ArrayList<Object>();
        ArrayList<Object> ids_copy = new ArrayList<Object>();

        for(Object obj: objects){
            Object obj_copy = Object.copy(obj);
            for(int i = 0; i < ids.size(); i++){
                if(obj_copy.name.equals(ids.get(i).name)){
                    obj_copy.result = values.get(i);
                }
            }
            objects_copy.add(obj_copy);
        }
        return objects_copy;
    }

}

