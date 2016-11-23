package com.huyvo.alphafitness.data;

public class DatabaseScheme {

    public static final class UserTable{
        public static final String NAME = "user";

        public final class Cols{
            public static final String ID = "_id";
            public static final String FIRST_NAME = "firstname";
            public static final String LAST_NAME = "lastname";
            public static final String WEIGHT = "weight";
            public static final String TOTAL_DISTANCE = "totaldistance";
            public static final String TOTAL_WORKOUT_COUNT = "totalworkoutcount";
            public static final String TOTAL_CALORIES_BURNED = "totalcaloriesburned";
            public static final String WEEK = "week";
        }
    }
}
