
/**
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {
	private IAVLNode root=null;
	private IAVLNode min=null;
	private IAVLNode max=null;
	public AVLTree() {
		
	}   

  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
  public boolean empty() {
   if(this.root==null) {
	   return true;
   }
   return false;
  }
  
	 /**
	   * public String search(int k)
	   *
	   * returns the info of an item with key k if it exists in the tree
	   * otherwise, returns null
	   */
  public String search(int k)
  {
	  if(empty()) {
		  return null;
	  }
	  
	  return tempSearch(this.root,k);
	  
	
  }
  /**
   * private String tempSearch(IAVLNode x,int k)
   *
   * Searches Node with key k ,starting from Node x in the tree. 
   * returns the info of that node. 
   * if the node doesnt exists-returns null
   */
  private String tempSearch(IAVLNode x,int k) {
	 while(x.isRealNode()) {
		 if(k==x.getKey()) {
			 return x.getValue();
			 
		 }
		 else {
			 if(k<x.getKey()) {
				 x=x.getLeft();
			 }
			 else {
				 x=x.getRight();
			 }
		 }
		 
	 }
	 return null;
  }

  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) {
	   if(empty()) {//if the tree is empty the root will be the new AVLNode
		   this.root=new AVLNode(k,i);
		   min=max=root;
		   return 0;
	   }
	   if(search(k)!=null) {// if k exists in the tree
		   return -1;
	   }
	      
	IAVLNode y=treePosition(this.root,k);// insert position of k
	AVLNode t=new AVLNode(k,i);
	if (min.getKey()>k)
		min=t;
	if (max.getKey()<k)
		max=t;
	int rebal =0;// rebalncing actions
	if(isLeaf(y)) {
		if(k<y.getKey()) {
			
			y.setLeft(t);
			t.setParent(y);
			promote(y);
			y.updateSize();
			rebal++;
			rebal=rebal+actOnCase(y.getParent());
			
		}
		else {
			
			y.setRight(t);
			t.setParent(y);
			promote(y);
			y.updateSize();
			rebal++;
			rebal=rebal+actOnCase(y.getParent());
			
		}
	}
	else {
		if(k<y.getKey()) {
			
			y.setLeft(t);
			t.setParent(y);
			y.updateSize();
			rebal= 0;// if the parent z isn't a leaf-no rebalnce
			
		}
		else {
			
			y.setRight(t);
			t.setParent(y);
			y.updateSize();
			rebal=0;// if the parent z isn't a leaf-no rebalnce
			
		}
		
		
	
	
	}
	
	
	while(y!=null) {// size updating
		y.updateSize();
		y=y.getParent();
	}
	
	return rebal;
	
	
	  
   }
 
   /**
    * private int actOnCase(IAVLNode z)
    *
    * rebalnces the tree after insert or join, starting from node z and up. 
    * returns the number of rebalnces operations 
    */
   private int actOnCase(IAVLNode z) {
	   if(z==null) {
		   return 0;
	   }
	   int zleft=z.getRank()-z.getLeft().getRank();
	   int zright=z.getRank()-z.getRight().getRank();
	   if((zleft==2&&zright==1)||(zleft==1&&zright==2)||(zleft==1&&zright==1)) {//valid case
		   return 0;
	   }
	   if((zleft==0&&zright==1)||(zleft==1&&zright==0)) {
		   promote(z);
		   return 1+actOnCase(z.getParent());
	   }
		   IAVLNode x=z.getLeft();// left node of z
		   IAVLNode y=z.getRight();// right node of z
		   if(zleft==0&&zright==2) {
			   int zgleft=x.getRank()-x.getLeft().getRank();
			   int zgright=x.getRank()-x.getRight().getRank();
			   if(zgleft==1&&zgright==2) {
				   rotateRight(z);
				   demote(z);
				   return 2;
			   }
			   else  {
				   if(zgleft==1&&zgright==1) {
					   rotateRight(z);
					   if(z.getParent()==null) {
						   promote(z.getParent());
						   return 2;// root of the tree-valid case
					   }
					   return 2+actOnCase(z.getParent());
				   }
				   rotateLeft(x);
				   rotateRight(z);
				   demote(x);
				   demote(z);
				   promote(z.getParent());
				   return 5;
				   
			   }
		   }
		   else{
			   
				   int zgleft=y.getRank()-y.getLeft().getRank();
				   int zgright=y.getRank()-y.getRight().getRank();
				   if(zgleft==2&&zgright==1) {
					   rotateLeft(z);
					   demote(z);
					   return 2;
				   }
				   else {
					   if(zgleft==1&&zgright==1) {
						   rotateLeft(z);
						   if(z.getParent()==null) {
							   promote(z.getParent());
							   return 2;// root of the tree-valid case
						   }
						   return 2+actOnCase(z.getParent());
					   }
					   rotateRight(y);
					   rotateLeft(z);
					   demote(y);
					   demote(z);
					   promote(z.getParent());
					   return 5;
				   }
			   
				   
				   
			  
			  
		   }
		
		   
	   
	   
	   
   }
   /**
    * private void rotateRight(IAVLNode z)
    *
    * rotates the node z to the right
    * no return 
    */
   private void rotateRight(IAVLNode z) {
	 
	 IAVLNode x=z.getLeft();
	 z.setLeft(x.getRight());
	 x.getRight().setParent(z);
	 x.setRight(z);
	 x.setParent(z.getParent());
	 z.setParent(x);
	 
	 if(this.root==z) {
		   this.root=x;
	   }
	 else {
		 IAVLNode parent=x.getParent();
		 if(parent.getKey()<x.getKey()) {
			 parent.setRight(x);
			 
		 }
		 else {
			 parent.setLeft(x);
		 }
	 }
	 
	 z.updateSize();
	 x.updateSize();
	   
   }
   /**
    * private void rotateLeft(IAVLNode x)
    *
    * rotates the node x to the left
    * no return 
    */
   private void rotateLeft(IAVLNode x) {
	   IAVLNode y=x.getRight();
	   x.setRight(y.getLeft());
	   y.getLeft().setParent(x);
	   y.setLeft(x);
	   y.setParent(x.getParent());
	   x.setParent(y);
	   IAVLNode parent=y.getParent();
	  
	   if(this.root==x) {
		   this.root=x.getParent();
	   }
	   else {
		   if(parent.getKey()<y.getKey()) {
				 parent.setRight(y);
				 
			 }
			 else {
				 parent.setLeft(y);
			 }
	   }
	   
	   x.updateSize();
	   y.updateSize();
	   
   }
   /**
    * private void demote(IAVLNode x)
    *
    * demotes the rank of x by 1
    * no return 
    */
   private void demote(IAVLNode x) {
	   x.setRank(x.getRank()-1);
	   x.setHeight(x.getRank());
   }
   /**
    * private void promote(IAVLNode x)
    *
    * promotes the rank of x by 1
    * no return 
    */
   private void promote(IAVLNode x) {
	   x.setRank(x.getRank()+1);
	   x.setHeight(x.getRank());
	
   }
   /**
    * private IAVLNode treePosition(IAVLNode x,int k)
    *
    * searches the position for a new node with key k,starting with node x and down.
    * if node with k exists,returns the node.
    * otherwise, returns the  position.
    *  
    */
   private IAVLNode treePosition(IAVLNode x,int k) {
	   IAVLNode y=null;
	   while(x.isRealNode()) {
		    y=x;
		    if(k==x.getKey()) {
		    	return x;
		    }
		   if(k<x.getKey()) {
			   
			   x=x.getLeft();
		   }
		   else {
			   x=x.getRight();
		   }
		    
	   }
	   return y;
   }
   /**
    * private boolean isLeaf(IAVLNode x)
    *
    * returns true if x is a leaf.
    * otherwise,return false.
    *  
    */
