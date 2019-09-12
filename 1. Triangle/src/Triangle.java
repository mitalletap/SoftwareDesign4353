// This program will take a users input and determine the following:
// 1. If the values accepted are float and not strings or characters
// 2. If the values accepted are greater than 0 and allow the contruction of a triangle
// 3. If the values accepted do not create an illegal triangle
// 4. What kind of triangle will be produced.

// Created By Mital Patel.

import java.util.Scanner;
import java.lang.Math;

class Point extends Triangle {

    float p1, p2, p3, perimeter;
    public Point(float point1, float point2){
        p1 = point1;
        p2 = point2;
    }
}







class Triangle {

    float l1, l2, l3;

    public Triangle() {
        // NULL
    }

    public Triangle(float length1, float length2, float length3) {
        l1 = length1;
        l2 = length2;
        l3 = length3;
    }

    public boolean isTriangle(){
        if(((l1 == 0) && (l2 == 0) && (l3 == 0)) || ((l1 == 1) && (l2 == 0) && (l3 == 0)) || ((l1 == 0) && (l2 == 1) && (l3 == 0)) || ((l1 == 0) && (l2 == 0) && (l3 == 1))){
            return false;
        } else {
            return true;
        }
    }

    public boolean isValid(){
        if((l1 > 0 && l2 > 0 && l3 > 0) && ((l1 + l2 >= l3) && (l1 + l3 >= l2)) && (l2 + l3 >= l1)) {
            return true;
        } else {
            return false;
        }
    }

    // Has Right Angle
    public boolean isRight() {
        // a*a + b*b = c*c (Pythagorean Theorem for Right Triangles)
        if (((l1 * l1) + (l2 * l2) == (l3 * l3)) || ((l1 * l1) + (l3 * l3) == (l2 * l2)) || ((l2 * l2) + (l3 * l3) == (l1 * l1))) {
            return true;
        } else {
            return false;
        }
    }

    // Has 0 Equal Sides
    public boolean isScalene() {
        if ((l1 != l2) && (l2 != l3) && (l1 != l3)) {
            return true;
        } else {
            return false;
        }
    }

    // Has 2 Equal Sides
    public boolean isIsosceles() {
        if (((l1 == l2) && (l1 != l3)) || ((l1 == l3) && (l1 != l2)) || ((l2 == l3) && (l2 != l1))) {
            return true;
        } else {
            return false;
        }
    }

    // Has 3 Equal Sides
    public boolean isEquilateral() {
        if ((l1 == l2) && (l1 == l3)) {
            return true;
        } else {
            return false;
        }
    }

    void compute(float l1, float l2, float l3) {
        if (isRight()) {
            System.out.println("The Triangle is Right");
        } else if (isIsosceles()) {
            System.out.println("The Triangle is Isosceles");
        } else if (isScalene()) {
            System.out.println("The Triangle is Scalene");
        } else if (isEquilateral()) {
            System.out.println("The Triangle is Equilateral");
        } else {
            System.out.println("Error. Something went wrong.");
        }
    }

    void getArea(float l1, float l2, float l3){
        // Herons Formula
        double s = ((l1 + l2 + l3)/2);
        double areaSqrt = (s*(s-l1)*(s-l2)*(s-l3));
        System.out.println("The Area is: " + Math.pow(areaSqrt, .5));
    }

}








class Test {
    public static void main(String[] args){
        Scanner userScanner = new Scanner(System.in);
        float f1=3, f2=3, f3=3;
        int s1;
        computeIt();
        userScanner.close();
    }

    private static void computeIt() {
        Scanner scanner = new Scanner(System.in);
        String userResponse = "y";
        int s1 = 0;
        do {
            System.out.println("Would you like to solve with lengths or points? Please type 1 for Lengths or 2 for Points");
            String inputType = scanner.nextLine();
            try {
                s1 = Integer.parseInt(inputType);
            } catch (NumberFormatException nfe){
                break;
            }

            if(s1 == 1){
                TriangleAnswer();
            } else if (s1 == 2){
                PointAnswer();
            } else {
                System.out.println("Please choose another selection.");
            }
        } while(userResponse == "y");
        System.out.println("Invalid Parameter(s)");
        return;
    }



    private static void TriangleAnswer() {
        Scanner scanner = new Scanner(System.in);
        String s1, s2, s3;
        float f1=3, f2=3, f3=3;

        System.out.println("Please enter 3 side lengths, one at a time.");
        System.out.println("First Length:");
        s1 = scanner.nextLine();
        System.out.println("Second Length:");
        s2 = scanner.nextLine();
        System.out.println("Third Length:");
        s3 = scanner.nextLine();

        try {
            f1 = Float.parseFloat(s1);
            f2 = Float.parseFloat(s2);
            f3 = Float.parseFloat(s3);

            Triangle t1 = new Triangle(f1, f2, f3);
            if(t1.isTriangle() && t1.isValid()){
                System.out.println("Triangle is Good");
            } else {
                System.out.println("Triangle is BAD");

            }
            System.out.println("First Length: " + s1);
            System.out.println("Second Length: " + s2);
            System.out.println("Third Length: " + s3);
            t1.compute(f1, f2, f3);
            t1.getArea(f1, f2, f3);

        } catch (NumberFormatException nfe){
            System.out.println("Invalid Parameter(s)");
        }


    }


    private static void PointAnswer() {
        Scanner scanner = new Scanner(System.in);
        String s1, s2, s3, s4, s5, s6;
        float p1, p2, p3, p4, p5, p6;
        System.out.println("Please enter 3 Ordered Pairs");
        System.out.println("First Pair:");
        s1 = scanner.next();
        s2 = scanner.next();
        System.out.println("Second Pair:");
        s3 = scanner.next();
        s4 = scanner.next();
        System.out.println("Third Pair:");
        s5 = scanner.next();
        s6 = scanner.next();

        try {
            p1 = Float.parseFloat(s1);
            p2 = Float.parseFloat(s2);
            p3 = Float.parseFloat(s3);
            p4 = Float.parseFloat(s4);
            p5 = Float.parseFloat(s5);
            p6 = Float.parseFloat(s6);

            Point a = new Point(p1,p2);
            Point b = new Point(p3,p4);
            Point c = new Point(p5,p6);


            System.out.println("First Pair: (" + p1 + "," + p2 + ")");
            System.out.println("Second Pair: (" + p3 + "," + p4 + ")");
            System.out.println("Third Pair: (" + p5 + "," + p6 + ")");

            float l1, l2, l3;
            double d1 = Math.pow(((Math.pow((p3 - p1), 2) + Math.pow((p4-p2), 2))), .5);
            double d2 = Math.pow(((Math.pow((p5 - p3), 2) + Math.pow((p6-p4), 2))), .5);
            double d3 = Math.pow(((Math.pow((p5 - p1), 2) + Math.pow((p6-p2), 2))), .5);

            l1 = (float)d1;
            l2 = (float)d2;
            l3 = (float)d3;
            System.out.println("Lengths from First to Second Point: " + l1);
            System.out.println("Lengths from Second to Third Point: " + l2);
            System.out.println("Lengths from First to Third Point: " + l3);
            Triangle t1 = new Triangle(l1, l2, l3);
            t1.compute(l1, l2, l3);


        } catch (NumberFormatException nfe){
            System.out.println("Invalid Parameter(s)");
        }
    }
}