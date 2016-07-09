package com.company;

import java.util.Scanner;

public class Main {

    public static double total = 1;
    public static boolean multipleRoundsTest = false;

    public static void main(String[] args) throws InterruptedException {

        multipleRoundsTest = total > 1;
        Player one = new Player("Player 1", Player.Side.BLACK);
        //Player two = new Player("Player 2", Player.Side.WHITE);

        //MinimaxAI one = new MinimaxAI(Player.Side.BLACK, 3);
        MinimaxAI two = new MinimaxAI(Player.Side.WHITE, 6);

        //RandomAI one = new RandomAI(Player.Side.BLACK);
        //RandomAI two = new RandomAI(Player.Side.WHITE);

        //one goes first if true;
        boolean turn = true;

        //System.out.println(board.toString());

        Scanner sc = new Scanner(System.in);

        int blackWin = 0;
        int whiteWin = 0;

        for(int t = 0; t< total; t++)
        {
            Board board = new Board();
            Player current = one;
            if(!turn)
                current = two;
            int c = 0;
            println(board.toString());
            while (c < 1000)
            {
                //Thread.sleep(500);
                c++;
                print(current.toString() + "'s turn: ");


                Board.Decision decision = null;
                if(current instanceof AI) {
                    decision = ((AI) current).makeMove(board);
                    println();
                }
                else {
                    String text = sc.nextLine();
                    if(text.equals("board"))
                    {
                        println(board.toString());
                    }
                    if (text.equals("rand"))
                    {
                        decision = current.makeRandomMove(board);
                    }
                    else
                    {
                        String[] split = text.split(" ");
                        Move m;
                        if(split.length == 1)
                        {
                            m = new Move(Integer.parseInt(text.charAt(0)+""), Integer.parseInt(text.charAt(1)+""),
                                    Integer.parseInt(text.charAt(2)+""), Integer.parseInt(text.charAt(3)+""));
                        }
                        else
                        {
                            int[] s = new int[split.length];
                            for(int i = 0; i< split.length; i++)
                            {
                                s[i] = Integer.parseInt(split[i]);
                            }
                            m = new Move(s[0], s[1], s[2], s[3]);


                        }
                        decision = current.makeMove(m, board);
                    }



                }

//            System.out.println("Decision: " + decision);
                if(decision == Board.Decision.FAILED_INVALID_DESTINATION || decision == Board.Decision.FAILED_MOVING_INVALID_PIECE)
                {
                    println("Move Failed");
                    // don't update anything
                }
                else if(decision == Board.Decision.COMPLETED)
                {
                    println(board.toString());
                    if(board.getNumBlackPieces() == 0)
                    {
                        println("White wins with " + board.getNumWhitePieces() + " pieces left");
                        whiteWin++;
                        break;
                    }
                    if(board.getNumWhitePieces() == 0)
                    {
                        println("Black wins with " + board.getNumBlackPieces() + " pieces left");
                        blackWin++;
                        break;
                    }
                    if(turn)
                        current = two;
                    else
                        current = one;
                    turn = !turn;

                }
                else if(decision == Board.Decision.ADDITIONAL_MOVE)
                {
                    println("Additional Move");
                }
                else if(decision == Board.Decision.GAME_ENDED)
                {
                    //current player cannot move
                    if(current.getSide() == Player.Side.BLACK)
                    {
                        println("White wins");
                        whiteWin++;

                    }
                    else {
                        println("Black wins");
                        blackWin++;
                    }
                    break;
                }



            }
            System.out.println("Game finished after: " + c + " rounds");
            if(one instanceof MinimaxAI)
                System.out.println("Avg time per move: " + ((MinimaxAI)one).getAverageTimePerMove());
        }
        System.out.println("Black won " + blackWin/total * 100+ "%" + ", White won " + whiteWin/total * 100 + "%");

    }

    public static void println(String s)
    {
        if(!multipleRoundsTest)
            System.out.println(s);
    }
    public static void print(String s)
    {
        if(!multipleRoundsTest)
            System.out.print(s);
    }
    public static void println()
    {
        if(!multipleRoundsTest)
            System.out.println();
    }
}
