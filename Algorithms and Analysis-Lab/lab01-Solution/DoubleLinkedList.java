
/**
 * Double linked list that implements interface MyList.
 * 
 * @author jkcchan
 */
public class DoubleLinkedList implements MyList
{
	/** Reference to head of list. */
    protected Node mHead;
    /** Reference to tail of list. */
    protected Node mTail;

    /** Length of list. */
    protected int mLength;


    public DoubleLinkedList() {
        mHead = null; 
        mTail = null;
        mLength = 0;
    } // end of DoubleLinkedList()


    /**
     * Add a new value to the start of the list.
     * 
     * @param newValue Value to add to list.
     */
    public void add(int newValue) {
        Node newNode = new Node(newValue);
             
        // If head is empty, then list is empty and head and tail references need to be initialised.
        if (mHead == null) {
            mHead = newNode;
            mTail = newNode;
        }
        // otherwise, add node to the head of list.
        else {
            newNode.setNext(mHead);
            mHead.setPrev(newNode);
            mHead = newNode;
        }
        
        mLength++;
    } // end of add()


    /**
     * Add value (and corresponding node) at position 'index'.  Indices start at 0.
     * 
     * @param index Position in list to add new value to.
     * @param newValue Value to add to list.
     * 
     * @throws IndexOutOfBoundsException In index are out of bounds.
     */
    public void add(int index, int newValue) throws IndexOutOfBoundsException {
        if (index >= mLength || index < 0) {
            throw new IndexOutOfBoundsException("Supplied index is invalid.");
        }

        Node newNode = new Node(newValue); 

        // index == 0
        if (mHead == null) {			
            mHead = newNode;
            mTail = newNode;
        }
        // list is not empty
        else {
            // depending on where index is, we either go forward or backward in
            // list 
            if (index < Math.ceil(mLength / 2)) {
                Node currNode = mHead;
                for (int i = 0; i < index-1; ++i) {
                    currNode = currNode.getNext();
                }

                // nextNode is the one being shift up
                Node nextNode = currNode.getNext();
                nextNode.setPrev(newNode);
                newNode.setNext(nextNode);
                newNode.setPrev(currNode);
                currNode.setNext(newNode);            
            }
            else {
                Node currNode = mTail;
                for (int i = mLength-1; i > index; --i) {
                    currNode = currNode.getPrev();
                }

                Node prevNode = currNode.getPrev();
                prevNode.setNext(newNode);
                newNode.setPrev(prevNode);
                newNode.setNext(currNode);
                currNode.setPrev(newNode);                            
            }
        }

        mLength += 1;
    } // end of add()


    /**
     * Returns the value stored in node at position 'index' of list.
     *  
     * @param index Position in list to get new value for.
     * @return Value of element at specified position in list.
     * 
     * @throws IndexOutOfBoundsException In index are out of bounds.
     */
    public int get(int index) throws IndexOutOfBoundsException {
        if (index >= mLength || index < 0) {
            throw new IndexOutOfBoundsException("Supplied index is invalid.");
        }

        Node currNode = null;
        if (index < Math.ceil(mLength / 2)) {
            currNode = mHead;
            for (int i = 0; i < index; ++i) {
                currNode = currNode.getNext();
            }
        }
        else {
            currNode = mTail;
            for (int i = mLength-1; i > index; --i) {
                currNode = currNode.getPrev();
            }                          
        }        

        return currNode.getValue();
    } // end of get()


    /**
     * Returns the value stored in node at position 'index' of list.
     * 
     * @param value Value to search for.
     * @return True if value is in list, otherwise false.
     */
    public boolean search(int value) {
        Node currNode = mHead;
        for (int i = 0; i < mLength; ++i) {
        	if (currNode.getValue() == value) {
        		return true;
        	}
            currNode = currNode.getNext();
        }

        return false;
    } // end of search()
    
    
    
    /**
     * Delete given value from list (delete first instance found).
     *   
     * @param value Value to remove.
     * @return True if deletion was successful, otherwise false.
     */
    public boolean remove(int value) {

        Node currNode = mHead;

        
        // check if value is head node
        if (currNode.getValue() == value) {
            // check if length of 1
            if (mLength == 1) {
                mHead = mTail= null;
            }
            else {
                mHead = currNode.getNext();
                mHead.setPrev(null);
                currNode = null;
            }
            
            mLength--;
            return true;
        }
        // search for value in rest of list
        else {
            currNode = currNode.getNext();

            while (currNode != null) {
                if (currNode.getValue() == value) {
                    Node prevNode = currNode.getPrev();
                    prevNode.setNext(currNode.getNext());
                    // check if tail
                    if (currNode.getNext() != null) {
                    	currNode.getNext().setPrev(prevNode);
                    }
                    else {
                    	mTail = prevNode;
                    }
                    currNode = null;
                    mLength--;
                    return true;
                }
 
                currNode = currNode.getNext();
            }	
        }

        // couldn't find a node with value
        return false;
    } // end of delete()


