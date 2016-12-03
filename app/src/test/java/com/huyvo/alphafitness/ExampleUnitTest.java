package com.huyvo.alphafitness;

import com.huyvo.alphafitness.model.Year;

import org.junit.Test;


public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Year year = new Year();

        System.out.println(year.getYear());
        System.out.println(year.numberOfWeeks());


    }
}