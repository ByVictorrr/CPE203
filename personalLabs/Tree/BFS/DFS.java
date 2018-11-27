import java.util.Queue;
import java.util.LinkedList;


public class DFS{

	static void transversal(Node node)
	{
		
		if(node == null)
			return;

		System.out.println(node.data);
		transversal(node.left);
		transversal(node.right);


	}


}
