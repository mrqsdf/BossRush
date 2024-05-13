package fr.mrqsdf.bossrush.res;

public enum GameState {

    MOVE,
    CAN_MOVE,
    WAIT,
    PLAYER_ACTION,
    MOB_ATTACK,
    ;

    public static GameState gameState = WAIT;

}
