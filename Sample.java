class ProgrammingLanguage {
  //attributes
  String language_name;
  String language_type;
 
  //constructor
  ProgrammingLanguage(String n, String t) {
    language_name = n;
    language_type = t;
  }
 
  //main method
  public static void main(String[] args) {
    //creating objects of class
    ProgrammingLanguage C = new ProgrammingLanguage("C", "Procedural");
    ProgrammingLanguage Cpp = new ProgrammingLanguage("C++", "Object oriented");
 
    //calling method    
    C.display();
    Cpp.display();
  }
 
  //method (function in C++ programming)
  void display() {
    System.out.println("Language name:"+language_name);
    System.out.println("Language type:"+language_type);
  }
}