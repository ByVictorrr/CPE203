
public class Main{


public static void main(String [] args){

	int [] values = {34, 23, 76, 45, 12, 32, 26, 78, 47, 55};
	Tree tree = new Tree(values);
	
	try{

		BFS.transversal(tree.root);
	}
	catch(Exception e){
	e.printStackTrace();
	}

	
}




}

