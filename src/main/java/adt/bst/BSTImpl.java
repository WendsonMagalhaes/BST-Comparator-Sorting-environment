package adt.bst;

import java.util.ArrayList;
import java.util.List;

public class BSTImpl<T extends Comparable<T>> implements BST<T> {

	protected BSTNode<T> root;

	public BSTImpl() {
		root = new BSTNode<T>();
	}

	public BSTNode<T> getRoot() {
		return this.root;
	}

	@Override
	public boolean isEmpty() {
		return root.isEmpty();
	}

	@Override
	public int height() {
		return this.height(this.root);
	}

	private int height(BSTNode<T> node) {
		int height;

		if (node.isEmpty()) {
			height = -1;
		} else {
			int heightLeft = this.height((BSTNode<T>) node.getLeft());
			int heightRight = this.height((BSTNode<T>) node.getRight());

			if (heightLeft < heightRight) {
				height = heightRight + 1;
			} else {
				height = heightLeft + 1;
			}
		}

		return height;
	}

	@Override
	public BSTNode<T> search(T element) {
		return this.search(this.root, element);
	}

	@SuppressWarnings("unchecked")
	private BSTNode<T> search(BSTNode<T> node, T element) {
		
		BSTNode<T> found = new BSTNode.Builder<T>().build();

		if (node.isEmpty() || node.getData().equals(element)) {
			found = node;
		} else if (element.compareTo(node.getData()) < 0) {
			found = this.search((BSTNode<T>) node.getLeft(), element);
		} else if (element.compareTo(node.getData()) > 0) {
			found = this.search((BSTNode<T>) node.getRight(), element);
		}

		return found;
	}

	@Override
	public void insert(T element) {
		if (element != null) {
			this.insert(root, element);
		}
	}

	@SuppressWarnings("unchecked")
	public void insert(BSTNode<T> node, T element) {

		if (node.isEmpty()) {
			node.setData(element);
			BSTNode<T> left = new BSTNode.Builder<T>().parent(node).build();
			node.setLeft(left);
			BSTNode<T> right = new BSTNode.Builder<T>().parent(node).build();
			node.setRight(right);

		} else if (element.compareTo(node.getData()) < 0) {
			this.insert((BSTNode<T>) node.getLeft(), element);
		} else if (element.compareTo(node.getData()) > 0) {
			this.insert((BSTNode<T>) node.getRight(), element);
		}
	}

	@Override
	public BSTNode<T> maximum() {
		return this.maximum(this.root);
	}

	private BSTNode<T> maximum(BSTNode<T> node) {
		if (!node.isEmpty()) {
			while (!node.getRight().isEmpty()) {
				node = (BSTNode<T>) node.getRight();
			}
		} else {
			node = null;
		}

		return node;
	}

	@Override
	public BSTNode<T> minimum() {
		return this.minimum(this.root);
	}

	private BSTNode<T> minimum(BSTNode<T> node) {
		if (!node.isEmpty()) {
			while (!node.getLeft().isEmpty()) {
				node = (BSTNode<T>) node.getLeft();
			}
		} else {
			node = null;
		}

		return node;
	}

	@Override
	public BSTNode<T> sucessor(T element) {
		BSTNode<T> node = this.search(element);
		return this.sucessor(node);
	}

	private BSTNode<T> sucessor(BSTNode<T> node) {
		if (!node.isEmpty()) {
			if (!node.getRight().isEmpty()) {
				node = this.minimum((BSTNode<T>) node.getRight());
			} else {
				BSTNode<T> parent = (BSTNode<T>) node.getParent();

				while (parent != null && node.equals(parent.getRight())) {
					node = parent;
					parent = (BSTNode<T>) parent.getParent();
				}

				node = parent;
			}
		} else {
			node = null;
		}

		return node;
	}

	@Override
	public BSTNode<T> predecessor(T element) {
		BSTNode<T> node = this.search(element);
		return this.predecessor(node);
	}

