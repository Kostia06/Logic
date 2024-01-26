import java.util.ArrayList;

class Show{

    public static final String VERTICAL = "║";
    public static final String HORIZONTAL = "═";
    public static final String TOP_LEFT = "╔";
    public static final String TOP_RIGHT = "╗";
    public static final String BOTTOM_LEFT = "╚";
    public static final String BOTTOM_RIGHT = "╝";
    public static final String LEFT = "╠";
    public static final String RIGHT = "╣";
    public static final String TOP = "╦";
    public static final String BOTTOM = "╩";
    public static final String CROSS = "╬";

    public Show(ArrayList<Object> objects, ArrayList<Object> ids, ArrayList<ResultRow> result_rows){
        ArrayList<Integer> pieces = new ArrayList<Integer>();
        String info = "";
        for(Object id: ids){ info += id.name + VERTICAL; pieces.add(info.length()-1); }
        for(Object obj: objects){ info += obj.name + " "; }
        System.out.print(Main.PURPLE);
        // print top
        System.out.print(TOP_LEFT);
        String row = create_row(info.length(),TOP, pieces);
        System.out.println(row + TOP_RIGHT);
        // print info
        System.out.print(VERTICAL);
        System.out.print(info);
        System.out.println(VERTICAL);
        // print bottom
        System.out.print(BOTTOM_LEFT);
        row = create_row(info.length(),BOTTOM, pieces);
        System.out.println(row + BOTTOM_RIGHT);
        // print results
        for(ResultRow row_result: result_rows){
            print_row(row_result,ids, pieces);
        }
        

    }

    private String create_row(int size, String piece, ArrayList<Integer> args){
        String row = "";
        for(int i = 0; i < size; i++){ 
            if(args.contains(i)){ row += piece; }
            else{ row += HORIZONTAL; }
        }
        return row;
    }

    private void print_row(ResultRow row_result,ArrayList<Object> ids, ArrayList<Integer> pieces){
        System.out.println();
        int size = 0;
        String str = "";
        // print true / false
        str+= (VERTICAL);
        for(int i =0; i < ids.size(); i++){
            Boolean value = row_result.table.get(i);
            str += (value ? Main.TRUE : Main.FALSE);
            String name = ids.get(i).name;
            for(int j = 0; j < name.length()-1; j++){ str+=(" "); }
            str+=(VERTICAL);
            size+=name.length()+1;
        }
        // print result
        for(Result result: row_result.results){
            str+=(result.result ? Main.GREEN : Main.RED);
            str+=(result.row_op);
            str+=(Main.PURPLE + VERTICAL);
            pieces.add(size-1);
            size+=result.row_op.length()+1;
        }
        // remvoe the first ids.size() pieces
        System.out.println(TOP_LEFT + create_row(size-1, TOP, pieces) + TOP_RIGHT);
        System.out.println(str);
        System.out.println(LEFT+create_row(size-1, CROSS, pieces)+RIGHT);         
    
        str = "";
        str+=(VERTICAL);
        for(int i =0; i < ids.size(); i++){
            String name = ids.get(i).name;
            for(int j = 0; j < name.length(); j++){ str+=(" "); }
            str+=(VERTICAL);
        }
        // print result
        for(Result result: row_result.results){
            str+=(result.result ? Main.GREEN : Main.RED);
            str+=(result.row_result);
            str+=(Main.PURPLE + VERTICAL);
        }
        System.out.println(str);
        System.out.println(BOTTOM_LEFT+create_row(size-1, BOTTOM, pieces)+BOTTOM_RIGHT);         
    }
}
