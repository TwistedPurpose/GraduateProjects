# File: Player.py
# Author: Soren Ludwig
# Date: 1/23/16
# Defines a simple artificially intelligent player agent
# You will define the alpha-beta pruning search algorithm
# You will also define the score function in the MancalaPlayer class,
# a subclass of the Player class.


from random import *
from decimal import *
from copy import *
from MancalaBoard import *

# a constant
INFINITY = 1.0e400

def max(a, b):
    if a>=b:
        return a
    return b
def min(a, b):
    if a<=b:
        return a
    return b

class Player:
    """ A basic AI (or human) player """
    HUMAN = 0
    RANDOM = 1
    MINIMAX = 2
    ABPRUNE = 3
    CUSTOM = 4
    
    def __init__(self, playerNum, playerType, ply=0):    
        self.num = playerNum
        self.opp = 2 - playerNum + 1
        self.type = playerType
        self.ply = ply

    def __repr__(self):
        return str(self.num)
        
    def minimaxMove( self, board, ply ):
        """ Choose the best minimax move.  Returns (move, val) """
        move = -1
        score = -INFINITY
        turn = self
        for m in board.legalMoves( self ):
            if ply == 0:
                return (self.score(board), m)
            if board.gameOver():
                return (-1, -1)  # Can't make a move, the game is over
            nb = deepcopy(board)
            nb.makeMove(self, m)
            opp = Player(self.opp, self.type, self.ply)
            s, oppMove = opp.minValue(nb, ply-1, turn)
            if s > score:
                move = m
                score = s
        return score, move

    def maxValue( self, board, ply, turn):
        """ Find the minimax value for the next move for this player
            at a given board configuation
            Returns (score, oppMove)"""
        if board.gameOver():
            return (turn.score( board ), -1)
        score = -INFINITY
        move = -1
        for m in board.legalMoves( self ):
            if ply == 0:
                return (turn.score( board ), m)
            # make a new player to play the other side
            opponent = Player(self.opp, self.type, self.ply)
            # Copy the board so that we don't ruin it
            nextBoard = deepcopy(board)
            nextBoard.makeMove( self, m )
            s, oppMove = opponent.minValue(nextBoard, ply-1, turn)
            if s > score:
                move = m
                score = s
        return (score, move)
    
    def minValue( self, board, ply, turn ):
        """ Find the minimax value for the next move for this player
            at a given board configuation"""
        if board.gameOver():
            return turn.score( board ), -1
        score = INFINITY
        move = -1
        for m in board.legalMoves( self ):
            if ply == 0:
                return (turn.score( board ), m)
            # make a new player to play the other side
            opponent = Player(self.opp, self.type, self.ply)
            # Copy the board so that we don't ruin it
            nextBoard = deepcopy(board)
            nextBoard.makeMove( self, m )
            s, oppMove = opponent.maxValue(nextBoard, ply-1, turn)
            if s < score:
                score = s
                move = m
        return (score, move)


    # The default player defines a very simple score function
    # You will write the score function in the MancalaPlayer below
    # to improve on this function.
    def score(self, board):
        """ Returns the score for this player given the state of the board """
        if board.hasWon( self.num ):
            return 100.0
        elif board.hasWon( self.opp ):
            return 0.0
        else:
            return 50.0

    # You should not modify anything before this point.
    # The code you will add to this file appears below this line.

    # You will write this function (and any helpers you need)
    # You should write the function here in its simplest form:
    #   1. Use ply to determine when to stop (when ply == 0)
    #   2. Search the moves in the order they are returned from the board's
    #       legalMoves function.
    # However, for your custom player, you may copy this function
    # and modify it so that it uses a different termination condition
    # and/or a different move search order.
    def alphaBetaMove( self, board, ply ):
        """ Choose a move with alpha beta pruning """
        move = -1
        score = -INFINITY
        turn = self

        a = -INFINITY
        b = INFINITY

        for m in board.legalMoves( self ):
            if ply == 0:
                return (self.score(board), m)
            if board.gameOver():
                return(-1,-1)

            nextBoard = deepcopy(board)
            nextBoard.makeMove(self,m)
            opponent = Player(self.opp, self.type, self.ply)
            s, oppMove, aReturn, bReturn = opponent.minValueAB(nextBoard,ply-1,turn,a,b)

            if s > score:
                move = m
                score = s
                a = score

        return score, move
        
    def maxValueAB( self, board, ply, turn, a, b):
        """ Find the minimax value for the next move for this player
            at a given board configuation
            Returns (score, oppMove)"""
        if board.gameOver():
            return (turn.score( board ), -1, a, b)
        score = -INFINITY
        move = -1

        for m in board.legalMoves( self ):
            if ply == 0:
                return (turn.score( board ), m, a, b)

            opponent = Player(self.opp, self.type, self.ply)
            nextBoard = deepcopy(board)
            nextBoard.makeMove( self, m )
            s, oppMove, aReturn, bReturn = opponent.minValueAB(nextBoard, ply-1, turn, a, b)

            if s >= b:
                score = b
                move = m
                break

            if s > score:
                score = s
                move = m
                a = score
        
        return (score, move, a, b)
    
    def minValueAB( self, board, ply, turn, a, b):
        """ Find the minimax value for the next move for this player
            at a given board configuation"""
        if board.gameOver():
            return turn.score( board ), -1, a, b

        score = INFINITY
        move = -1

        for m in board.legalMoves( self ):
            if ply == 0:
                return (turn.score( board ), m, a, b)
            opponent = Player(self.opp, self.type, self.ply)
            nextBoard = deepcopy(board)
            nextBoard.makeMove( self, m )
            s, oppMove, aReturn, bReturn = opponent.maxValueAB(nextBoard, ply-1, turn, a, b)

            if s <= a:
                score = a
                move = m
                break

            if s < score:
                score = s
                move = m
                b = score

        return (score, move, a, b)

                
    def chooseMove( self, board ):
        """ Returns the next move that this player wants to make """
        if self.type == self.HUMAN:
            move = input("Please enter your move:")
            while not board.legalMove(self, move):
                print move, "is not valid"
                move = input( "Please enter your move" )
            return move
        elif self.type == self.RANDOM:
            move = choice(board.legalMoves(self))
            print "chose move", move
            return move
        elif self.type == self.MINIMAX:
            val, move = self.minimaxMove( board, self.ply )
            print "chose move", move, " with value", val
            return move
        elif self.type == self.ABPRUNE:
            val, move = self.alphaBetaMove( board, self.ply)
            print "chose move", move, " with value", val
            return move
        elif self.type == self.CUSTOM:
            val, move = self.alphaBetaMove( board, 10)
            print "chose move", move, " with value", val
            return move
        else:
            print "Unknown player type"
            return -1



class SuperSoren(Player):
    """ Defines a player that knows how to evaluate a Mancala gameboard
        intelligently """

    def zeroCounting(self, board):
        score = 0
        isFirst = True
        myCups = board.getPlayersCups(self.num)
        opponentCups = board.getPlayersCups(self.opp)[::-1]

        if board.hasWon( self.num ):
            return INFINITY
        elif board.hasWon( self.opp ):
            return -INFINITY
        else:
            for idx, myCup in enumerate(myCups):
                if myCup == 0 and not isFirst:
                    score = score + opponentCups[idx]/2
                isFirst = False

            isFirst = True
            for idx, opponentCup in enumerate(opponentCups):
                if opponentCup == 0 and not isFirst:
                    score = score - myCups(idx)/2
        score = score + board.scoreCups[self.num-1]
        score = score - board.scoreCups[self.opp-1]

        return score

    def score(self, board):
        """ Evaluate the Mancala board for this player """
        score = 0

        score = SuperSoren.zeroCounting(self, board)
        
        return score


