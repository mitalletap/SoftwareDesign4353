// This program will take a users input and determine the following:
// 1. If the values accepted are float and not strings or characters
// 2. If the values accepted are greater than 0 and allow the contruction of a triangle
// 3. If the values accepted do not create an illegal triangle
// 4. What kind of triangle will be produced.

// Created By Mital Patel.
import java.util.Scanner;

class Triangle {

    float l1, l2, l3, perimeter;

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

}


class Test {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String s1, s2, s3;
        float f1=3, f2=3, f3=3;

        String userResponse = "y";
        while(!userResponse.equals("n")){
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
            } catch (NumberFormatException nfe){
                System.out.println("Invalid Parameter(s)");
                break;
            }

            Triangle t1 = new Triangle(f1, f2, f3);

            if(t1.isTriangle() && t1.isValid()){
                System.out.println("Triangle is Good");
            } else {
                System.out.println("Triangle is BAD");
                break;
            }


            System.out.println("First Length: " + s1);
            System.out.println("Second Length: " + s2);
            System.out.println("Third Length: " + s3);
            t1.compute(f1, f2, f3);

            System.out.println("Would you like to compute another Triangle? 'y' or 'n'");
            userResponse = scanner.next();
            System.out.println("=========================================================");
        }

    }
}