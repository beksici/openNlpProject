package openNlpProject;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.stemmer.*;
import java.util.stream.Stream;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer.ALGORITHM;


import javax.lang.model.element.Element;

import org.apache.commons.collections15.Transformer;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import info.debatty.java.stringsimilarity.Cosine;


public class MyFrame {

	JButton open;
	Scanner scan;
	String fileContent = "";
	String title = "";
	String titleTokens[];
	String titleTokensStemmedAndLowercase[];
	String sentences[];
	String url = "http://swoogle.umbc.edu/StsService/GetStsSim?operation=api&phrase1=";
	InputStream inputStream = null;
	InputStream tokenModelInput = null;
	InputStream posModelInput = null;
	SnowballStemmer stemmer = new SnowballStemmer(ALGORITHM.ENGLISH);
	List<List<String>> allDocument = new ArrayList<List<String>>();
double tresholdOfScore; 
	double[] valueOfBiggerThanThresholdSimilarityForSentencesParameter;
	ArrayList<Double> tfidfAverageForSentencesParameter = new ArrayList();
	ArrayList<Double> properNounControlForSentencesParameter = new ArrayList();
	ArrayList<Double> numericValueControlForSentencesParameter = new ArrayList();
	ArrayList<Double> includedTitleWordsControlForSentencesParameter = new ArrayList();
	ArrayList<Double> ScoreOFSentencesWithWeightedAverage = new ArrayList();
	ArrayList<String> tokensSentencesArraylist = new ArrayList();
	HashMap<Integer, String[]> resultCosinusSimilarity = new HashMap();
	HashMap<Integer, String[]> resultSemanticSimilarity = new HashMap();
	double[][] resultOfSemanticAndCosinusSimilarity;
	ArrayList<Integer> valueOfBiggerThanThresholdNodes = new ArrayList();

	HashMap<Integer, String[]> nonStopwordsSentencesTokensMap = new HashMap();
	HashMap<Integer, String[]> nonStopwordsSentencesTokensStemmedMap = new HashMap();
	HashMap<Integer, String[]> sentencesTokensMap = new HashMap();
	HashMap<Integer, String[]> sentencesTokensTagsMap = new HashMap();
	String[] stopWordsNltk = { "i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
			"yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
			"itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
			"these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
			"do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
			"of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
			"after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
			"further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
			"few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
			"too", "very", "s", "t", "can", "will", "just", "don", "should", "now" };


	
		
