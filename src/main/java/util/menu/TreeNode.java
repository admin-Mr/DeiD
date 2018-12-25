package util.menu;

import org.zkoss.zul.DefaultTreeNode;

public class TreeNode<E> extends DefaultTreeNode<E> {
	private static final long serialVersionUID = -7012663776755277499L;
	
	private boolean open = false;
	E data;
	
	public TreeNode(E data, DefaultTreeNode<E>[] children) {
		super(data, children);
		this.data = data;
	}

	public TreeNode(E data, DefaultTreeNode<E>[] children, boolean open) {
		super(data, children);
		this.data = data;
		setOpen(open);
	}

	public TreeNode(E data) {
		super(data);
		this.data = data;
	}

	public E getData() {
		return data;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

}
