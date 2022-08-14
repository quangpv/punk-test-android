package com.quangpv.punk.widget.pagination

import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class PagedListTest {

    private lateinit var pageList: PagedList<String>

    @Before
    fun before() {
        pageList = PagedList(10)
    }

    @Test
    fun `without lock, clear and addMore should not be executed`() {
        assertThrows(IllegalStateException::class.java) { pageList.clear() }
        assertThrows(IllegalStateException::class.java) { pageList.addMore(emptyList()) }

        pageList.lock(true)
        pageList.clear()
        pageList.addMore(emptyList())
        pageList.unlock()
    }

    @Test
    fun `current page should be 0 and next page should be 1 when initialized`() {
        assert(pageList.currentPage == 0)
        assert(pageList.nextPage == 1)
    }

    @Test
    fun `after add more an empty list, the page list is not able to fetch more`() {
        pageList.withLock {
            pageList.addMore(emptyList())
        }

        assert(!pageList.canFetchMore)
    }

    @Test
    fun `when add more is not empty list the page list is able to fetch more`() {
        pageList.withLock {
            pageList.addMore(listOf("a", "b"))
        }
        assert(pageList.canFetchMore)
    }

    @Test
    fun `the page list should be locked after lock`() {
        pageList.lock(true)
        assert(pageList.isLocked)
    }

    @Test
    fun `when page list is locked, the clear or addMore should not effect to original list of page list`() {
        pageList.withLock {
            pageList.addMore(listOf("A", "B"))
            assert(pageList.size == 0)
        }
        pageList.withLock {
            pageList.clear()
            assert(pageList.size == 2)
        }
    }

    @Test
    fun `the clear or addMore just effected after unlocked`() {
        pageList.withLock {
            pageList.addMore(listOf("A", "B"))
        }
        assert(pageList.size == 2)
        pageList.withLock {
            pageList.clear()
        }
        assert(pageList.size == 0)
    }
}