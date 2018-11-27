import java.util.Queue;
import java.util.LinkedList;


public class BFS{

	static void transversal(Node node)
	{
	Queue<Node> q = new LinkedList<Node>();

	q.add(node);

	while(!q.isEmpty())
	{
	node = q.remove();
	System.out.println(node.key + " ");
	
	if(node.left != null)
	{
	 q.add(node.left);

	}
	if(node.right != null)
	{
		q.add(node.right);
	}

	}


}
}
