package Math_Evaluation_Library.Objects;

/**
 * Created by Antonio on 2017-10-19.
 */
public class TextFunction extends MathObject {

    private int[] numParameters;
    private String description = "";

    public TextFunction (String function, int... numParameters){
        this.function = function;
        if (numParameters.length > 0) this.numParameters = numParameters;
    }
    public TextFunction (String function, String description, int... numParameters){
        this(function, numParameters);
        this.description = description;
    }

    public String evaluate(String parameter, boolean df){ return ""; }
    public String evaluate(String[] parameters, boolean df){ return ""; }

    public int[] numParameters() {
        return numParameters;
    }
    public boolean validNumParameters(int n){
        if (numParameters != null) {
            for (int i = 0; i < numParameters.length; i++) {
                if (numParameters[i] == n) return true;
            }
        }
        return false;
    }

    public String getDescription() {
        return description;
    }
}
