public class Move {



    public boolean moveChecker(int startpt, int endpt, Checker checker){

        if(!legalMove(startpt,endpt,checker))
            return false;
        return true;
    }

    public boolean legalMove(int startpt, int endpt, Checker checker){
        boolean legal = true;
        return legal;
    }
}
