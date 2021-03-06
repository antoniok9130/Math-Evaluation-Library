package Tests;

/**
 * Created by Antonio on 2017-10-01.
 */
public class Unit_Conversion_Tests extends Engine_Tests {

    public Unit_Conversion_Tests(String name) {
        super(name);
    }


    @Override
    public void run(){

        evaluationTest("[1mi->km]", 1.609344);
        evaluationTest("[2.356mth->s]", 6195681.575999999);
//        evaluationTest("[32yr->s]", 6195862.90751904);
        evaluationTest("[2.356km->s]", "NaN");
        evaluationTest("[1m/s->km/h]", 3.6);
        evaluationTest("[30°C->°F]", 86);
        evaluationTest("unit(1, m/s, km/h)", 3.6);

    }
}
