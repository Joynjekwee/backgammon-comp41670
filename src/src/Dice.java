public class Dice {

    private int[] numbers;
    private final int NUMOFDICE = 2;
    public Dice() {
        numbers = new int[]{1,1};
    }

    public void rollSingleDie(){
        numbers[0] = (int)(Math.random()*6+1);
    }

    public void rollDice(){
        for(int i = 0; i < NUMOFDICE; i++){
            numbers[i] = (int)(Math.random()*6+1);
        }
    }

    public int getDie(int index){
        if (index >= 0 && index < NUMOFDICE) {
            return numbers[index];
        } else {
            throw new IllegalArgumentException("Invalid die index");
        }
    }

    public String toString(){
        return "Dice 1: " + numbers[0] + ", Dice 2: " + numbers[1];
    }
}
