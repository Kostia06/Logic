import java.util.ArrayList;

class Object{
    String name;
    boolean result;
    OP_TYPE type;
    ArrayList<Object> children = new ArrayList<Object>();

    public Object(String name, OP_TYPE type){
        this.name = name;
        this.type = type;
        this.result = false;
    }

    public static final Object find_object(ArrayList<Object> objects, String name){
        for(Object obj: objects){ if(obj.name.equals(name)){ return obj; } }
        return null;
    }

    public static final Object copy(Object object){
        Object obj = new Object(object.name, object.type);
        obj.result = object.result;
        for(Object child: object.children){ obj.children.add(Object.copy(child)); }
        return obj;
    }

    boolean is_op(){
        return type.ordinal() >= OP_TYPE.OP_START.ordinal() && type.ordinal() <= OP_TYPE.OP_END.ordinal();
    }

    void add_not(String not){
        name = not + name;
    }

    void print(int depth){
        for(int i = 0; i < depth; i++){ System.out.print("\t"); }
        System.out.println(name + "\t" + type + "\t" + result);
        for(Object child: children){ child.print(depth+1); }
    }
}

