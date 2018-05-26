package mju_avengers.please_my_fridge.recipe_model;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.List;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;


public class TensorflowRecommend  implements Recommend {
    // Only returns if at least this confidence
    //must be a classification percetnage greater than this
    private static final float THRESHOLD = 0.1f;

    private TensorFlowInferenceInterface tfHelper;

    private String name;
    private String inputName0;
    private String inputName1;
    private String outputName;


    private List<String> labels;
    private float[] output;
    private String[] outputNames;

    //given a saved drawn model, lets read all the classification labels that are
    //stored and write them to our in memory labels list
    private static List<String> readLabels(AssetManager am, String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(am.open(fileName)));

        String line;
        List<String> labels = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            labels.add(line);
        }

        br.close();
        return labels;
    }

    //given a model, its label file, and its metadata
    //fill out a classifier object with all the necessary
    //metadata including output prediction
    public static TensorflowRecommend create(AssetManager assetManager, String name,
                                             String modelPath, String labelFile, String inputName0, String inputName1,
                                             String outputName) throws IOException {
        Log.e("에셋", assetManager.toString());

        //intialize a classifier
        TensorflowRecommend c = new TensorflowRecommend();

        //store its name, input and output labels
        c.name = name;

        c.inputName0 = inputName0;
        c.inputName1 = inputName1;
        c.outputName = outputName;

        //read labels for label file
        c.labels = readLabels(assetManager, labelFile);

        //set its model path and where the raw asset files are
        c.tfHelper = new TensorFlowInferenceInterface(assetManager, modelPath);
        int numClasses = 1;


        c.outputNames = new String[] { outputName };

        c.outputName = outputName;
        c.output = new float[numClasses];



        return c;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Recommendation recognize(final int id, final int recipeid) {

        //using the interface
        //give it the input name, raw pixels from the drawing,
        //input size
        float[] id_array = new float[1];
        id_array[0] = (float)id;
        float[] recipeid_array = new float[1];
        recipeid_array[0] = (float)recipeid;

        tfHelper.feed(inputName0, id_array, 1, 1);
        tfHelper.feed(inputName1, recipeid_array, 1, 1);

        //get the possible outputs
        tfHelper.run(outputNames);

        //get the output
        tfHelper.fetch(outputName, output);

        // Find the best classification
        //for each output prediction
        //if its above the threshold for accuracy we predefined
        //write it out to the view
        Recommendation ans = new Recommendation();

        ans.update(output[0], Integer.toString(recipeid));

        return ans;
    }
}
