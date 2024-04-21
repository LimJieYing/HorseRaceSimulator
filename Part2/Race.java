import java.util.concurrent.TimeUnit;
import javax.swing.*;
import java.io.*;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McFarewell
 * @version 1.0
 */
public class Race {
    int raceLength;
    List<customHorse> horses;

    private boolean laneError = false;
    JTextArea textArea = new JTextArea();

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance    the length of the racetrack (in metres/yards...)
     * @param no_of_lanes the number of lanes
     */
    public Race(int distance, int no_of_lanes) {
        // initialise instance variables
        this.raceLength = distance;

        horses = new ArrayList<customHorse>();
        for (int i = 0; i < no_of_lanes; i++) {
            horses.add(null);
        }
    }

    // TODO Constructor to accept units (m or yd) for distance.
    // yards are converted to metres.
    // @param unit. The unit of the distance. Either metres(m) or yards(yd)
    public Race(int distance, String unit, int no_of_lanes) {
        // if its metres, no conversion needed
        if (unit.equals("metres") || unit.equals("m")) {

            this.raceLength = distance;

        }
        // if its yards, convert to metres by x0.9144
        else if (unit.equals("yards") || unit.equals("yd")) {

            double distance_converted_to_m = distance * 0.9144;
            this.raceLength = (int) distance_converted_to_m;

        }
        // if unit not accepted, set distance to 0 which prevents race from starting
        else {
            String unit_errorMessage = "Invalid unit, distance automatically set to 0. Please enter either metres(m) or yards(yd).";
            textArea.append(unit_errorMessage + "\n");

            this.raceLength = 0;
        }

        horses = new ArrayList<customHorse>();
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
            horses.set(laneNumber - 1, (customHorse) theHorse);
        }
        // TODO input validation for laneNumber
        else {
            String lane_errorMessage = "Cannot add horse to lane " + laneNumber + " because there is no such lane";
            textArea.append(lane_errorMessage + "\n");
            laneError = true;
        }

    }


    public boolean finished;
    JFrame race_frame = new JFrame(); // main race frame

    public void startRaceGUI() throws IOException {

        finished = false;
        double startTime = System.currentTimeMillis();
       
        // reset all the lanes (all horses not fallen and back to 0).
        for (int i = 0; i < horses.size(); i++) {
            if (horses.get(i) != null) {
                horses.get(i).goBackToStart();
            }
        }

        
        race_frame.setSize(820, 800);
        race_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        race_frame.setTitle("Horse Racing Simulator");
        race_frame.setLayout(null);
        race_frame.setResizable(true);

        textArea.setBackground(RaceGUI.color_of_race);
        textArea.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));

        // Add the textArea to the frame
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(10, 10, 780, 760);
        race_frame.add(scrollPane);

        // dispay error mesages
        if (laneError) {
            finished = true;
            JOptionPane.showMessageDialog(race_frame, "Cannot start the race due to lane error.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (raceLength <= 0) {
            finished = true;
            JOptionPane.showMessageDialog(race_frame, "Distance cannot be less than or equal to 0.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (checkConfidence()) {
            finished = true;
            JOptionPane.showMessageDialog(race_frame, "Confidence cannot be less than 0 or greater than 1.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        race_frame.setVisible(true);

        // a timer for animating the movement of horses
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!finished) {
                    // move each horse
                    for (int i = 0; i < horses.size(); i++) {
                        if (horses.get(i) != null) {
                            moveHorse(horses.get(i));
                        }
                    }

                    // print the race positions
                    printRaceGUI(race_frame);

                    // if any of the horses has won, the race is finished

                    // TODO print the winner
                    // TODO increase the confidence of the winner by 0.1. if 1, do not increase
                    // further
                    
                    for (int i = 0; i < horses.size(); i++) {
                        if (horses.get(i) != null && raceWonBy(horses.get(i))) {
                            finished = true;

                            textArea.append("\nAnd the winner is " + horses.get(i).getName() + "\n");
                            double endTime = System.currentTimeMillis();
                            double timeTaken = (endTime - startTime) / 1000;

                            try {
                                recordStats(horses.get(i), timeTaken);
                            } catch (IOException ea) {
                                ea.printStackTrace();
                            }
                            if (horses.get(i).getConfidence() < 1.0) {
                                horses.get(i).setConfidence(horses.get(i).getConfidence() + 0.10);
                            }

                            break;
                        }
                    }

                    // TODO End the game when all horses has fallen
                    if (AllhasFallen()) {
                        finished = true;
                        
                        textArea.append("\nAll horses have fallen, No winner.\n");
                    }

                    detectFallen(finished);

                    // wait for 100 milliseconds
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (Exception eb) {
                    }
                }
            }
        });

        timer.start();

    }

    private void printRaceGUI(JFrame race_frame) {

        // Clear the JTextArea
        textArea.setText("");

        multiplePrintGUI('=', raceLength + 3); // top edge of track
        textArea.append("\n");

        // TODO use a for loop to print each lane
        for (int i = 0; i < horses.size(); i++) {
            printLaneGUI(horses.get(i));
            textArea.append("\n");
        }

        multiplePrintGUI('=', raceLength + 3); // bottom edge of track
        textArea.append("\n");

        // Update the GUI
        race_frame.repaint();
        race_frame.revalidate();

    }

    private void printLaneGUI(customHorse theHorse) {
        // TODO handles empty lanes
        if (theHorse == null) {
            textArea.append("|");
            multiplePrintGUI(' ', raceLength *2 + 5);
            textArea.append("| \n");
        } else {
        
            int spacesBefore = theHorse.getDistanceTravelled();
            int spacesAfter = raceLength - theHorse.getDistanceTravelled();

            // print | for beginning 
            textArea.append("|");

            //spaces before horse
            multiplePrintGUI(' ', spacesBefore *2 - 1);

            //if fallen print X else print accesory and breed char 
            if (theHorse.hasFallen()) {
                textArea.append(" X ");
            } else {
                textArea.append(theHorse.getAccesory() + theHorse.getBreed());
            }

            //  spaces after  horse
            multiplePrintGUI(' ', spacesAfter *2);

            // print  | for end 
            textArea.append("|");

            // TODO after every lane, print name and confidence
            textArea.append( " " + theHorse.getSymbol() + " " + theHorse.getName() + " (Current confidence " + theHorse.getConfidence() + ") \n");
        }

    }

    private void multiplePrintGUI(char aChar, int times) {
        for (int i = 0; i < times; i++) {
            textArea.append(Character.toString(aChar));
        }
    }

    // TODO record results in a file
    //to be loaded when the menu bar option is clicked
    private void recordStats(Horse winner, double timeTaken) throws IOException {

        try (PrintWriter results = new PrintWriter(new FileWriter("Race Results.txt", true))) {

            results.write("Length of track + " + this.raceLength + "m " + "\n");
            results.write("The winner: " + winner.getName() + "\n");
            results.write("confidence: " + winner.getConfidence() + "\n");
            results.write("distance travelled: " + winner.getDistanceTravelled() + "\n");
            results.write("Time taken: " + timeTaken + " seconds" + "\n");

            results.write("The  of the other horses' stats are: " + "\n");

            for (int i = 0; i < horses.size(); i++) {
                if (horses.get(i) != null && !horses.get(i).getName().equals(winner.getName())) {
                    results.write(horses.get(i).getName() + " (" + horses.get(i).getSymbol() + ") "
                            + " has a confidence of " + horses.get(i).getConfidence() + "\n");

                }
            }
            results.write("------------------------------------------------------------" + "\n");
            results.close();
        } catch (IOException e) {
            System.out.println("An error occured");
        }
    }

    // TODO input validatiuon for confidence. reject if < 0 or > 1
    private boolean checkConfidence() {
        for (int i = 0; i < horses.size(); i++) {
            if (horses.get(i) != null && (horses.get(i).getConfidence() < 0 || horses.get(i).getConfidence() > 1)) {

                System.out
                        .println("Confidence for " + horses.get(i).getName() + " is " + horses.get(i).getConfidence());
                return true;
            }
        }
        return false;
    }

    // TODO when all horses has fallen, return true
    public boolean AllhasFallen() {
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

    public Horse getWinner() {
        for (Horse horse : horses) {
            if (raceWonBy(horse)) {
                return horse;
            }
        }
        return null;
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
    /* private void printRace() {
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
    } */

    /**
     * print a horse's lane during the race
     * for example
     * | X |
     * to show how far the horse has run
     */
    /* private void printLane(customHorse theHorse) {
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
            System.out.print(" " + theHorse.getName() + " (Current confidence " + theHorse.getConfidence() + ")"
                    + theHorse.getBreed() + " " + theHorse.getAccesory() + " " + theHorse.getColour());
        }

    } */

    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    /* private void multiplePrint(char aChar, int times) {
        int i = 0;
        while (i < times) {
            System.out.print(aChar);
            i = i + 1;
        }
    } */



    /*  *
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the
     * race is finished
     * 
     * @throws IOException
     */

    /* public void startRace() throws IOException {

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
        // TODO input validation for confidence
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

                    recordStats(horses.get(i));
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

    } */
}