private boolean isLeaf(IAVLNode x) {
	if((!x.getLeft().isRealNode())&&(!x.getRight().isRealNode())) {
		return true;
	}
	return false;
	
}
  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   int rebal=0;
	  String info=search(k);
	  if(info==null) {
		  return -1;
	  }
	  IAVLNode node=treePosition(this.root,k);//node woth key k
	  IAVLNode parent=node.getParent();
	  if(node==min) {
		  this.min=successor(node);
	  }
	  if(node==max) {
		  this.max=predesseccor(node);
	  }
	  if(isLeaf(node)) {
		  
		  if(parent==null) {
			  this.root=null;
			  this.max=null;
			  this.min=null;
		  }
		  else {
			  if(parent.getRight()==node) {
				 IAVLNode virt=new AVLNode();
				 parent.setRight(virt);
				 virt.setParent(parent);
				 parent.updateSize();
				 
			  }
			  
			  else {
				  IAVLNode virt=new AVLNode();
					 parent.setLeft(virt);
					 virt.setParent(parent);
					 parent.updateSize();
				  
			  }
		  }
		  
	  }
	  else {
		  if(isUnary(node)) {
			  IAVLNode son;
			  if(node.getRight().isRealNode()) {
				  son=node.getRight();
			  }
			  else {
				  son=node.getLeft();
			  }
			  if(parent==null) {
				  this.root=son;
				  son.setParent(null);
			  }
			  else {
			  if(parent.getRight()==node)
			  {
				  parent.setRight(son);
			  }
			  else
			  {
				  parent.setLeft(son);
			  }
			  son.setParent(parent);
			  }
			  
		  }
		  
		  else {
			  IAVLNode y=successor(node);
			  IAVLNode parenty=y.getParent();
			  if(parenty.getLeft()==y)
			  {
				  parenty.setLeft(y.getRight());//y doesnt have left child
				  parenty.getLeft().setParent(parenty);
			  }
			  else
			  {
				  parenty.setRight(y.getRight());
				  parenty.getRight().setParent(parenty);
			  }
			  
			  
			  y.setRight(node.getRight());
			  y.setLeft(node.getLeft());
			  y.setParent(node.getParent());
			  y.getRight().setParent(y);
			  y.getLeft().setParent(y);
			  y.setRank(node.getRank());
			  if(node==this.root) {
				  this.root=y;
			  }
			  else {
				  if(node.getParent().getLeft()==node) {
					  node.getParent().setLeft(y);
				  }
				  else {
					  node.getParent().setRight(y);
				  }
			  }
			  if(parenty!=node) {
			  parent=parenty;
			  }
			  else {
				  parent=y;
			  }
			  
			 
			  
			  
		  }
		 
		  
		  
		  
		  
	  }
	  rebal=actOnCase2(parent);//rebalncing the tree
	  while(parent!=null) {// updating size of the nodes
		  parent.updateSize();
		  root=parent;
		  parent=parent.getParent();
	  }
	  return rebal;
	  
	  
   }
   /**
    * private int actOnCase2(IAVLNode z)
    *
    * rebalnces the tree after delete, starting from node z and up. 
    * returns the number of rebalnces operations 
    */
   private int actOnCase2(IAVLNode z) {
	   if(z==null) {
		   return 0;
	   }
	   int zleft=z.getRank()-z.getLeft().getRank();
	   int zright=z.getRank()-z.getRight().getRank();
	   if((zleft==2&&zright==1)||(zleft==1&&zright==2)||(zleft==1&&zright==1)) {//valid case
		   return 0;
	   }
	   if(zleft==2&&zright==2) {
		   demote(z);
		   z.updateSize();
		   return 1+actOnCase2(z.getParent());
		   
	   }
	   else {
		   if(zleft==3&&zright==1) {
			   IAVLNode y=z.getRight();
			   int zgleft=y.getRank()-y.getLeft().getRank();
			   int zgright=y.getRank()-y.getRight().getRank();
			   if(zgleft==1&&zgright==1) {
				   rotateLeft(z);
				   demote(z);
				   promote(y);
				   return 3;
			   }
			   if(zgleft==2&&zgright==1) {
				   rotateLeft(z);
				   demote(z);
				   demote(z);
				   return 3+actOnCase2(y.getParent());
			   }
			   else {
				   rotateRight(y);
				   rotateLeft(z);
				   demote(z);
				   demote(z);
				   demote(y);
				   promote(z.getParent());
				   return 6+actOnCase2(z.getParent().getParent());
			   }
			 
		   }
		   else {
			   IAVLNode y=z.getLeft();
			   int zgleft=y.getRank()-y.getLeft().getRank();
			   int zgright=y.getRank()-y.getRight().getRank();
			   if(zgleft==1&&zgright==1) {
				   rotateRight(z);
				   demote(z);
				   promote(y);
				   return 3;
			   }
			   if(zgleft==1&&zgright==2) {
				   rotateRight(z);
				   demote(z);
				   demote(z);
				   return 3+ actOnCase2(y.getParent());
			   }
			   else {
				   rotateLeft(y);
				   rotateRight(z);
				   demote(z);
				   demote(z);
				   demote(y);
				   promote(z.getParent());
				   return 6+actOnCase2(z.getParent().getParent());
			   }
			   
		   }
	   }
	   
   }
   /**
    * private boolean isUnary(IAVLNode x)
    *return true if x has only one child.
    *otherwise, return false.
    */
