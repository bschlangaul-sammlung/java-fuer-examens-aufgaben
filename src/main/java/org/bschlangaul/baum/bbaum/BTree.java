package org.bschlangaul.baum.bbaum;

/**
 * https://programmer.group/b-tree-java-implementation.html
 */
class BTreeNode {

  int[] keys; // keys of nodes
  int MinDeg; // Minimum degree of B-tree node
  BTreeNode[] children; // Child node
  int num; // Number of keys of node
  boolean isLeaf; // True when leaf node

  // Constructor
  public BTreeNode(int deg, boolean isLeaf) {

    this.MinDeg = deg;
    this.isLeaf = isLeaf;
    this.keys = new int[2 * this.MinDeg - 1]; // Node has 2*MinDeg-1 keys at most
    this.children = new BTreeNode[2 * this.MinDeg];
    this.num = 0;
  }

  // Find the first location index equal to or greater than key
  public int findKey(int key) {

    int idx = 0;
    // The conditions for exiting the loop are: 1.idx == num, i.e. scan all of them
    // once
    // 2. IDX < num, i.e. key found or greater than key
    while (idx < num && keys[idx] < key)
      ++idx;
    return idx;
  }

  public void remove(int key) {

    int idx = findKey(key);
    if (idx < num && keys[idx] == key) { // Find key
      if (isLeaf) // key in leaf node
        removeFromLeaf(idx);
      else // key is not in the leaf node
        removeFromNonLeaf(idx);
    } else {
      if (isLeaf) { // If the node is a leaf node, then the node is not in the B tree
        System.out.printf("The key %d is does not exist in the tree\n", key);
        return;
      }

      // Otherwise, the key to be deleted exists in the subtree with the node as the
      // root

      // This flag indicates whether the key exists in the subtree whose root is the
      // last child of the node
      // When idx is equal to num, the whole node is compared, and flag is true
      boolean flag = idx == num;

      if (children[idx].num < MinDeg) // When the child node of the node is not full, fill it first
        fill(idx);

      // If the last child node has been merged, it must have been merged with the
      // previous child node, so we recurse on the (idx-1) child node.
      // Otherwise, we recurse to the (idx) child node, which now has at least the
      // keys of the minimum degree
      if (flag && idx > num)
        children[idx - 1].remove(key);
      else
        children[idx].remove(key);
    }
  }

  public void removeFromLeaf(int idx) {

    // Shift from idx
    for (int i = idx + 1; i < num; ++i)
      keys[i - 1] = keys[i];
    num--;
  }

  public void removeFromNonLeaf(int idx) {

    int key = keys[idx];

    // If the subtree before key (children[idx]) has at least t keys
    // Then find the precursor 'pred' of key in the subtree with children[idx] as
    // the root
    // Replace key with 'pred', recursively delete pred in children[idx]
    if (children[idx].num >= MinDeg) {
      int pred = getPred(idx);
      keys[idx] = pred;
      children[idx].remove(pred);
    }
    // If children[idx] has fewer keys than MinDeg, check children[idx+1]
    // If children[idx+1] has at least MinDeg keys, in the subtree whose root is
    // children[idx+1]
    // Find the key's successor 'succ' and recursively delete succ in
    // children[idx+1]
    else if (children[idx + 1].num >= MinDeg) {
      int succ = getSucc(idx);
      keys[idx] = succ;
      children[idx + 1].remove(succ);
    } else {
      // If the number of keys of children[idx] and children[idx+1] is less than
      // MinDeg
      // Then key and children[idx+1] are combined into children[idx]
      // Now children[idx] contains the 2t-1 key
      // Release children[idx+1], recursively delete the key in children[idx]
      merge(idx);
      children[idx].remove(key);
    }
  }

  public int getPred(int idx) { // The predecessor node is the node that always finds the rightmost node from
                                // the left subtree

    // Move to the rightmost node until you reach the leaf node
    BTreeNode cur = children[idx];
    while (!cur.isLeaf)
      cur = cur.children[cur.num];
    return cur.keys[cur.num - 1];
  }

  public int getSucc(int idx) { // Subsequent nodes are found from the right subtree all the way to the left

    // Continue to move the leftmost node from children[idx+1] until it reaches the
    // leaf node
    BTreeNode cur = children[idx + 1];
    while (!cur.isLeaf)
      cur = cur.children[0];
    return cur.keys[0];
  }

