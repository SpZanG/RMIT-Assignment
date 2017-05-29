package player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import ship.Ship;
import world.World;

/**
 * Greedy guess player (task B).
 * Please implement this class.
 *
 * @author Youhan, Jeffrey
 */
public class GreedyGuessPlayer  implements Player{

    World world;
    int maxGuesses = -1;
    int DIRECTIONS = 4;
    //ArrayList<Guess> guesses = new ArrayList<>();
    ArrayList<World.ShipLocation> shipLoc = new ArrayList<>();
    HashSet<Guess> guesses = new HashSet<>();
    ArrayList<Guess> tmp = new ArrayList<>();
    ArrayList<Guess> hits = new ArrayList<>();
    ArrayList<Guess> target = new ArrayList<>();
    String lastDirection = "none";
    Guess firstHit;
    String newDirection = "none";
    ArrayList<String> currentShip = new ArrayList<>();
    boolean inTargetMode = false;
    boolean goToFirstGuess = false;

    @Override
    public void initialisePlayer(World world)
    {
        this.world = world;
        maxGuesses = world.numRow * world.numColumn / 2;
        // ship locations
        for (int i = 0; i < world.shipLocations.size(); i++)
        {
            shipLoc.add(world.shipLocations.get(i));
        }



        for (int i = 0; i < shipLoc.size(); i++)
        {

        }


        // Generate guesses
        odd();
        even();


        tmp.addAll(guesses);


    }// end of initialisePlayer()

    public void even()
    {

        for (int r = 0; r < world.numRow; r+=2 )
        {
            for (int c = 0; c < world.numColumn; c+=2)
            {
                Guess guess = new Guess();
                guess.row = r;
                guess.column = c;

                guesses.add(guess);
            }
        }


    }
    public void odd()
    {

        for (int r = 1; r < world.numRow; r+=2 )
        {
            for (int c = 1; c < world.numColumn; c+=2)
            {
                Guess guess = new Guess();
                guess.row = r;
                guess.column = c;

                guesses.add(guess);
            }
        }
    }

    @Override
    public Answer getAnswer(Guess guess)
    {
        Answer answer = new Answer();
        ArrayList<World.Coordinate> cdns;
        World.Coordinate cdn;
        Ship ship;
        for (int i = 0; i < this.shipLoc.size(); i++) {
            cdns = this.shipLoc.get(i).coordinates;
            ship = this.shipLoc.get(i).ship;
            for (int m = 0; m < cdns.size(); m++) {
                cdn = cdns.get(m);
                if (cdn.row == guess.row && cdn.column == guess.column)
                {
                    answer.isHit = true;
                    System.out.println("answer: HIT");
                    cdns.remove(m);
                    // check whether current ship is sunk
                    if (cdns.isEmpty())
                    {
                        answer.shipSunk = ship;
                        this.shipLoc.remove(i);
                        return answer;
                    }
                }

            }
        }// end for loop

        return answer;
    } // end of getAnswer()


    @Override
    public Guess makeGuess()
    {
        //System.out.println("makeGuess: hits " + hits.size());
        Guess guessLocal = new Guess();
        // Targeting mode
        if (inTargetMode)
        {
            //System.out.println("Targeting Mode");
            Guess targetGuess = hits.get(hits.size() - 1);

            if (goToFirstGuess)
            {
                targetGuess = firstHit;
                hits.clear();
                hits.add(targetGuess);
            }

            
            targetGuess.row = targetGuess.row - 1;




            return targetGuess;
        }
        // Hunting mode
        else
        {

            //System.out.println("Hunting Mode");
            if (tmp.size() > 0)
            {
                int index = generateRandomNum("guess");

                guessLocal = tmp.get(index);

                tmp.remove(index);
            }


            //System.out.println(guessLocal.toString())



        }
        return guessLocal;
    } // end of makeGuess()

    public Guess getDirection(Guess currentGuess)
    {
        Guess newGuess = new Guess();


        if (currentShip.size() == 0)
        {
            newDirection = "n";
            newGuess.row = currentGuess.row + 1;
            lastDirection = "n";
            currentShip.add(lastDirection);
        }
        else
        {
            for (int i = 0; i < currentShip.size(); i++)
            {
                if (currentShip.get(i).equalsIgnoreCase("n"))
                {
                    newDirection = "s";
                    lastDirection = "s";
                    currentShip.add(newDirection);
                    newGuess.row = currentGuess.row -1;
                }

            }
        }


        return newGuess;
    }



