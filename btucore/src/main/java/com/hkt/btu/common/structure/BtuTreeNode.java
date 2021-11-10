package com.hkt.btu.common.structure;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BtuTreeNode<T> implements Cloneable {
	
	private static final Logger LOG = LogManager.getLogger(BtuTreeNode.class);

	private T data;
	@JsonBackReference
	private BtuTreeNode<T> parent;
	@JsonManagedReference
	private List<BtuTreeNode<T>> children;
	protected int level = 1;
	
	public BtuTreeNode(T data) {
		if (data == null)
			throw new NullPointerException("Do not accept null data");
		this.data = data;
		this.children = new ArrayList<BtuTreeNode<T>>();
	}
	
	public BtuTreeNode<T> addChild(T child) {
		//checking duplicate
		try {
			if (checkDuplicated(child))
				throw new IllegalArgumentException("Data already exists in Tree");
		} catch (Exception e) {
			LOG.warn(e.getMessage(), e);
		}
		
		BtuTreeNode<T> childNode = new BtuTreeNode<T>(child);
		childNode.parent = this;
		childNode.level = this.level + 1;
		this.children.add(childNode);
		return childNode;
	}
	
	public BtuTreeNode<T> addChild(BtuTreeNode<T> childNode) {
		//checking duplicate
		try {
			if (checkDuplicated(childNode.getNodeData()))
				throw new IllegalArgumentException("Data already exists in Tree");
		} catch (Exception e) {
			LOG.warn(e.getMessage(), e);
		}

		childNode.setParent(this);
		this.getChildren().add(childNode);
		childNode.setLevel(this.level + 1);
		return childNode;
	}
	
	/*
	 * @return Return removed Tree Node, null if data not match any child
	 */
	public BtuTreeNode<T> removeChild(T data) {
		if (data == null)
			throw new NullPointerException("Do not accept null data");
		BtuTreeNode<T> target = null;
		for (BtuTreeNode<T> node : children) {
			if (node.checkDataEquals(data)) {
				target = node;
				break;
			}
		}
		if (target != null) {
			children.remove(target);
			target.parent = null;
		}
		return target;
	}
	
	/*
	 * @return Return removed Tree Node, null if parameter Node not in children Nodes
	 */
	public BtuTreeNode<T> removeChild(BtuTreeNode<T> childNode) {
		if (data == null)
			throw new NullPointerException("Do not accept null TreeNode");
		boolean removed = children.remove(childNode);
		if (removed) {
			childNode.parent = null;
			return childNode;
		}
		return null;
	}
	
	@Override
	public BtuTreeNode<T> clone() throws CloneNotSupportedException {
		BtuTreeNode<T> result = copyInstance();
		//result.setLevel(1);
		
		return result;
	}
	
	protected BtuTreeNode<T> copyInstance() {
		// no security way to copy data as a unknown type, just use it instead
		BtuTreeNode<T> copy = new BtuTreeNode<T>(data);
		for (BtuTreeNode<T> node : children) {
			BtuTreeNode<T> copyNode = node.copyInstance();
			copyNode.parent = copy;
			copy.children.add(copyNode);
		}
		return copy;
	}

	public BtuTreeNode<T> disconnectFromParent() {
		if (!isRoot()) {
			parent.removeChild(this);
			// set to null by self for safe
			parent = null;
			this.setLevel(1);
		}
		return this;
	}
	
	/*
	 * Get the depth from this Node.
	 * 
	 * @return Return 1 if it is leaf
	 */
	public int depth() {
		if (isLeaf())
			return 1;
		else {
			int childDepth = 0;
			for (BtuTreeNode<T> node : children) {
				childDepth = NumberUtils.max(childDepth, node.depth());
			}
			return childDepth + 1;
		}
	}
	
	public boolean isLeaf() {
		return children.isEmpty();
	}
	
	public boolean isRoot() {
		return parent == null;
	}
	
	public BtuTreeNode<T> root() {
		BtuTreeNode<T> temp = this;
		while (temp.parent != null) {
			temp = temp.parent;
		}
		return temp;
	}
	
	/*
	 * Search Tree from this Node, include this Node data
	 * 
	 * @param targetFieldValue use default compare column of NodeCompareBean to find the Node
	 * 
	 * @return Return the Node containing target data, null if not found
	 */
	public BtuTreeNode<T> searchTree(Object targetFieldValue) throws Exception {
		return searchTree(targetFieldValue, true);
	}
	
	/*
	 * Search Tree from this Node
	 * 
	 * @param targetFieldValue Use default compare column of NodeCompareBean to find the Node
	 * @param includeThisNode Indicate to include this Node or not
	 * 
	 * @return Return the Node containing target data, null if not found
	 */
	public BtuTreeNode<T> searchTree(Object targetFieldValue, boolean includeThisNode) throws Exception {
		if (!(data instanceof BtuNodeCompareBean))
			throw new IllegalAccessException("The TreeNode does not support search by value only");
		if (targetFieldValue == null)
			throw new IllegalArgumentException("Null searching parameter(s)");
		return searchDown(null, targetFieldValue, includeThisNode);
	}
	
	/*
	 * Search Tree from this Node, include this Node
	 * 
	 * @param targetFieldName Use target field to compare Data
	 * @param targetFieldValue Target Field Value
	 * 
	 * @return Return the Node containing target data, null if not found
	 */
	public BtuTreeNode<T> searchTree(String targetFieldName, Object targetFieldValue) throws Exception {
		return searchTree(targetFieldName, targetFieldValue, true);
	}
	
	/*
	 * Search Tree from this Node
	 * 
	 * @param targetFieldName Use target field to compare Data
	 * @param targetFieldValue Target Field Value
	 * @param includeThisNode Indicate to include this Node or not
	 * 
	 * @return Return the Node containing target data, null if not found
	 */
	public BtuTreeNode<T> searchTree(String targetFieldName, Object targetFieldValue, boolean includeThisNode) throws Exception {
		if (targetFieldName == null || targetFieldValue == null || /* java 11 targetFieldName.isBlank()*/ targetFieldName.trim().isEmpty())
			throw new IllegalArgumentException("Null searching parameter(s)");
		return searchDown(targetFieldName, targetFieldValue, includeThisNode);
	}
	
	/*
	 * Search Tree parents from this Node, include this Node
	 * 
	 * @param targetFieldValue use default compare column of NodeCompareBean to find the Node
	 * 
	 * @return Return the Node containing target data, null if not found
	 */
	public BtuTreeNode<T> searchParent(Object targetFieldValue) throws Exception {
		if (!(data instanceof BtuNodeCompareBean))
			throw new IllegalAccessException("The TreeNode does not support search by value only");
		if (targetFieldValue == null)
			throw new IllegalArgumentException("Null searching parameter(s)");
		return searchUp(null, targetFieldValue, true);
	}
	
	/*
	 * Search Tree parents from this Node, include this Node
	 * 
	 * @param targetFieldName Use target field to compare Data
	 * @param targetFieldValue Target Field Value
	 * 
	 * @return Return the Node containing target data, null if not found
	 */
	public BtuTreeNode<T> searchParent(String targetFieldName, Object targetFieldValue) throws Exception {
		if (targetFieldName == null || targetFieldValue == null || /* java 11 targetFieldName.isBlank()*/ targetFieldName.trim().isEmpty())
			throw new IllegalArgumentException("Null searching parameter(s)");
		return searchUp(targetFieldName, targetFieldValue, true);
	}
	
	/*
	 * Search Tree from this Node
	 * 
	 * @param targetFieldName Use target field to compare Data, null for default field of NodeCompareBean
	 * @param targetFieldValue Target Field Value
	 * @param searchThis Indicate to include this Node or not
	 * 
	 * @return Return the Node containing target data, null if not found
	 */
	protected BtuTreeNode<T> searchDown(String targetFieldName, Object targetFieldValue, boolean searchThis) throws Exception {
		
		if (searchThis && searchData(targetFieldName, targetFieldValue))
			return this;
		else {
			BtuTreeNode<T> result = null;
			for (BtuTreeNode<T> node : children) {
				result = node.searchDown(targetFieldName,targetFieldValue, true);
				if (result != null)
					break;
			}
			return result;		
		}
	}
	
	protected BtuTreeNode<T> searchDown(T data, boolean searchThis) {
		if (searchThis && checkDataEquals(data))
			return this;
		else {
			BtuTreeNode<T> result = null;
			for (BtuTreeNode<T> node : children) {
				result = node.searchDown(data, true);
				if (result != null)
					break;
			}
			return result;
		}
	}
	
	/*
	 * Search Tree parents from this Node
	 * 
	 * @param targetFieldName Use target field to compare Data, null for default field of NodeCompareBean
	 * @param targetFieldValue Target Field Value
	 * @param searchThis Indicate to include this Node or not
	 * 
	 * @return Return the Node containing target data, null if not found
	 */
	protected BtuTreeNode<T> searchUp(String targetFieldName, Object targetFieldValue, boolean searchThis) throws Exception {
		
		if (searchThis && searchData(targetFieldName, targetFieldValue))
			return this;
		else {
//			BtuTreeNode<T> result = null;
//			if (parent != null)
//				result = parent.searchUp(targetFieldName, targetFieldValue, true);
//			return result;		
			if (parent == null)
				return null;
			return parent.searchUp(targetFieldName, targetFieldValue, true);
		}
	}
	
	protected BtuTreeNode<T> searchUp(T data, boolean searchThis) {
		if (searchThis && checkDataEquals(data))
			return this;
		else {
//			BtuTreeNode<T> result = null;
//			if (parent != null)
//				result = parent.searchUp(data, true);
//			return result;
			if (parent == null)
				return null;
			return parent.searchUp(data, true);
		}
	}
	
//	protected BtuTreeNode<T> search(String targetFieldName, Object targetFieldValue, boolean searchThis, boolean searchDown) throws Exception {
//		
//		if (searchThis && searchData(targetFieldName, targetFieldValue))
//			return this;
//		else {
//			BtuTreeNode<T> result = null;
//			if (searchDown) {
//				for (BtuTreeNode<T> node : children) {
//					result = node.searchDown(targetFieldName,targetFieldValue, true);
//					if (result != null)
//						break;
//				}
//			} else {
//				if (parent != null)
//					result = parent.searchUp(targetFieldName, targetFieldValue, true);
//			}
//			return result;		
//		}
//	}
	
	/*
	 * Search This Node data
	 * 
	 * @param targetFieldName Use target field to compare Data, null for default field of NodeCompareBean
	 * @param targetFieldValue Target Field Value
	 * 
	 * @return Return true if data matched, both data value and target value are null will also return true
	 */
	protected boolean searchData(String targetFieldName, Object targetFieldValue) throws Exception {
		if (StringUtils.isEmpty(targetFieldName) && data instanceof BtuNodeCompareBean) {
			return equals(targetFieldValue, ((BtuNodeCompareBean) data).DefaultCompareValue());
		}
//		// direct compare of data, for internal search
//		if (CommonFunction.isEmptyValue(targetFieldName) && targetFieldValue != null && data != null && data.equals(targetFieldValue)) {
//			
//		}
		
		Class<?> TClass = data.getClass();
		String methodName = "get" + targetFieldName.toUpperCase().charAt(0)
				+ (targetFieldName.length() > 1 ? targetFieldName.substring(1) : "") ;
		Method targetMethod = TClass.getMethod(methodName);
		Object dataValue = targetMethod.invoke(data);
		
		return equals(dataValue, targetFieldValue);
	}
	
	protected boolean checkDataEquals(T data) {
		if (data == null || this.data == null)
			throw new IllegalArgumentException("Do not support null comparison");
		// may need to override equals function for duplicate
		return this.data.equals(data);
	}
	
	protected boolean checkDuplicated(T data) {
		return root().searchDown(data, true) != null;
	}

	// access by tree node only
	protected void setLevel(int level) {
		this.level = level;
		for (BtuTreeNode<T> node : children) {
			node.setLevel(level + 1);
		}
	}
	
	@JsonIgnore
	public List<T> getTreeData() {
		ArrayList<T> result = new ArrayList<>();
		result.add(data);
		for (BtuTreeNode<T> node : children) {
			result.addAll(node.getTreeData());
		}
		return result;
	}

	public T getNodeData() {
		return data;
	}

	public void setNodeData(T data) {
		this.data = data;
	}

	@JsonIgnore
	public BtuTreeNode<T> getParent() {
		return parent;
	}

	@JsonIgnore
	public void setParent(BtuTreeNode<T> parent) {
		parent.children.add(this);
		this.parent = parent;
		this.setLevel(parent.level + 1);
	}

	public List<BtuTreeNode<T>> getChildren() {
		return children;
	}

	public void setChildren(List<BtuTreeNode<T>> children) {
		for (BtuTreeNode<T> child : children) {
			child.parent = this;
		}
		this.children = children;
	}

	public int getLevel() {
		return this.level;
	}

	protected boolean equals(Object x, Object y) {
		// String, Integer, Date, boolean
		if (x == null && y == null)
			return true;
		else if (x == null || y == null)
			return false;
		else if (x instanceof String && y instanceof String)
			return ((String) x).contentEquals((String) y);
		else if (x instanceof Integer && y instanceof Integer)
			return ((Integer) x).intValue() == ((Integer) y).intValue();
		else if (x instanceof Date && y instanceof Date)
			return ((Date) x).getTime() == ((Date) y).getTime();
		else if (x instanceof Boolean && y instanceof Boolean)
			return /*(Boolean)*/ x == /*(Boolean)*/ y;
		else if (x instanceof Number && y instanceof Number)
			return ((Number) x).doubleValue() == ((Number) y).doubleValue(); // not always equals for different types of Number
		else
			return x.equals(y);
	}
}

