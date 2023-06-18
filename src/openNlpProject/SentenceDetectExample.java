package openNlpProject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
 
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceDetectExample {
 
    public static void main(String[] args) {
        try {
            new SentenceDetectExample().sentenceDetect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public void sentenceDetect() throws IOException {
        String paragraph = "Apache openNLP supports the most common NLP tasks, such as tokenization, sentence segmentation, part-of-speech tagging, named entity extraction, chunking, parsing, and coreference resolution. These tasks are usually required to build more advanced text processing services. OpenNLP also includes maximum entropy and perceptron based machine learning.";
 
        // refer to model file "en-sent,bin", available at link http://opennlp.sourceforge.net/models-1.5/
        InputStream is = new FileInputStream("en-sent.bin");
        SentenceModel model = new SentenceModel(is);
 
        // load the model
        SentenceDetectorME sdetector = new SentenceDetectorME(model);
 
        // detect sentences in the paragraph
        String sentences[] = sdetector.sentDetect(paragraph);
 
        // print the sentences detected, to console
        for(int i=0;i<sentences.length;i++){
            System.out.println(sentences[i]);
        }
        is.close();
    }
}