	private BSTNode<T> predecessor(BSTNode<T> node) {
		if (!node.isEmpty()) {
			if (!node.getLeft().isEmpty()) {
				node = this.maximum((BSTNode<T>) node.getLeft());
			} else {
				BSTNode<T> parent = (BSTNode<T>) node.getParent();

				while (parent != null && node.equals(parent.getLeft())) {
					node = parent;
					parent = (BSTNode<T>) parent.getParent();
				}

				node = parent;
			}
		} else {
			node = null;
		}

		return node;
	}

	@Override
	public void remove(T element) {
		BSTNode<T> node = this.search(element);
		this.remove(node);
	}

	@SuppressWarnings("unchecked")
	private void remove(BSTNode<T> node) {
		if (!node.isEmpty()) {
			if (node.isLeaf()) {
				BSTNode<T> leaf = new BSTNode.Builder<T>().parent(node.getParent()).build();

				if (node.getParent() == null) {
					this.root = leaf;
				} else if (node.getParent().getLeft().equals(node)) {
					node.getParent().setLeft(leaf);
				} else {
					node.getParent().setRight(leaf);
				}
			} else if (node.getLeft().isLeaf() || node.getRight().isLeaf()) {
				if (node.getParent() != null) {
					if (node.equals(node.getParent().getLeft())) {
						if (!node.getLeft().isEmpty()) {
							node.getLeft().setParent(node.getParent());
							node.getParent().setLeft(node.getLeft());
						} else {
							node.getRight().setParent(node.getParent());
							node.getParent().setLeft(node.getRight());
						}
					} else {
						if (!node.getLeft().isEmpty()) {
							node.getLeft().setParent(node.getParent());
							node.getParent().setRight(node.getLeft());
						} else {
							node.getRight().setParent(node.getParent());
							node.getParent().setRight(node.getRight());
						}
					}
				} else {
					if (!node.getLeft().isEmpty() && node.getLeft().isLeaf()) {
						node.setData(node.getLeft().getData());
						node.setLeft(new BSTNode.Builder<T>().parent(node).build());
					} else if (!node.getRight().isEmpty() && node.getRight().isLeaf()) {
						node.setData(node.getRight().getData());
						node.setRight(new BSTNode.Builder<T>().parent(node).build());
					}
				}
			} else {
				BSTNode<T> next;

				if (node.getRight().isEmpty()) {
					next = this.predecessor(node);
				} else {
					next = this.sucessor(node);
				}

				node.setData(next.getData());
				this.remove(next);
			}
		}
	}

	@Override
	public T[] preOrder() {
		return this.preOrder(this.root, new ArrayList<>());
	}

	private T[] preOrder(BSTNode<T> node, List<T> nodes) {
		if (!node.isEmpty()) {
			nodes.add(node.getData());
			preOrder((BSTNode<T>) node.getLeft(), nodes);
			preOrder((BSTNode<T>) node.getRight(), nodes);
		}

		return (T[]) nodes.toArray(new Comparable[this.size()]);
	}


	@Override
	public T[] order() {
		return this.order(this.root, new ArrayList<>());
	}

	private T[] order(BSTNode<T> node, List<Comparable<T>> nodes) {
		if (!node.isEmpty()) {
			order((BSTNode<T>) node.getLeft(), nodes);
			nodes.add(node.getData());
			order((BSTNode<T>) node.getRight(), nodes);
		}

		return (T[]) nodes.toArray(new Comparable[this.size()]);
	}

	@Override
	public T[] postOrder() {
		return this.postOrder(this.root, new ArrayList<>());
	}

	private T[] postOrder(BSTNode<T> node, List<T> nodes) {
		if (!node.isEmpty()) {
			postOrder((BSTNode<T>) node.getLeft(), nodes);
			postOrder((BSTNode<T>) node.getRight(), nodes);
			nodes.add(node.getData());
		}

		return (T[]) nodes.toArray(new Comparable[this.size()]);
	}

	/**
	 * This method is already implemented using recursion. You must understand how
	 * it work and use similar idea with the other methods.
	 */
	@Override
	public int size() {
		return size(root);
	}

	private int size(BSTNode<T> node) {
		int result = 0;
		
		if (!node.isEmpty()) { 
			result = 1 + size((BSTNode<T>) node.getLeft()) + size((BSTNode<T>) node.getRight());
		}
		return result;
	}

}

