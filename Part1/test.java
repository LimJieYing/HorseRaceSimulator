public class test {
    public static void main(String[] args) {
        Race race = new Race(30, "m" ,2 );
        Horse horse1 = new Horse('+', "Frank", 0.7);
        Horse horse2 = new Horse('*', "Bob", 0.5);
        
        race.addHorse(horse1, 1);
        race.addHorse(horse2, 2);
        
       
        race.startRace();

    }
}
