package adt.bst.extended;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import adt.bst.BSTImpl;
import adt.bst.BSTNode;

/**
 * Implementacao de SortComparatorBST, uma BST que usa um comparator interno em
 * suas funcionalidades e possui um metodo de ordenar um array dado como
 * parametro, retornando o resultado do percurso desejado que produz o array
 * ordenado.
 * 
 * @author Adalberto
 *
 * @param <T>
 */
public class SortComparatorBSTImpl<T extends Comparable<T>> extends BSTImpl<T> implements SortComparatorBST<T> {

	private Comparator<T> comparator;

	public SortComparatorBSTImpl(Comparator<T> comparator) {
		super();
		this.comparator = comparator;
	}

	@Override
	public T[] sort(T[] array) {
		
		for (int i = 1; i < array.length; i++) {

			T aux = array[i];
			int j = i;

			while ((j > 0) && (array[j - 1].compareTo(aux) < 0)) {
				array[j] = array[j - 1];
				j --;
			}
			array[j] = aux;

		}
		return array;

	}

	@Override
	public T[] reverseOrder() {
		return this.reverseOrder(this.root, new ArrayList<>());
	}

	private T[] reverseOrder(BSTNode<T> node, List<Comparable<T>> nodes) {

		if (!node.isEmpty()) {
			reverseOrder((BSTNode<T>) node.getRight(), nodes);
			nodes.add(node.getData());
			reverseOrder((BSTNode<T>) node.getLeft(), nodes);
		}

		return (T[]) nodes.toArray(new Comparable[this.size()]);
	}

	public Comparator<T> getComparator() {
		return comparator;
	}

	public void setComparator(Comparator<T> comparator) {
		this.comparator = comparator;
	}

}
