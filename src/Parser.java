import java.util.ArrayList;
import java.lang.Math;

class Parser{
    
    Table table;
    int count = 1;
    int truths = 0;
    int falses = 0;
    ArrayList<String> ops = new ArrayList<String>();

    public Parser(ArrayList<Object> objects, ArrayList<Object> ids){

        int row_size = (int)Math.pow(2,ids.size());
        table = new Table(ids.size(), row_size);


        for(int i = 0; i < row_size; i++){
            ArrayList<Object> ids_copy = new ArrayList<Object>();
            ArrayList<Object> objects_copy = copy(objects, ids_copy);
            
            table.apply(ids_copy, i);
            Object obj = parse(objects_copy);
            boolean result = obj.result;

            System.out.print(Main.PURPLE+"Lexing: " + Main.logic);
            if(result){ System.out.println(Main.GREEN);}
            else{ System.out.println(Main.RED); }
           
            for(Object id: ids_copy){ System.out.print(id.name + ": " + id.result + "\t"); }
            System.out.println();
            for(String op: ops){ System.out.println(op); }
            System.out.println("Result: " + result + "\n");
            if(result){ truths++; }
            else{ falses++; }

            count = 1;
            ops.clear();
        }
        System.out.println("\n"+ Main.PURPLE + truths + " truths\t" + falses + " falses\t" + (truths+falses) + " total" + Main.RESET);
        System.out.println(Main.RESET);
    }


    ArrayList<Object> copy(ArrayList<Object> objects, ArrayList<Object> ids){
        ArrayList<Object> objects_copy = new ArrayList<Object>();

        for(Object obj: objects){
            Object id_copy = Object.find_object(ids, obj.name);
            if(obj.type == OP_TYPE.PAREN){
                Object obj_copy = Object.copy(obj);
                obj_copy.children = copy(obj.children, ids);
                objects_copy.add(obj_copy);
            }
            else if(id_copy != null && id_copy.name.equals(obj.name)){
                objects_copy.add(id_copy); 
            }
            else{ 
                Object obj_copy = Object.copy(obj);
                objects_copy.add(obj_copy); 
                if(obj.type == OP_TYPE.ID){ 
                    ids.add(obj_copy); 
                }
            }

        }
        return objects_copy;
    }



    Object parse(ArrayList<Object> objects){
       int index = 0;
       boolean not = false;
       while(index < objects.size()){
            Object obj = objects.get(index);
            if(obj.type == OP_TYPE.PAREN){
                Object result = parse(obj.children);
                objects.set(index, result);
                index = 0;
                continue;
            }
            else if(obj.type == OP_TYPE.NOT){
                not = !not;
                objects.remove(index);
                continue;
            }  
            else if(obj.is_op()){
                Object left = objects.get(index-1);
                Object op = obj;
                Object right = objects.get(index+1);
                if(left.type != OP_TYPE.ID || right.type != OP_TYPE.ID){ index++; continue; }
                Object result = new Object(""+count, OP_TYPE.ID);
                result.result = Lexer.find_and_apply(op.type, left, right);
                ops.add("\t"+count++ + ": " + left.name + "\t" + op.type + "\t" + right.name + "\t=\t" + result.result);
                objects.set(index-1, result);
                objects.remove(index);
                objects.remove(index);
                index = 0;
                continue;
            }
            else if(obj.type == OP_TYPE.ID){
                if(not){ 
                    obj.result = !obj.result; not = false; index = 0;
                }
            }
            index++;
        
       }
       return objects.get(0);
    }
}