private boolean isUnary(IAVLNode x) {
	if((x.getRight().isRealNode()&&!x.getLeft().isRealNode())||(x.getLeft().isRealNode()&&!x.getRight().isRealNode()))
			return true;
	return false;
}
   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min()
   {
	   if(empty())
		   return null;
	   return min.getValue();
   }
   
  

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()
   {
	   if(empty())
		   return null;
	   return max.getValue(); // to be replaced by student code
   }
   
   

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
  public int[] keysToArray()
  {
	  	if(this.root!=null)
	  	{
        int[] arr = new int[this.root.getSize()]; 
        IAVLNode x=min;
        int i=0;
        while(x!=null) {
        	arr[i]=x.getKey();
        	i++;
        	x=successor(x);
        }
        return arr;      
	  	}
	  	return new int[0];
  }
  

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray()
  {
	  if(this.root!=null) {
	  String[] arr = new String[this.root.getSize()]; 
      IAVLNode x=min;
      int i=0;
      while(x!=null) {
      	arr[i]=x.getValue();
      	i++;
      	x=successor(x);
      }
      return arr;
	  }
	  return  new String[0];
  }
  /**
   * public IAVLNode successor(IAVLNode x)
   *returns the successor of x int the tree,if the successor exists.
   *otherwise,returns null.
   */
  public IAVLNode successor(IAVLNode x) {
	  if (x.getRight().isRealNode()){
		  x=x.getRight();
		  while (x.getLeft().isRealNode())
			  x=x.getLeft();
		  return x;
	  }
	  else {
		  IAVLNode y=x.getParent();
		  while(y!=null && x==y.getRight()){
			  x=y;
			  y=x.getParent();
		  }
		  return y;
	  }
	 
  }
  /**
   * public IAVLNode predesseccor(IAVLNode x)
   *returns the predesseccor of x int the tree,if the predesseccor exists.
   *otherwise,returns null.
   */
  public IAVLNode predesseccor(IAVLNode x) {
	  if (x.getLeft().isRealNode()){
		  x=x.getLeft();
		  while (x.getRight().isRealNode())
			  x=x.getRight();
		  return x;
	  }
	  else {
		  IAVLNode y=x.getParent();
		  while(y!=null && x==y.getLeft()){
			  x=y;
			  y=x.getParent();
		  }
		  return y;
	  }
	 
  }

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   public int size()
   {
	   if(this.root!=null) {
		   return this.root.getSize();
	   }
	   return 0;
   }
   
     /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   public IAVLNode getRoot()
   {
	  
	   return this.root;
   }
     /**
    * public string split(int x)
    *
    * splits the tree into 2 trees according to the key x. 
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	  * precondition: search(x) != null
    * postcondition: none
    */   
   public AVLTree[] split(int x)
   {
	   AVLNode v=new AVLNode();//temporary min and max
	  AVLTree Tsmall=new AVLTree();//new Tree with keys that small then x
	  AVLTree Tbig=new AVLTree();//new Tree with keys that big then x
	 IAVLNode xNode=treePosition(this.root,x);
	 if(xNode.getLeft().isRealNode()) {
		 Tsmall.root=xNode.getLeft(); 
		 Tsmall.root.setParent(null);
		
	 }
	 if(xNode.getRight().isRealNode()) {
		 Tbig.root=xNode.getRight();
		 Tbig.root.setParent(null);
		 
	 }
	 
	 Tsmall.min=v;
	 Tsmall.max=v;
	 Tbig.min=v;
	 Tbig.max=v;
	    
	 
	 IAVLNode y=xNode.getParent();
	 while(y!=null) {
		 if(y.getRight()==xNode) {//we now on a node that small then x
			 AVLTree tempTree=new AVLTree();
			 if(y.getLeft().isRealNode()) {
			 tempTree.root=y.getLeft();
			 }
			 tempTree.min=Tsmall.min;
			 tempTree.max=Tsmall.max;
			 if(tempTree.root!=null) {
				 tempTree.root.setParent(null);
			 }
			 
			 IAVLNode tempNode=new AVLNode(y.getKey(),y.getValue());
			 Tsmall.join(tempNode,tempTree);
			 
		 }
		 else {
			 AVLTree tempTree=new AVLTree();
			 if(y.getRight().isRealNode()) {
			 tempTree.root=y.getRight();
			
			 }
			 tempTree.min=Tbig.min;
			 tempTree.max=Tbig.max;
			 IAVLNode tempNode=new AVLNode(y.getKey(),y.getValue());
			
			 if(tempTree.root!=null) {
				 tempTree.root.setParent(null);
			 }
			 
			 
			 Tbig.join(tempNode,tempTree);
			
			 
		 }
		 xNode=xNode.getParent();
		 y=y.getParent();
		 
	 }
	 AVLTree[] res=new AVLTree[2];
	 res[0]=Tsmall;
	 res[1]=Tbig;
	 
	 if(Tsmall.getRoot()!=null) {
		 Tsmall.min=findMin(Tsmall.getRoot());
		 Tsmall.max=findMax(Tsmall.getRoot());
	 }
	 if(Tbig.getRoot()!=null) {
		 Tbig.min=findMin(Tbig.getRoot());
		 Tbig.max=findMax(Tbig.getRoot());
	 }
	 
	 return res;
   }
   /**
    * private IAVLNode findMin(IAVLNode x)
    *returns the minimum-the search starts with node x and down.
    *if x=null-returns null
    */  
   private IAVLNode findMin(IAVLNode x) {
	   if(x!=null) {
	   while(x.getLeft().isRealNode()) {
		   x=x.getLeft();
	   }
	   return x;
	   }
	   return null;
   }
   /**
    * private IAVLNode findMax(IAVLNode x)
    *returns the maximum-the search starts with node x and down.
    *if x=null-returns null
    */  
   private IAVLNode findMax(IAVLNode x) {
	   if(x!=null) {
	   while(x.getRight().isRealNode()) {
		   x=x.getRight();
	   }
	   return x;
	   }
	   return null;
   }
   /**
    * public join(IAVLNode x, AVLTree t)
    *
    * joins t and x with the tree. 	
    * Returns the complexity of the operation (rank difference between the tree and t+1)
	  * precondition: keys(x,t) < keys() or keys(x,t) > keys()
    * postcondition: none
    */   
   public int join(IAVLNode x, AVLTree t){
	   int ret=0;
	   if(this.empty()&&t.empty()) {
		   this.insert(x.getKey(),x.getValue());
		   return 1;
	   }
	   
	   else {
		   
	   
	   if(empty()) {
		   ret=t.getRoot().getRank()+2;
		   this.root=t.getRoot();
		   this.min=t.min;
		   this.max=t.max;
		   this.insert(x.getKey(),x.getValue());
		   return ret;
		   
	   }
	   
	   if(t.empty()){
		   ret=this.getRoot().getRank()+2;
		   this.insert(x.getKey(),x.getValue());
		   return ret;
	   }
	   }
	   
	   ret=Math.abs(this.getRoot().getRank()-t.getRoot().getRank())+1;
	   
	   if(x.getKey()<this.root.getKey()) {
		   this.min=t.min;
		   if(t.getRoot().getRank()<=this.getRoot().getRank()) {
			   x.setRank(t.getRoot().getRank()+1);
			   IAVLNode y=this.getRoot();
			   while(y.getRank()>t.getRoot().getRank())
				   y=y.getLeft();
			   x.setLeft(t.getRoot());
			   x.setRight(y);
			   x.setParent(y.getParent());
			   t.getRoot().setParent(x);
			   y.setParent(x);
			   if(x.getParent()!=null) {
			   x.getParent().setLeft(x);
			   }
			   
			   
			   
			   }
			   
			   else {
				   x.setRank(this.getRoot().getRank()+1);
				   IAVLNode y=t.getRoot();
				   while(y.getRank()>this.getRoot().getRank())
					   y=y.getRight();
				   x.setLeft(y);
				   x.setRight(this.getRoot());
				   x.setParent(y.getParent());
				   this.getRoot().setParent(x);
				   y.setParent(x);
				   if(x.getParent()!=null) {
				   x.getParent().setRight(x);
				   }
				   
			   }
				   
		   }
		   
		   else {
			   this.max=t.max;
			   if(t.getRoot().getRank()<=this.getRoot().getRank()) {
				   x.setRank(t.getRoot().getRank()+1);
				   IAVLNode y=this.getRoot();
				   while(y.getRank()>t.getRoot().getRank())
					   y=y.getRight();
				   x.setRight(t.getRoot());
				   x.setLeft(y);
				   x.setParent(y.getParent());
				   t.getRoot().setParent(x);
				   y.setParent(x);
				   if(x.getParent()!=null) {
					    x.getParent().setRight(x);
				   }
				  
			   }
			   
			   else {
				   x.setRank(this.getRoot().getRank()+1);
				   IAVLNode y=t.getRoot();
				   while(y.getRank()>this.getRoot().getRank())
					   y=y.getLeft();
				   x.setRight(y);
				   x.setLeft(this.getRoot());
				   x.setParent(y.getParent());
				   this.getRoot().setParent(x);
				   y.setParent(x);
				   if(x.getParent()!=null) {
				   x.getParent().setLeft(x);
				   }
				
			   }
		   }
	   
	   //root updating
	   while(this.root.getParent()!=null) {
		   this.root=this.root.getParent();
	   }
	   
	 //size updating
	   IAVLNode tmp=x;
	   while(tmp!=null) {
		   tmp.updateSize();
		   tmp=tmp.getParent();
		   
	   }
	   
	   x.setHeight(Math.max(x.getRight().getHeight(),x.getLeft().getHeight())+1); //x.height updating
	   
	   actOnCase(x.getParent()); //rebalancing 
	   
	   return ret;
	   
	   }
   

	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode{	
		public int getKey(); //returns node's key (for virtual node return -1)
		public String getValue(); //returns node's value [info] (for virtual node return null)
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
    	public void setHeight(int height); // sets the height of the node
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
    	public void setRank(int i);// sets the rank of the AVLNode
    	public int getRank();// returns the rank of the AVLNode
    	public int getSize();// Returns the size of the subtree that the current node is it's root
    	public void setSize(int size);// Sets the size of the subtree that the current node is it's root
    	public void updateSize();// updates the size of the subtree that the current node is it's root
	}

   /**
   * public class AVLNode
   *
   * If you wish to implement classes other than AVLTree
   * (for example AVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IAVLNode)
   */
  public class AVLNode implements IAVLNode{
	  private int key=-1;
	  private String value="";
	  private int rank=-1;
	  private IAVLNode left=null;
	  private IAVLNode right=null;
	  private IAVLNode parent=null;
	  private int height=-1; 
	  private int size=0;
	  
	  /**
	   * public AVLNode()
	   * creates an emptry AVLNode
	   */
	  public AVLNode() {
		  
	  }
	  /**
	   * public AVLNode(int key,String value)
	   * creates an AVLNode and sets the paramters: key and value
	   */
	  public AVLNode(int key,String value) {
		  this.key=key;
		  this.value=value;
		  setRank(0);
		  setHeight(0);
		  this.left=new AVLNode();
		  this.right=new AVLNode();
		  this.left.setParent(this);
		  this.right.setParent(this);
		  size=1;
		  
	  }
	  /**
	   * public void setRank(int rank)
	   * sets the rank of the node
	   */
	  public void setRank(int rank)
	  {
		  this.rank=rank;
	  }
	  /**
	   * public int getRank()
	   * returns the rank of the node
	   */
	  public int getRank(){
		  return this.rank;
	  }
	  /**
	   * public int getKey()
	   * returns the key of the node
	   */
		public int getKey()
		{
			return key;
		}
		 /**
		   * public String getValue()
		   * returns the value of the node
		   */
		public String getValue()
		{
			return value; 
		}
		 /**
		   * public void setLeft(IAVLNode node)
		   * sets the left child of the current node to be node
		   */
		public void setLeft(IAVLNode node)
		{
			this.left=node;
			
			
		}
		 /**
		   * public IAVLNode getLeft()
		   * returns the left child
		   */
		public IAVLNode getLeft()
		{
			return left;
		}
		 /**
		   * public void setRight(IAVLNode node)
		   * sets the right child of the current node to be node
		   */
		public void setRight(IAVLNode node)
		{
			this.right=node;
			
		}
		 /**
		   * public IAVLNode getRight()
		   * returns the right child
		   */
		public IAVLNode getRight()
		{
			return right;
		}
		 /**
		   * public void setParent(IAVLNode node)
		   * sets the parent of the current node to be parent
		   */
		public void setParent(IAVLNode node)
		{
			this.parent=node;
		}
		 /**
		   * public IAVLNode getParent(IAVLNode node)
		   * returns the parent
		   */
		public IAVLNode getParent()
		{
			return parent; 
		}
		 /**
		   * public boolean isRealNode()
		   * returns true if the node is real.
		   * otherwise, returns false
		   */
		public boolean isRealNode()
		{
			if (rank==-1)
				return false;
			return true;
		}
		 /**
		   * public void setHeight(int height)
		   * sets the height of the current node to be height
		   */
    public void setHeight(int height)
    {
    	this.height=height;
    }
    /**
	   * public int getHeight(int height)
	   * returns the height
	   */
    public int getHeight()
    {
      return rank; 
    }
    /**
	   * public int getSize()
	   * returns the amount of nodes under the current node including himself.
	   */
    public int getSize()
    {
      return size; 
    }
    /**
	   * public void setSize()
	   * sets the size.
	   */
    public void setSize(int size)
    {
      this.size=size; 
    }
    
    /**
	   * public void updateSize()
	   * increasing the size of the node by 1
	   */
    public void updateSize() {
    	size=1+right.getSize()+left.getSize();
    }
   
    
  }


}
  

