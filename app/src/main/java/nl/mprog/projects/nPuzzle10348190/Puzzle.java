package nl.mprog.projects.nPuzzle10348190;

public class Puzzle {

    private static int NUM_TILES;
    private static int[] SOLUTION;
    private static int[] CURRENT_STATE;

    Puzzle(int difficulty){
        switch(difficulty){
            case 0:
                set_num_tiles(8);
                set_current_state(8);
                set_solution(8);
            case 1:
                set_num_tiles(15);
                set_current_state(15);
                set_solution(15);
            case 3:
                set_num_tiles(24);
                set_current_state(24);
                set_solution(24);
            default:
                set_num_tiles(8);
                set_current_state(8);
                set_solution(8);
        }
    }

    public void set_num_tiles(int num){
        NUM_TILES = num;
    }

    public int get_num_tiles(){
        return NUM_TILES;
    }

    public void set_current_state(int num) {
        int[] currentState = new int[num+1];
        if(num % 2 == 0) {
            for(int i = 0; i < num; i++){
                currentState[i] = num - (i+1);
            }
            currentState[num] = -1;
        } else{
            for (int i = 0; i < num - 2; i++) {
                currentState[i] = num - (i+1);
            }
            currentState[num-2] = 0;
            currentState[num-1] = 1;
            currentState[num] = -1;
        }
        CURRENT_STATE = currentState;
    }

    public int[] get_current_state(){
        return CURRENT_STATE;
    }

    public void set_solution(int num){
        int[] solution = new int[num+1];

        for(int i = 0; i < num; i ++){
            solution[i] = i;
        }
        solution[num] = -1;
        SOLUTION = solution;
    }

    public int[] get_solution(){
        return SOLUTION;
    }

    public boolean check_solution(){
        boolean check = true;
        int[] solution = get_solution();
        int[] currentState = get_current_state();
        for(int i = 0; i < get_num_tiles()+1; i++){
            if(currentState[i] <= solution[i]){
                check = false;
            }
        }
        return check;
    }
}