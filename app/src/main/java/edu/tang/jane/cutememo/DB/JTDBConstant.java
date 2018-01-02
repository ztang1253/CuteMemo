package edu.tang.jane.cutememo.DB;

/**
 * Created by admin on 2017/10/16.
 */

public class JTDBConstant {
    public static final int MIN_SLEEP = 0;
    public static final int MAX_SLEEP = 24;

    public static final int MIN_WEIGHT = 1;
    public static final int MAX_WEIGHT = 300;

    public static final String[] DINNERS = {
            "All finished", //0
            "Only ate lunch and dinner", //1
            "Only ate breakfast and lunch", //2
            "Only ate breakfast and dinner", //3
            "Only ate dinner", //4
            "Only ate lunch", //5
            "Only ate breakfast" //6
    };

    public static final int MIN_SPORTS = 0;
    public static final int MAX_SPORTS = 10;

    public static final String[] PERIOD_COLORS = {
            "Dark red", //0
            "Red", //1
            "Light red", //2
            "Brown", //3
            "Light yellow", //4
            "White", //5
            "N/A" //6
    };

    public static final String[] PERIOD_GRADE = {
            "Much", //0
            "Medium", //1
            "Little", //2
            "Clean", //3
            "N/A" //4
    };

    public static final String[] PEE_COLOR = {
            "Light yellow", //0
            "Yellow", //1
            "Dark yellow", //2
            "Other", //3
            "N/A" //4
    };

    public static int dinnerEnum2DinnerCt(int dinner) {
        switch (dinner) {
            case 0: // All finished
                return 3;
            case 1: //Only ate lunch and dinner
            case 2: //Only ate breakfast and lunch
            case 3: //"Only ate breakfast and dinner
                return 2;
            default:
                return 1;
        }
    }

    public static int[] dinnerEnum2DinnerArray(int dinner) {
        switch (dinner) {
            case 0: // All finished
                return new int[] { 1, 1, 1 };
            case 1: //Only ate lunch and dinner
                return new int[] { 0, 1, 1 };
            case 2: //Only ate breakfast and lunch
                return new int[] { 1, 1, 0 };
            case 3: //"Only ate breakfast and dinner
                return new int[] { 1, 0, 1 };
            case 4:
                return new int[] { 0, 0, 1 };
            case 5:
                return new int[] { 0, 1, 0 };
            case 6:
            default:
                return new int[] { 1, 0, 0 };
        }
    }
}