  // Fill children[idx] with less than MinDeg keys
  public void fill(int idx) {

    // If the previous child node has multiple MinDeg-1 keys, borrow from them
    if (idx != 0 && children[idx - 1].num >= MinDeg)
      borrowFromPrev(idx);
    // The latter sub node has multiple MinDeg-1 keys, from which to borrow
    else if (idx != num && children[idx + 1].num >= MinDeg)
      borrowFromNext(idx);
    else {
      // Merge children[idx] and its brothers
      // If children[idx] is the last child node
      // Then merge it with the previous child node or merge it with its next sibling
      if (idx != num)
        merge(idx);
      else
        merge(idx - 1);
    }
  }

  // Borrow a key from children[idx-1] and insert it into children[idx]
  public void borrowFromPrev(int idx) {

    BTreeNode child = children[idx];
    BTreeNode sibling = children[idx - 1];

    // The last key from children[idx-1] overflows to the parent node
    // The key[idx-1] underflow from the parent node is inserted as the first key in
    // children[idx]
    // Therefore, sibling decreases by one and children increases by one
    for (int i = child.num - 1; i >= 0; --i) // children[idx] move forward
      child.keys[i + 1] = child.keys[i];

    if (!child.isLeaf) { // Move children[idx] forward when they are not leaf nodes
      for (int i = child.num; i >= 0; --i)
        child.children[i + 1] = child.children[i];
    }

    // Set the first key of the child node to the keys of the current node [idx-1]
    child.keys[0] = keys[idx - 1];
    if (!child.isLeaf) // Take the last child of sibling as the first child of children[idx]
      child.children[0] = sibling.children[sibling.num];

    // Move the last key of sibling up to the last key of the current node
    keys[idx - 1] = sibling.keys[sibling.num - 1];
    child.num += 1;
    sibling.num -= 1;
  }

  // Symmetric with borowfromprev
  public void borrowFromNext(int idx) {

    BTreeNode child = children[idx];
    BTreeNode sibling = children[idx + 1];

    child.keys[child.num] = keys[idx];

    if (!child.isLeaf)
      child.children[child.num + 1] = sibling.children[0];

    keys[idx] = sibling.keys[0];

    for (int i = 1; i < sibling.num; ++i)
      sibling.keys[i - 1] = sibling.keys[i];

    if (!sibling.isLeaf) {
      for (int i = 1; i <= sibling.num; ++i)
        sibling.children[i - 1] = sibling.children[i];
    }
    child.num += 1;
    sibling.num -= 1;
  }

  // Merge childre[idx+1] into childre[idx]
  public void merge(int idx) {

    BTreeNode child = children[idx];
    BTreeNode sibling = children[idx + 1];

    // Insert the last key of the current node into the MinDeg-1 position of the
    // child node
    child.keys[MinDeg - 1] = keys[idx];

    // keys: children[idx+1] copy to children[idx]
    for (int i = 0; i < sibling.num; ++i)
      child.keys[i + MinDeg] = sibling.keys[i];

    // children: children[idx+1] copy to children[idx]
    if (!child.isLeaf) {
      for (int i = 0; i <= sibling.num; ++i)
        child.children[i + MinDeg] = sibling.children[i];
    }

    // Move keys forward, not gap caused by moving keys[idx] to children[idx]
    for (int i = idx + 1; i < num; ++i)
      keys[i - 1] = keys[i];
    // Move the corresponding child node forward
    for (int i = idx + 2; i <= num; ++i)
      children[i - 1] = children[i];

    child.num += sibling.num + 1;
    num--;
  }

  public void insertNotFull(int key) {

    int i = num - 1; // Initialize i as the rightmost index

    if (isLeaf) { // When it is a leaf node
      // Find the location where the new key should be inserted
      while (i >= 0 && keys[i] > key) {
        keys[i + 1] = keys[i]; // keys backward shift
        i--;
      }
      keys[i + 1] = key;
      num = num + 1;
    } else {
      // Find the child node location that should be inserted
      while (i >= 0 && keys[i] > key)
        i--;
      if (children[i + 1].num == 2 * MinDeg - 1) { // When the child node is full
        splitChild(i + 1, children[i + 1]);
        // After splitting, the key in the middle of the child node moves up, and the
        // child node splits into two
        if (keys[i + 1] < key)
          i++;
      }
      children[i + 1].insertNotFull(key);
    }
  }

