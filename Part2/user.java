public class user {
    String name;
    int balance;

    public user(String name){
        this.name = name;
        this.balance = 20;
    }

    public void addBalance(int amount){
        this.balance += amount;
    }

    public void removeBalance(int amount){
        this.balance -= amount;
    }

    public String toString() {
        return this.name; // Assuming 'name' is the attribute that stores the horse's name
    }

    public int getBalance(){
        return this.balance;
    }
}
