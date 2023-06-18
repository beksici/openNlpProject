package openNlpProject;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class DrawingGraph extends JFrame {

	static String summaryText = "";

	public DrawingGraph() {
		setBounds(0, 0, 1920, 1080);
	}

	public DrawingGraph(ArrayList<Double> ScoreOFSentencesWithWeightedAverage,
			ArrayList<Integer> valueOfBiggerThanThresholdNodes, String[] sentences,
			double[][] resultOfSemanticAndCosinusSimilarity, double tresholdOfScore) {
		setBounds(0, 0, 1920, 1080);
		/*String[] nodes = new String[sentences.length];
		String[] nodesScores = new String[sentences.length];
		String[] nodesConnectionOfBiggerThanTreshold = new String[sentences.length];
*/
		// Grafik oluşturma GRAPH VISULATION
		Graph<String, String> graph = new SparseGraph<>();
		Graph<String, String> graph1 = new SparseGraph<>();
		for (int i = 0; i < sentences.length; i++) {

			graph.addVertex("sentence :" + (i + 1));
			graph1.addVertex(String.valueOf(ScoreOFSentencesWithWeightedAverage.get(i)));
			graph1.addVertex(String.valueOf(valueOfBiggerThanThresholdNodes.get(i)));

		}
		for (int i = 0; i < sentences.length - 1; i++) {

			for (int j = i; j < sentences.length; j++) {
				if (i == j) {
					continue;
				} else {

					graph.addEdge(
							"s" + (i + 1) + "-" + (j + 1) + ": " + String.valueOf(String
									.format("%.2f", resultOfSemanticAndCosinusSimilarity[i][j]).replace(",", ".")),
							"sentence :" + (i + 1), "sentence :" + (j + 1));
				}
			}
		}
		// graph.addEdge("e1", "A", "C");

		// Grafik düzenini oluşturma  --- GRAPH SETTING FOR VISULATION
		CircleLayout<String, String> layout = new CircleLayout<>(graph);
		Layout<String, String> layout1 = new FRLayout<>(graph1);
		// layout.setSize(new Dimension(400, 400));
		// layout1.setSize(new Dimension(400, 400));

		// Grafik görselleştirme sunucusunu oluşturma
		VisualizationViewer<String, String> panel = new VisualizationViewer<String, String>(layout,
				new Dimension(600, 600));
	//	VisualizationViewer<String, String> panel1 = new VisualizationViewer<String, String>(layout1,
		//		new Dimension(50, 50));
		// BasicVisualizationServer<String, String> visualizationServer = new
		// BasicVisualizationServer<>(layout);

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		// getContentPane().setLayout(null);
		getContentPane().add(panel);
	//	getContentPane().add(panel1);

		
		// HASHSET FOR ATRIBUTES 
		HashSet<String> hashSetAtribute = new HashSet<>();
		for (int i = 0; i < sentences.length; i++) {
			hashSetAtribute.add(String.valueOf(valueOfBiggerThanThresholdNodes.get(i)));
			hashSetAtribute.add(String.valueOf(ScoreOFSentencesWithWeightedAverage.get(i)));

		}
		//System.out.println("hash sayısı");
		//System.out.println(hashSetAtribute.size());
		//System.out.println(hashSetAtribute);

		for (int i = 0; i < sentences.length; i++) {

			//***************** LOCATION ATRIBUTES(LABELS) FOR ALL NODES
			// layout.setLocation(String.valueOf(ScoreOFSentencesWithWeightedAverage.get(i)),
			// layout.getX("sentence :"+(i+1))+20, layout.getY("sentence :"+(i+1)));
			// layout.setLocation(String.valueOf(valueOfBiggerThanThresholdNodes.get(i)),
			// layout.getX("sentence :"+(i+1)), layout.getY("sentence :"+(i+1))-20);
			graph.addVertex("s" + (i + 1) + ": " + String.valueOf(valueOfBiggerThanThresholdNodes.get(i)));
			graph.addVertex("s" + (i + 1) + ": " + String.valueOf(ScoreOFSentencesWithWeightedAverage.get(i)));

			layout.setLocation("s" + (i + 1) + ": " + String.valueOf(valueOfBiggerThanThresholdNodes.get(i)),
					layout1.transform(String.valueOf(valueOfBiggerThanThresholdNodes.get(i))));
			// graph.addVertex(String.valueOf(ScoreOFSentencesWithWeightedAverage.get(i)));

			layout.setLocation("s" + (i + 1) + ": " + String.valueOf(valueOfBiggerThanThresholdNodes.get(i)),
					layout.getX("sentence :" + (i + 1)) + 20, layout.getY("sentence :" + (i + 1)));
			layout.setLocation("s" + (i + 1) + ": " + String.valueOf(ScoreOFSentencesWithWeightedAverage.get(i)),
					layout1.transform(String.valueOf(ScoreOFSentencesWithWeightedAverage.get(i))));
			// graph.addVertex(String.valueOf(ScoreOFSentencesWithWeightedAverage.get(i)));

			layout.setLocation("s" + (i + 1) + ": " + String.valueOf(ScoreOFSentencesWithWeightedAverage.get(i)),
					layout.getX("sentence :" + (i + 1)) - 20, layout.getY("sentence :" + (i + 1)));

		}

		// B düğümünü A düğümünün yanına yerleştirme
		// layout.setLocation("A", 100, 100);
		// layout.setLocation("B", layout.getX("A")+20, layout.getY("A"));
		// layout.setLocation("D", layout.getX("A"), layout.getY("A")-20);

		// Düğüm etiketlerini gösterme
		panel.getRenderContext().setVertexLabelTransformer(new CustomVertexLabelTransformer());
		panel.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		// panel1.getRenderContext().setVertexLabelTransformer(new
		// CustomVertexLabelTransformer());
		// panel1.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

		// panel.setLayout(null);

		
		//NODE COLOR
		Transformer<String, Paint> nodeFillColor = new Transformer<String, Paint>() {
			@Override
			public Paint transform(String n) {
				String[] s = n.split(" ");

				if (hashSetAtribute.contains(s[1]))
					return Color.GREEN;
				else
					return Color.RED;
				/*
				 * if(n.equals(String.valueOf(ScoreOFSentencesWithWeightedAverage.get(k))) ||
				 * n.equals(String.valueOf(valueOfBiggerThanThresholdNodes.get(k))) ) { k++;
				 * return Color.BLUE;} else { k++; return Color.RED;}
				 */

			}
		};

		panel.getRenderContext().setVertexFillPaintTransformer(nodeFillColor);
		panel.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<String>());
		// panel1.getRenderContext().setVertexFillPaintTransformer(nodeFillColor);
		// panel1.getRenderContext().setEdgeLabelTransformer(new
		// ToStringLabeller<String>());

		
		// MOUSE SETTINGS
		DefaultModalGraphMouse<String, String> gm = new DefaultModalGraphMouse<String, String>();
		gm.setMode(DefaultModalGraphMouse.Mode.PICKING);
		panel.setGraphMouse(gm);

		//NODE FRAME COLOR
		Transformer<String, Paint> nodeDrawColor = new Transformer<String, Paint>() {
			@Override
			public Paint transform(String n) {
				return Color.yellow;
			}
		};
		panel.getRenderContext().setVertexDrawPaintTransformer(nodeDrawColor);

		
		// NODE SHAPE 
		Transformer<String, Shape> nodeShapeTransformer = new Transformer<String, Shape>() {
			@Override
			public Shape transform(String n) {
				return new Rectangle(-15, -10, 30, 20); // 20 yükseklik diğerlerigenişlik
			}
		};
		panel.getRenderContext().setVertexShapeTransformer(nodeShapeTransformer);

		
		//EDGE FONT
		Transformer<String, Font> edgeFonteTransformer = new Transformer<String, Font>() {
			@Override
			public Font transform(String n) {
				Font font = new Font("", Font.BOLD, 12);
				return font; // 20 yükseklik diğerlerigenişlik
			}
		};
		panel.getRenderContext().setEdgeFontTransformer(edgeFonteTransformer);

		//EDGE COLOR
		Transformer<String, Paint> edgeColor = new Transformer<String, Paint>() {
			@Override
			public Paint transform(String e) {

				return Color.BLUE;
			}
		};
		panel.getRenderContext().setEdgeDrawPaintTransformer(edgeColor);

		//NODE NOTES (SENTENCE VISUALITE)
		Transformer<String, String> pointedNodeNote = new Transformer<String, String>() {
			@Override
			public String transform(String n) {
				if (n.contains("sentence")) {
					String s[] = n.split(":");
					return sentences[(Integer.parseInt(s[1]) - 1)];

				} else {
					return n;
				}

			}
		};

		panel.setVertexToolTipTransformer(pointedNodeNote);

		// SUMMARY TEXT IN ONE STRING  TO WRITE AREA
		for (int i = 0; i < sentences.length; i++) {

			if (ScoreOFSentencesWithWeightedAverage.get(i) >= tresholdOfScore) {
				summaryText += sentences[i] + " ";
			}

		}
		
String sentencesAll="";
for(int i =0;i<sentences.length;i++)
	sentencesAll+="s "+(i+1)+": "+sentences[i]+"\n";


		// setLayout(null);
		//BUTTON AND AREA SETTINGS
		JButton rougeCalculate = new JButton("ROUGE CALCULATE");
		rougeCalculate.setBounds(50, 200, 95, 30);
		getContentPane().add(rougeCalculate);
		JTextArea area = new JTextArea(summaryText +"\n------------------\n"+sentencesAll);
		area.setBounds(10, 30, 200, 200);
		area.setLineWrap(true); // GET DOWN LINE
		getContentPane().add(area);
		// getContentPane().remove(panel1);
		//panel1.setVisible(false);
		panel.repaint(); //REPAINT ALL CONTENT F5:)

		rougeCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//ROUGE CALCULATION FERERANCE SUMMARY -GENERATED SUMMARY
				RougeCalculator calculate = new RougeCalculator();
				calculate.setVisible(true);

			}
		});

	}
}
//VERTEX LABEL VISIBLE
class CustomVertexLabelTransformer implements Transformer<String, String> {

	@Override
	public String transform(String vertex) {
		return vertex;
	}
}