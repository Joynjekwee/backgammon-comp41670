public class DoublingCube {
    private int stake;
    private Player owner;
    private boolean doublingOffered;

    public DoublingCube() {
        this.stake = 1; // Default stake
        this.owner = null; // No owner initially
        this.doublingOffered = false;
    }

    public int getStake() {
        return stake;
    }

    public boolean isDoublingOffered() {
        return doublingOffered;
    }

    public void offerDoubling(Player player) {
        this.owner = player;
        this.doublingOffered = true;
    }

    public void acceptDoubling() {
        this.stake *= 2;
        this.doublingOffered = false;
    }

    public void refuseDoubling() {
        this.doublingOffered = false;
    }

    public Player getOwner() {
        return owner;
    }
}
