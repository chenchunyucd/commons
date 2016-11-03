package ccy.work.commons.utils;

import java.io.Serializable;
import java.util.*;

/**
 * 可序列化分页
 * @author chenchunyu
 *
 * @param <T>
 */
public class PaginatedArrayList<T> implements Serializable, Iterable<T> {

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public PaginatedArrayList() {
        items = new ArrayList<T>();
        repaginate();
    }

    public PaginatedArrayList(int index, int pageSize) {
        items = new ArrayList<T>();
        this.index = index;
        this.pageSize = pageSize;
        repaginate();
    }
    
    public boolean isFirstPage() {
        return index <= 1;
    }
    
    public boolean isMiddlePage() {
        return !isFirstPage() && !isLastPage();
    }
    
    public boolean isLastPage() {
        return index >= totalPage;
    }
    
    public boolean isNextPageAvailable() {
        return !isLastPage();
    }
    
    public boolean isPreviousPageAvailable() {
        return !isFirstPage();
    }
    
    public int getNextPage() {
        if (isLastPage())
            return totalItem;
        else
            return index + 1;
    }
    
    public int getPreviousPage() {
        if (isFirstPage())
            return 1;
        else
            return index - 1;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        repaginate();
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
        repaginate();
    }
    
    public int getTotalItem() {
        return totalItem;
    }
    
    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
        repaginate();
    }
    
    public int getTotalPage() {
        return totalPage;
    }
    
    public int getStartRow() {
        return startRow;
    }
    
    public int getEndRow() {
        return endRow;
    }
    
    private void repaginate() {
        if (pageSize < 1)
            pageSize = PAGESIZE_DEFAULT;
        if (index < 1)
            index = 1;
        if (totalItem > 0) {
            totalPage = totalItem / pageSize + (totalItem % pageSize <= 0 ? 0 : 1);
            if (index > totalPage)
                index = totalPage;
            endRow =  pageSize;
            startRow = (index-1)*pageSize;
        }
    }
    
    public int size() {
        return items.size();
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public boolean contains(Object o) {
        return items.contains(o);
    }
    
    public Iterator<T> iterator() {
        return items.iterator();
    }
    
    public Object[] toArray() {
        return items.toArray();
    }
    
    public Object[] toArray(Object a[]) {
        return items.toArray(a);
    }
    
    public boolean add(T t) {
        return items.add(t);
    }
    
    public boolean remove(Object o) {
        return items.remove(o);
    }
    
    public boolean containsAll(Collection<T> c) {
        return items.containsAll(c);
    }
    
    public boolean addAll(Collection<T> c) {
        return items.addAll(c);
    }
    
    public boolean addAll(int index, Collection<T> c) {
        return items.addAll(index, c);
    }
    
    public boolean removeAll(Collection<T> c) {
        return items.removeAll(c);
    }
    
    public boolean retainAll(Collection<T> c) {
        return items.retainAll(c);
    }
    
    public void clear() {
        items.clear();
    }
    
    public Object get(int index) {
        return items.get(index);
    }
    
    public Object set(int index, T element) {
        return items.set(index, element);
    }
    
    public void add(int index, T element) {
        items.add(index, element);
    }
    
    public Object remove(int index) {
        return items.remove(index);
    }
    
    public int indexOf(Object o) {
        return items.indexOf(o);
    }
    
    public int lastIndexOf(Object o) {
        return items.lastIndexOf(o);
    }
    
    public ListIterator<T> listIterator() {
        return items.listIterator();
    }
    
    public ListIterator<T> listIterator(int index) {
        return items.listIterator(index);
    }
    
    public List<T> subList(int fromIndex, int toIndex) {
        return items.subList(fromIndex, toIndex);
    }
    
    public static final int PAGESIZE_DEFAULT = 20;
    
    private static final long serialVersionUID = 0xa768df9cdf26d1fcL;
    
    private int pageSize;
    
    private int index;
    
    private int totalItem;
    
    private int totalPage;
    
    private int startRow;
    
    private int endRow;
    
    private List<T> items;
    
}