    /**
     * Delete value (and corresponding node) at position 'index'.  Indices start at 0.
     * 
     * @param index Position in list to get new value for.
     * @param dummy Dummy variable, serves no use apart from distinguishing overloaded methods.
     * @return Value of node that was deleted.
     */
    public int remove(int index, boolean dummy) throws IndexOutOfBoundsException {
        if (index >= mLength || index < 0) {
            throw new IndexOutOfBoundsException("Supplied index is invalid.");
        }

        Node currNode = mHead;
        
        int value;
        
        // deleting head
        if (index == 0) {
            value = currNode.getValue();
            mHead = currNode.getNext();
            mHead.setPrev(null);
        }
        // deleting tail
        else if (index == mLength-1) {
            value = mTail.getValue();
            mTail = mTail.getPrev();
            mTail.setNext(null);
        }
        // go to appropriate node 
        else {
	        if (index < Math.ceil(mLength / 2)) {
	            currNode = mHead;
	            for (int i = 0; i < index; ++i) {
	                currNode = currNode.getNext();
	            }
	            value = currNode.getValue();
	            currNode.getPrev().setNext(currNode.getNext());
	            currNode.getNext().setPrev(currNode.getPrev());
	        }
	        else {
	            currNode = mTail;
	            for (int i = mLength-1; i > index; --i) {
	                currNode = currNode.getPrev();
	            }                          
	            value = currNode.getValue();
	            currNode.getPrev().setNext(currNode.getNext());
	            currNode.getNext().setPrev(currNode.getPrev());
	        }         
        }

        mLength--;
        
        return value;
    } // end of delete()
	
	
    /**
     * Returns the minimum value in the list.
     * 
     * @return Minimum value in the list.
     * 
     * @throws IndexOutOfBoundsException In index are out of bounds.
     */
    public int min() throws IllegalStateException {
        if (mLength == 0) {
            throw new IllegalStateException("Min is not defined for an empty list.");
        }
        
        // traverse list, looking for the minimum valued element
        int minValue = mHead.getValue();
        Node currNode = mHead.getNext();
        
        while (currNode != null) {
            if (currNode.getValue() < minValue) {
                minValue = currNode.getValue();
            }
            currNode = currNode.getNext();
        }
        
        return minValue;
    } // end of min()
    
    

    /**
     * Returns the maximum value in the list.
     * 
     * @return Maximum value in the list.
     * 
     * @throws IndexOutOfBoundsException In index are out of bounds.
     */
    public int max() throws IndexOutOfBoundsException {
        if (mLength == 0) {
            throw new IllegalStateException("Min is not defined for an empty list.");
        }
        
        // traverse list, looking for the minimum valued element
        int maxValue = mHead.getValue();
        Node currNode = mHead.getNext();
        
        while (currNode != null) {
            if (currNode.getValue() > maxValue) {
                maxValue = currNode.getValue();
            }
            currNode = currNode.getNext();
        }
        
        return maxValue;
    } // end of max()
    

    /**
     * Print the list in head to tail.
     */
    public void print() {
        System.out.println(toString());
    } // end of print()
    
    
    /**
     * Print the list from tail to head.
     */
    public void reversePrint() {
        
        Node currNode = mTail;
        while (currNode != null) {
            System.out.print(currNode.getValue() + " ");
            currNode = currNode.getPrev();
        }       
        System.out.println("");
        

    } // end of reversePrint()
        
	
    /**
     * @return String representation of the list.
     */
    public String toString() {
        Node currNode = mHead;

        StringBuffer str = new StringBuffer();

        while (currNode != null) {
            str.append(currNode.getValue() + " ");
            currNode = currNode.getNext();
        }

        return str.toString();
    } // end of getString();
	
	
	
    /**
     * Node type, inner private class.
     */
    private class Node
    {
        /** Stored value of node. */
        private int mValue;
        /** Reference to next node. */
        private Node mNext;
        /** Reference to previous node. */
        private Node mPrev;

        public Node(int value) {
            mValue = value;
            mNext = null;
            mPrev = null;
        }

        public int getValue() {
            return mValue;
        }


        public Node getNext() {
            return mNext;
        }
        
        
        public Node getPrev() {
            return mPrev;
        }


        public void setValue(int value) {
            mValue = value;
        }


        public void setNext(Node next) {
            mNext = next;
        }
        
        public void setPrev(Node prev) {
            mPrev = prev;
        }
    } // end of inner class Node
	
	

	
} // end of class SimpleList<int>




