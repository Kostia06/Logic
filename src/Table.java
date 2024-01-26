import java.util.ArrayList;

class Table{
    public boolean[][] table;
    private int size;

    public Table(int size, int row_size){
        table = new boolean[size][row_size];
        this.size = size;
        int increment = 2;
        for(int i = 0; i < size; i++){
            int half = (int)row_size/increment;
            int count = 0;
            boolean bool = true;
            for(int j = 0; j < row_size; j++){
                if(count == half){ count = 0; bool = !bool;  }
                table[i][j] = bool;
                count++;
            }
            increment *= 2;
        }
        int half = row_size / increment;
    }

    ArrayList<Boolean> apply(int index){
        ArrayList<Boolean> results = new ArrayList<Boolean>();
        for(int i = 0; i < size;i++){
            results.add(table[i][index]);
        }
        return results;
    }
}
