package openNlpProject;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;


import javax.swing.JTextArea;
import java.awt.Font;

import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class RougeCalculator extends JFrame {

	private JPanel contentPane;
	private JTextField txtGeneratedSumma;
	private JTextField txtSummary;

	String referenceSummary;
	String generatedSummary;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RougeCalculator frame = new RougeCalculator();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public RougeCalculator() {
		generatedSummary = DrawingGraph.summaryText;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 858, 523);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtGeneratedSumma = new JTextField();
		txtGeneratedSumma.setHorizontalAlignment(SwingConstants.CENTER);
		txtGeneratedSumma.setFont(new Font("Tahoma", Font.BOLD, 16));
		txtGeneratedSumma.setEditable(false);
		txtGeneratedSumma.setText("Generated Summary");
		txtGeneratedSumma.setBounds(85, 20, 212, 34);
		contentPane.add(txtGeneratedSumma);
		txtGeneratedSumma.setColumns(10);

		txtSummary = new JTextField();
		txtSummary.setText("Reference Summary");
		txtSummary.setHorizontalAlignment(SwingConstants.CENTER);
		txtSummary.setFont(new Font("Tahoma", Font.BOLD, 16));
		txtSummary.setEditable(false);
		txtSummary.setColumns(10);
		txtSummary.setBounds(522, 20, 212, 34);
		contentPane.add(txtSummary);

		JTextArea textArea = new JTextArea(404,316);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Monospaced", Font.BOLD, 14));
		textArea.setEditable(false);
		textArea.setBounds(10, 68, 404, 316);
		JScrollPane scrollPane1 = new JScrollPane();
	scrollPane1.setBounds(10, 68, 404, 316);
	//  contentPane.add(textArea);
		contentPane.add(scrollPane1);
		scrollPane1.setViewportView(textArea);
		
		JTextArea textArea_1 = new JTextArea(404,316);
		textArea_1.setLineWrap(true);
		textArea_1.setFont(new Font("Monospaced", Font.BOLD, 14));
		textArea_1.setBounds(428, 68, 404, 316);
		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(428, 68, 404, 316);
		//  contentPane.add(textArea_1);
			contentPane.add(scrollPane2);
			scrollPane2.setViewportView(textArea_1);
		
	
		
		JTextArea textArea_2 = new JTextArea();
		textArea_2.setEditable(false);
		textArea_2.setBounds(156, 395, 111, 78);
		contentPane.add(textArea_2);

		textArea.setText(generatedSummary);

		JButton btnNewButton = new JButton("Calculate Rogue");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textArea_1.getText().isBlank() || textArea.getText().isBlank()) {
					showMessageDialog(null, "Please Check Areas!!!");
				} else {
					referenceSummary = textArea_1.getText();

					String resultOfRogue = ResultOfRogue(referenceSummary, textArea.getText());
					textArea_2.setText(resultOfRogue);
				}
			}
		});
		btnNewButton.setBounds(588, 419, 89, 34);
		contentPane.add(btnNewButton);
	}

	public String ResultOfRogue(String referenceSummary, String generatedSummary) {

		// Cümleleri kelimelere ayır
		String[] referenceWords = referenceSummary.split("\\s+");
		String[] generatedWords = generatedSummary.split("\\s+");

		// Referans ve üretilen özetlerdeki tüm kelimeleri birleştirerek bir kelime
		// listesi oluştur
		List<String> allWords = new ArrayList<>();
		for (String word : referenceWords) {
			if (!allWords.contains(word)) {
				allWords.add(word);
			}
		}
		for (String word : generatedWords) {
			if (!allWords.contains(word)) {
				allWords.add(word);
			}
		}

		// Referans özet metnindeki ve üretilen özet metnindeki her kelimenin sayısını
		// hesapla
		int[] referenceWordCounts = new int[allWords.size()];
		int[] generatedWordCounts = new int[allWords.size()];
		for (int i = 0; i < allWords.size(); i++) {
			String word = allWords.get(i);
			for (String refWord : referenceWords) {
				if (word.equals(refWord)) {
					referenceWordCounts[i]++;
				}
			}
			for (String genWord : generatedWords) {
				if (word.equals(genWord)) {
					generatedWordCounts[i]++;
				}
			}
		}

		// ROUGE Precision, Recall ve F1-Score hesapla
		double precision = calculatePrecision(referenceWordCounts, generatedWordCounts);
		double recall = calculateRecall(referenceWordCounts, generatedWordCounts);
		double f1Score = calculateF1Score(precision, recall);

		/*
		 * System.out.println("ROUGE Precision: " + precision);
		 * System.out.println("ROUGE Recall: " + recall);
		 * System.out.println("ROUGE F1-Score: " + f1Score);
		 */
		return "precision : " + String.valueOf(String.format("%.2f", precision)) + "\n" + "recall : "
				+ String.valueOf(String.format("%.2f", recall)) + "\n" + "f1score : "
				+ String.valueOf(String.format("%.2f", f1Score));

	}

	// Precision hesaplama
	private static double calculatePrecision(int[] referenceCounts, int[] generatedCounts) {
		int totalReferenceWords = sumArray(referenceCounts);
		int totalGeneratedWords = sumArray(generatedCounts);
		int commonWords = countCommonWords(referenceCounts, generatedCounts);

		return (double) commonWords / totalGeneratedWords;
	}

	// Recall hesaplama
	private static double calculateRecall(int[] referenceCounts, int[] generatedCounts) {
		int totalReferenceWords = sumArray(referenceCounts);
		int totalGeneratedWords = sumArray(generatedCounts);
		int commonWords = countCommonWords(referenceCounts, generatedCounts);

		return (double) commonWords / totalReferenceWords;
	}

	// F1-Score hesaplama
	private static double calculateF1Score(double precision, double recall) {
		return (2 * precision * recall) / (precision + recall);
	}

	// Bir dizideki tüm elemanların toplamını hesaplama
	private static int sumArray(int[] array) {
		int sum = 0;
		for (int value : array) {
			sum += value;
		}
		return sum;
	}

	// İki dizi arasındaki ortak kelime sayısını hesaplama
	private static int countCommonWords(int[] array1, int[] array2) {
		int count = 0;
		for (int i = 0; i < array1.length; i++) {
			if (array1[i] > 0 && array2[i] > 0) {
				count++;
			}
		}
		return count;
	}
}