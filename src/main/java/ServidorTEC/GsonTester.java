package ServidorTEC;

import com.google.gson.Gson;  

public class GsonTester { 
   public static void main(String args[]) { 
      Gson gson = new Gson(); 
      Student student = new Student(); 
      student.setAge(10); 
      student.setName("Mahesh"); 
      
      String jsonString = gson.toJson(student);
      System.out.println(jsonString); 
      
      Student student1 = gson.fromJson(jsonString, Student.class); 
      System.out.println(student1.getLol()[1][1]); 
   }   
} 
class Student { 
   private String name; 
   private int age; 
   private int[][] lol= {{3},{4,5}};
   public Student(){} 
   
   public void setLol() {
	   
   }
   
   public int [][] getLol() {
	   return lol;
   }
   
   public String getName() { 
      return name; 
   } 
   public void setName(String name) { 
      this.name = name; 
   } 
   public int getAge() { 
      return age; 
   } 
   public void setAge(int age) { 
      this.age = age; 
   } 
   public String toString() { 
      return "Student [ name: "+name+", age: "+ age+ " ]"; 
   }  
}