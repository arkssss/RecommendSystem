import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

class Person implements Comparable{

    public String name;
    public String age;

    public Person(String name, String age){
        this.name = name;
        this.age = age;
    }

    @Override
    public int compareTo(Object o) {
        Person p = (Person) o;
        return this.name.compareTo(p.name);
    }
}

public class main {


    public static void main(String[] args){

         // read data
        UserCF userCf = new UserCF();
        System.out.println(userCf.getUserRecommed("2"));



    }





}