    @Override
    public void update(Guess guess, Answer answer)
    {
        Ship ship;
        if (answer.shipSunk == null)
        {
            if (answer.isHit)
            {
                hits.add(guess);
                firstHit = guess;
                inTargetMode = true;
                System.out.println("update : HIT");
                System.out.println("update: lastDirection: " + lastDirection);
            }
            else
            {
                // set new direction
                if (inTargetMode)
                {
                    goToFirstGuess = true;
                }
            }
        }


    } // end of update()


    @Override
    public boolean noRemainingShips()
    {
        // To be implemented.

        // dummy return
        //return this.shipLoc.isEmpty();
        return true;
    } // end of noRemainingShips()

    public int generateRandomNum(String type)
    {
        int result = -1;
        Random random = new Random();

        if (type.equalsIgnoreCase("row"))
        {
            result = random.nextInt(world.numRow);
        }
        else if (type.equalsIgnoreCase("col"))
        {
            result = random.nextInt(world.numColumn);
        }
        else if (type.equalsIgnoreCase("guess"))
        {
            result = random.nextInt(tmp.size());
        }
        else if (type.equalsIgnoreCase("dir"))
        {
            result = random.nextInt(DIRECTIONS);
        }


        return result;
    }

    class TargetMode
    {
        // North row + 1
        int north = 1;
        // East  col + 1
        int east = 1;
        // South  col - 1
        int south = 1;
        // West row -1
        int west = 1;

        String lastDirection;

        ArrayList<Guess> tried = new ArrayList<>();
        Guess nextGuess;
        Guess firstHit;
        Guess lastGuess;
        boolean isFirstHit = true;


        public TargetMode(ArrayList<Guess> hits)
        {
            tried.addAll(hits);
            this.firstHit = tried.get(0);
        }



        public Guess goNorth()
        {
            nextGuess = tried.get(tried.size() - 1);
            nextGuess.row = nextGuess.row + north;
            tried.add(nextGuess);
            this.lastDirection = "n";
            lastGuess = nextGuess;
            return nextGuess;
        }

        public Guess goEast()
        {
            nextGuess = tried.get(tried.size() - 1);
            nextGuess.column = nextGuess.column + east;
            this.lastDirection = "e";
            tried.add(nextGuess);
            lastGuess = nextGuess;
            return nextGuess;
        }

        public Guess goSouth()
        {
            nextGuess = tried.get(tried.size() - 1);
            nextGuess.row = nextGuess.row - south;
            this.lastDirection = "s";
            tried.add(nextGuess);
            lastGuess = nextGuess;
            return nextGuess;
        }

        public Guess goWest()
        {
            nextGuess = tried.get(tried.size() - 1);
            nextGuess.column = nextGuess.column - west;
            this.lastDirection = "w";
            tried.add(nextGuess);
            lastGuess = nextGuess;
            return nextGuess;
        }

        public Guess processNextGuess(Guess guess, String lastDirection)
        {
            guess = firstHit;

            if (lastDirection.equalsIgnoreCase("n"))
            {
                this.setLastDirection("s");
            }
            else if (lastDirection.equalsIgnoreCase("e"))
            {
                this.setLastDirection("w");
            }
            else if (lastDirection.equalsIgnoreCase("s"))
            {
                this.setLastDirection("n");
            }
            else if (lastDirection.equalsIgnoreCase("w"))
            {
                this.setLastDirection("e");
            }


            return guess;
        }

        public String getLastDirection()
        {
            return this.lastDirection;
        }

        public void setLastDirection(String direction)
        {
            this.lastDirection = direction;
        }

        public Guess getFirstHit()
        {
            return this.firstHit;
        }

        public Guess getRecentHit()
        {
            return tried.get(tried.size() - 1);
        }

        public void setIsFirstHit(boolean isFirstHit)
        {
            this.isFirstHit = isFirstHit;
        }

        public boolean getIsFirstHit()
        {
            return isFirstHit;
        }

        public ArrayList getTried()
        {
            return this.tried;
        }

        public Guess getNextGuess()
        {
            return nextGuess;
        }

        public Guess getLastGuess()
        {
            return lastGuess;
        }

        public void addTried(Guess guess)
        {
            tried.add(guess);
        }
    }
} // end of class GreedyGuessPlayer


