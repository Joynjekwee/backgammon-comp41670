/**
 * Represents a doubling cube in a game.
 * Manages the doubling stake and ownership between players.
 */

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

    // Opposing player accepts double
    public void acceptDoubling(Player player) {
        if(player == this.owner) {
            throw new IllegalStateException("Offering player can't accept the double");
        }
        this.owner = player;
        this.stake *= 2;
        this.doublingOffered = false;
    }

    // Resets offer status when doubling offer refused.
    public void refuseDoubling() {
        this.doublingOffered = false;
    }

    public Player getOwner() {
        return owner;
    }

    /**
     * Resets the doubling cube to its initial state.
     */
    public void reset() {
        this.stake = 1;
        this.doublingOffered = false;
        this.owner = null;
    }
}