	public void graphCalculate() {
		//TAKE FİLE FROM SELECTION
				File file = Main.file;
				// System.out.println(file);

				//FILE READ TITLE AND CONTENT
				try {
					// Başlık atlatmak için pass title
					int k = 0;
					scan = new Scanner(file);
					while (scan.hasNextLine()) {
						String line = scan.nextLine();

						if (k == 0 && !line.isEmpty()) {
							title = title.concat(line);
							k = 1;
						} else if (!line.isEmpty()) {
							fileContent = fileContent.concat(line + "\n");

						}
					}

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//System.out.println("title--- " + title);
				//System.out.println("File content----" + fileContent + "\n------------");

				//TOKENAZATION SETTINGS 
				try {
					inputStream = new FileInputStream("en-sent.bin");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				SentenceModel model = null;
				try {
					model = new SentenceModel(inputStream);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// Instantiating the SentenceDetectorME class
				SentenceDetectorME detector = new SentenceDetectorME(model);

				// Detecting the sentence
				sentences = detector.sentDetect(fileContent);

				// Printing the sentences
				//for (String sent : sentences)
				//	System.out.println(sent);

				try {
					inputStream.close();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			//	System.out.println("------------------------");
//title tokens
				titleTokens = titleTokens(title);

				for (int i = 0; i < sentences.length; i++) {

					// tokenize the sentence
					try {
						tokenModelInput = new FileInputStream("en-token.bin");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					TokenizerModel tokenModel = null;
					try {
						tokenModel = new TokenizerModel(tokenModelInput);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					Tokenizer tokenizer = new TokenizerME(tokenModel);
					String tokensSentences[] = tokenizer.tokenize(sentences[i]);
					// Parts-Of-Speech Tagging
					// reading parts-of-speech model to a stream
					try {
						posModelInput = new FileInputStream("en-pos-maxent.bin");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// loading the parts-of-speech model from stream
					POSModel posModel = null;
					try {
						posModel = new POSModel(posModelInput);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// initializing the parts-of-speech tagger with model
					POSTaggerME posTagger = new POSTaggerME(posModel);
					// Tagger tagging the tokens
					String tags[] = posTagger.tag(tokensSentences);
					// Getting the probabilities of the tags given to the tokens

					// All sentences tokenize except title
					sentencesTokensMap.put(i, tokensSentences);
					sentencesTokensTagsMap.put(i, tags);
				}

				/*
				 * String s[] = sentencesTokensMap.get(0); for(String token:s) {
				 * System.out.println(token); }
				 */

				// P1 Cümlede özel isim içerme kontrolü proper Noun check
				// proper noun is NNP or NNPS

				for (int i = 0; i < sentencesTokensTagsMap.size(); i++) {

					String tags[] = sentencesTokensTagsMap.get(i);
					int count = 0;
					double parameterP1NNP = 0;
					int sizeOfSentence = tags.length;
					for (int k = 0; k < tags.length; k++) {
						if (tags[k].equalsIgnoreCase("NNP") || tags[k].equalsIgnoreCase("NNPS")) {
							count++;
						}
					}
				//	System.out.println("\n----------P1--------");
					//System.out.println("sizeofsent= " + sizeOfSentence);
				//	System.out.println("countOfProperN= " + count);

					parameterP1NNP = count / (double) sizeOfSentence;
					parameterP1NNP = Double.parseDouble(String.format("%.2f", parameterP1NNP).replace(',', '.'));
					//System.out.println("P1 parameter= " + parameterP1NNP);
					properNounControlForSentencesParameter.add(parameterP1NNP);

					// P2 parameter check include a numeric value
					String tokensSentence[] = sentencesTokensMap.get(i);
					count = 0;
					// int countOfIncludedTitleToken=0;
					double parameterP2Numeric = 0;
					sizeOfSentence = tokensSentence.length;
					for (int l = 0; l < tokensSentence.length; l++) {
						if (tokensSentence[l].matches(".*\\d.*")) {
							count++;
						}
						/*
						 * for(int k=0;k<titleTokens.length;k++) {
						 * tokensSentence[l].equalsIgnoreCase(titleTokens[k]); }
						 */
					}

				//	System.out.println("----------P2--------");
				//	System.out.println("sizeofsent= " + sizeOfSentence);
				//	System.out.println("countOfNumeric= " + count);

					parameterP2Numeric = count / (double) sizeOfSentence;
					parameterP2Numeric = Double
							.parseDouble(String.format("%.2f", parameterP2Numeric).replace(',', '.'));
				//	System.out.println("P2 parameter= " + parameterP2Numeric);
					numericValueControlForSentencesParameter.add(parameterP2Numeric);

				} /*
					 * PorterStemmer stem = new PorterStemmer();
					 * 
					 * System.out.println( stem.stem("am"));
					 * 
					 * 
					 */
				/*
				 * String token=""; for (int i = 0; i < sentencesTokensMap.size(); i++) { String
				 * sentence[]= sentencesTokensMap.get(i); for(int k=0;k<sentence.length;k++) {
				 * token=sentence[k].toLowerCase(); if(stopWordsNltk.contains(token)) {
				 * sentence[k]=sentence[k].toLowerCase().replace(token, "");
				 * 
				 * }
				 * 
				 * } for (String sent : sentence) System.out.println(sent);
				 * nonStopwordsSentencesTokensMap.put(i, sentence); }
				 * 
				 * 
				 * /*for(int i=0; i<sentences.length;i++) { String data =sentences[i];
				 * data=data.toLowerCase(); for(int k=0;k<stopWordsNltk.size();k++) {
				 * 
				 * 
				 * } }
				 */

				
				// REMOVE STOPWORDS AND STEMMED ALL SENTENCES
				nonStopwordsSentencesTokensMap = removeStopWord(sentencesTokensMap);
				nonStopwordsSentencesTokensStemmedMap = stemmerAllSentences(nonStopwordsSentencesTokensMap);
				// PorterStemmer stemmer = new PorterStemmer();

				/*
				 * String word = "swimmer";
				 * 
				 * SnowballStemmer st = new SnowballStemmer(ALGORITHM.ENGLISH);
				 * 
				 * System.out.println("Kelime: " + st.stem(word));
				 */

				/*
				 * String abc[] = nonStopwordsSentencesTokensStemmedMap.get(1); for (String a :
				 * abc) System.out.println(a);
				 */

				// SEMANTIC AND COSINUS SIMILARITY
				resultSemanticSimilarity = calculateSemanticSimilarity(resultSemanticSimilarity, sentences);
				resultCosinusSimilarity = calculateCosinusSimilarity(resultCosinusSimilarity, sentences);

				// print similarity
				/*
				 * for(int p=0;p<resultSemanticSimilarity.size();p++) { String[] s =
				 * resultSemanticSimilarity.get(p); for(String x :s) System.out.println(x); }
				 */

				/*
				 * String modelPath = "pytorch_model (1).bin"; WordVectors wordVectors =
				 * WordVectorSerializer.readAsBinary(new File(modelPath)); // Karşılaştırılacak
				 * cümleler String sentence1 = "A car"; String sentence2 = "A car was crashed";
				 * 
				 * INDArray vector1 = wordVectors.getWordVectorMatrix(sentence1); INDArray
				 * vector2 = wordVectors.getWordVectorMatrix(sentence2);
				 * 
				 * // Benzerlik skorunu hesapla (cosine similarity) double similarityScore =
				 * Transforms.cosineSim(vector1, vector2);
				 * 
				 * System.out.println("Cümleler arasındaki benzerlik skoru: " +
				 * similarityScore);
				 */

				
				// TITLE REMOVE STOPWORDS AND STEMMED 
				titleTokensStemmedAndLowercase = removeStopWordsOfTitle(titleTokens);
				titleTokensStemmedAndLowercase = titleStemmerAndLowercase(stemmer, titleTokensStemmedAndLowercase);

				
				
				//---- P4 paramate calculate included title words of sentences
				includedTitleWordsControlForSentencesParameter = calculateOfTitleWordsIncludedInSentence(
						nonStopwordsSentencesTokensStemmedMap, titleTokensStemmedAndLowercase);
				
				
				/*
				 * for(int p=0;p<nonStopwordsSentencesTokensStemmedMap.size();p++) { String[] s
				 * = nonStopwordsSentencesTokensStemmedMap.get(p); System.out.println(s.length);
				 * } for (Double a : includedTitleWordsControlForSentencesParameter)
				 * System.out.println(a);
				 */

				
				// all senteces became a list for calculate tfidf
				for (int i = 0; i < nonStopwordsSentencesTokensStemmedMap.size(); i++) {
					List<String> doc = Arrays.asList(nonStopwordsSentencesTokensStemmedMap.get(i));
					allDocument.add(doc);
				}

				// P5 PARAMATER TFTDF CALCULATION FOR ALL SENTENCES
				tfidfAverageForSentencesParameter = calculateTFIDFAllSentences(nonStopwordsSentencesTokensStemmedMap,
						tfidfAverageForSentencesParameter);
				//for (int p = 0; p < tfidfAverageForSentencesParameter.size(); p++) {
				//	System.out.println(tfidfAverageForSentencesParameter.get(p));
				//}

				
				// AVERAGE SEMANTIC AND COSINUS SIMILARITY
				resultOfSemanticAndCosinusSimilarity = new double[sentences.length][sentences.length];
				for (int i = 0; i < sentences.length - 1; i++) {

					String[] rcossentence = resultCosinusSimilarity.get(i);
					String[] rsemantic = resultSemanticSimilarity.get(i);

					for (int j = i; j < rcossentence.length; j++) {
						resultOfSemanticAndCosinusSimilarity[i][j] = Double.parseDouble( String.format("%.2f",((Double.parseDouble(rcossentence[j])+ Double.parseDouble(rsemantic[j])) / 2)).replace(',', '.')); 
						resultOfSemanticAndCosinusSimilarity[j][i] = Double.parseDouble(String.format("%.2f", ((Double.parseDouble(rcossentence[j])+ Double.parseDouble(rsemantic[j])) / 2)).replace(',', '.'));
						resultOfSemanticAndCosinusSimilarity[j][j] = 1;
						resultOfSemanticAndCosinusSimilarity[i][i] = 1;
					}

				}

				// P3 parameter calculate bigger than given threshold
				valueOfBiggerThanThresholdSimilarityForSentencesParameter = calculateOfParamaterP3(
						resultOfSemanticAndCosinusSimilarity, sentences);

				// PARAMATER WEIGHT
				// p1 2
				// p2 1
				// p3 5
				// p4 3
				// p5 4

//***** Score calculate weighted Average
				double weight = 15;
				for (int i = 0; i < sentences.length; i++) {

					double score = (properNounControlForSentencesParameter.get(i) * 2)
							+ (numericValueControlForSentencesParameter.get(i) * 1)
							+ (valueOfBiggerThanThresholdSimilarityForSentencesParameter[i] * 5)
							+ (includedTitleWordsControlForSentencesParameter.get(i) * 3)
							+ (tfidfAverageForSentencesParameter.get(i) * 4);

					score = Double.parseDouble(String.format("%.2f", score / weight).replace(',', '.'));
					ScoreOFSentencesWithWeightedAverage.add(score);
				}
				/*
				 * System.out.println("--------------------------"); int b=0; for(double
				 * a:ScoreOFSentencesWithWeightedAverage) { System.out.println(b +" = "+a);
				 * b++;}
				 * 
				 * int c=0; for(String s:sentences) { System.out.println(c +" = "+ s); c++; }
				 *
				 */
			//	System.out.println("sayılar-----");
			//	for(int i=0;i<sentences.length;i++) {
				//	System.out.println("valueOfBiggerThanThresholdNodes " + valueOfBiggerThanThresholdNodes.get(i) );
			//		System.out.println("ScoreOFSentencesWithWeightedAverage " + ScoreOFSentencesWithWeightedAverage.get(i) );
			//	}
				//kullanıcı giricek main ekranından gelicek
				tresholdOfScore=Main.thresholdOfSentenceScore;
				DrawingGraph dGraph = new DrawingGraph(ScoreOFSentencesWithWeightedAverage,valueOfBiggerThanThresholdNodes,sentences,resultOfSemanticAndCosinusSimilarity,tresholdOfScore);
				dGraph.setVisible(true);
				//Graph<String, String> graph = new SparseGraph<>();

				// Grafik düzenini oluşturma
				//CircleLayout<String, String> layout = new CircleLayout<String,String>(graph );
				//layout.setSize(new Dimension(400, 400));

				// Grafik görselleştirme sunucusunu oluşturma
				//VisualizationViewer<String, String> panel = new VisualizationViewer<String, String>(layout,
				//		new Dimension(1000, 600));
				// BasicVisualizationServer<String, String> visualizationServer = new
				// BasicVisualizationServer<>(layout);
/*
				String[] nodes = new String[sentences.length];
				String[] nodesScores = new String[sentences.length];
				String[] nodesConnectionOfBiggerThanTreshold = new String[sentences.length];

				for (int i = 0; i < sentences.length; i++) {

					nodes[i] = new String("sentence: " + (i + 1));
					nodesScores[i] = new String(ScoreOFSentencesWithWeightedAverage.get(i).toString());
					nodesConnectionOfBiggerThanTreshold[i] = new String(
							valueOfBiggerThanThresholdNodes.get(i).toString());
/*
					graph.addVertex(nodes[i]);
					graph.addVertex(nodesScores[i]);
					graph.addVertex(nodesConnectionOfBiggerThanTreshold[i]);
					
				}

				
				for(int i=0;i<sentences.length;i++) {
					
					layout.setLocation(nodesScores[i], layout.getX(nodes[i]) + 10,
							layout.getY(nodes[i]) - 20);
					layout.setLocation(nodesConnectionOfBiggerThanTreshold[i],
							layout.getX(nodes[i]) - 10, layout.getY(nodes[i]) - 10);

				}
				
				System.out.println(sentences.length);
				for (int i = 0; i < sentences.length-1 ; i++) {
					
					for (int j = i; j < sentences.length; j++) {
						if (i == j) {
							continue;
						} else {

							graph.addEdge("score"+i+j+" "+String.valueOf(resultOfSemanticAndCosinusSimilarity[i][j]),
									nodes[i].toString(), nodes[j].toString());
						}
					}
				}

				// Düğüm etiketlerini gösterme
				panel.getRenderContext().setVertexLabelTransformer(new CustomVertexLabelTransformer());
				panel.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<String>());
				DefaultModalGraphMouse<String,String> gm = new DefaultModalGraphMouse<String,String>();
		        gm.setMode(DefaultModalGraphMouse.Mode.PICKING); 
		        panel.setGraphMouse(gm);
				JFrame frame = new JFrame("Graph View: Random Layout");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().add(panel);
				frame.pack();
				frame.setVisible(true);
*/
				
				
		
			
		}
	

	private double[] calculateOfParamaterP3(double[][] sumCos, String sentences[]) {
		//BU TRESHOLD DEĞERİ DIŞARIDAN ALINIP HESAPLANACAK
		double thresholdOfSimilarity = Main.thresholdOfSentenceSimilarity;
		double[] valueOfThreshold = new double[sentences.length];
		for (int i = 0; i < sentences.length; i++) {
			int counter = 0;
			for (int j = 0; j < sentences.length; j++) {
				
				if(i==j) {
					continue;
				}else if (sumCos[i][j] >= thresholdOfSimilarity) {
					counter++;
				//	System.out.println( " sumcos  " +sumCos[i][j]); 
				}
			}
			valueOfBiggerThanThresholdNodes.add(counter);
			double value = (double) counter / (((sentences.length) * (sentences.length - 1)) / 2);
			value = Double.parseDouble(String.format("%.2f", value).replace(",", "."));
			valueOfThreshold[i] = value;

		}
		return valueOfThreshold;
	}

	private ArrayList<Double> calculateTFIDFAllSentences(
			HashMap<Integer, String[]> nonStopwordsSentencesTokensStemmedMap,
			ArrayList<Double> tfidfAverageForSentencesParameter) {
		for (int i = 0; i < nonStopwordsSentencesTokensStemmedMap.size(); i++) {

			String[] sentence = nonStopwordsSentencesTokensStemmedMap.get(i);
			List<String> doc1 = Arrays.asList(sentence);

			int lengthOfSentence = sentence.length;
			if (lengthOfSentence <= 10) {

				tfidfAverageForSentencesParameter.add(calculateTFIDF(lengthOfSentence, 1, sentence, doc1, allDocument));

			} else if (lengthOfSentence <= 20) {
				tfidfAverageForSentencesParameter.add(calculateTFIDF(lengthOfSentence, 2, sentence, doc1, allDocument));

			} else if (lengthOfSentence <= 30) {
				tfidfAverageForSentencesParameter.add(calculateTFIDF(lengthOfSentence, 3, sentence, doc1, allDocument));

			} else if (lengthOfSentence <= 40) {
				tfidfAverageForSentencesParameter.add(calculateTFIDF(lengthOfSentence, 4, sentence, doc1, allDocument));

			} else if (lengthOfSentence <= 50) {
				tfidfAverageForSentencesParameter.add(calculateTFIDF(lengthOfSentence, 5, sentence, doc1, allDocument));

			} else if (lengthOfSentence <= 60) {
				tfidfAverageForSentencesParameter.add(calculateTFIDF(lengthOfSentence, 6, sentence, doc1, allDocument));

			}

		}
		return tfidfAverageForSentencesParameter;
	}

	private double calculateTFIDF(int lengthOfSentence, int size, String sentence[], List<String> doc1,
			List<List<String>> allDocument) {

		ArrayList<Integer> randomNumber = new ArrayList<>();

		randomNumber = getRondomNumber(size, lengthOfSentence);
		double tfidf = 0;
		for (int k = 0; k < randomNumber.size(); k++) {
			String word = sentence[randomNumber.get(k)];
			TFIDFCalculator calculator = new TFIDFCalculator();
			tfidf = tfidf + calculator.tfIdf(doc1, allDocument, word);
		//	System.out.println("TF-IDF " + word + " = " + tfidf);
		}
		tfidf = tfidf / randomNumber.size();
		tfidf = Double.parseDouble(String.format("%.2f", tfidf).replace(',', '.'));

		return tfidf;
	}

	private ArrayList<Integer> getRondomNumber(int amount, int upperbound) {
		//UNIQ RANDOM NUMBER GENERATER
		ArrayList<Integer> list = new ArrayList<Integer>();
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 0; i < upperbound; i++)
			list.add(i);
		Collections.shuffle(list);
		for (int i = 0; i < amount; i++)
			numbers.add(list.get(i));

		return numbers;
	}

	// P4 paramate calculate included title words of sentences
	private ArrayList<Double> calculateOfTitleWordsIncludedInSentence(
			HashMap<Integer, String[]> nonStopwordsSentencesTokensStemmedMap, String[] titleTokensStemmedAndLowercase) {
		int countOfIncluedTitleWords = 0;
		ArrayList<Double> resultOfCalculatedOfTitleWordsIncludedInSentence = new ArrayList<>();

		for (int i = 0; i < nonStopwordsSentencesTokensStemmedMap.size(); i++) {

			String sentence[] = nonStopwordsSentencesTokensStemmedMap.get(i);
			double lengthOfSentence = sentence.length;
			for (int j = 0; j < titleTokensStemmedAndLowercase.length; j++) {

				for (int k = 0; k < sentence.length; k++) {

					if (sentence[k].contains(titleTokensStemmedAndLowercase[j])
							|| sentence[k].equals(titleTokensStemmedAndLowercase[j])) {
						countOfIncluedTitleWords++;
					}

				}

			}

			double resultOfParamater = countOfIncluedTitleWords / lengthOfSentence;
			resultOfParamater = Double.parseDouble(String.format("%.2f", resultOfParamater).replace(',', '.'));
			resultOfCalculatedOfTitleWordsIncludedInSentence.add(resultOfParamater);

		}
		return resultOfCalculatedOfTitleWordsIncludedInSentence;
	}

	private HashMap<Integer, String[]> calculateSemanticSimilarity(HashMap<Integer, String[]> resultSemanticSimilarity,
			String sentences[]) {
		String[][] similarityInSentence = new String[sentences.length][sentences.length];
		for (int j = 0; j < sentences.length - 1; j++) {
			String resultSemantic = "";
			String firstSentence = removePunctuations(sentences[j]);
			for (int m = j + 1; m < sentences.length; m++) {
				String secondSentence = removePunctuations(sentences[m]);

				try {
					final Document document = Jsoup.connect(url.concat(firstSentence + "&phrase2=" + secondSentence))
							.get();
					Elements elements = document.getElementsByTag("body");
					for (org.jsoup.nodes.Element element : elements) {
						resultSemantic = element.text().toString();
						resultSemantic = String
								.valueOf(String.format("%.2f", Double.parseDouble(resultSemantic)).replace(',', '.'));
						similarityInSentence[j][m] = resultSemantic;
						similarityInSentence[m][j] = resultSemantic;
						similarityInSentence[j][j] = "1";
						similarityInSentence[m][m] = "1";
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// System.out.println(j+": " +m+": = " + resulSemantic);
			}

		}
		for (int a = 0; a < sentences.length; a++) {
			String[] values = new String[sentences.length];
			for (int b = 0; b < sentences.length; b++) {
				values[b] = similarityInSentence[a][b];

			}
			resultSemanticSimilarity.put(a, values);
		}
		return resultSemanticSimilarity;
	}

	private HashMap<Integer, String[]> calculateCosinusSimilarity(HashMap<Integer, String[]> resultOfCosinusSimilarity,
			String sentences[]) {
		String[][] similarityInSentence = new String[sentences.length][sentences.length];
		// Let's work with sequences of 2 characters...
		Cosine cosine = new Cosine(2);
		for (int j = 0; j < sentences.length - 1; j++) {
			String resultSemantic = "";
			String firstSentence = removePunctuations(sentences[j]);
			for (int m = j + 1; m < sentences.length; m++) {
				String secondSentence = removePunctuations(sentences[m]);
				// Pre-compute the profile of strings
				Map<String, Integer> profile1 = cosine.getProfile(firstSentence);
				Map<String, Integer> profile2 = cosine.getProfile(secondSentence);
				resultSemantic = String
						.valueOf(String.format("%.2f", cosine.similarity(profile1, profile2)).replace(',', '.'));

				similarityInSentence[j][m] = resultSemantic;
				similarityInSentence[m][j] = resultSemantic;
				similarityInSentence[j][j] = "1";
				similarityInSentence[m][m] = "1";

			}

		}
		for (int a = 0; a < sentences.length; a++) {
			String[] values = new String[sentences.length];
			for (int b = 0; b < sentences.length; b++) {
				values[b] = similarityInSentence[a][b];

			}
			resultOfCosinusSimilarity.put(a, values);
		}
		return resultOfCosinusSimilarity;
	}

	public HashMap<Integer, String[]> stemmerAllSentences(HashMap<Integer, String[]> inStemmer) {
		HashMap<Integer, String[]> outStemmer = new HashMap();
		SnowballStemmer stemmer = new SnowballStemmer(ALGORITHM.ENGLISH);
		for (int i = 0; i < inStemmer.size(); i++) {
			String[] sentenceWithToken = inStemmer.get(i);
			sentenceWithToken = titleStemmerAndLowercase(stemmer, sentenceWithToken);
			outStemmer.put(i, sentenceWithToken);

		}

		return outStemmer;
	}

	public HashMap<Integer, String[]> removeStopWord(HashMap<Integer, String[]> sentencesTokensMap) {
		HashMap<Integer, String[]> nonStopWordsTokensMap = new HashMap();
		for (int m = 0; m < sentencesTokensMap.size(); m++) {
			String[] s = sentencesTokensMap.get(m);
			ArrayList<String> list = new ArrayList<String>(Arrays.asList(s));
			for (int i = 0; i < list.size(); i++) {
				list.set(i, list.get(i).toLowerCase());
				list.set(i, removePunctuations(list.get(i)));
				// System.out.println(list.get(i));
				if (list.get(i).isEmpty())
					list.remove(i);
			}

			LinkedHashSet<String> wordWithStopWord = new LinkedHashSet(list);

			// System.out.println(wordWithStopWord);
			/*
			 * for (String i : wordWithStopWord) { System.out.println(i); }
			 */
			LinkedHashSet<String> StopWordsSet = new LinkedHashSet<String>(Arrays.asList(stopWordsNltk));
			wordWithStopWord.removeAll(StopWordsSet);
			// System.out.println(wordWithStopWord);
			/*
			 * for (String i : wordWithStopWord) { System.out.println(i); }
			 */
			int size = wordWithStopWord.size();
			String[] array = new String[size];
			int index = 0;
			for (String element : wordWithStopWord) {
				array[index] = element;
				index++;
			}
			nonStopWordsTokensMap.put(m, array);

		}
		return nonStopWordsTokensMap;
	}

	public String removePunctuations(String source) {
		return source.replaceAll("\\p{Punct}", "");
	}

	private String[] titleTokens(String title) {

		try {
			tokenModelInput = new FileInputStream("en-token.bin");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		TokenizerModel tokenModel = null;
		try {
			tokenModel = new TokenizerModel(tokenModelInput);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Tokenizer tokenizer = new TokenizerME(tokenModel);
		String tokensSentences[] = tokenizer.tokenize(title);

		return tokensSentences;
	}

	private String[] titleStemmerAndLowercase(SnowballStemmer stemmer, String[] tokensTitle) {
		for (int j = 0; j < tokensTitle.length; j++) {
			tokensTitle[j] = (String) stemmer.stem(tokensTitle[j].toLowerCase());

		}
		return tokensTitle;
	}

	public String[] removeStopWordsOfTitle(String tokenazedTitle[]) {
		String[] s = tokenazedTitle;
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(s));
		for (int i = 0; i < list.size(); i++) {
			list.set(i, list.get(i).toLowerCase());
			list.set(i, removePunctuations(list.get(i)));
			// System.out.println(list.get(i));
			if (list.get(i).isEmpty())
				list.remove(i);
		}
		LinkedHashSet<String> wordWithStopWord = new LinkedHashSet(list);
		LinkedHashSet<String> StopWordsSet = new LinkedHashSet<String>(Arrays.asList(stopWordsNltk));
		wordWithStopWord.removeAll(StopWordsSet);

		int size = wordWithStopWord.size();
		String[] array = new String[size];
		int index = 0;
		for (String element : wordWithStopWord) {
			array[index] = element;
			index++;
		}
		return array;
	}

}

