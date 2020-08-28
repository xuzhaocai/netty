/*
 * Copyright 2013 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.channel.nio;

import java.nio.channels.SelectionKey;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

final class SelectedSelectionKeySet extends AbstractSet<SelectionKey> {
    //放selectionKey的数组
    SelectionKey[] keys;
    int size;// size

    SelectedSelectionKeySet() {// 初始化就先来1024个
        keys = new SelectionKey[1024];
    }
    // 添加方法
    @Override
    public boolean add(SelectionKey o) {
        if (o == null) {
            return false;
        }

        keys[size++] = o;
        // 如果数组的大小与实际存了的大小相等的话
        if (size == keys.length) {// 进行扩容
            increaseCapacity();
        }

        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int size() {// 大小
        return size;
    }

    @Override
    public Iterator<SelectionKey> iterator() {
        return new Iterator<SelectionKey>() {
            private int idx;

            @Override
            public boolean hasNext() {
                return idx < size;
            }

            @Override
            public SelectionKey next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return keys[idx++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    void reset() {
        reset(0);
    }

    void reset(int start) {
        Arrays.fill(keys, start, size, null);
        size = 0;
    }

    private void increaseCapacity() {
        // 重新创建个数组，新数组大小 是原来的2倍，左移一位就是 *2
        SelectionKey[] newKeys = new SelectionKey[keys.length << 1];
        // 数组copy
        System.arraycopy(keys, 0, newKeys, 0, size);
        keys = newKeys;
    }
}
