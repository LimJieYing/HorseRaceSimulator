public class test {
    public static void main(String[] args) {

        //create a new Race object
        Race race = new Race(10, "m" ,4 );

        //create three Horse objects
        Horse horse1 = new Horse('+', "Frank", 0.7);
        Horse horse2 = new Horse('*', "Bob", 0.5);
        Horse horse3 = new Horse('%', "cOW", 0.3);
        
        race.addHorse(horse1, 1);
        race.addHorse(horse2, 2);
        //the third lane is left empty
        race.addHorse(horse3, 4);
        
        //Start the race
        race.startRace();
        //to start the race multiple times, use race.startRace(); multiple times

    }
}
