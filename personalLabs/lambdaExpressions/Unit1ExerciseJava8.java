import java.util.Arrays;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;


public class Unit1ExerciseJava8 {

		public static void main(String[] args) {
			List<Person> people = Arrays.asList(
				new Person("Charles", "Dickens", 60),
				new Person("Lewis", "Carroll", 42),
				new Person("Thomas", "Carlyle", 51),
				new Person("Charlotte", "Bronte", 45),
				new Person("Matthew", "Arnold", 39)
				);
		
		// Step 1: Sort list by last name
		// sort(list, comparator)
		Collections.sort(people, (p1, p2) -> p1.getLastName().compareTo(p2.getLastName()));


		// Step 2: Create a method that prints all elements in the list
		System.out.println("printing all persons");
		printConditionally(people, p-> true);
		//	printAll(people);
			


		// Step 3: Create a method that prints all people that have last name beginning with C 

		//printLastNameBeginningWithC(people);
		
		System.out.print("Person with last name starting with C");
		printConditionally(people, p->p.getFirstName().startsWith("C"));

		}


//dynamic condition
private static void printConditionally(List<Person> people, Condition condition)
{
 	for (Person p: people){
		if(condition.test(p)){
		System.out.print(p);

		}

	}

}

		//not most effient way of filtering 
		private static void printLastNameBeginningWithC(List <Person> people){
	
			for(Person p: people){
		
				if(p.getLastName().startsWith("C")){
			System.out.println(p);
			}
	}
}

	private static void printAll(List <Person> people){
	
		for(Person p: people){
		System.out.println(p);
		}
	}

	

interface Condition{
boolean test(Person p);

}




}
