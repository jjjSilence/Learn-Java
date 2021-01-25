package com.example.annotations;

import com.example.net.mindview.atunit.Test;
import com.example.net.mindview.util.OSExecute;

public class AtUnitExample1 {
    public String methodOne() {
        return "This is methodOne";
    }

    public int methodTwo() {
        System.out.println("This is methodTwo");
        return 2;
    }

    @Test
    boolean methodOneTest() {
        return methodOne().equals("This is methodOne");
    }

    @Test
    boolean m2() {
        return methodTwo() == 2;
    }

    @Test
    private boolean m3() {
        return true;
    }

    // Shows output for failure:
    @Test
    boolean failureTest() {
        return false;
    }

    @Test
    boolean anotherDisappointment() {
        return false;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("----------------start");
        OSExecute.command(
                "java net.mindview.atunit.AtUnit AtUnitExample1");
        System.out.println("----------------end");
    }
}
