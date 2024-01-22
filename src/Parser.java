import java.util.ArrayList;
import java.lang.Math;



class Parser{
    public String GREEN = "\033[1m\033[32m";
    public String RED = "\033[1m\033[31m";
    public String PURPLE = "\033[1m\033[35m";
    public String RESET = "\u001B[0m";
    ArrayList<Boolean> results = new ArrayList<Boolean>();
    int index;


    int count_ids(ArrayList<Object> objects){
        ArrayList<String> ids = new ArrayList<String>();
        int count = 0;
        for(int i = 0; i < objects.size(); i++){
            if(objects.get(i).type == TokenType.ID){ 
                if(!ids.contains(objects.get(i).value)){ 
                    ids.add(objects.get(i).value);  count++; 
                }
            }
        }
        return count;
    }

    void create_table_for_ids(ArrayList<Object> objects, int size){
        ArrayList<String> ids = new ArrayList<String>();
        for(int i = 0; i < objects.size(); i++){
            if(objects.get(i).type == TokenType.ID && !ids.contains(objects.get(i).value)){ 
                objects.get(i).create_table(size, (int)Math.pow(2, ids.size()+1));
                ids.add(objects.get(i).value);
            }
        }
    } 


    public Parser(ArrayList<Object> objects){
        int truths = 0;
        int falses = 0;
        int size = (int)Math.pow(2, count_ids(objects));
        for(index = 0; index < size; index++){
            System.out.println("-------------------------------------------------------");
            ArrayList<Object> objects_copy = new ArrayList<Object>(objects);
            create_table_for_ids(objects, size); 
            // set the results for each id
            ArrayList<String> ids = new ArrayList<String>();
            ArrayList<Boolean> ids_results = new ArrayList<Boolean>();
            for(int i = 0; i < objects_copy.size(); i++){
                if(objects_copy.get(i).type == TokenType.ID && !ids.contains(objects_copy.get(i).value)){ 
                    objects_copy.get(i).set_result(index);
                    ids.add(objects_copy.get(i).value);
                    ids_results.add(objects_copy.get(i).table.get(index));
                }
            }
            System.out.println();
            // print the current logic expression
            Object result = parse(objects_copy);
            // print the results
            if(result.result){ System.out.print(GREEN); }
            else{ System.out.print(RED); }
            System.out.print("\t");
            for(int i = 0; i < ids_results.size(); i++){
                System.out.print(ids.get(i) + " -> " + ids_results.get(i) + "\t");
            }
            System.out.println();
            System.out.print("\t");
            for(int i = 0; i < objects.size(); i++){
                System.out.print(objects.get(i).value + " ");
            }
            System.out.println();
            System.out.println("\tResult: " + result.result);
            System.out.println(RESET);
            // count the truths and falses
            if(result.result){ truths++; }
            else{ falses++; }
        }
        System.out.println("-------------------------------------------------------");
        System.out.println(PURPLE);
        System.out.println("\tTotal: " + (truths + falses));
        System.out.println("\tTruths: " + truths);
        System.out.println("\tFalses: " + falses);
        System.out.println(RESET);
    }

    Object parse(ArrayList<Object> objects){
        int i = 0;
        boolean not = false;
        while(i < objects.size()){
            Object obj = objects.get(i);
            // handle sub expressions
            if(obj.type == TokenType.OPEN_PAREN){
                ArrayList<Object> sub_objects = new ArrayList<Object>();
                int paren_count = 1;
                objects.remove(i);

                while(i < objects.size()){
                    Object sub_obj = objects.get(i);
                    objects.remove(i);
                    if(sub_obj.type == TokenType.OPEN_PAREN){ paren_count++; }
                    else if(sub_obj.type == TokenType.CLOSE_PAREN){ paren_count--; }
                    if(paren_count == 0){ break; }
                    sub_objects.add(sub_obj);
                }
                Object result = parse(sub_objects);
                objects.add(i, result);
                i = 0;
            }
            // handle nots
            else if(obj.type == TokenType.NOT){
                not = !not;
                objects.remove(i);
                continue;
            }
            // handle 2 value expression
            else if(obj.type == TokenType.AND || obj.type == TokenType.OR || obj.type == TokenType.XOR){
                Object obj1 = objects.get(i-1);
                Object op  =  objects.get(i);   
                Object obj2 = objects.get(i+1);
                if(obj1.type != TokenType.RESULT && obj1.type != TokenType.ID){ i++; continue; }
                if(obj2.type != TokenType.RESULT && obj2.type != TokenType.ID){ i++; continue; }
                i--;
                boolean result = false;
                if(op.type == TokenType.AND){ result = obj1.get_value() && obj2.get_value(); }
                else if(op.type == TokenType.OR){ result = obj1.get_value() || obj2.get_value(); }
                else if(op.type == TokenType.XOR){ result = obj1.get_value() ^ obj2.get_value(); }
                objects.remove(i);
                objects.remove(i);
                objects.remove(i);
                objects.add(i, new Object("res" ,TokenType.RESULT));
                objects.get(i).result = result;

            }
            // handle the value if they are "not"
            else if(obj.type == TokenType.ID || obj.type == TokenType.RESULT){
                if(not){ 
                    obj.result = obj.result ? false : true ; not = false;
                    i = 0;
                    continue;
                }
            }
            
            i++;
        }
        return objects.get(0);
    }
}
