package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

const(
	STRING_LENGTH = 10
)

func getRandomLetter() byte{
	randNumber := rand.Intn(4)

	switch randNumber {
	case 0:
		return 'A'
	case 1:
		return 'B'
	case 2:
		return 'C'
	case 3:
		return 'D'
	}

	return ' '
}

func generateString() []byte{
	var str []byte

	for i := 0; i < STRING_LENGTH; i++{
		str = append(str, getRandomLetter())
	}

	return str
}

func changeStringRoutine(id int, str *[]byte, waitGroup *sync.WaitGroup){
	defer (*waitGroup).Done()

	randNumber := rand.Intn(4)

	var from, to byte

	switch randNumber {
	case 0:
		from = 'A'
		to = 'C'
	case 1:
		from = 'C'
		to = 'A'
	case 2:
		from = 'B'
		to = 'D'
	case 3:
		from = 'D'
		to = 'B'
	}

	for i := 0; i < len(*str); i++{
		if (*str)[i] == from{
			(*str)[i] = to
		}
	}

	fmt.Printf("#%d changed string to %s, using pattern %c => %c\n", id, string(*str), from, to)
}

func calculateAB(str *[]byte) (int, int){
	a, b := 0, 0

	for i := 0; i < len(*str); i++{
		if (*str)[i] == 'A'{
			a++
		}else if (*str)[i] == 'B' {
			b++
		}
	}

	return a, b
}

func checkABForMultiple(strArr *[][]byte)bool{
	var a, b int

	for i := 0; i < len(*strArr); i++{
		aTemp, bTemp := calculateAB(&(*strArr)[i])
		a += aTemp
		b += bTemp
	}

	return a == b
}

func sliceWithout(slice [][]byte, i int) [][]byte{
	return append(slice[:i], slice[i+1:]...)
}

func checkAB(strs *[][]byte) bool {
	answer := false

	for i := 0; i < len(*strs); i++ {
		sliceCopy := make([][]byte, len((*strs)))
		copy(sliceCopy, *strs)
		sliceToCheck := sliceWithout(sliceCopy, i)
		answer = answer || checkABForMultiple(&sliceToCheck)
		if answer {
			fmt.Printf("Strings without %d have num(a) == num(b)\n", i)
			break
		}
	}

	if !answer{
		answer = answer || checkABForMultiple(strs)

		if answer {
			fmt.Println("All strings have num(a) == num(b)")
		}
	}

	return answer
}

func simulateWork(){
	var waitGroup sync.WaitGroup

	var strs [][]byte

	fmt.Println("Strings before threads:")

	for i := 0; i < 4; i++ {
		strs = append(strs, generateString())
		fmt.Println(string(strs[i]))
	}

	if strs == nil{
		return
	}

	for !checkAB(&strs) {
		waitGroup.Add(4)

		for i := 0; i < 4; i++ {
			go changeStringRoutine(i,&(strs[i]), &waitGroup)
		}

		waitGroup.Wait()

		for i := 0; i < 4; i++ {
			fmt.Println(string(strs[i]))
		}
	}

	fmt.Println("Strings after threads:")

	for i := 0; i < 4; i++ {
		fmt.Println(string(strs[i]))
	}
}

func main(){
	rand.Seed(time.Now().UnixNano())
	simulateWork()
}
