package com.company;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinimaxAI extends Player implements AI{

    private Point skippingPoint;
    private int depth;
    private long totalTimeElapsed;
    private double numMovesCalled;
    private int pruned = 0;
    public MinimaxAI(String name, Side s)
    {
        super(name, s);
    }
    public MinimaxAI(Side s, int depth)
    {
        super("MinimaxAI", s);
        this.depth = depth;
        this.totalTimeElapsed = 0;
    }
    public Board.Decision makeMove(Board board)
    {
        numMovesCalled++;
        long startTime = System.nanoTime();
        Move m = minimaxStart(board, depth, getSide(), true);
        totalTimeElapsed += System.nanoTime() - startTime;
        //System.out.println("m is: " + m);
        //Move move = board.getAllValidMoves(getSide()).get(m);
        Board.Decision decision = board.makeMove(m, getSide());
        if(decision == Board.Decision.ADDITIONAL_MOVE)
            skippingPoint = m.getEnd();

        //System.out.println("Pruned tree: " + pruned + " times");
        return decision;
    }
    public String getAverageTimePerMove()
    {
        return totalTimeElapsed/numMovesCalled * Math.pow(10, -6) + " milliseconds";
    }
    private Move minimaxStart(Board board, int depth, Side side, boolean maximizingPlayer)
    {
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        List<Move> possibleMoves;
        if(skippingPoint == null)
            possibleMoves = board.getAllValidMoves(side);
        else
        {
            possibleMoves = board.getValidSkipMoves(skippingPoint.x, skippingPoint.y, side);
            skippingPoint = null;
        }
        //System.out.println("side: " + side + " " + possibleMoves.size());

        List<Double> heuristics = new ArrayList<>();
        if(possibleMoves.isEmpty())
            return null;
        Board tempBoard = null;
        for(int i = 0; i < possibleMoves.size(); i++)
        {
            tempBoard = board.clone();
            tempBoard.makeMove(possibleMoves.get(i), side);
            heuristics.add(minimax(tempBoard, depth - 1, flipSide(side), !maximizingPlayer, alpha, beta));
        }
        //System.out.println("\nMinimax at depth: " + depth + "\n" + heuristics);

        double maxHeuristics = Double.NEGATIVE_INFINITY;

        Random rand = new Random();
        for(int i = heuristics.size() - 1; i >= 0; i--) {
            if (heuristics.get(i) >= maxHeuristics) {
                maxHeuristics = heuristics.get(i);
            }
        }
        //Main.println("Unfiltered heuristics: " + heuristics);
        for(int i = 0; i < heuristics.size(); i++)
        {
            if(heuristics.get(i) < maxHeuristics)
            {
                heuristics.remove(i);
                possibleMoves.remove(i);
                i--;
            }
        }
        //Main.println("Filtered/max heuristics: " + heuristics);
        return possibleMoves.get(rand.nextInt(possibleMoves.size()));
    }

    private double minimax(Board board, int depth, Side side, boolean maximizingPlayer, double alpha, double beta)
    {
        if(depth == 0) {
            return getHeuristic(board);
        }
        List<Move> possibleMoves = board.getAllValidMoves(side);

        double initial = 0;
        Board tempBoard = null;
        if(maximizingPlayer)
        {
            initial = Double.NEGATIVE_INFINITY;
            for(int i = 0; i < possibleMoves.size(); i++)
            {
                tempBoard = board.clone();
                tempBoard.makeMove(possibleMoves.get(i), side);

                double result = minimax(tempBoard, depth - 1, flipSide(side), !maximizingPlayer, alpha, beta);

                initial = Math.max(result, initial);
                alpha = Math.max(alpha, initial);

                if(alpha >= beta)
                    break;
            }
        }
        //minimizing
        else
        {
            initial = Double.POSITIVE_INFINITY;
            for(int i = 0; i < possibleMoves.size(); i++)
            {
                tempBoard = board.clone();
                tempBoard.makeMove(possibleMoves.get(i), side);

                double result = minimax(tempBoard, depth - 1, flipSide(side), !maximizingPlayer, alpha, beta);

                initial = Math.min(result, initial);
                alpha = Math.min(alpha, initial);

                if(alpha >= beta)
                    break;
            }
        }

        return initial;
    }

    private double getHeuristic(Board b)
    {
        //naive implementation
//        if(getSide() == Side.WHITE)
//            return b.getNumWhitePieces() - b.getNumBlackPieces();
//        return b.getNumBlackPieces() - b.getNumWhitePieces();

        double kingWeight = 1.2;
        double result = 0;
        if(getSide() == Side.WHITE)
            result = b.getNumWhiteKingPieces() * kingWeight + b.getNumWhiteNormalPieces() - b.getNumBlackKingPieces() *
                    kingWeight -
                    b.getNumBlackNormalPieces();
        else
            result = b.getNumBlackKingPieces() * kingWeight + b.getNumBlackNormalPieces() - b.getNumWhiteKingPieces() *
                    kingWeight -
                    b.getNumWhiteNormalPieces();
        return result;

    }

    private Side flipSide(Side side)
    {
        if(side == Side.BLACK)
            return Side.WHITE;
        return Side.BLACK;
    }
}
