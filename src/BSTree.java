import java.util.ArrayDeque;
import java.util.Queue;

public class BSTree<T> 
{
	private BSNode<T> root;

	public BSTree()
	{
		root = new BSNode<T>();
	}

	public boolean isValid()
	{
		Queue<BSNode<T>> queue = new ArrayDeque<BSNode<T>>();

		queue.offer(root);

		while (!queue.isEmpty())
		{
			BSNode<T> node = queue.poll();

			if (node.isEmpty())
			{
				continue;
			}

			if (node != getRoot())
			{
				//se o no eh esquerdo e a chave dele eh maior do que a do pai
				if (node.isLeft() && node.getKey() > node.getParent().getKey())
					return false;

				//se o no eh direito e a chave dele eh menor do que a do pai
				if (node.isRight() && node.getKey() < node.getParent().getKey())
					return false;

				queue.offer(node.getLeft());
				queue.offer(node.getRight());
			}
		}

		return true;
	}

	public void breadthTraversalPrint()
	{
		Queue<BSNode<T>> queue = new ArrayDeque<BSNode<T>>();

		queue.offer(root);

		int k = 1;
		int i = 0;
		
		while (!queue.isEmpty())
		{
			i++;
			
			if (k == i)
			{
				k = k * 2;
				System.out.println();
			}
			
			BSNode<T> node = queue.poll();

			if (node.isEmpty())
			{
				System.out.print("E - ");
				continue;
			}

			System.out.print(node.getValue() + " - ");
			queue.offer(node.getLeft());
			queue.offer(node.getRight());
		}
		
		System.out.println("=====================");
	}

	public void printInOrder()
	{
		inorder(root);
	}

	private void inorder(BSNode<T> node)
	{
		if (node.isEmpty())
			return;

		inorder(node.getLeft());
		System.out.println(node.getValue());
		inorder(node.getRight());
	}

	public void printPostOrder()
	{
		postorder(root);
	}

	private void postorder(BSNode<T> node)
	{
		if (node.isEmpty())
			return;

		postorder(node.getLeft());
		postorder(node.getRight());
		System.out.println(node.getValue());
	}

	public void printPreOrder()
	{
		preorder(root);
		System.out.println("\n==============================");
	}

	private void preorder(BSNode<T> node)
	{
		if (node.isEmpty())
			return;

		System.out.print(node.getValue() + " ");	
		preorder(node.getLeft());
		preorder(node.getRight());
	}

	public T search(int key)
	{
		BSNode<T> foundNode = searchHelper(root, key);
		return foundNode == null ? null : foundNode.getValue();
	}

	private BSNode<T> searchHelper(BSNode<T> node, int key)
	{
		if (node.isEmpty())
			return null;

		if (key > node.getKey())
			return searchHelper(node.getRight(), key);
		else if (key < node.getKey())
			return searchHelper(node.getLeft(), key);

		return node;
	}

	public T getMin()
	{
		return getMinHelper(root).getValue();
	}

	private BSNode<T> getMinHelper(BSNode<T> node)
	{	
		while (!node.getLeft().isEmpty())
		{
			node = node.getLeft();
		}

		return node;
	}

	public T getMax()
	{
		return getMaxHelper(root).getValue();
	}

	private BSNode<T> getMaxHelper(BSNode<T> node)
	{
		while (!node.getRight().isEmpty())
		{
			node = node.getRight();
		}

		return node; 
	}

	public T getPredecessor(int key)
	{
		BSNode<T> predecessorNode = getPredecessorHelper(key);
		return predecessorNode == null ? null : predecessorNode.getValue();
	}

	public BSNode<T> getPredecessorHelper(int key)
	{
		BSNode<T> node = searchHelper(root, key);

		if (node.hasLeftChild())
		{
			return getMaxHelper(node.getLeft());
		}
		else 
		{
			if (node == getRoot())
				return null;

			node = node.getParent();

			while (node.getKey() > key)
			{
				node = node.getParent();
			}

			return node;
		}
	}

	public T getSuccessor(int key)
	{
		BSNode<T> successorNode = getSuccessorHelper(key);
		return successorNode == null ? null : successorNode.getValue();    
	}

