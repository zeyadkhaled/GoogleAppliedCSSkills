/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;


import android.graphics.Point;
import android.util.Log;

import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    private class Node {
        Point point;
        Node prev, next;

        Node(Point point,Node prev,Node next)
        {
            this.point=point;
            this.prev=prev;
            this.next=next;
        }

    }

    Node head = null;

    public void insertBeginning(Point p) {
        if ( head == null  ) {
            head = new Node(p,null,null);
            head.prev = head;
            head.next = head;

        } else {
            Node tmp = new Node( p, head.prev, head);
            head.prev.next = tmp;
            head.prev = tmp;
            }
        }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {
        float total = 0;
        if (head != null) {
            Node current = head;
            while ( current.next != head ) {
                total += distanceBetween(current.point, current.next.point);
                current = current.next;
            }
        }
        return total;
    }

    public void insertNearest(Point p) {
        if ( head == null  )
            insertBeginning(p);

         else if ( head.next == null)
            insertBeginning(p);

        else {
            Node current = head;
            Node nearest = current;
            float distance = distanceBetween(current.point, p);
            float min = Float.MAX_VALUE;

            while ( current.next != head ) {

                distance = distanceBetween(current.point, p);
                if ( distance < min) {
                    min = distance;
                    nearest = current;
                }
                current = current.next;
            }

            Node tmp = new Node( p, nearest, nearest.next);
            nearest.next = tmp;
        }
    }

    public void insertSmallest(Point p) {
        Node temp=head;
        //No Node at All Case
        if(temp==null)
        {
            insertBeginning(p);
        }
        //Only One Node Case
        else if(temp.next==head)
        {
            insertBeginning(p);
        }
        //More than 2 nodes case
        else {
            Node prevNode = temp;
            temp = temp.next;

            float prevDist = distanceBetween(prevNode.point, p);
            float nextDist = distanceBetween(p, temp.point);

            //We Need to Minimise the sum of prevDist + nextDist and Insert the new node at the minimum point

            while (temp != head) {
                float presentTotalDistance = distanceBetween(temp.point, p) + distanceBetween(p, temp.next.point);
                if (presentTotalDistance < (prevDist + nextDist)) {
                    prevNode = temp;
                }
                temp = temp.next;
            }

            Node node = new Node(p, prevNode, prevNode.next);
            prevNode.next = node;

        }
    }

    public void reset() {
        head = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
