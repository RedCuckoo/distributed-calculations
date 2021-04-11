package task2

import (
"math/rand"
"time"
)

const(
	BOOKS_AMOUNT = 20
)

var LETTERS = []rune("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")

type Book struct {
	name string
	author string
}

type Reader struct {
	name string
}

type Librarian struct { }

func generateSequence(n int) string {
	b := make([]rune, n)

	for i := range b {
		b[i] = LETTERS[rand.Intn(len(LETTERS))]
	}

	return string(b)
}

func (reader Reader) takeBook(bookChannel chan Book, readerSemaphore chan int, librarianSemaphore chan bool) {
	for {
		if <-readerSemaphore == -1 {
			return
		}

		//reader got book
		firstBook := <-bookChannel
		secondBook := <-bookChannel
		time.Sleep(100 * time.Millisecond)
		//reader read book

		librarianSemaphore <- true
	}
}

func (Librarian) giveBooks(bookChannel chan Book, readerSemaphore chan int, librarianSemaphore chan bool, endChannel chan bool) {

	for i := 0; i < BOOKS_AMOUNT; i++ {
		if i != 0 {
			<-librarianSemaphore
		}

		var firstBook = Book{name: generateSequence(10), author: generateSequence(8)}
		var secondBook = Book{name: generateSequence(10), author: generateSequence(8)}

		//librarian sent books
		bookChannel <- firstBook
		bookChannel <- secondBook

		readerSemaphore <- 1
	}

	<-librarianSemaphore

	readerSemaphore <- -1
	readerSemaphore <- -1
	readerSemaphore <- -1

	endChannel <- true
}
