import java.util.*;

public class AIAgent{
  Random rand;

  public AIAgent(){
    rand = new Random();
  }

/*
  The method randomMove takes as input a stack of potential moves that the AI agent
  can make. The agent uses a rondom number generator to randomly select a move from
  the inputted Stack and returns this to the calling agent.
*/
  /* start of random move */
  public Move randomMove(Stack possibilities){

    int moveID = rand.nextInt(possibilities.size());
    System.out.println("Agent randomly selected move : "+moveID);
    for(int i=1;i < (possibilities.size()-(moveID));i++){
      possibilities.pop();
    }
    Move selectedMove = (Move)possibilities.pop();
    return selectedMove;
  }
  /* end of random move */

  // start of next best move

  public Move nextBestMove(Stack possiblewhitemoves, Stack possibleblackmoves) {// white and black stack possiblilites are used so we could get the moves
    Stack nextbestmove = (Stack) possiblewhitemoves.clone();
    Stack blackStack = (Stack) possibleblackmoves.clone(); // blackstack cloned of off blackpossibilites and put into its own variable
    Move bnextMove;
    Move  whiteMove; //testing- list of all the white AI moves
    Square PositionOfBlack;
    int Score = 0;
    int pieceScore = 0;
    Move bestMove = null;
    while (!possiblewhitemoves.empty()) { // keep popping until the white stack is empty 
    whiteMove = (Move) possiblewhitemoves.pop();// taking the top move
    bnextMove = whiteMove; // takes the best move 
    while (!blackStack.isEmpty()) { // keep popping until the black stack is empty
      Score = 0;
          PositionOfBlack = (Square) blackStack.pop();
          // bnextmove is getting the landing XC for the AI and checking to see if they are = to the black XC, and it does the excat same on the YC checking to see if the AI lands on black postions
          if ((bnextMove.getLanding().getXC() == PositionOfBlack.getXC()) && (bnextMove.getLanding().getYC() == PositionOfBlack.getYC())) {
                    //Assign Score to pieces

                    if (PositionOfBlack.getName().equals("BlackKing")){
                      Score = 10;
                    }
                    else if (PositionOfBlack.getName().equals("BlackQueen")) {
                      Score = 9;
                  } else if (PositionOfBlack.getName().equals("BlackRook")) {
                      Score = 5;
                  } else if (PositionOfBlack.getName().equals("BlackBishup") || PositionOfBlack.getName().equals("BlackKnight")) { // bishop and knight take the same score as they are seen the same values
                      Score = 3;
                  } else if (PositionOfBlack.getName().equals("BlackPawn")) {
                      Score = 1;
                  } 
                }
                if (pieceScore > 0) {
                  System.out.println("AI AGENT SELECTED - Next Best Move: " +pieceScore); // make the best move if avaliable if not make a random move 
                  return bestMove; // displays once the AI has picked, NEXT BEST MOVE 
              }
              //replacing the current best score
              if (Score > pieceScore) {
                  pieceScore = Score;
                  bestMove = bnextMove;
              }
          }
          // updaing the black moves and making sure the best possibilites are moved for the AI
         blackStack = (Stack) possibleblackmoves.clone();
      }
      return randomMove(nextbestmove);

  }
    //end of next best move

    public Move twoLevelsDeep(Stack possibilities){
      Move selectedMove = new Move();
      //missing implementation
      return selectedMove;
    }

  }