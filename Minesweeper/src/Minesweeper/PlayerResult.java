package Minesweeper;

//Return the state and gamestate, if state is false that means we could not choose that cell
public class PlayerResult {
    private boolean successful;
    private Gameplay.GameState state;
    public PlayerResult(boolean successful, Gameplay.GameState state) {
        this.successful = successful;
        this.state = state;
    }

    public boolean successfulPick() {
        return successful;
    }
    public Gameplay.GameState getState() {
        return state;
    }
}
