package org.nextime.ion.frontoffice.taglib;

public class IterateStatus {

    protected int _size;
    protected int _index;

    public int getSize() {
        return _size;
    }

    public int getCount() {
        return _index;
    }

    public boolean getFirst() {
        return (_index == 0);
    }

    public boolean getLast() {
        return (_index == _size);
    }

}
