
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class betAndStats {

    Race newRace;
    ArrayList<Bet> bets;
    ArrayList<user> users;

    public betAndStats(Race newRace) {
        this.newRace = newRace;
        this.bets = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    JTextArea textArea;

    public void displayHorseStats() {

        //Main frame

        JFrame statsFrame = new JFrame();
        statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        statsFrame.setTitle("Horse Statistics");
        statsFrame.setSize(800, 800);
        statsFrame.setResizable(true);

        //STatistics panel (TOP HALF)
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(2, 2));
        statsPanel.setBounds(0, 0, 800, 400);

        // scrollPane to the statsPanel
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea); 
        textArea.setEditable(false); 
        textArea.setFont(new Font("Arial", Font.PLAIN, 20));

        textArea.append("The length of the track is " + newRace.raceLength + " meters." + "\n\n");
        for (Horse horse : newRace.horses) {

            if (horse == null) {
                continue;
            } else {
                String currentName = horse.getName();
                double currentConfidence = horse.getConfidence();
                double probability = 0.1 * currentConfidence * currentConfidence *10000;
                int probabilityInt = (int) probability;
                double probabilityRounded = probabilityInt / 100.0;
                
                if (currentConfidence < 0.5) {
                    textArea.append(currentName + " is very slow has a low probability to Fall! with a probability of "
                            + probabilityRounded + "%" + "\n");
                } else if (currentConfidence >= 0.5 && currentConfidence < 0.8) {
                    textArea.append(
                            currentName + " is fast and has a medium probability to Fall! with a probability of "
                                    + probabilityRounded + "%" + "\n");
                } else {
                    textArea.append(
                            currentName + " is very fast but has a high probability to Fall! with a probability of "
                                    + probabilityRounded + "%" + "\n");
                }
                textArea.append("\n");
            }

        }

        
        statsPanel.add(scrollPane); 

        // Panel for the bottom half
        JPanel bottomPanel = new JPanel(); 
        bottomPanel.setLayout(new GridLayout(1, 2));
        bottomPanel.setBackground(Color.LIGHT_GRAY);

        //Start button (bottom left)
        JButton Start = new JButton("Start Race");
        Start.setBorder(BorderFactory.createLineBorder(Color.CYAN, 10));
        Start.setBackground(Color.GREEN);
        Start.setFont(new Font("Comic Sans MS", Font.BOLD, 30));

        
        Start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //reset Race frame
                    newRace.race_frame.getContentPane().removeAll(); 
                    newRace.race_frame.repaint(); 

                    newRace.startRaceGUI();

                    Timer timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(newRace.finished){
                                // Stop the Timer
                                ((Timer)e.getSource()).stop();

                                textArea.append("\n");
                                textArea.append("Bets: \n");

                                if (users == null || users.isEmpty() || bets == null || bets.isEmpty()){
                                    textArea.append("No users are betting.");
                                    return;
                                }
                                else if(newRace.AllhasFallen())
                                {
                                    textArea.append("All horses have fallen, no one wins");

                                }
                                else{
                                    Horse winningHorse = newRace.getWinner();
                                    horseWon(winningHorse);
                                }
                                
                                displayBalances();
                            }
                        }
                    });
        
                    // Start the Timer
                    timer.start();


                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });
        bottomPanel.add(Start); 

        // Betting Panel (bottom right)
        JPanel bettingPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        bettingPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        bettingPanel.setLayout(new GridLayout(0, 1, 10, 10));
        

        JComboBox<user> user_menu_box = new JComboBox<user>();

        // set number of users panel. min 1 max 10 users can bet                                                                     
        JPanel num_of_usersPanel = new JPanel(new BorderLayout());
        JLabel num_of_UsersLabel = new JLabel("Number of Users: ");
        JSpinner numUsersSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1)); 


        num_of_usersPanel.add(num_of_UsersLabel, BorderLayout.WEST);
        num_of_usersPanel.add(numUsersSpinner, BorderLayout.CENTER);
        bettingPanel.add(num_of_usersPanel);

        // Set Users Button
        JButton setUsersButton = new JButton("Set Users");
        setUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numUsers = (int) numUsersSpinner.getValue();
                for (int i = 0; i < numUsers; i++) {
                    String userName = JOptionPane.showInputDialog("Enter name for user " + (i + 1) + ":");
                    user newUser = new user(userName);
                    user_menu_box.addItem(newUser);
                    users.add(newUser);
                }
            }
        });
        bettingPanel.add(setUsersButton);

        // Panel for user to select name
        JPanel userPanel = new JPanel(new BorderLayout());
        JLabel userLabel = new JLabel("User: ");
         // Add users to the combo box
        userPanel.add(userLabel, BorderLayout.WEST);
        userPanel.add(user_menu_box, BorderLayout.CENTER);
        bettingPanel.add(userPanel);

        

        // Panel for user to select horse to bet
        JPanel horsePanel = new JPanel(new BorderLayout());
        JLabel horseLabel = new JLabel("Horse: ");
        JComboBox<Horse> horse_menu_Box = new JComboBox<Horse>(); // Add horses to this combo box
        for (Horse horse : newRace.horses) {
            horse_menu_Box.addItem(horse);
        }
        horsePanel.add(horseLabel, BorderLayout.WEST);
        horsePanel.add(horse_menu_Box, BorderLayout.CENTER);
        bettingPanel.add(horsePanel);

        // Panel for user to select amount to bet
        JPanel amountPanel = new JPanel(new BorderLayout());
        JLabel amountLabel = new JLabel("Bet Amount: ");
        JSpinner amountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)); // Set the minimum, maximum, and
                                                                                     // step for the spinner
        amountPanel.add(amountLabel, BorderLayout.WEST);
        amountPanel.add(amountSpinner, BorderLayout.CENTER);
        bettingPanel.add(amountPanel);

        // Place Bet Button
        JButton placeBetButton = new JButton("Place Bet");
        placeBetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                user selectedUser = (user) user_menu_box.getSelectedItem();
                Horse selectedHorse = (Horse) horse_menu_Box.getSelectedItem();
                int betAmount = (int) amountSpinner.getValue();
                betAndStats.this.bet(selectedUser, selectedHorse, betAmount);
            }
        });
        bettingPanel.add(placeBetButton);

        // Add the bettingPanel to the bottom half of the bottomPanel
        bottomPanel.add(bettingPanel);

        // Add the bottomPanel to the statsPanel
        statsPanel.add(bottomPanel);

        // Add the statsPanel to the statsFrame
        statsFrame.add(statsPanel);
        statsFrame.setVisible(true);

    }

    public void bet(user user, Horse horse, Integer amount) {
        if (user == null || amount == null) {
            System.out.println("No users are betting.");
            return;
        }

        if (user.getBalance() >= amount) {
            user.removeBalance(amount);
            bets.add(new Bet(user, horse, amount));
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    public void horseWon(Horse winningHorse) {
        
            int totalBetAmount = 0;
            List<user> winningUsers = new ArrayList<>();

            for (Bet bet : bets) {
                if (bet.horse.equals(winningHorse)) {
                    bet.user.addBalance(bet.amount * 2);
                    winningUsers.add(bet.user);
                }
                totalBetAmount += bet.amount;
            }

            int denominator = users.size() - winningUsers.size();
            int deductionAmount = 0;
            if (denominator != 0) {
                deductionAmount = totalBetAmount / denominator;
            }

            for (user user : users) {
                if (!winningUsers.contains(user)) {
                    if(user.getBalance() >= 0){
                        user.removeBalance(deductionAmount);
                    }
                    else{
                        textArea.append( user.toString() + " is bankrupt and is out of the game." + "\n");
                        user.bankrupt();
                    }
                }
            }
        
    }

    public void displayBalances() {
        for (user user : users) {
            textArea.append(user.toString() + "'s balance: " + user.getBalance() + "\n");
        }
    }

}
