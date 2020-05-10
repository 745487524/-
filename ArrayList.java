/*
 * Copyright (c) 1997, 2017, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.util;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import sun.misc.SharedSecrets;

/**
 * Resizable-array implementation of the <tt>List</tt> interface.  Implements
 * all optional list operations, and permits all elements, including
 * <tt>null</tt>.  In addition to implementing the <tt>List</tt> interface,
 * this class provides methods to manipulate the size of the array that is
 * used internally to store the list.  (This class is roughly equivalent to
 * <tt>Vector</tt>, except that it is unsynchronized.)
 *
 * <p>The <tt>size</tt>, <tt>isEmpty</tt>, <tt>get</tt>, <tt>set</tt>,
 * <tt>iterator</tt>, and <tt>listIterator</tt> operations run in constant
 * time.  The <tt>add</tt> operation runs in <i>amortized constant time</i>,
 * that is, adding n elements requires O(n) time.  All of the other operations
 * run in linear time (roughly speaking).  The constant factor is low compared
 * to that for the <tt>LinkedList</tt> implementation.
 *
 * <p>Each <tt>ArrayList</tt> instance has a <i>capacity</i>.  The capacity is
 * the size of the array used to store the elements in the list.  It is always
 * at least as large as the list size.  As elements are added to an ArrayList,
 * its capacity grows automatically.  The details of the growth policy are not
 * specified beyond the fact that adding an element has constant amortized
 * time cost.
 *
 * <p>An application can increase the capacity of an <tt>ArrayList</tt> instance
 * before adding a large number of elements using the <tt>ensureCapacity</tt>
 * operation.  This may reduce the amount of incremental reallocation.
 *
 * <p><strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access an <tt>ArrayList</tt> instance concurrently,
 * and at least one of the threads modifies the list structurally, it
 * <i>must</i> be synchronized externally.  (A structural modification is
 * any operation that adds or deletes one or more elements, or explicitly
 * resizes the backing array; merely setting the value of an element is not
 * a structural modification.)  This is typically accomplished by
 * synchronizing on some object that naturally encapsulates the list.
 *
 * If no such object exists, the list should be "wrapped" using the
 * {@link Collections#synchronizedList Collections.synchronizedList}
 * method.  This is best done at creation time, to prevent accidental
 * unsynchronized access to the list:<pre>
 *   List list = Collections.synchronizedList(new ArrayList(...));</pre>
 *
 * <p><a name="fail-fast">
 * The iterators returned by this class's {@link #iterator() iterator} and
 * {@link #listIterator(int) listIterator} methods are <em>fail-fast</em>:</a>
 * if the list is structurally modified at any time after the iterator is
 * created, in any way except through the iterator's own
 * {@link ListIterator#remove() remove} or
 * {@link ListIterator#add(Object) add} methods, the iterator will throw a
 * {@link ConcurrentModificationException}.  Thus, in the face of
 * concurrent modification, the iterator fails quickly and cleanly, rather
 * than risking arbitrary, non-deterministic behavior at an undetermined
 * time in the future.
 *
 * <p>Note that the fail-fast behavior of an iterator cannot be guaranteed
 * as it is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification.  Fail-fast iterators
 * throw {@code ConcurrentModificationException} on a best-effort basis.
 * Therefore, it would be wrong to write a program that depended on this
 * exception for its correctness:  <i>the fail-fast behavior of iterators
 * should be used only to detect bugs.</i>
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see     Collection
 * @see     List
 * @see     LinkedList
 * @see     Vector
 * @since   1.2
 */