  public void splitChild(int i, BTreeNode y) {

    // First, create a node to hold the keys of MinDeg-1 of y
    BTreeNode z = new BTreeNode(y.MinDeg, y.isLeaf);
    z.num = MinDeg - 1;

    // Pass the properties of y to z
    for (int j = 0; j < MinDeg - 1; j++)
      z.keys[j] = y.keys[j + MinDeg];
    if (!y.isLeaf) {
      for (int j = 0; j < MinDeg; j++)
        z.children[j] = y.children[j + MinDeg];
    }
    y.num = MinDeg - 1;

    // Insert a new child into the child
    for (int j = num; j >= i + 1; j--)
      children[j + 1] = children[j];
    children[i + 1] = z;

    // Move a key in y to this node
    for (int j = num - 1; j >= i; j--)
      keys[j + 1] = keys[j];
    keys[i] = y.keys[MinDeg - 1];

    num = num + 1;
  }

  public void traverse() {
    int i;
    for (i = 0; i < num; i++) {
      if (!isLeaf)
        children[i].traverse();
      System.out.printf(" %d", keys[i]);
    }

    if (!isLeaf) {
      children[i].traverse();
    }
  }

  public BTreeNode search(int key) {
    int i = 0;
    while (i < num && key > keys[i])
      i++;

    if (keys[i] == key)
      return this;
    if (isLeaf)
      return null;
    return children[i].search(key);
  }
}

class BTree {
  BTreeNode root;
  int MinDeg;

  // Constructor
  public BTree(int deg) {
    this.root = null;
    this.MinDeg = deg;
  }

  public void traverse() {
    if (root != null) {
      root.traverse();
    }
  }

  // Function to find key
  public BTreeNode search(int key) {
    return root == null ? null : root.search(key);
  }

  public void insert(int key) {

    if (root == null) {

      root = new BTreeNode(MinDeg, true);
      root.keys[0] = key;
      root.num = 1;
    } else {
      // When the root node is full, the tree will grow high
      if (root.num == 2 * MinDeg - 1) {
        BTreeNode s = new BTreeNode(MinDeg, false);
        // The old root node becomes a child of the new root node
        s.children[0] = root;
        // Separate the old root node and give a key to the new node
        s.splitChild(0, root);
        // The new root node has 2 child nodes. Move the old one over there
        int i = 0;
        if (s.keys[0] < key)
          i++;
        s.children[i].insertNotFull(key);

        root = s;
      } else
        root.insertNotFull(key);
    }
  }

  public void remove(int key) {
    if (root == null) {
      System.out.println("The tree is empty");
      return;
    }

    root.remove(key);

    if (root.num == 0) { // If the root node has 0 keys
      // If it has a child, its first child is taken as the new root,
      // Otherwise, set the root node to null
      if (root.isLeaf)
        root = null;
      else
        root = root.children[0];
    }
  }

  public static void main(String[] args) {

    BTree t = new BTree(2); // A B-Tree with minium degree 2
    t.insert(1);
    t.insert(3);
    t.insert(7);
    t.insert(10);
    t.insert(11);
    t.insert(13);
    t.insert(14);
    t.insert(15);
    t.insert(18);
    t.insert(16);
    t.insert(19);
    t.insert(24);
    t.insert(25);
    t.insert(26);
    t.insert(21);
    t.insert(4);
    t.insert(5);
    t.insert(20);
    t.insert(22);
    t.insert(2);
    t.insert(17);
    t.insert(12);
    t.insert(6);

    System.out.println("Traversal of tree constructed is");
    t.traverse();
    System.out.println();

    t.remove(6);
    System.out.println("Traversal of tree after removing 6");
    t.traverse();
    System.out.println();

    t.remove(13);
    System.out.println("Traversal of tree after removing 13");
    t.traverse();
    System.out.println();

    t.remove(7);
    System.out.println("Traversal of tree after removing 7");
    t.traverse();
    System.out.println();

    t.remove(4);
    System.out.println("Traversal of tree after removing 4");
    t.traverse();
    System.out.println();

    t.remove(2);
    System.out.println("Traversal of tree after removing 2");
    t.traverse();
    System.out.println();

    t.remove(16);
    System.out.println("Traversal of tree after removing 16");
    t.traverse();
    System.out.println();
  }
}
