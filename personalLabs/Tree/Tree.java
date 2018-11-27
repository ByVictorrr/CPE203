// A Java program to introduce Binary Tree
public class Tree
{
    // Root of Binary Tree
    Node root = null;

public Node buildTree(int [] values, int lo,int hi)
{
  if(lo > hi){return null;} //base case
  
	 int mid = (hi+lo)/2;

	  Node n =  new Node(values[mid]);

	  n.left = buildTree(values, lo, mid -1);
	  n.right = buildTree(values, mid+1, hi);

	return n;


}
    // Constructors
   public Tree(int [] values)
    {
        root = buildTree(values, 0, values.length-1);
    }



/* Given a binary tree, print its nodes in preorder*/
    public void printPreorder(Node node) 
    {  
        if (node == null) 
            return; 
  
        /* first print data of node */
        System.out.print(node.key + " "); 
  
        /* then recur on left sutree */
        printPreorder(node.left); 
  
        /* now recur on right subtree */
        printPreorder(node.right); 
    } 

}


