package openNlpProject;

import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class Main extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldOfSentenceSimilarity;
	private JTextField textFieldOfSentenceScore;
	static File file;
	static double thresholdOfSentenceSimilarity;
	static double thresholdOfSentenceScore;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	//THIS CONSTRUCTOR WE GİVE MENU FOR SELECT TRESHOLD
	public Main() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 576, 379);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Threshold of Sentence Similarity : ");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBounds(38, 44, 239, 27);
		contentPane.add(lblNewLabel);
		
		JLabel lblThresholdOfSentence = new JLabel("Threshold of Sentence Score : ");
		lblThresholdOfSentence.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblThresholdOfSentence.setBounds(38, 107, 239, 27);
		contentPane.add(lblThresholdOfSentence);
		
		textFieldOfSentenceSimilarity = new JTextField();
		textFieldOfSentenceSimilarity.setBounds(277, 48, 86, 23);
		contentPane.add(textFieldOfSentenceSimilarity);
		textFieldOfSentenceSimilarity.setColumns(10);
		
		textFieldOfSentenceScore = new JTextField();
		textFieldOfSentenceScore.setColumns(10);
		textFieldOfSentenceScore.setBounds(277, 111, 86, 23);
		contentPane.add(textFieldOfSentenceScore);
		
		JButton btnNewButton = new JButton("Choose A File");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.setBounds(251, 197, 145, 23);
		contentPane.add(btnNewButton);
		//BUUTON OF SELECT FİLE
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String valueOfSimilarity = textFieldOfSentenceSimilarity.getText();
				String valueOfScore = textFieldOfSentenceScore.getText();
				if(!valueOfSimilarity.isBlank() && !valueOfScore.isBlank() && isNumeric(valueOfSimilarity) && isNumeric(valueOfScore) && Double.parseDouble(valueOfSimilarity)<=1 && Double.parseDouble(valueOfSimilarity)>=0 && Double.parseDouble(valueOfScore)<=1 && Double.parseDouble(valueOfScore)>=0) {
				//THRESHOLDS
					thresholdOfSentenceScore=Double.parseDouble(valueOfScore);
					thresholdOfSentenceSimilarity=Double.parseDouble(valueOfSimilarity);
				//FİLE SELEECT
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setCurrentDirectory(new File("C:\\Users\\lenovo\\Desktop")); // positioning
					int response = fileChooser.showOpenDialog(null);// select file to open
					// if opened return 0 else canseled 1
					// System.out.println(fileChooser.showOpenDialog(null));
					if (response == JFileChooser.APPROVE_OPTION) {
						file = new File(fileChooser.getSelectedFile().getAbsolutePath());
						// System.out.println(file);
						//TRUE SELECTION
						MyFrame myGraph = new  MyFrame();
						myGraph.graphCalculate();
					}
				}else {
					showMessageDialog(null, "Check Out Your Inputs!!!");
				}
			}
		});
	}
	//NUMERİC CONTROL FUNCTION
	public boolean isNumeric(String string) {
	    double doubleValue;
			
	    //System.out.println(String.format("Parsing string: \"%s\"", string));
			
	    if(string == null || string.equals("")) {
	        //System.out.println("String cannot be parsed, it is null or empty.");
	        return false;
	    }
	    
	    try {
	    	doubleValue = Double.parseDouble(string);
	        return true;
	    } catch (NumberFormatException e) {
	    	showMessageDialog(null, "Input String cannot be parsed to Double.");
	    }
	    return false;
	}
}
