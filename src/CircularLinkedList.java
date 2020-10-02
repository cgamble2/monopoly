/*
 * CS2050 - Computer Science II - Spring 2020
 * Instructor: Thyago Mota
 * Description: CS2 Coding Competition - CircularLinkedList
 */

// In circular linked list, the tail node points to the head. In this particular implementation, nodes carry autogenerated id's.
public class CircularLinkedList<T> {

    private Node<T> head, tail;
    private static int nextId = 0;

    public CircularLinkedList() {
        head = tail = null;
    }

    public CircularLinkedList(int nextId) {
        head = tail = null;
        CircularLinkedList.nextId = nextId;
    }

    // TODOd: add a new node with nextId, incrementing nextId after; the new node becomes the
    // tail of the list
    public void append() {
        Node<T> newNode = new Node(nextId++);
        if(size() == 0) {
            head = tail = newNode;
            newNode.setNext(newNode);
        } else {
            Node<T> current = head;
            while(current != tail) {
                current = current.getNext();
            }
            current.setNext(newNode);
            newNode.setNext(head);
            tail = newNode;
        }
    }

    // TODOd: remove the head of the list
    public void remove() {
        if(size() == 1){
            tail.setNext(null);
            head = tail = null;
            return;
        }
        Node<T> temp = head;
        head = head.getNext();
        temp.setNext(null);
        tail.setNext(head);
    }

    public boolean isEmpty() {
        return head == null;
    }

    // TODOd: return the number of nodes
    public int size() {
        if(head == null) {
            return 0;
        }
        int size = 1;
        Node<T> current = head.getNext();
        while (current != head){
            current = current.getNext();
            size++;
        }
        return size;
    }

    @Override
    public String toString() {
        String out = "";
        Node<T> current = head;
        while (current != null) {
            out += current.toString() + " ";
            current = current.getNext();
            if (current == head)
                break;
        }
        return out;
    }

    // TODOd: if the list is NOT empty, move tail to head and head to the next node
    public void next() {
        if (!isEmpty()) {
            tail = head;
            head = head.getNext();
        }
    }

    // TODOd: call next until it founds a node with the given id; otherwise, the list should
    // recirculate back to the original configuration
//    public void nextToId(int id) {
//        if (!isEmpty()) {
//            Node<T> current = head;
//            while (true) {
//                if (head.getId() == id)
//                    break;
//                next();
//                if (current == head)
//                    break;
//            }
//        }
//    }

    public Node<T> getHead() {
        return head;
    }

    public Node<T> getTail() {
        return tail;
    }
}