public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 8683452581122892189L;

    /**
     * 初始化
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 用于空实例的共享空数组
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     * 用于默认大小的空实例的共享空数组。我们将它与空数组区EMPTY_ELEMENTDATA区分开来，以确定第一个元素添加时需要膨胀的大小空间
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /**
     * 存储到ArrayList中的元素的缓存区数组。ArrayList的容量大小就是这个数组缓冲区的长度。
     * 当elementData使用DEFAULTCAPACITY_EMPTY_ELEMENTDATA时，
     * 任意一个空Arralist的大小在添加第一个元素时，扩容大小到DEFAULT_CAPACITY(10)
     */
    transient Object[] elementData; // 非私有以简化嵌套类的访问 non-private to simplify nested class access

    /**
     * ArrayList的大小(ArrayList包含的元素个数)
     *
     * 在内存中是连续存储的
     * @serial
     */
    private int size;

    /**
     * 使用指定的初始化大小构造一个空链表
     * 指定的初始化的大小initialCapacity：
     * initialCapacity > 0：
     * 		缓冲区数组初始化大小为：initialCapacity；
     * initialCapacity = 0：
     * 		缓冲区数组初始化为空数组EMPTY_ELEMENTDATA，大小为：0；
     * initialCapacity < 0：
     * 		抛出异常：IllegalArgumentException；
     *
     * @param  initialCapacity  list的初始化大小
     * @throws IllegalArgumentException 如果指定的初始化大小
     */
    public ArrayList(int initialCapacity) {
    	//初始化容量大于0
        if (initialCapacity > 0) {
        	//缓冲区数组初始化为容量为initialCapacity大小的数组
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
        	//初始化容量设置为0时，缓冲区数组为一个空数组
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
        	//初始化容量大小小于0时，抛出异常
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
    }

    /**
     * 使用10的初始化大小构造一个空的链表，在没有添加元素，缓冲区数组的大小为0
     *
     */
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    /**
     * 创建一个包含指定集合的链表，按集合的迭代顺序返回。
     * 传入集合c：
     * c的length大小 > 0时：
     * 		缓冲区数组elementData替换为前c.length长度内为c中的元素的数组
     * c的length大小 = 0时：
     * 		缓冲区数组引用指向共享空数组EMPTY_ELEMENTDATA
     *
     * @param c 需要替换其中的元素到链表中的集合
     * @throws NullPointerException 当传入的集合为null时抛出
     */
    public ArrayList(Collection<? extends E> c) {
    	//数组缓冲区指向传入集合转换为数组之后的位置，toArray()方法返回对象Object[]
        elementData = c.toArray();
        //如果缓冲数组的大小赋值为list的size大小不等于0时
        if ((size = elementData.length) != 0) {
        	//toArray()方法可能不会返回正确的Object[](Object数组对象),错误代码：6260652
            if (elementData.getClass() != Object[].class)
            	//缓冲区数组赋值为elementData数组从index：0开始到index：size-1结束截取copy的数组的引用
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // replace with empty array.
            // 当传入的数组c为空数组时，使用共享空数组替换缓冲数组
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }

    /**
     * modCount：从结构上改变此列表的次数(AbstarctList定义)
     * 剪切ArrayList实例的容器大小为list当前的size大小。一个应用可以使用这个操作去缩小一个ArrayList实例的存储空间
     * 当ArrayList的大小size < 缓冲区数组的长度时：
     * 		如果 size == 0：缓冲区数组引用共享空数组；
     *		否则：缓冲区数组指向从缓冲区数组elementData中copy的前size-1个元素(index：0 ~ index：size-1)形成的新数组的引用；
     */
    public void trimToSize() {
    	//修改列表次数+1
        modCount++;
        //列表大小是否小于缓冲数组的长度
        if (size < elementData.length) {
        	//对缓冲数组elementData赋值
            elementData = (size == 0)
            //列表长度 == 0，赋值共享空数组引用
              ? EMPTY_ELEMENTDATA
              //列表长度不等于0，赋值从elementData中copy的前index：size-1(包括size-1)的新数组的引用
              : Arrays.copyOf(elementData, size);
        }
    }

    /**
     * 扩容ArrayList的容量大小，如果必要，保证ArrayList可以最少持有指定的最小的容量参数minCapacity数量的元素个数；
     * @param   minCapacity   希望保证的最小的容量大小
     */
    public void ensureCapacity(int minCapacity) {
    	//minExpand： 缓冲区数组是不是指向DEFAULTCAPACITY_EMPTY_ELEMENTDATA空数组引用？0：DEFAULT_CAPACITY(10)
    	//DEFAULTCAPACITY_EMPTY_ELEMENTDATA：是否初始化用有没有添加元素
    	//==：使用ArrayList()创建链表；
    	//!=：使用ArrayList(int miniCapacity)创建链表或者使用ArrayList()创建完链表后进行了相关操作；
        int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
            // any size if not default element table
        	// 如果不是指向默认的元素列表DEFAULTCAPACITY_EMPTY_ELEMENTDATA的话，任意大小都会将minExpand的大小设为0
            ? 0
            // larger than default for default empty table. It's already
            // supposed to be at default size.
            : DEFAULT_CAPACITY;
        //如果传入的最小容量参数miniCapacity大于minExpand
        if (minCapacity > minExpand) {
        	//确定链表的最小合适容量
            ensureExplicitCapacity(minCapacity);
        }
    }


    /**
    * 计算数组elementData指定的最小容量值
    */
    private static int calculateCapacity(Object[] elementData, int minCapacity) {
    	//elementData是否引用向DEFAULTCAPACITY_EMPTY_ELEMENTDATA
    	//==：使用ArrayList()创建完链表；
    	//!=：使用ArrayList(int minCapacity)创建链表或者使用ArrayList()创建完链表后执行了一些操作；
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        	//返回DEFAULT_CAPACITY(10)和minCapacity中的较大的值，
        	//返回DEFAULT_CAPACITY(10)时，保证了ArrayList初始化后可用的最小容量大小为10；
        	//返回minCapacity时，保证容量的创建后的需要的最小容量；
        	//保证minCapacity的最小容量值为10，可以为自定义的容量大小；
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        //在创建时或者之后执行了操作后，返回传入的minCapacity容量的最小值；
        return minCapacity;
    }

    /**
    * 确保容量值的合理大小
    */
    private void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }

    /**
    * 确定容器最小容量值的大小，即确定扩容的合适大小
    */
    private void ensureExplicitCapacity(int minCapacity) {
    	//list修改结构次数+1
        modCount++;

        // 溢出代码
        // 如果传入的miniCapacity大于缓冲区数组elementData的大小,也就是说传入的最小容量值大于缓冲区elementData实际的大小
        if (minCapacity - elementData.length > 0)
        	//将miniCapacity扩容到合适的大小，
        	//当miniCapacity大于1.5倍的elementData数组的长度时，elementData copy为一个长度为miniCapacity的新数组
        	//当miniCapacity小于1.5倍的elementData数组的长度时，elementData copy为一个长度为1.5倍elementData长度的新数组
        	//当newCapacity(1.5oldCapacity)大于MAX_VALUE_SIZE(Integer.MAX_VALUE - 8)时，即1.5倍的扩容后的数值超出了可使用的最大容量值；
        	//进一步比较定义的miniCapacity是否满足容量值的需求，当miniCapacity > MAX_ARRAY_SIZE时，扩容为Integer.MAX_VALUE大小，
        	//										    	 当miniCapacity < MAX_ARRAY_SIZE时，扩容为MAX_ARRAY_SIZE大小；
            grow(minCapacity);
    }
    /**
    * 要分配的数组的最大大小
    * 一些VMs参数在数组中占用了一部分的头信息。分配过大的空间会导致堆内存溢出：Requested array size exceeds VM limit(请求分配的数组空间的大小超过了VM的限制)
    */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 扩容容量大小以保证可以保证指定的最小的容量参数的元素个数的目标
     *
     * @param minCapacity 希望可以保证的最小的容量
     */
    private void grow(int minCapacity) {
        // 溢出代码
        // 已有容量大小oldCapacity = 缓冲区数组长度
        int oldCapacity = elementData.length;
        // oldCapacity >> 1 => oldCapacity / 2 = 0.5oldCapacity
        // newCapacity = 1.5oldCapacity
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        // 当newCapacity < miniCapcatiy时，newCapacity赋值为miniCapacity
        if (newCapacity - minCapacity < 0)
        	//newCapacity > minCapacity：newCapacity = miniCapacity
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
        	//newCapacity > MAX_ARRAY_SIZE时：newCapacity膨胀到可使用的最大容量值
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        // miniCapacity通常接近于size的大小，所以这是合理的；
        // copy一个数组，copy的原数组为缓冲数组elementData，copy后所需的数组长度为newCapacity，并将缓冲数组指向copy后的新数组的引用；
        elementData = Arrays.copyOf(elementData, newCapacity);
    }


    /**
    * 将miniCapacity膨胀到可到的最大数值： miniCapacity > Integer.MAX_VALUE - 8 ? Integer.MAX_VALUE :MAX_ARRAY_SIZE(Integer.MAX_VALUE - 8);
    *
    */
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
        //miniCapacity < 0，容量溢出
            throw new OutOfMemoryError();
            //miniCapacity > Integer.MAX_VALUE - 8 => （0x7fffffff - 8)
            //miniCapacity > MAX_ARRAY_SIZE ? 0x7ffffff : 0x7ffffff - 8;
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }

    /**
     * 返回链表中元素的个数数量
     *
     * @return 链表中元素个数数量
     */
    public int size() {
        return size;
    }

    /**
     * 如果链表中没有包含元素，返回true
     *
     * @return 链表中没有元素时返回true
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 当链表中包含指定的元素时，返回true；
     * 更准确的来说，当且仅当链表中至少包含一个元素e满足条件参数o == null ? e == null : o.equals(e)时返回true；
     * o == null时，至少有一个e == null 返回true；
     * o != null时，至少有一个o.equals(e) == true 返回true;
     *
     * @param o 判断在链表中是否包含的元素
     * @return 如果链表中包含指定的元素返回true
     */
    public boolean contains(Object o) {
    	//判断对象o在链表中的位置：
    	// >= 0：在链表中存在对象o的位置；
    	// < 0：在链表中不存在对象o的位置；
        return indexOf(o) >= 0;
    }

    /**
    * 返回对象在链表中第一次出现的位置，如果链表中不包括这个对象，则返回-1值；
    * 更准确的说，返回满足条件参数o == null ? get(i) == null : o.equals(get(i))条件的i的最小值，或者在没有满足条件的i的情况下，返回-1值；
    * o == null时，如果存在在i的位置上的元素也为空，则返回满足条件的i的最小值；
    * o != null时，如果存在在i的位置上的元素iValue满足o.equals(iValue)，则返回满足条件的i的最小值；
    * 如果不存在满足上述条件的i的值，则返回-1作为返回值；
    */
    public int indexOf(Object o) {
    	//参数对象o == null?
        if (o == null) {
        	// == null,遍历缓存数组,查找是否有为null的元素
            for (int i = 0; i < size; i++)
                if (elementData[i]==null)
                    return i;
        } else {
        	// != null,遍历缓存数组，查找是否有满足o.equals(e)的元素
            for (int i = 0; i < size; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        //默认返回值为-1
        return -1;
    }

    /**
    * 返回指定元素o在链表中的位置，如果没有包含在链表中则返回-1值；
    * 准确来说，返回满足条件o == null ？ get(i) == null ? o.equals(get(i))的i的最大值，如果没有满足条件的则返回-1；
    * o == null时，返回满足get(i) == null为true的i的最大值；
    * o !=null时，返回满足o.equals(e)为true的i的最大值；
    * 如果没有满足条件的i，则返回-1值；
    */
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size-1; i >= 0; i--)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = size-1; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    /**
    * 返回当前ArrayList的浅拷贝。(元素本身不会被拷贝)
    *
    *@return ArrayList的拷贝副本
    */
    public Object clone() {
        try {
        	//通过本地方法clone获得一个ArrayList类型的clone副本v
            ArrayList<?> v = (ArrayList<?>) super.clone();
            //clone副本v clone 原Arralist的缓冲数组元素到自身的缓冲数组
            v.elementData = Arrays.copyOf(elementData, size);
            //将clone副本v的操作次数计数器清0
            v.modCount = 0;
            //返回clone副本v
            return v;
        } catch (CloneNotSupportedException e) {
            // 因为我们使用的是clone的方式，所以这种异常基本不会发生
            throw new InternalError(e);
        }
    }

    /**
    * 返回一个包含链表中所有数组元素的数组(按照顺序从第一个到最后一个的顺序)
    *
    * 返回的数组是安全的，因为链表不会维护这个数组。(换句话说，这个方法将会分配一个新数组)。 
    * 因此，调用者可以自由的修改这个数组
    *
    * 这个方法充当数组和集合的API之间的桥梁
    *
    * @return 一个包含链表中顺序排列元素的数组
    */
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    /**
     * 返回一个包含链表中所有元素的数组，顺序按照链表中从第一个到最后一个的顺序；
     * 返回的数组的运行时状态将使用指定的数组的运行时类型。如果链表符合指定的数组，则返回该链表。
     * 否则，将分配一个指定的数组的运行时类型以及大小为链表长度的新的数组。
     *
     * 如果链表符合指定的数组类型，并且具有多余的空间(即、数组具有比链表中更多的元素)，紧跟在数组中的最后一个元素后的集合内容将填充为null，
     * (如果调用者知道链表中并不包括null元素，这只在确定链表长度时有用)
     *
     *
     * @param a 存储链表元素的数组，如果足够大将返回传入的数组；否则，将分配一个新的与指定运行时类型相同的数组的新数组
     * @return 一个包含链表元素的数组
     * @throws ArrayStoreException 如果指定的数组的运行时类型不是链表中每个元素的运行时类型的父类型时抛出
     * @throws NullPointerException 如果指定的数组为null时抛出
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
    	//如果传入数组的长度比当前链表的长度小
        if (a.length < size)
        	// 创建一个运行时类型的新数组，包含链表中的元素并返回该数组
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        //将elementData数组元素从0开始拷贝到数组a从0开始到size-1的位置中
        System.arraycopy(elementData, 0, a, 0, size);
        // 如果传入的数组长度比当前链表的长度长
        if (a.length > size)
        	//数组size-1的位置将填充为null
            a[size] = null;
        //返回传入的数组a
        return a;
    }

    // 获取缓冲区数组对应位置元素的方法

    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    /**
     * 返回在链表中指定位置的元素
     *
     * @param  返回元素的索引
     * @return 在链表中指定位置的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E get(int index) {
        rangeCheck(index);

        return elementData(index);
    }

    /**
     * 替换链表中指定位置上的元素
     *
     * @param index 替换元素的索引位置
     * @param element 要被存储到指定位置上的元素
     * @return 指定位置上替换之前的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E set(int index, E element) {
        rangeCheck(index);

        //从缓冲数组中取得索引index上的元素
        E oldValue = elementData(index);
        //将缓冲数组索引index位置上的元素替换为指定的元素
        elementData[index] = element;
        //返回旧元素
        return oldValue;
    }

    
    /**
     * 在链表末尾添加指定的元素
     *
     * @param e 需要追加的元素
     * @return <tt>true</tt> (实现 {@link Collection#add})
     */
    public boolean add(E e) {
    	//确定容器的容器的大小
        ensureCapacityInternal(size + 1);  // 自增链表操作次数
        //在缓冲区数组size+1的位置上添加元素e
        elementData[size++] = e;
        //操作完成返回true
        return true;
    }

    /**
     * 在链表指定位置index添加元素对象element。
     * 向右移动指定位置上(如果有的话)和之后的元素并在索引中添加一个位置，
     *
     * @param index 指定元素插入的索引位置
     * @param element 插入的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public void add(int index, E element) {
        rangeCheckForAdd(index);

        //确定容器的容量大小
        ensureCapacityInternal(size + 1);  // 链表操作次数自增
        //copy elementData中从index位置开始到之后的元素到elementData的index + 1位置之后，copy (size - index)个元素(即将index及之后的元素移动到index + 1之后的位置)
        //当前elementData在索引index的位置上的元素为elementData[index]上的原元素
        System.arraycopy(elementData, index, elementData, index + 1,
                         size - index);
        //在index索引位置替换元素element，此时elementData[index]上的元素为添加的元素element
        elementData[index] = element;
        //链表长度size自增
        size++;
    }

    /
    /**
     * 删除链表上指定位置上的元素
     * 向左移动该位置上和之后的元素，索引数量-1
     *
     * @param index 要移除指定元素的索引位置
     * @return 从指定位置上移除的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E remove(int index) {
        rangeCheck(index);

        //链表操作数 + 1
        modCount++;
        //获取缓冲数组elementData在索引index位置上的元素
        E oldValue = elementData(index);

        //需要移动的元素数量
        int numMoved = size - index - 1;
        //如果需要移动的元素数量 > 0
        if (numMoved > 0)
        	//将elementData从index + 1以之后位置上的元素copy到elementData的index及之后的位置，共移动numMoved = size - index - 1个元素；
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        //将缓冲数组elementData的最后一个元素置为null，当GC发生时会回收该空间，链表长度-1;
        elementData[--size] = null; 
        //返回删除的元素
        return oldValue;
    }

    /**
     * 从链表中删除第一个匹配的元素(如果存在的话)。 如果链表中不包含该元素, 链表将不会发生改变.
     * 更准确的说，删除满足条件o == null ? get(i) == null : o.equals(get(i))的索引i的最小值对应位置上的元素(如果满足条件的元素存在的话)
     * 当链表中存在指定元素时，返回true(或者说，这个链表会因为该方法的调用而发生改变)。
     *
     * @param o 需要进行移除的元素(如果存在的话)
     * @return 如果该链表包含指定元素的话返回true
     */
    public boolean remove(Object o) {
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
        } else {
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }

    /*
     * 私有溢出方法，不进行边界检查rangCheck(index)并且不返回删除的元素
     */
    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        elementData[--size] = null; // GC发生时回收空间
    }

    /**
     * 移除列表中的所有元素。  该列表在这个方法调用后将清空。
     */
    public void clear() {
        modCount++;

        // 清空列表中元素引用，root cann`t be reached，根节点不可达算法，GC发生时回收空间
        for (int i = 0; i < size; i++)
            elementData[i] = null;
        //链表长度清0
        size = 0;
    }

    /**
     * 在链表末尾添加指定集合中的所有元素, 添加的顺序按照指定集合的Iterator迭代顺序.  
     * 如果在这个过程中修改了指定集合中的元素，那么这个操作可能是无效的 (也就是说如果指定的集合是该列表，
     * 并且该列表是非空的，那么这个方法调用的结果是未明确的)
     *
     * @param c 添加到列表中的集合元素
     * @return 如果调用该方法之后列表结果发生了改变
     * @throws NullPointerException 如果指定添加的集合为null时抛出
     */
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        //要添加的集合的长度，也就是确定要添加元素的个数
        int numNew = a.length;
        //确定容器在添加元素之后所需容量大小
        ensureCapacityInternal(size + numNew);  // 自增操作次数
        //在elementData的size位置开始添加数组a从0开始的numNew个元素
        System.arraycopy(a, 0, elementData, size, numNew);
        //确定链表添加完元素之后的新的长度
        size += numNew;
        //返回是否添加的是空集合的判断结果，非空集合链表添加元素后链表结果发生改变，结果为true；空集合链表添加元素后链表未发生改变，结果为false
        return numNew != 0;
    }

     /**
     * 在链表的指定位置添加指定集合中的全部元素. 向右移动该位置及之后的元素 (增加索引大小).  
     * 新添加的元素在链表中添加的顺序将会按照在指定集合的迭代器中的出现的顺序添加
     *
     * @param index 在链表中添加指定集合中的第一个元素的索引位置
     * @param c 将要添加到链表中的集合元素
     * @return 调用前后链表的结果是否发生了改变
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws NullPointerException 在添加的集合为null时抛出
     */
    public boolean addAll(int index, Collection<? extends E> c) {
    	//边界检查
        rangeCheckForAdd(index);

        //将集合转换为数组
        Object[] a = c.toArray();
        //判断数组的长度，即要添加的元素个数
        int numNew = a.length;
        //确定添加元素后容器的容量大小
        ensureCapacityInternal(size + numNew);  // 自增操作次数

        //链表中要移动的元素个数
        int numMoved = size - index;
        if (numMoved > 0)
        	//将elementData从index位置之后的numMoved个元素copy到从elementData的(index + numNew)的位置
            System.arraycopy(elementData, index, elementData, index + numNew,
                             numMoved);
        //copy a数组从0开始之后的numNew个元素到elementData的index位置
        System.arraycopy(a, 0, elementData, index, numNew);
        //改变链表长度
        size += numNew;
        //返回链表在操作完成之后链表长度是否发生了改变，即添加的元素是否大于0
        return numNew != 0;
    }

    /**
     * 移除在链表中formIndex开始到toIndex位置的内的元素, 包括formIndex上的元素, 不包括toIndex位置上的元素，范围[ fromIndex , toIndex ).
     * 左移之后的元素(减少索引大小).
     * 通过减少(toIndex - fromIndex)个元素缩短链表长度
     * (如果fromIndex == toIndex的话，这个操作是无效的)
     *
     * @throws IndexOutOfBoundsException 如果fromIndex < 0 或者 fromIndex >= size 或者 toIndex > size 或者 toIndex < fromIndex时抛出的异常
     */
    protected void removeRange(int fromIndex, int toIndex) {
    	//自增操作次数
        modCount++;
        //确定要移动的元素个数
        int numMoved = size - toIndex;
        //将elementData从toIndex位置及之后的共numMoved个元素copy到elementData从fromIndex开始的位置
        System.arraycopy(elementData, toIndex, elementData, fromIndex,
                         numMoved);

        // 移除引用，GC时回收空间，计算移除元素后链表的长度
        int newSize = size - (toIndex-fromIndex);
        for (int i = newSize; i < size; i++) {
            elementData[i] = null;
        }
        //设置移除元素后链表长度的计数器size
        size = newSize;
    }

    /**
     * 检查索引是否在范围之内.  如果不在范围之内, 跑出一个合适的运行时异常.  
     * 这个方法不会检查传入的索引是否有效: 
     * 这个方法常用在数组转换之前，如果传入的索引是无效的，则抛出数组索引异常ArrayIndexOutOfBoundsException;
     */
    private void rangeCheck(int index) {
    	//如果传入的index超出链表的长度，则抛出索引范围异常IndexOutOfBoundsException
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * 用于添加元素的方法add和addAll的范围检查方法
     */
    private void rangeCheckForAdd(int index) {
    	//如果传入的索引位置index > 链表的长度size 或者 传入的索引index < 0,则抛出索引超出范围异常IndexOutOfBoundsException
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * 用于构造索引超出异常的异常信息内容；
     * 在许多异常处理的方式中，这种以大纲形式(简略异常信息)的方式对VM服务器和客户端的性能最好
     */
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    /**
     * 移除链表中包含在指定集合中的所有元素
     *
     * @param c 需要从链表中移除的集合
     * @return 返回链表在执行完方法后，链表是否发生了改变
     * @throws ClassCastException 如果链表的元素类型和传入集合中的元素类型不相匹配时抛出的异常
     * @throws NullPointerException 如果链表中包含null元素而集合中不允许null元素或者传入的集合为null
     */
    public boolean removeAll(Collection<?> c) {
    	//判断集合是否为null
        Objects.requireNonNull(c);
        return batchRemove(c, false);
    }

    /**
     * 仅保存链表中和指定集合中的共有元素. 换句话说,移除链表中的不包含在集合中的元素.
     *
     * @param c 想要在链表中保有的元素集合
     * @return 执行方法后链表是否发生了改变
     * @throws ClassCastException 链表中的元素类型和集合中的元素类型不匹配
     * @throws NullPointerException 在链表中包含null元素并且指定的集合中不允许null元素时抛出
     */
    public boolean retainAll(Collection<?> c) {
    	//集合c的非null检查
        Objects.requireNonNull(c);
        return batchRemove(c, true);
    }


    private boolean batchRemove(Collection<?> c, boolean complement) {
    	//clone一个局部缓存数组
        final Object[] elementData = this.elementData;
        //遍历角标标识r，保有元素数组修改的角标标识w
        int r = 0, w = 0;
        //修改flag：表示操作开始
        boolean modified = false;
        try {
        	//遍历缓存数组
            for (; r < size; r++)
            	//当complement为true时，保有相同的元素；
            	//当complement为false时，移除相同的元素；
                if (c.contains(elementData[r]) == complement)
                    elementData[w++] = elementData[r];
        } finally {
            // 维护抽象集合的结构完整性
            // 几遍c.contains()方法抛出异常信息
            if (r != size) {
            	//r != size：在遍历数组时发生异常
            	//将elementData从索引r位置及之后共(size-r)个元素copy到elementData的w位置
                System.arraycopy(elementData, r,
                                 elementData, w,
                                 size - r);
                //先执行size-r：未遍历的集合元素数量，再执行w+=：移除元素后数组中保有元素的数量;w += size - r
                w += size - r;
            }
            //整理数组包含元素
            if (w != size) {
            	//如果保有元素的数量比size的长度短，即不保有链表中的所有元素，有元素在操作中发生了移除
                // 将已用有效元素长度之后的无用数组长度元素置null，解除相关无用元素的在内存之中的引用，在GC时回收空间
                for (int i = w; i < size; i++)
                    elementData[i] = null;
                //增加自操作数次数
                modCount += size - w;
                //修改链表长度
                size = w;
                //修改flag：表示操作完成
                modified = true;
            }
        }
        //返回操作结果
        return modified;
    }

    /**
     * 将ArrayList的信息状态写入一个流中并且进行序列化.
     *
     * @serialData 数组的长度+按顺序排列的数组元素信息.
     */
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException{
        // 输出元素个数统计信息以及隐藏的信息，存储当前链表以操作次数
        int expectedModCount = modCount;
        //写入当前对象的非静态和非transient修饰的变量信息，即输出流输出默认必须输出的信息
        s.defaultWriteObject();

        // 将容器大小信息以与方法clone()兼容的变量类型写入，输出流进行输出size信息 
        // writeInt和writeObject方法在输出size和元素信息时将对象转换成byte类型的数据进行输出
        s.writeInt(size);

        // 按顺序写入所有元素信息，输出流进行输出所有元素信息
        for (int i=0; i<size; i++) {
            s.writeObject(elementData[i]);
        }

        //如果自操作数不等于存储的已操作次数，表示在输出过程中链表发生了改变，抛出异常ConcurrentModificationException
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    /**
     * 从一个流中恢复ArrayList实例 (反序列化过程).
     */
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        	//初始化缓冲区数组为一个空数组
        elementData = EMPTY_ELEMENTDATA;

        // 读取输入流大小以及隐藏的基本信息
        s.defaultReadObject();

        // 读取容量大小
        s.readInt(); // ignored

        if (size > 0) {
            // 类似于clone(),计算容量大小 基于size大小分配数组空间而不是基于容器容量分配数组空间
            int capacity = calculateCapacity(elementData, size);
            //检查流s中是否有包含容器大小为capacity的Object[].class的数组信息
            SharedSecrets.getJavaOISAccess().checkArray(s, Object[].class, capacity);
            //确定容器的合适大小
            ensureCapacityInternal(size);

            //按顺序读取流中的所有元素信息
            Object[] a = elementData;
            for (int i=0; i<size; i++) {
                a[i] = s.readObject();
            }
        }
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence), starting at the specified position in the list.
     * The specified index indicates the first element that would be
     * returned by an initial call to {@link ListIterator#next next}.
     * An initial call to {@link ListIterator#previous previous} would
     * return the element with the specified index minus one.
     *
     * <p>The returned list iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    /**
     * 返回当前列表从索引index开始的一个遍历器(以适当的顺序遍历),从链表的指定位置index开始进行遍历 。
     * 传入的index表示在最开始调用ListIterator中的next方法之后将要返回的第一个元素。
     * 最开始调用ListIterator中的previous将会返回指定的索引位置-1即前一个元素
     *
     * 返回的迭代器在执行某些操作时会出现问题
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public ListIterator<E> listIterator(int index) {
    	//如果传入的索引 < 0 或者 索引 > 链表长度size，抛出索引超出异常；
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: "+index);
        //通过索引边界检查后，返回结果返回从index位置开始的迭代器
        return new ListItr(index);
    }

    /**
     * 返回一个当前链表从索引0开始的迭代器(以适当的顺序进行迭代)
     *
     * 返回的迭代器易出错
     *
     * @see #listIterator(int)
     */
    public ListIterator<E> listIterator() {
    	//返回从索引0开始的迭代器
        return new ListItr(0);
    }

    /**
     * 返回一个按照一定顺序迭代元素的迭代器
     *
     * 返回的迭代器哟出错
     *
     * @return 一个按照一定顺序进行迭代的迭代器
     */
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * AbstractList.Itr类的具体实现
     */
    private class Itr implements Iterator<E> {
        int cursor;       // 下一个将要迭代的元素的索引
        int lastRet = -1; // 返回的最后一个元素的索引; 没有时返回-1
        //预计的链表自操作数，预计->(链表自操作数不会发生改变)
        int expectedModCount = modCount;

        /**
        * 无参构造器
        */
        Itr() {}

        /**
        * 是否有下一个元素
        */
        public boolean hasNext() {
        	//当游标大小等于链表长度时即索引大小等于链表长度时则不存在下一个元素
            return cursor != size;
        }

        /**
        * 获取下一个元素
        */
        @SuppressWarnings("unchecked")
        public E next() {
        	//链表在操作过程中是否发生了改变
            checkForComodification();
            //获取索引游标所在位置
            int i = cursor;
            //如果索引游标指向大于或者等于链表长度的位置，抛出没有该元素异常NoSuchElementException
            if (i >= size)
                throw new NoSuchElementException();
            //获取当前链表的缓冲数组
            Object[] elementData = ArrayList.this.elementData;
            //如果索引游标的大小大于或者等于缓冲数组的长度，则抛出并发执行异常ConcurrentModificationException
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            //如果没有发生错误，索引游标加1
            cursor = i + 1;
            //返回索引游标未发生改变时所指向的元素，并将对于最后一个返回元素的索引标识lastRet置为未发生改变时的索引游标的大小
            return (E) elementData[lastRet = i];
        }

       	/**
       	* 移除元素
       	*/
        public void remove() {
        	//如果最后一个返回的元素的索引标识 < 0,即未执行遍历操作或对列表元素进行了增删操作，抛出非法状态异常IllegalStateException
            if (lastRet < 0)
                throw new IllegalStateException();
            //检查链表在操作过程中是否发生了改变
            checkForComodification();

            try {
            	//移除最后一个返回的元素，在这个方法中将改变自增操作数modCount
                ArrayList.this.remove(lastRet);
                //索引游标指向最后一个返回元素的位置
                cursor = lastRet;
                //最后一个返回元素的索引标识初始化置为-1
                lastRet = -1;
                //将改变后的自增操作数赋值给预估操作数
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        /**
        * 将ArrayList中的元素添加到消费者consumer之中
    	*/
        @Override
        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> consumer) {
        	//consumer对象非null检查
            Objects.requireNonNull(consumer);
            //获取链表当前长度
            final int size = ArrayList.this.size;
            //获取迭代器当前索引游标位置
            int i = cursor;
            //如果索引游标位置超出链表长度，则结束方法
            if (i >= size) {
                return;
            }
            //获取缓冲数组
            final Object[] elementData = ArrayList.this.elementData;
            //当游标位置超出缓冲数组长度，抛出并行操作异常ConcurrentModificationException
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            //在游标未超出链表长度且保证自操作数相同的前提下，使用消费者添加数组元素
            while (i != size && modCount == expectedModCount) {
                consumer.accept((E) elementData[i++]);
            }
            // 更新一次索引游标和最后一次返回元素的索引，减少堆的写次数
            cursor = i;
            lastRet = i - 1;
            //检查在操作过程中是否发生了并发操作，改变了自操作数
            checkForComodification();
        }

        /**
        * 检查是否发生了并发操作
        */
        final void checkForComodification() {
        	//如果自操作数发生了改变，说明发生了链表进行了增删改的操作
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    /**
     * AbstractList.ListItr类的具体实现
     */
    private class ListItr extends ListItr implements ListIterator<E> {
    	/**
    	* 构造方法，将游标指向index位置
    	*/
        ListItr(int index) {
            super();
            cursor = index;
        }

        /**
        * 是否是第一个元素，索引游标是否在开始位置
        */
        public boolean hasPrevious() {
            return cursor != 0;
        }

        /**
        * 获取下一个元素位置，索引游标所在位置
        */
        public int nextIndex() {
            return cursor;
        }

        /**
        * 获取前一个索引游标所在位置
        */
        public int previousIndex() {
            return cursor - 1;
        }

        /**
        * 迭代器获取迭代中当前元素的前一个元素
        */
        @SuppressWarnings("unchecked")
        public E previous() {
        	//并发操作检查，链表是否发生了改变
            checkForComodification();
            //获取索引游标的前一个索引位置
            int i = cursor - 1;
            //i < 0：当前迭代元素是第一个元素，抛出没有该元素的异常NoSuchElementException
            if (i < 0)
                throw new NoSuchElementException();
            //获取当前ArrayList缓冲数组
            Object[] elementData = ArrayList.this.elementData;
            //如果索引i大于缓冲数组elementData的长度，说明缓冲数组的长度发生了改变，抛出并发操作异常ConcurrentModificationException
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            //将当前游标索引更新为前一个迭代元素索引位置
            cursor = i;
            //更新迭代返回的最后一个元素的索引位置为游标索引的位置并返回迭代元素前一个元素
            return (E) elementData[lastRet = i];
        }

        /**
        * 替换迭代位置的当前元素
        */
        public void set(E e) {
        	//最后一个返回的元素的索引位置 < 0，抛出非法状态访问异常IllegalStateException
            if (lastRet < 0)
                throw new IllegalStateException();
            //并发操作访问异常，在操作过程中对链表元素进行更改操作
            checkForComodification();

            try {
            	//替换迭代元素位置的元素内容
                ArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        /**
        * 在当前迭代的位置添加元素内容
        */
        public void add(E e) {
        	//并发操作检查，来拿表是否发生了更改操作
            checkForComodification();

            try {
            	//获取当前索引游标的位置
                int i = cursor;
                //使用ArrayList的add方法添加元素
                ArrayList.this.add(i, e);
                //更改索引游标位置
                cursor = i + 1;
                //初始化返回元素索引
                lastRet = -1;
                //更新自操作数，ArrayList的add方法会更改自操作数modCount
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * 返回fromIndex到toIndex之间的部分，[fromIndex, toIndex)。 (如果左右区间位置相等, 返回的列表为null。) 
     * 返回的列表由原列表提供维护, 反之，对返回列表的操作也将影响到原列表。
     * 返回的列表可以支持所有对于列表的操作。
     *
     * 这个方法消除了显式范围的操作，通常这种方法常用在数组操作之中. 
     * 任意对于列表的操作都可以通过操作局部列表替换操作全部列表的操作。 例如下面对于局部列表的移除部分元素的操作：list.subList(from, to).clear();
	 *
     * 类似的方法例如indexOf(index)方法和lastIndexOf(index)方法同样适用, 集合类Collection中的所有方法都适用于局部列表.
     *
     * 如果备份列表也就是当前列表以任何方式进行了修改而不是通过这个方法对返回的列表进行操作，那么返回的列表将会没有任何意义 
     * (通过改变当前列表的大小或者以类似的方法修改列表的结构操作都会使迭代器返回不正确的结果信息)
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    public List<E> subList(int fromIndex, int toIndex) {
    	//截取链表区间范围正确性检查
        subListRangeCheck(fromIndex, toIndex, size);
        return new SubList(this, 0, fromIndex, toIndex);
    }

    /**
    * 截取列表的范围正确性检查
    */
    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
    	//如果左区间小于0，抛出索引范围异常IndexOutOfBoundsException
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        //如果左区间大于列表长度，抛出索引范围异常IndexOutOfBoundsException
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        //如果左区间大于右区间，抛出非法状态访问异常IllegalArgumentException
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                                               ") > toIndex(" + toIndex + ")");
    }

    /**
    * 抽象类AbstractList、接口RandomAccess的具体实现
    */
    private class SubList extends AbstractList<E> implements RandomAccess {

    	/**
    	* 被截取的列表
    	*/
        private final AbstractList<E> parent;
        /**
        * 相对于截取的列表在被截列表的偏移量
        */
        private final int parentOffset;
        /**
        * 截取列表开始位置对于被截列表的已存在的偏移量
        */
        private final int offset;
        /**
        * 截取出来的列表长度
        */
        int size;

        /**
        * 有参构造器
        */
        SubList(AbstractList<E> parent,
                int offset, int fromIndex, int toIndex) {
            this.parent = parent;
            this.parentOffset = fromIndex;
            this.offset = offset + fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = ArrayList.this.modCount;
        }

        /**
        * 替换截取出来的列表在索引index位置上的元素
        */
        public E set(int index, E e) {
        	//索引范围合理性检查
            rangeCheck(index);
            //并发操作检查
            checkForComodification();
            //获取索引index相对于在原列表缓冲数组中的元素
            E oldValue = ArrayList.this.elementData(offset + index);
            //替换索引index相对于在原列表缓冲数组中的元素
            ArrayList.this.elementData[offset + index] = e;
            //返回被替换的旧元素
            return oldValue;
        }

        /**
        * 获取截取出来的列表上的索引位置index上的元素
        */
        public E get(int index) {
        	//索引范围合理性检查
            rangeCheck(index);
            //并发操作检查
            checkForComodification();
            // 返回索引index相对于在缓冲数组中的元素，添加偏移量之后的位置
            return ArrayList.this.elementData(offset + index);
        }

        /**
        * 获取被截列表的长度
        */
        public int size() {
        	//并发操作检查
            checkForComodification();
            //返回被截列表的长度
            return this.size;
        }

        /**
        * 在被截列表的指定索引位置index添加元素e
        */
        public void add(int index, E e) {
        	//索引范围性检查
            rangeCheckForAdd(index);
            //并发操作检查
            checkForComodification();
            //原列表在指定位置添加元素e
            parent.add(parentOffset + index, e);
            //同步自操作数
            this.modCount = parent.modCount;
            //自增被截列表的长度
            this.size++;
        }

        /**
        * 移除被截列表索引index位置上的元素
        */
        public E remove(int index) {
        	//索引范围性检查
            rangeCheck(index);
            //并发操作检查
            checkForComodification();
            //从原列表中移除指定位置元素
            E result = parent.remove(parentOffset + index);
            //同步自操作数
            this.modCount = parent.modCount;
            //截取出来的列表长度自减
            this.size--;
            //返回移除的元素
            return result;
        }

        /**
        * 删除被截列表索引fromIndex到索引toIndex范围内的元素，[fromIndex, toIndex)
        */
        protected void removeRange(int fromIndex, int toIndex) {
        	//并发操作检查
            checkForComodification();
            //parentOffset + fromIndex : 在原列表中开始的索引位置
            //parentOffset + toIndex : 在原列表中结束的索引位置
            parent.removeRange(parentOffset + fromIndex,
                               parentOffset + toIndex);
            //同步自操作数
            this.modCount = parent.modCount;
            //toInde - fromIndex：被截列表的长度，移除元素后被截列表的长度
            this.size -= toIndex - fromIndex;
        }

        /**
        * 在被截列表中添加指定集合
        */
        public boolean addAll(Collection<? extends E> c) {
            return addAll(this.size, c);
        }

        /**
        * 在被截列表指定的索引index位置添加指定的集合
        */
        public boolean addAll(int index, Collection<? extends E> c) {
        	//范围添加合理性检查
            rangeCheckForAdd(index);
            //获取添加集合的长度
            int cSize = c.size();
            //如果添加的集合是个空集合，则返回false
            if (cSize==0)
                return false;

            //并发操作检查
            checkForComodification();
            //在原列表相应位置添加集合c
            parent.addAll(parentOffset + index, c);
            //同步自操作数
            this.modCount = parent.modCount;
            //计算添加集合后被截列表的长度
            this.size += cSize;
            //添加操作完成，返回true
            return true;
        }

        /**
        * 截取出来的列表迭代
        */
        public Iterator<E> iterator() {
            return listIterator();
        }

        /**
        * 从截取的列表的index位置开始迭代
        */
        public ListIterator<E> listIterator(final int index) {
        	//并发操作检查
            checkForComodification();
            //索引范围合理性检查
            rangeCheckForAdd(index);
            //获取偏移量
            final int offset = this.offset;

            //返回新建迭代器
            return new ListIterator<E>() {
            	//获取索引位置
                int cursor = index;
                //初始化迭代元素的索引位置
                int lastRet = -1;
                //缓存当前列表的自操作数
                int expectedModCount = ArrayList.this.modCount;

                /**
                * 判断是否有下一个元素
                */
                public boolean hasNext() {
                	//判断将要迭代元素的索引位置是否超过了截取列表的长度，即是否已迭代完截取列表中的元素
                    return cursor != SubList.this.size;
                }

                /**
                * 获取迭代的下一个元素
                */
                @SuppressWarnings("unchecked")
                public E next() {
                	//并发操作检查
                    checkForComodification();
                    //获取索引游标位置
                    int i = cursor;
                    //索引游标位置是否超出了截取数组的长度
                    if (i >= SubList.this.size)
                        throw new NoSuchElementException();
                    //获取原列表的缓冲数组
                    Object[] elementData = ArrayList.this.elementData;
                    //偏移量+游标位置是否超出了缓冲数组的长度
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    //游标自增1，指向下一个元素索引位置
                    cursor = i + 1;
                    //更新迭代元素索引位置lastRet，返回原列表缓冲数组中对应位置offset + lastRet的元素
                    return (E) elementData[offset + (lastRet = i)];
                }

                /**
                * 是否有上一个元素
                */
                public boolean hasPrevious() {
                	//当当前迭代的元素为第一个元素时，没有上一个元素返回false，否则返回true
                    return cursor != 0;
                }

                /**
                * 返回当前迭代元素的上一个元素
                */
                @SuppressWarnings("unchecked")
                public E previous() {
                	//并发操作检查
                    checkForComodification();
                    //指向上一个元素索引位置
                    int i = cursor - 1;
                    //是否是第一个元素
                    if (i < 0)
                        throw new NoSuchElementException();
                    //获取源列表的缓冲数组
                    Object[] elementData = ArrayList.this.elementData;
                    //计算偏移后的索引位置是否超出缓冲数组的长度
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    //更新索引位置为上一个元素的位置
                    cursor = i;
                    //更新返回元素的索引位置为上一个元素的索引位置，返回在缓冲数组中的相应位置的元素
                    return (E) elementData[offset + (lastRet = i)];
                }

                /**
                * 保存列表中的元素到指定的消费者中
                */
                @SuppressWarnings("unchecked")
                public void forEachRemaining(Consumer<? super E> consumer) {
                	//非null参数检查
                    Objects.requireNonNull(consumer);
                    //获取截取出来的列表的长度
                    final int size = SubList.this.size;
                    //获取当前索引游标位置
                    int i = cursor;
                    //判断是否迭代到最后一个元素的位置
                    if (i >= size) {
                        return;
                    }
                    //获取原列表的缓冲数组
                    final Object[] elementData = ArrayList.this.elementData;
                    //判断偏移后的索引位置是否超出了缓冲数组的长度
                    if (offset + i >= elementData.length) {
                        throw new ConcurrentModificationException();
                    }
                    //当没有迭代完元素且在这个过程原列表没有增删改的操作
                    while (i != size && modCount == expectedModCount) {
                    	//消费者添加缓冲数组中相应位置上的元素
                        consumer.accept((E) elementData[offset + (i++)]);
                    }
                    // 只更新一次迭代返回元素的索引位置，减少堆写入
                    lastRet = cursor = i;
                    //并发操作检查
                    checkForComodification();
                }

                /**
                * 获取迭代的下一个元素位置
                */
                public int nextIndex() {
                    return cursor;
                }

                /**
                * 获取迭代的上一个元素的位置
                */
                public int previousIndex() {
                    return cursor - 1;
                }

                /**
                * 移除当前迭代的元素
                */
                public void remove() {
                	//如果迭代的元素索引位置 < 0,或者没有开始迭代，或者在列表中执行了增删改操作使列表内容元素发生了改变，抛出非法状态访问异常IllegalStateException
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    //并发操作检查
                    checkForComodification();

                    try {
                    	//在截取的列表中移除位置lastRest上的元素
                        SubList.this.remove(lastRet);
                        //更新当前迭代元素的位置
                        cursor = lastRet;
                        //初始化迭代元素索引，表示列表内容发生了改变
                        lastRet = -1;
                        //同步自操作数
                        expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                /**
                * 替换指定位置上的元素
                */
                public void set(E e) {
                	//如果迭代器未进行迭代或者列表内容发生了增删改操作，抛出非法访问异常IllegalStateException
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    //并发操作检查
                    checkForComodification();

                    try {
                    	//替换加上偏移量之后索引offset + lastRest位置上的元素
                        ArrayList.this.set(offset + lastRet, e);
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                /**
                * 在当前迭代位置添加元素
                */
                public void add(E e) {
                	//并发操作检查
                    checkForComodification();

                    try {
                    	//获取当前索引游标位置
                        int i = cursor;
                        //在截取列表的指定位置添加元素e
                        SubList.this.add(i, e);
                        //索引游标位置 + 1
                        cursor = i + 1;
                        //初始化迭代元素索引位置，表示列表内容发生了改变
                        lastRet = -1;
                        //同步列表自操作数
                        expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                /**
                * 并发操作检查
				*/
                final void checkForComodification() {
                	//如果缓存的自操作数与列表的自操作数不相等，表示列表发生了增删改操作使得列表的内容发生了改变
                    if (expectedModCount != ArrayList.this.modCount)
                        throw new ConcurrentModificationException();
                }
            };
        }

        /**
        * 从[fromIndex, toIndex)内截取列表
        */
        public List<E> subList(int fromIndex, int toIndex) {
        	//截取列表的范围合理性检查
            subListRangeCheck(fromIndex, toIndex, size);
            //返回截取列表的可操作对象
            return new SubList(this, offset, fromIndex, toIndex);
        }

        /**
        * 索引范围性检查
        */
        private void rangeCheck(int index) {
        	//当索引 < 0 或者 索引超出当前截取列表的长度，抛出索引范围异常IndexOutOfBoundsException
            if (index < 0 || index >= this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        /**
        * 索引范围合理性检查
        */
        private void rangeCheckForAdd(int index) {
        	//当索引 < 0 或者 索引超出当前截取列表的长度，抛出索引范围异常IndexOutOfBoundsException
            if (index < 0 || index > this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        /*
        * 格式化异常信息
        */
        private String outOfBoundsMsg(int index) {
            return "Index: "+index+", Size: "+this.size;
        }

        /**
        * 并发操作检查
        */
        private void checkForComodification() {
        	//如果原列表的自操作数不等于缓存的自操作数，表示列表内容发生了改变，抛出并发操作异常
            if (ArrayList.this.modCount != this.modCount)
                throw new ConcurrentModificationException();
        }

        /**
        * 
        */
        public Spliterator<E> spliterator() {
            checkForComodification();
            return new ArrayListSpliterator<E>(ArrayList.this, offset,
                                               offset + this.size, this.modCount);
        }
    }

    /**
    * 将原列表元素添加到消费者action中
    */
    @Override
    public void forEach(Consumer<? super E> action) {
    	//非null参数检查
        Objects.requireNonNull(action);
        //获取当前自操作数
        final int expectedModCount = modCount;
        @SuppressWarnings("unchecked")
       	//获取当前列表的缓冲数组
        final E[] elementData = (E[]) this.elementData;
        //获取截取列表的长度
        final int size = this.size;
        //当列表内容没有发生改变时，遍历缓冲数组，将缓冲数组中的元素添加到消费者action中
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            action.accept(elementData[i]);
        }
        //检查在这个过程中列表内容是否发生了改变，即是否有并发操作修改了元素的内容
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    /**
     * 在当前列表的元素上创建一个允许延时绑定和失败的快速拆分器.
     *
     * 这个拆分器将记录大小、补偿和命令信息，重写其他特征值报告信息。
     *
     * @return 列表元素的快速拆分器
     * @since 1.8
     */
    @Override
    public Spliterator<E> spliterator() {
        return new ArrayListSpliterator<>(this, 0, -1, 0);
    }

    /** 基于索引的允许延时初始化的快速拆分器 */
    static final class ArrayListSpliterator<E> implements Spliterator<E> {

        /*
         * 如果列表是不变的或者是结构不变的(没有发生添加、移除等操作), 我们可以使用数组拆分器实现列表的拆分器.
         * 相反我们遍历尽可能多的干扰信息方式既实用有保证了性能. 我们依靠modCounts自操作数来保证这种检查方式的实现.
         * 这些方式不能保证遍历的并发冲突性问题，而且有时对于线程内部的干扰的操作过于保守，但是检测到的问题已经具有一定的实用价值.
         * 为了实现这个目标， 我们采取以下措施进行实现：
         * (1).直到我们需要重新提交再次检查的信息状态的时候，延时初始化迭代的最后一个元素为索引位置和预计的自操作数；从而提高精度，(这种方式不适用于创建当前非惰性值的快速拆分器的截取的列表)
         * (2) 我们在每个方法的末尾执行一次并发异常ConcurrentModificationException检查，这些方法对性能结构是敏感的；
         * 我们将在除了迭代之外的每个方法的末尾进行性能检测，而不是在方法开始时进行性能检测. 进一步来说，CME的触发检查适用于所有假设情况,比如：由于干扰引起缓冲数组为null值或者元素数量太少的情况；  
         * 这种方式使得我们可以在每个方法内部进行循环而不用去多次检查是否产生了列表结构的改变, 并且简化了lambda分辨率。 注意：在list的流操作中需要进行多次检查.
         * 对于每一个forEach(a)方法，除了在forEach(a)方法内部之外不进行检查或者其他计算.  其他的不常用的方法不会利用大多数的这种流线型操作。
         */

        /**
        * 指定列表
        */
        private final ArrayList<E> list;
        /**
        * 当前索引位置,进行前进或者拆分时进行修改
        */
        private int index;
        /**
        * 返回的最后一个元素的索引位置，在未使用之前为-1,在使用之后为返回的最后一个索引
        */
        private int fence; 
        /**
        * 预计的自操作数，当迭代的最后一个元素索引位置进行设定时进行初始化操作
        */
        private int expectedModCount; 

        /** 创建一个覆盖指定范围的新的拆分器 */
        ArrayListSpliterator(ArrayList<E> list, int origin, int fence,
                             int expectedModCount) {
            this.list = list; // 如果不进行遍历的话可以为null
            this.index = origin;
            this.fence = fence;
            this.expectedModCount = expectedModCount;
        }

        /**
        * 指向列表中最后一个元素的位置
        * @return 0:传入的列表list为null；
        * 		!=0:传入的列表的长度大小；
        */
        private int getFence() { // 第一次使用时初始化fence
            int hi; // (在方法forEach中使用了一个专门的变量)
            ArrayList<E> lst; //局部列表
            if ((hi = fence) < 0) {//列表是否未进行迭代
                if ((lst = list) == null)//将局部列表变量赋值为传入的列表，判断使用的列表是否为null
                    hi = fence = 0;//将迭代元素指向第一个元素索引位置
                else {
                	//初始化列表的自操作数
                    expectedModCount = lst.modCount;
                    //将迭代元素指向最后一个元素之后的索引位置，即指向迭代完元素的位置，将索引位置赋值给局部变量
                    hi = fence = lst.size;
                }
            }
            return hi;
        }

        /**
        * 创建列表拆分器
        */
        public ArrayListSpliterator<E> trySplit() {
        	//获取列表大小，最低范围索引值为当前索引位置，中间索引位置为(当前索引位置 + 列表大小) / 2；
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            //判断当前索引位置与中间索引位置的比较，即当前索引位置是否超过了列表的一半索引，超过一半不进行拆分返回null，否则返回一个新建的列表拆分器
            return (lo >= mid) ? null : // 将列表分为两半除非太小
                new ArrayListSpliterator<E>(list, lo, index = mid,
                                            expectedModCount);
        }

        /**
        * 将列表中下一个元素添加到消费者action中
        * 如果有下一个元素，添加成功返回true；
        * 如果没有下一个元素返回false；
        */
        public boolean tryAdvance(Consumer<? super E> action) {
        	//传入的消费者action是否为null
            if (action == null)
                throw new NullPointerException();
            //获取列表的大小，当前索引位置
            int hi = getFence(), i = index;
            //当前索引位置是否小于列表的大小，即是否在列表索引范围内
            if (i < hi) {
            	//索引位置 + 1指向下一个元素的索引位置
                index = i + 1;
                //获取列表中下一个元素
                @SuppressWarnings("unchecked") E e = (E)list.elementData[i];
                //将元素添加到消费者中
                action.accept(e);
                //列表在操作过程中是否发生了增删改操作
                if (list.modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                //执行完成返回true
                return true;
            }
            //如果索引位置大于或者等于列表长度即索引超出了列表的索引范围，返回false
            return false;
        }

        /**
        * 将列表中元素添加到消费者中
        */
        public void forEachRemaining(Consumer<? super E> action) {
            int i, hi, mc; // 提高访问和检查效率
            ArrayList<E> lst; Object[] a;
            //判断传入的消费者是否为null
            if (action == null)
                throw new NullPointerException();
            //判断传入的列表和列表的缓冲数组是否为null
            if ((lst = list) != null && (a = lst.elementData) != null) {
            	//如果均不为null，将列表大小赋值给局部变量hi，判断局部变量即列表大小是否小于0即是否未对列表进行迭代操作或者对列表进行了增删改操作
                if ((hi = fence) < 0) {
                	//获取列表的自操作数
                    mc = lst.modCount;
                    //获取列表的大小
                    hi = lst.size;
                }
                else
                	//未对列表进行了增删改操作
                    mc = expectedModCount;
                //判断索引位置是否大于等于0并且索引是否小于等于缓冲数组的长度，将当前索引位置赋值给i，并将列表的大小赋值给变量index
                if ((i = index) >= 0 && (index = hi) <= a.length) {
                	//遍历缓冲数组
                    for (; i < hi; ++i) {
                    	//获取缓冲数组的元素添加到消费者action中
                        @SuppressWarnings("unchecked") E e = (E) a[i];
                        action.accept(e);
                    }
                    //判断列表是否发生了增删改操作
                    if (lst.modCount == mc)
                        return;
                }
            }
            throw new ConcurrentModificationException();
        }

        /**
        * 预估剩余空间大小
        */
        public long estimateSize() {
            return (long) (getFence() - index);
        }

        /**
        * 特征值数值
        */
        public int characteristics() {
        	//返回拆分器ORDERED和SIZED以及SUBSIZED特征值或运算结果
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }

    /**
    * 移除符合一定规则的元素
    *
    * @return 返回是否移除元素
    */
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        // 在指出要移除的元素的过程中引发任意异常都不会修改姐
        // 移除元素的数量
        int removeCount = 0;
        // 创建列表大小size大小的BitSet类型的集合，BitSet集合内使用long[]数组存储int类型的数值
        final BitSet removeSet = new BitSet(size);
        //获取列表自操作数
        final int expectedModCount = modCount;
        //获取当前列表的大小
        final int size = this.size;
        //在保证未对列表产生增删改操作的前提下遍历缓冲数组
        for (int i=0; modCount == expectedModCount && i < size; i++) {
        	//获取缓冲数组中的元素
            @SuppressWarnings("unchecked")
            final E element = (E) elementData[i];
            //筛选器筛选元素
            if (filter.test(element)) {
            	//移除集合添加元素索引
                removeSet.set(i);
                //要移除的元素数量 + 1
                removeCount++;
            }
        }
        //判断在这个过程中列表是否发生了增删改操作
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }

        // 将剩余的元素移动到删除元素留下的空间中
        // 是否移除了元素
        final boolean anyToRemove = removeCount > 0;
        // 判断是否需要移除元素
        if (anyToRemove) {
        	//如果需要移除元素
        	//获取剩余元素数量
            final int newSize = size - removeCount;
            //在i < size 并且 j < newSize的范围内遍历元素
            for (int i=0, j=0; (i < size) && (j < newSize); i++, j++) {
            	//nextClearBit返回传入参数与从起始位置开始清除的第一个索引不同的参数，即传入参数i是否包含在移除集合removeSet之中，不包含返回参数本身，否则返回第一个相等的元素索引
            	//将i设置为第一个不存在移除集合中的索引数值
                i = removeSet.nextClearBit(i);
                //将未删除的元素移动到缓冲数组的前newSize个元素的位置
                elementData[j] = elementData[i];
            }
            //清空索引newSize之后未使用的数组元素空间
            for (int k=newSize; k < size; k++) {
            	//解除移除元素的引用，当GC发生时回收数组空间
                elementData[k] = null;  // Let gc do its work
            }
            //重新计算列表长度
            this.size = newSize;
            //在这个方法执行时列表是否发生了增删改操作
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            //自操作数自增
            modCount++;
        }

        //返回是否移除了元素
        return anyToRemove;
    }


    /**
    * 使用指定操作改变数组中的元素
    */
    @Override
    @SuppressWarnings("unchecked")
    public void replaceAll(UnaryOperator<E> operator) {
    	//参数非null判断
        Objects.requireNonNull(operator);
        //缓存自操作数
        final int expectedModCount = modCount;
        //获取当前列表长度
        final int size = this.size;
        //在保证列表元素不发生改变的情况下遍历缓存数组；
        for (int i=0; modCount == expectedModCount && i < size; i++) {
        	//将指定操作应用于指定元素，即使用指定操作改变元素
            elementData[i] = operator.apply((E) elementData[i]);
        }
        //并发操作检查
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        //列表自操作数自增
        modCount++;
    }

    /**
    * 按照指定的比较规则对列表进行相应的排序
    */
    @Override
    @SuppressWarnings("unchecked")
    public void sort(Comparator<? super E> c) {
    	//缓存链表自操作数
        final int expectedModCount = modCount;
        //缓冲数组索引0到size的范围内的元素按照一定的比较规则进行排序,[0, size)
        Arrays.sort((E[]) elementData, 0, size, c);
        //并发操作检查
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        //列表自操作数自增
        modCount++;
    }
}
