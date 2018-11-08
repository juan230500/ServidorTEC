package ServidorTEC;

import java.util.Arrays;

import com.google.gson.Gson;  

public class GsonTester { 
	public static void main(String args[]) { 
	      Gson gson = new Gson();  
	      String name = "Mahesh Kumar"; 
	      long rollNo = 1; 
	      boolean verified = false; 
	      int[] marks = {100,90,85};
	      
	      String json=gson.toJson(marks);
	      int[] m2=gson.fromJson(json, int[].class);
	      System.out.println("marks:" + Arrays.toString(m2));
	      
	      //Serialization 
	      System.out.println("{"); 
	      System.out.println("name: " + gson.toJson(name) +","); 
	      System.out.println("rollNo: " + gson.toJson(rollNo) +","); 
	      System.out.println("verified: " + gson.toJson(verified) +","); 
	      System.out.println("marks:" + gson.toJson(marks)); 
	      System.out.println("}");  
	      
	      //De-serialization 
	      name = gson.fromJson(gson.toJson(name), String.class); 
	      rollNo = gson.fromJson("1", Long.class); 
	      verified = gson.fromJson("false", Boolean.class); 
	      marks = gson.fromJson("[100,90,85]", int[].class);  
	      
	      System.out.println("name: " + name); 
	      System.out.println("rollNo: " + rollNo); 
	      System.out.println("verified: " +verified); 
	      System.out.println("marks:" + Arrays.toString(marks)); 
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