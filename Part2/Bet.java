
public class Bet {
    
        user user;
        Horse horse;
        int amount;

        public Bet(user user, Horse horse, int amount) {
            this.user = user;
            this.horse = horse;
            this.amount = amount;
        }

        // Getters and setters...
        public String toString() {
            return this.user.toString(); // Assuming 'name' is the attribute that stores the user's name
        }

        public Horse getHorse() {
            return this.horse;
        }

        public int getAmount() {
            return this.amount;
        }

        public void setUser(user user) {
            this.user = user;
        }

        public void setHorse(Horse horse) {
            this.horse = horse;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    
}
