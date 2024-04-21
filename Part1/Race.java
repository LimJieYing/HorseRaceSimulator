import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

/**
 * A multiple lane horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author Lim Jie Ying
 * @version 1.0
 */
public class Race {
    private int raceLength;
    private List<Horse> horses;

    private boolean laneError = false;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     * @param no_of_lanes the number of lanes
     */
    public Race(int distance, int no_of_lanes) {
        // initialise instance variables
        this.raceLength = distance;

        horses = new ArrayList<Horse>();
        for (int i = 0; i < no_of_lanes; i++) {
            horses.add(null);
        }
    }

    // TODO Constructor to accept units (m or yd) for distance.
    // yards are converted to metres.
    //@param unit. The unit of the distance. Either metres(m) or yards(yd)
    public Race(int distance, String unit, int no_of_lanes) {
        // if its metres, no conversion needed
        if(unit.equals("metres") || unit.equals("m")){

            this.raceLength = distance;

        }
        // if its yards, convert to metres by x0.9144
        else if(unit.equals("yards") || unit.equals("yd")){

            double distance_converted_to_m = distance * 0.9144;
            this.raceLength = (int) distance_converted_to_m; 

        }
        // if unit not accepted, set distance to 0 which prevents race from starting
        else{
            System.out.println("Invalid unit, distance automatically set to 0. Please enter either metres(m) or yards(yd).");
            this.raceLength = 0;
        }

        horses = new ArrayList<Horse>();
        for (int i = 0; i < no_of_lanes; i++) {
            horses.add(null);
        }
    }

    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse   the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber) {

        if (laneNumber <= horses.size() && laneNumber >= 1) {
            horses.set(laneNumber - 1, theHorse);
        }
        // TODO input validation for laneNumber
        else {
            System.out.println("Cannot add horse to lane " + laneNumber + " because there is no such lane");
            laneError = true;
        }

    }

    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the
     * race is finished
     */
    public void startRace() {

        // declare a local variable to tell us when the race is finished
        boolean finished = false;

        // reset all the lanes (all horses not fallen and back to 0).
        for (int i = 0; i < horses.size(); i++) {
            if (horses.get(i) != null) {
                horses.get(i).goBackToStart();
            }
        }

        // TODO input validation for laneError
        if (laneError) {
            finished = true;
            System.out.println("Cannot start the race due to lane error.");
        }
        // TODO Input validation for raceLength
        if (raceLength <= 0) {
            finished = true;
            System.out.println("Distance cannot be less than or equal to 0.");
        }
        //TODO input validation for confidence
        if (checkConfidence()) {
            finished = true;
            System.out.println("Confidence cannot be less than 0 or greater than 1.");
        }

        while (!finished) {
            // move each horse
            for (int i = 0; i < horses.size(); i++) {
                if (horses.get(i) != null) {
                    moveHorse(horses.get(i));
                }
            }

            // print the race positions
            printRace();

            // if any of the horses has won, the race is finished

            // TODO print the winner
            // TODO increase the confidence of the winner by 0.1. if 1, do not increase
            // further
            System.out.println();
            for (int i = 0; i < horses.size(); i++) {
                if (horses.get(i) != null && raceWonBy(horses.get(i))) {
                    finished = true;

                    System.out.println("And the winner is " + horses.get(i).getName());
                    if (horses.get(i).getConfidence() < 1.0) {
                        horses.get(i).setConfidence(horses.get(i).getConfidence() + 0.10);
                    }

                    break;
                }
            }

            // TODO End the game when all horses has fallen
            if (AllhasFallen()) {
                finished = true;
                System.out.println();
                System.out.println("All horses have fallen, No winner.");
            }

            detectFallen(finished);

            // wait for 100 milliseconds
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
            }
        }

    }

    // TODO input validatiuon for confidence. reject if < 0 or > 1
    private boolean checkConfidence() {
        for (int i = 0; i < horses.size(); i++) {
            if (horses.get(i) != null && (horses.get(i).getConfidence() < 0 || horses.get(i).getConfidence() > 1)) {
                
                System.out.println("Confidence for " + horses.get(i).getName() + " is " + horses.get(i).getConfidence());
                return true;
            }
        }
        return false;
    }

    // TODO when all horses has fallen, return true
    private boolean AllhasFallen() {
        for (int i = 0; i < horses.size(); i++) {
            if (horses.get(i) != null && !horses.get(i).hasFallen()) {
                return false;
            }
        }
        return true;
    }

    // TODO when a horse falls in a a race, confidence rating is reduced by 0.1
    // if 0, then do not decrease further
    private void detectFallen(boolean isfinished) {
        if (isfinished) {
            for (int i = 0; i < horses.size(); i++) {
                if (horses.get(i) != null && horses.get(i).hasFallen() && horses.get(i).getConfidence() > 0) {
                    horses.get(i).setConfidence(horses.get(i).getConfidence() - 0.10);
                }
            }

        }
    }

    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse) {
        // if the horse has fallen it cannot move,
        // so only run if it has not fallen

        if (!theHorse.hasFallen()) {
            // the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence()) {
                theHorse.moveForward();
            }

            // the probability that the horse will fall is very small (max is 0.1)
            // but will also will depends exponentially on confidence
            // so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1 * theHorse.getConfidence() * theHorse.getConfidence())) {
                theHorse.fall();
            }
        }
    }

    /**
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse) {
        if (theHorse.getDistanceTravelled() == raceLength) {
            return true;
        } else {
            return false;
        }
    }

    /***
     * Print the race on the terminal
     */
    private void printRace() {
        // System.out.print('\u000C'); //clear the terminal window
        // TODO clear terminal window fix
        System.out.println("\033\143"); // clear the terminal window

        multiplePrint('=', raceLength + 3); // top edge of track
        System.out.println();

        // TODO use a for loop to print each lane

        for (int i = 0; i < horses.size(); i++) {
            printLane(horses.get(i));
            System.out.println();
        }

        multiplePrint('=', raceLength + 3); // bottom edge of track
        System.out.println();
    }

    /**
     * print a horse's lane during the race
     * for example
     * | X |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse) {
        // TODO handles empty lanes
        if (theHorse == null) {
            System.out.print("|");
            multiplePrint(' ', raceLength + 1);
            System.out.print("|");
        } else {
            // calculate how many spaces are needed before
            // and after the horse
            int spacesBefore = theHorse.getDistanceTravelled();
            int spacesAfter = raceLength - theHorse.getDistanceTravelled();

            // print a | for the beginning of the lane
            System.out.print('|');

            // print the spaces before the horse
            multiplePrint(' ', spacesBefore);

            // if the horse has fallen then print dead
            // else print the horse's symbol
            if (theHorse.hasFallen()) {
                System.out.print('X');
            } else {
                System.out.print(theHorse.getSymbol());
            }

            // print the spaces after the horse
            multiplePrint(' ', spacesAfter);

            // print the | for the end of the track
            System.out.print('|');

            // TODO after every lane, print name and confidence
            System.out.print(" " + theHorse.getName() + " (Current confidence " + theHorse.getConfidence() + ")");
        }

    }

    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times) {
        int i = 0;
        while (i < times) {
            System.out.print(aChar);
            i = i + 1;
        }
    }
}
