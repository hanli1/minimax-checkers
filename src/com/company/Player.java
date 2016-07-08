package com.company;

import java.util.List;
import java.util.Random;

public class Player {
    private Side side;
    private String name;

    public Player()
    {}

    public enum Side
    {
        BLACK, WHITE
    }
    public Player(String name, Side side)
    {
        this.name = name;
        this.side = side;
    }
    public Player(Side side)
    {
        this.name = side.toString();
        this.side = side;
    }

    public Side getSide()
    {
        return side;
    }

    public Board.Decision makeMove(Move m, Board b)
    {
        return b.makeMove(m, side);
    }

    public Board.Decision makeRandomMove(Board b)
    {
        List<Move> moves = b.getAllValidMoves(side);
        Random rand = new Random();
        return b.makeMove(moves.get(rand.nextInt(moves.size())), side);
    }
    public String toString()
    {
        return name + "/" + side;
    }
}
