import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class RaceGUI {

    public static Color color_of_race;
    public static void main(String[] args) {

        //main frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Horse racing simulator");
        frame.setSize(900, 700);

        frame.setResizable(true);
        frame.setLayout(null);

        // menu bar
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem Loadresults = new JMenuItem("Load previous results");

        menuBar.add(fileMenu);
        fileMenu.add(Loadresults);

        frame.setJMenuBar(menuBar);

        //load results from text file
        Loadresults.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (BufferedReader reader = new BufferedReader(new FileReader("Race Results.txt"))) {
                    JFrame ResultsFrame = new JFrame();
                    ResultsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    ResultsFrame.setTitle("Race History");
                    ResultsFrame.setSize(800, 800);
                    ResultsFrame.setResizable(true);

                    String current;
                    String text = "";
                    while ((current = reader.readLine()) != null) {
                        text += current + "\n";
                    }
                    reader.close();

                    JTextArea textArea = new JTextArea(text);
                    textArea.setFont(new Font("Georgia", Font.PLAIN, 20));
                    textArea.setEditable(false);

                    JScrollPane scrollPane = new JScrollPane(textArea);
                    ResultsFrame.add(scrollPane);

                    ResultsFrame.setVisible(true);

                } catch (Exception ex) {
                    // Handle the exception here
                    ex.printStackTrace();
                }
            }
        });

        
        //title Label
        JLabel title = new JLabel("Welcome to the Horse Racing Simulator");
        title.setFont(new Font("Georgia", Font.BOLD, 25));
        title.setForeground(Color.BLUE);
        title.setBounds(125, 10, 550, 50);
        frame.add(title);

        // *******RACE PANEL********
        JPanel RacePanel = new JPanel();
        RacePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        RacePanel.setBounds(50, 70, 700, 60);

        RacePanel.setBackground(Color.LIGHT_GRAY);

        JLabel track_nunberLabel = new JLabel("Number of tracks: ");
        RacePanel.add(track_nunberLabel);

        JTextField track_numberInput = new JTextField(10);
        RacePanel.add(track_numberInput);

        JLabel lengthLabel = new JLabel("Length of Track:");
        RacePanel.add(lengthLabel);

        JTextField length_Input = new JTextField(10);
        RacePanel.add(length_Input);

        JLabel unitLabel = new JLabel("Unit of Length:");
        RacePanel.add(unitLabel);

        JTextField unit_Input = new JTextField(10);
        RacePanel.add(unit_Input);

        JLabel colorLabel = new JLabel("Color of race:");
        RacePanel.add(colorLabel);

        JButton colorButton = new JButton("Choose Color");
        RacePanel.add(colorButton);

        colorButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            color_of_race = JColorChooser.showDialog(null, "Choose a color", Color.WHITE);
        }
    });
        
        JButton CreateButton = new JButton("Create Race");
        RacePanel.add(CreateButton);

        frame.add(RacePanel);
        frame.setVisible(true);

        // END RACE PANEL
        CreateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(length_Input.getText().isEmpty() || track_numberInput.getText().isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Please fill in the number of tracks and the length", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else{

                    try{
                        String unit;
                        int length = Integer.parseInt(length_Input.getText());
                        int track_number = Integer.parseInt(track_numberInput.getText());
                        if (unit_Input.getText().isEmpty()) {
                            unit = "N/A";
                        } else {
                            unit = unit_Input.getText();
                        }

                        if(unit.equals("N/A") || unit.equals("m") || unit.equals("yd") || unit.equals("metres") || unit.equals("yards") ){
                            addHorses(length, track_number, unit, frame);
                        }
                        else{
                            JOptionPane.showMessageDialog(frame, "Please enter either m(metres) or yd(yards) for the units", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                    }
                    catch(NumberFormatException ex){
                        JOptionPane.showMessageDialog(frame, "Please enter a number only", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
    

    //****Horse Panels for each lane ****************
    public static void addHorses(int length, int track_number, String unit, JFrame frame) {
        Race new_race;

        if (unit.equals("N/A")) {
            new_race = new Race(length, track_number);
        } else {
            new_race = new Race(length, unit, track_number);
        }

        List<JTextField> horseSymbolInputs = new ArrayList<>();
        List<JTextField> horseNameInputs = new ArrayList<>();
        List<JTextField> horseConfidenceInputs = new ArrayList<>();
        List<JComboBox<String>> horseBreedInputs = new ArrayList<>();
        List<JComboBox<String>> horseAccesoryInputs = new ArrayList<>();
        

        for (int i = 0; i < track_number; i++) {
            int y = 140 + (i * 75);
            JPanel HorsePanel = new JPanel();
            HorsePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
            HorsePanel.setBounds(50, y, 800, 80);
            HorsePanel.setBackground(Color.LIGHT_GRAY);
            HorsePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            //Default fields
            JLabel horseLane = new JLabel("Lane " + (i + 1) + ": ");
            HorsePanel.add(horseLane);

            JLabel horseSymbol = new JLabel("Horse Symbol: ");
            HorsePanel.add(horseSymbol);

            JTextField horseSymbolInput = new JTextField(10);
            HorsePanel.add(horseSymbolInput);
            horseSymbolInputs.add(horseSymbolInput);

            JLabel horseName = new JLabel("Horse Name: ");
            HorsePanel.add(horseName);

            JTextField horseNameInput = new JTextField(10);
            HorsePanel.add(horseNameInput);
            horseNameInputs.add(horseNameInput);

            JLabel horseConfidence = new JLabel("Horse Confidence: ");
            HorsePanel.add(horseConfidence);

            JTextField horseConfidenceInput = new JTextField(10);
            HorsePanel.add(horseConfidenceInput);
            horseConfidenceInputs.add(horseConfidenceInput);

            // custom fields
            JLabel horseBreed = new JLabel("Horse Breed: ");
            HorsePanel.add(horseBreed);

            String[] horseBreeds = { "\u265E", "\u2658" ,"\uD83D\uDC34", "\uD83D\uDC0E", "\uD83C\uDFC7", "\uD83E\uDD84", "\uD83D\uDC34" };

            JComboBox<String> horseBreedInput = new JComboBox<>(horseBreeds);
            HorsePanel.add(horseBreedInput);
            horseBreedInputs.add(horseBreedInput);

            JLabel horseAccesory = new JLabel("Horse Accesory: ");
            HorsePanel.add(horseAccesory);

            String[] horseAccesories = { "\u2283", "\uD83D\uDC52", "\u26D1", "\uD83C\uDFA9", "\uD83D\uDED2", "\uD83D\uDC51", "\uD83E\uDD47", "\uD83C\uDFC6" };
            JComboBox<String> horseAccesoryInput = new JComboBox<>(horseAccesories);
            HorsePanel.add(horseAccesoryInput);
            horseAccesoryInputs.add(horseAccesoryInput);

    
            frame.add(HorsePanel);
            frame.revalidate();
            frame.repaint();
        }

        
        JButton start = new JButton("Add Horses");
        frame.add(start);
        start.setBounds(100, 150 + track_number * 80, 100, 60);
        frame.setVisible(true);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i < track_number; i++) {

                    String symbol = horseSymbolInputs.get(i).getText();
                    String name = horseNameInputs.get(i).getText();
                    String confidence = horseConfidenceInputs.get(i).getText();
                    String breed = (String) horseBreedInputs.get(i).getSelectedItem();
                    String accesory =(String) horseAccesoryInputs.get(i).getSelectedItem();
                    
                    if (symbol.isEmpty() || name.isEmpty() || confidence.isEmpty()) {
                        continue;
                    } else {
                        char symbolChar = symbol.charAt(0);
                        Double confidenceDouble = Double.parseDouble(confidence);

                        customHorse horse = new customHorse(symbolChar, name, confidenceDouble, breed, accesory);
                        new_race.addHorse(horse, i + 1);
                    }

                }
                
                    betAndStats betStats = new betAndStats(new_race); // Create a betAndStats object
                    betStats.displayHorseStats();

            }

        });
    }

}
