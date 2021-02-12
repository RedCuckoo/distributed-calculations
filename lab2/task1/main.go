package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

const (
	ROWS            = 10
	COLUMNS         = 30
	BEE_PACK_NUMBER = 3
)

func generateForest() [][]bool {
	forest := make([][]bool, ROWS)

	for index := range forest {
		forest[index] = make([]bool, COLUMNS)
	}

	placeBear(forest)

	return forest
}

func placeBear(forest [][]bool) {
	rand.Seed(time.Now().UnixNano())
	row := rand.Intn(ROWS)
	column := rand.Intn(COLUMNS)

	forest[row][column] = true
}

func run(forest [][]bool, mutex *sync.Mutex, waitingGroup *sync.WaitGroup, currentRow *int, bearFound *bool, id int){
	var workRow int

	for {
		mutex.Lock()

		if *bearFound{
			mutex.Unlock()
			fmt.Printf("%d finished, because the bear was already found\n", id)
			waitingGroup.Done()
			return
		}

		workRow = *currentRow
		*currentRow = *currentRow + 1
		mutex.Unlock()

		if workRow >= ROWS {
			fmt.Printf("%d finished, didn't find bear\n", id)
			waitingGroup.Done()
			return
		}

		fmt.Printf("%d started checking part #%d\n",id,workRow)

		for columnIndex := range forest[workRow]{
			if forest[workRow][columnIndex]{
				mutex.Lock()
				*bearFound = true
				mutex.Unlock()

				fmt.Printf("%d found the bear and punished him\n", id)

				waitingGroup.Done()
				return
			}
		}

		time.Sleep(time.Millisecond*100)
		fmt.Printf("%d returned to hive\n",id)
	}
}

func findBear(forest [][]bool){
	currentRow := 0
	var mutex sync.Mutex
	var waitingGroup sync.WaitGroup

	bearFound := false

	for i :=0; i < BEE_PACK_NUMBER; i++{
		waitingGroup.Add(1)
		go run(forest,&mutex,&waitingGroup,&currentRow,&bearFound,i)
	}

	waitingGroup.Wait()
}

func main() {
	findBear(generateForest())
}