	public BSNode<T> getSuccessorHelper(int key)
	{
		BSNode<T> node = searchHelper(root, key);

		if (node.hasRightChild())
		{
			return getMinHelper(node.getRight());
		}
		else 
		{
			node = node.getParent();

			while (node.getKey() < key)
			{
				node = node.getParent();
			}

			return node;
		}
	}

	public boolean insert(T value, int key)
	{
		return insertHelper(root, value, key);
	}

	private boolean insertHelper(BSNode<T> node, T value, int key)
	{
		if (node.isEmpty())
		{
			node.setValue(value);
			node.setKey(key);

			return true;
		}
		else
		{
			if (key > node.getKey())
				return insertHelper(node.getRight(), value, key);
			else if (key < node.getKey())
				return insertHelper(node.getLeft(), value, key);

			return false;
		}
	}

	public T remove(int key)
	{
		BSNode<T> foundNode = searchHelper(root, key);
		
		if (foundNode == null)
			return null;
		
		//se o n�� n��o tiver filhos
		if (!(foundNode.hasLeftChild() || foundNode.hasRightChild()))
		{	
			if (getRoot() == foundNode) 
			{
				setRoot(new BSNode<T>());
			} 
			else 
			{
				BSNode<T> parent = foundNode.getParent();
				
				if (foundNode.isLeft())
					parent.setLeft(new BSNode<T>());
				else
					parent.setRight(new BSNode<T>());
			}
		}
		
		if (foundNode.hasRightChild()) 
		{
			BSNode<T> successor = getSuccessorHelper(key);
			
			if (successor.hasRightChild())
			{	
				BSNode<T> parent = successor.getParent();
					
				if (parent != foundNode)
					parent.setLeft(successor.getRight());
			}
			 
			successor.setLeft(foundNode.getLeft());
			
			if (foundNode != successor.getParent())
				successor.setRight(foundNode.getRight());
			
			successor.getParent().setLeft(new BSNode<T>());
			
			if (getRoot() == foundNode)
				setRoot(successor);
			else 
				if (foundNode.isLeft())
					foundNode.getParent().setLeft(successor);
				else
					foundNode.getParent().setRight(successor);
		} 
		else if (foundNode.hasLeftChild()) 
		{
			if (getRoot() == foundNode)
				setRoot(foundNode.getLeft());
			else 
				if (foundNode.isLeft())
					foundNode.getParent().setLeft(foundNode.getLeft());
				else
					foundNode.getParent().setRight(foundNode.getLeft());
		}
		
		return foundNode.getValue();
	}

	public BSNode<T> getRoot() 
	{
		return root;
	}

	public void setRoot(BSNode<T> root) 
	{
		this.root = root;
	}
	
	
	
}
































////CASO 1: NÓ SEM FILHOS
//
////verifica se é filho esquerdo ou direito e seta
////o respectivo filho de seu pai pra null
//
////falta fazer o caso em que o foundNode é raiz
//
//BSNode<T> parent = foundNode.getParent();
//
//if( ! ( foundNode.hasLeftChild() || foundNode.hasRightChild() ) )
//{
//	if (foundNode.isLeft())
//	{
//		parent.setLeft(new BSNode<T>());
//	}
//	else
//	{
//		parent.setRight(new BSNode<T>());
//	}
//}
//
////CASO 2: NÓ SÓ TEM UM FILHO
//
////substitui o pai
//
//if ( ( foundNode.hasLeftChild() || foundNode.hasRightChild() ) 
//		&& !( foundNode.hasLeftChild() && foundNode.hasRightChild() )
//		)
//{
//	if(foundNode.hasLeftChild()){
//		
//		BSNode<T> son = foundNode.getLeft();
//		
//		if (foundNode.isLeft())
//		{
//			parent.setLeft(son);
//		}
//		else
//		{
//			parent.setRight(son);
//		}
//	}
//	else{
//		
//		BSNode<T> son = foundNode.getRight();
//		
//		if (foundNode.isLeft())
//		{
//			parent.setLeft(son);
//		}
//		else
//		{
//			parent.setRight(son);
//		}
//		
//	}
//}
//
////CASO 3: NÓ TEM DOIS FILHOS
//
////substitui pelo sucessor
//
//if( foundNode.hasLeftChild() && foundNode.hasRightChild())
//{	} 