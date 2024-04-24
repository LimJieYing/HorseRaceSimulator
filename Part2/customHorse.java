

public class customHorse extends Horse {
    
    String breed;
    String accesory;
    

    public customHorse(char horseSymbol, String horseName, double horseConfidence, String breed, String accesory){
        super(horseSymbol, horseName, horseConfidence);
        this.breed = breed;
        this.accesory = accesory;
       
    }

    public customHorse(char horseSymbol, String horseName, double horseConfidence, String breed){
        super(horseSymbol, horseName, horseConfidence);
        this.breed = breed;
        this.accesory = "";
      
    }

    
    public customHorse(char horseSymbol, String horseName, double horseConfidence){
        super(horseSymbol, horseName, horseConfidence);
        this.breed = "";
        this.accesory = "";
    }


    public String getBreed(){
        return this.breed;
    }

    public String getAccesory(){
        return this.accesory;
    }

    
    public void setBreed(String breed){
        this.breed = breed;
    }

    public void setAccesory(String accesory){
        this.accesory = accesory;
    }

    


}
